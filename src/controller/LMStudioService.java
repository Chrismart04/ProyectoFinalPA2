package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import model.ChatMessage;

/**
 * Servicio para conectar con LM Studio u otros modelos locales
 * LM Studio expone una API compatible con OpenAI en http://localhost:1234/v1/chat/completions
 */
public class LMStudioService {
    
    private static final String LM_STUDIO_URL = "http://localhost:1234/v1/chat/completions";
    private static String MODEL_NAME = "google/gemma-3-1b"; // Modelo Gemma 3 1B
    
    // Modelos alternativos recomendados para evitar problemas de compatibilidad
    private static final String[] RECOMMENDED_MODELS = {
        "microsoft/DialoGPT-medium",
        "microsoft/DialoGPT-large", 
        "huggingface/CodeBERTa-small-v1",
        "llama2:7b-chat",
        "codellama:7b",
        "mistral:7b"
    };
    
    /**
     * Envía un mensaje al modelo de IA y obtiene la respuesta
     * Implementa múltiples estrategias para manejar diferentes modelos
     */
    public String sendMessage(List<ChatMessage> conversationHistory, String systemContext) {
        // Intentar con estrategias ordenadas por efectividad 
        // system_message: usa rol "system" (funciona con GPT y algunos otros)
        // embedded_context: embebe en primer mensaje usuario (más compatible)
        // simple: contexto mínimo embebido
        // alternating_strict: para modelos muy estrictos como Gemma
        String[] strategies = {"system_message", "embedded_context", "simple", "alternating_strict"};
        
        System.out.println("DEBUG - Iniciando envío con contexto de " + (systemContext != null ? systemContext.length() : 0) + " caracteres");
        
        for (String strategy : strategies) {
            try {
                String result = sendMessageWithStrategy(conversationHistory, systemContext, strategy);
                if (!result.contains("Error rendering prompt with jinja template")) {
                    return result;
                }
            } catch (Exception e) {
                // Continuar con la siguiente estrategia
                System.out.println("Estrategia " + strategy + " falló: " + e.getMessage());
            }
        }
        
        // Si todas las estrategias fallan, devolver mensaje de error específico
        return "❌ **Error de compatibilidad del modelo**\n\n" +
               "El modelo 'google/gemma-3-1b' tiene problemas con el formato de mensajes.\n\n" +
               "**Soluciones recomendadas:**\n" +
               "1. **Cambiar a un modelo compatible:**\n" +
               "   - `microsoft/DialoGPT-medium`\n" +
               "   - `microsoft/DialoGPT-large` \n" +
               "   - `huggingface/CodeBERTa-small-v1`\n" +
               "   - Cualquier modelo de la familia Llama\n\n" +
               "2. **En LM Studio:**\n" +
               "   - Ve a 'My Models' > selecciona tu modelo\n" +
               "   - Haz clic en 'Model Settings'\n" +
               "   - En 'Prompt Template', selecciona una plantilla diferente\n" +
               "   - Prueba: 'ChatML', 'Alpaca', o 'Vicuna'\n\n" +
               "3. **Usa el comando `/modelos` para ver modelos disponibles**\n\n" +
               "**Nota técnica:** Gemma requiere un patrón muy específico de alternancia user/assistant.";
    }
    
    /**
     * Envía mensaje usando una estrategia específica
     */
    private String sendMessageWithStrategy(List<ChatMessage> conversationHistory, String systemContext, String strategy) {
        try {
            URL url = new URL(LM_STUDIO_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Configurar la conexión
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000); // 30 segundos
            connection.setReadTimeout(60000); // 60 segundos
            
            // Construir el JSON de la petición según la estrategia
            String jsonRequest = buildJsonRequestWithStrategy(conversationHistory, systemContext, strategy);
            System.out.println("Estrategia: " + strategy + " | JSON Request: " + jsonRequest); // Para debug
            
            // Enviar la petición
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Leer la respuesta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
                
                return parseResponse(response.toString());
            } else {
                // Leer el error del servidor
                StringBuilder errorResponse = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                } catch (Exception e) {
                    // Si no se puede leer el error
                }
                
                String error = "Error del servidor (código " + responseCode + "): " + errorResponse.toString();
                
                // Si es el error de template, lanzar excepción para intentar otra estrategia
                if (errorResponse.toString().contains("Error rendering prompt with jinja template")) {
                    throw new RuntimeException(error);
                }
                
                return error + "\n\nPosibles soluciones:\n" +
                       "1. Verifica que el modelo '" + MODEL_NAME + "' esté cargado en LM Studio\n" +
                       "2. Prueba con un modelo diferente\n" +
                       "3. Reinicia LM Studio\n" +
                       "4. Usa el comando '/estado' para verificar la conexión";
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error de conexión: " + e.getMessage() + 
                   "\n\nAsegúrate de que LM Studio esté ejecutándose en http://localhost:1234");
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Construye el JSON usando diferentes estrategias para diferentes modelos
     */
    private String buildJsonRequestWithStrategy(List<ChatMessage> conversationHistory, String systemContext, String strategy) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"model\": \"").append(MODEL_NAME).append("\",");
        json.append("\"messages\": [");
        
        boolean hasMessages = false;
        
        switch (strategy) {
            case "system_message":
                // Estrategia 0: Usar rol "system" (mejor para modelos que lo soportan)
                hasMessages = buildSystemMessageStrategy(json, conversationHistory, systemContext);
                break;
                
            case "embedded_context":
                // Estrategia 1: Contexto embebido en el primer mensaje del usuario
                hasMessages = buildEmbeddedContextStrategy(json, conversationHistory, systemContext);
                break;
                
            case "simple":
                // Estrategia 2: Mensajes simples con contexto completo
                hasMessages = buildSimpleStrategy(json, conversationHistory, systemContext);
                break;
                
            case "alternating_strict":
                // Estrategia 3: Alternar estrictamente user/assistant
                hasMessages = buildAlternatingStrictStrategy(json, conversationHistory, systemContext);
                break;
                
            default:
                // Fallback a la estrategia system_message
                hasMessages = buildSystemMessageStrategy(json, conversationHistory, systemContext);
        }
        
        json.append("],");
        json.append("\"temperature\": 0.1,");
        json.append("\"max_tokens\": 16000,");
        json.append("\"stream\": false");
        json.append("}");
        
        return json.toString();
    }
    
    /**
     * Estrategia 0: Usar mensaje de rol "system" (ideal para modelos que lo soportan como GPT)
     */
    private boolean buildSystemMessageStrategy(StringBuilder json, List<ChatMessage> conversationHistory, String systemContext) {
        boolean hasMessages = false;
        
        // Agregar mensaje del sistema si existe contexto
        if (systemContext != null && !systemContext.trim().isEmpty()) {
            json.append("{");
            json.append("\"role\": \"system\",");
            json.append("\"content\": \"").append(escapeJson(systemContext)).append("\"");
            json.append("}");
            hasMessages = true;
            System.out.println("DEBUG - System message strategy enviando contexto completo (" + systemContext.length() + " caracteres)");
        }
        
        // Agregar todos los mensajes de la conversación
        for (ChatMessage message : conversationHistory) {
            if (hasMessages) json.append(",");
            
            json.append("{");
            json.append("\"role\": \"").append(getRoleForApi(message.getType())).append("\",");
            json.append("\"content\": \"").append(escapeJson(message.getContent())).append("\"");
            json.append("}");
            hasMessages = true;
        }
        
        return hasMessages;
    }
    
    /**
     * Estrategia 1: Embeber el contexto COMPLETO en el primer mensaje del usuario
     */
    private boolean buildEmbeddedContextStrategy(StringBuilder json, List<ChatMessage> conversationHistory, String systemContext) {
        boolean hasMessages = false;
        
        if (!conversationHistory.isEmpty()) {
            ChatMessage firstMessage = conversationHistory.get(0);
            
            // Crear el primer mensaje con el contexto COMPLETO embebido
            String contextualizedContent = "";
            if (systemContext != null && !systemContext.trim().isEmpty()) {
                // USAR TODO EL CONTEXTO, no solo una línea resumida
                contextualizedContent = systemContext + "\n\n=== PREGUNTA DEL USUARIO ===\n";
                System.out.println("DEBUG - Enviando contexto completo (" + systemContext.length() + " caracteres)");
            }
            contextualizedContent += firstMessage.getContent();
            
            json.append("{");
            json.append("\"role\": \"user\",");
            json.append("\"content\": \"").append(escapeJson(contextualizedContent)).append("\"");
            json.append("}");
            hasMessages = true;
            
            // Agregar el resto del historial
            for (int i = 1; i < conversationHistory.size(); i++) {
                ChatMessage message = conversationHistory.get(i);
                if (hasMessages) json.append(",");
                json.append("{");
                json.append("\"role\": \"").append(getRoleForApi(message.getType())).append("\",");
                json.append("\"content\": \"").append(escapeJson(message.getContent())).append("\"");
                json.append("}");
                hasMessages = true;
            }
        }
        
        return hasMessages;
    }
    
    /**
     * Estrategia 2: Mensajes simples con contexto COMPLETO en el primer mensaje
     */
    private boolean buildSimpleStrategy(StringBuilder json, List<ChatMessage> conversationHistory, String systemContext) {
        boolean hasMessages = false;
        boolean isFirstUserMessage = true;
        
        for (ChatMessage message : conversationHistory) {
            if (hasMessages) json.append(",");
            
            json.append("{");
            json.append("\"role\": \"").append(getRoleForApi(message.getType())).append("\",");
            
            String content = message.getContent();
            
            // En el primer mensaje del usuario, agregar contexto COMPLETO
            if (isFirstUserMessage && message.getType() == ChatMessage.MessageType.USER) {
                if (systemContext != null && !systemContext.trim().isEmpty()) {
                    content = systemContext + "\n\n=== PREGUNTA DEL USUARIO ===\n" + content;
                    System.out.println("DEBUG - Simple strategy enviando contexto completo (" + systemContext.length() + " caracteres)");
                } else {
                    content = "Contexto: Eres asistente de clínica dental con acceso a datos de pacientes, citas y productos. " +
                             "Responde con información específica y directa. Pregunta: " + content;
                }
                isFirstUserMessage = false;
            }
            
            json.append("\"content\": \"").append(escapeJson(content)).append("\"");
            json.append("}");
            hasMessages = true;
        }
        
        return hasMessages;
    }
    
    /**
     * Estrategia 3: Asegurar alternancia estricta user/assistant
     * Especialmente diseñada para modelos como Gemma que son muy estrictos
     */
    private boolean buildAlternatingStrictStrategy(StringBuilder json, List<ChatMessage> conversationHistory, String systemContext) {
        boolean hasMessages = false;
        String lastRole = null;
        boolean isFirstMessage = true;
        
        for (ChatMessage message : conversationHistory) {
            String currentRole = getRoleForApi(message.getType());
            
            // Saltar mensajes del sistema (problemas con algunos modelos)
            if ("system".equals(currentRole)) {
                continue;
            }
            
            // Forzar que empiece siempre con "user"
            if (isFirstMessage && !"user".equals(currentRole)) {
                // Si el primer mensaje no es del usuario, crear uno artificial con contexto completo
                if (hasMessages) json.append(",");
                json.append("{");
                json.append("\"role\": \"user\",");
                String contextMessage;
                if (systemContext != null && !systemContext.trim().isEmpty()) {
                    contextMessage = systemContext + "\n\n=== PREGUNTA DEL USUARIO ===\n" + message.getContent();
                    System.out.println("DEBUG - Alternating strategy (artificial user) enviando contexto completo (" + systemContext.length() + " caracteres)");
                } else {
                    contextMessage = "Eres un asistente de clínica dental especializado. " + message.getContent();
                }
                json.append("\"content\": \"").append(escapeJson(contextMessage)).append("\"");
                json.append("}");
                hasMessages = true;
                lastRole = "user";
                isFirstMessage = false;
                
                // Si el mensaje actual es "assistant", agregarlo
                if ("assistant".equals(currentRole)) {
                    json.append(",");
                    json.append("{");
                    json.append("\"role\": \"assistant\",");
                    json.append("\"content\": \"").append(escapeJson(message.getContent())).append("\"");
                    json.append("}");
                    lastRole = "assistant";
                }
                continue;
            }
            
            // Saltar si es el mismo rol que el anterior (forzar alternancia)
            if (currentRole.equals(lastRole)) {
                continue;
            }
            
            if (hasMessages) json.append(",");
            
            json.append("{");
            json.append("\"role\": \"").append(currentRole).append("\",");
            
            // En el primer mensaje del usuario, agregar contexto COMPLETO
            String content = message.getContent();
            if (isFirstMessage && "user".equals(currentRole) && systemContext != null && !systemContext.trim().isEmpty()) {
                content = systemContext + "\n\n=== PREGUNTA DEL USUARIO ===\n" + content;
                System.out.println("DEBUG - Alternating strategy enviando contexto completo (" + systemContext.length() + " caracteres)");
            } else if (isFirstMessage && "user".equals(currentRole)) {
                content = "Eres un asistente de clínica dental especializado. " + content;
            }
            
            json.append("\"content\": \"").append(escapeJson(content)).append("\"");
            json.append("}");
            hasMessages = true;
            lastRole = currentRole;
            isFirstMessage = false;
        }
        
        // Si terminamos con un mensaje del assistant y la lista está vacía, agregar un mensaje de usuario
        if (!hasMessages) {
            json.append("{");
            json.append("\"role\": \"user\",");
            json.append("\"content\": \"Hola, ¿puedes ayudarme?\"");
            json.append("}");
            hasMessages = true;
        }
        
        return hasMessages;
    }
    
    /**
     * Convierte el tipo de mensaje interno al formato de la API
     */
    private String getRoleForApi(ChatMessage.MessageType type) {
        switch (type) {
            case USER:
                return "user";
            case ASSISTANT:
                return "assistant";
            case SYSTEM:
                return "system";
            default:
                return "user";
        }
    }
    
    /**
     * Escapa caracteres especiales para JSON
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Extrae la respuesta del JSON devuelto por la API
     */
    private String parseResponse(String jsonResponse) {
        try {
            System.out.println("DEBUG - JSON Response recibido: " + jsonResponse);
            
            // Método más robusto usando expresiones regulares para extraer el contenido
            if (jsonResponse != null && jsonResponse.contains("\"content\"")) {
                // Buscar el patrón "content":"..." dentro de choices
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                    "\"content\"\\s*:\\s*\"([^\"]*(?:\\\\.[^\"]*)*)\""
                );
                java.util.regex.Matcher matcher = pattern.matcher(jsonResponse);
                
                if (matcher.find()) {
                    String content = matcher.group(1);
                    // Decodificar caracteres escapados
                    String unescapedContent = unescapeJson(content);
                    System.out.println("DEBUG - Contenido extraído: " + unescapedContent);
                    return cleanContent(unescapedContent);
                }
            }
            
            // Fallback: método manual mejorado
            int choicesIndex = jsonResponse.indexOf("\"choices\"");
            if (choicesIndex != -1) {
                String fromChoices = jsonResponse.substring(choicesIndex);
                String searchPattern = "\"content\":\"";
                int startIndex = fromChoices.indexOf(searchPattern);
                
                if (startIndex != -1) {
                    startIndex += searchPattern.length();
                    int endIndex = findEndOfJsonString(fromChoices, startIndex);
                    
                    if (endIndex != -1) {
                        String content = fromChoices.substring(startIndex, endIndex);
                        String unescapedContent = unescapeJson(content);
                        return cleanContent(unescapedContent);
                    }
                }
            }
            
            System.out.println("ERROR - No se pudo parsear la respuesta");
            return "Error: No se pudo parsear la respuesta del modelo\nRespuesta cruda: " + jsonResponse.substring(0, Math.min(500, jsonResponse.length())) + "...";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al procesar respuesta: " + e.getMessage();
        }
    }
    
    /**
     * Limpia el contenido extraído removiendo comandos duplicados y formateando para respuestas naturales
     */
    private String cleanContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "No pude generar una respuesta. ¿Podrías reformular tu pregunta?";
        }
        
        String cleaned = content.trim();
        
        // Remover comandos al inicio más agresivamente
        while (cleaned.startsWith("/") || cleaned.startsWith("`/")) {
            int firstNewline = cleaned.indexOf("\n");
            if (firstNewline > 0) {
                cleaned = cleaned.substring(firstNewline + 1).trim();
            } else {
                break;
            }
        }
        
        // Remover frases técnicas y genéricas que el modelo puede repetir
        cleaned = cleaned.replaceAll("(?i)^Aquí tienes la respuesta:\\s*", "")
                        .replaceAll("(?i)^La respuesta es:\\s*", "")
                        .replaceAll("(?i)^Respuesta:\\s*", "")
                        .replaceAll("(?i)^Basándome en (la información|los datos).*?:\\s*", "")
                        .replaceAll("(?i)^De acuerdo (con|a) (la información|los datos).*?:\\s*", "")
                        .replaceAll("(?i)^Según (la información|los datos).*?:\\s*", "")
                        .replaceAll("(?m)^`[^`]*`\\s*$", "") // Remover líneas que solo tienen código
                        .replaceAll("(?m)^\\*\\*[^*]*\\*\\*:\\s*$", "") // Remover títulos markdown solos
                        .trim();
        
        // Limpiar patrones de respuesta robóticos
        cleaned = cleaned.replaceAll("(?i)\\bpara tu consulta sobre\\b", "")
                        .replaceAll("(?i)\\ben base a tu pregunta\\b", "")
                        .replaceAll("(?i)\\bcon respecto a tu pregunta\\b", "")
                        .replaceAll("(?i)\\brespecto a tu consulta\\b", "")
                        .trim();
        
        // Mejorar formato de respuesta natural
        cleaned = cleaned.replace("\\n", "\n")
                        .replace("\\*", "*")
                        .replace("\\\"", "\"")
                        .replaceAll("\\s+", " ") // Normalizar espacios múltiples pero mantener saltos de línea
                        .replaceAll("(?m)^\\s+", "") // Remover espacios al inicio de líneas
                        .replaceAll("(?m)\\s+$", "") // Remover espacios al final de líneas
                        .replaceAll("\n{3,}", "\n\n") // Máximo 2 saltos de línea consecutivos
                        .trim();
        
        // Si la respuesta queda muy corta o sin sentido, proporcionar mensaje más útil
        if (cleaned.length() < 10 || cleaned.matches("^[\\s\\p{Punct}]*$")) {
            return "No pude generar una respuesta apropiada. ¿Podrías ser más específico en tu pregunta?";
        }
        
        // Asegurar que la respuesta termine apropiadamente
        if (!cleaned.matches(".*[.!?]\\s*$")) {
            cleaned += ".";
        }
        
        return cleaned;
    }
    
    /**
     * Encuentra el final de una cadena JSON considerando caracteres escapados
     */
    private int findEndOfJsonString(String json, int startIndex) {
        for (int i = startIndex; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"') {
                // Verificar si está escapado
                int backslashes = 0;
                for (int j = i - 1; j >= startIndex && json.charAt(j) == '\\'; j--) {
                    backslashes++;
                }
                // Si hay un número par de backslashes, la comilla no está escapada
                if (backslashes % 2 == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Decodifica caracteres escapados de JSON
     */
    private String unescapeJson(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }
        
        return text.replace("\\n", "\n")
                  .replace("\\\"", "\"")
                  .replace("\\\\", "\\")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t")
                  .replace("\\b", "\b")
                  .replace("\\f", "\f")
                  .replace("\\u00a0", " ") // Espacio no rompible
                  .replace("\\u2028", "\n") // Separador de línea
                  .replace("\\u2029", "\n"); // Separador de párrafo
    }
    
    /**
     * Verifica si LM Studio está disponible
     */
    public boolean isLMStudioAvailable() {
        try {
            URL url = new URL(LM_STUDIO_URL.replace("/chat/completions", "/models"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Obtiene la lista de modelos disponibles en LM Studio
     */
    public String getAvailableModels() {
        try {
            URL url = new URL(LM_STUDIO_URL.replace("/chat/completions", "/models"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
                
                // Parsear la lista de modelos
                String jsonResponse = response.toString();
                StringBuilder models = new StringBuilder("📋 **Modelos disponibles en LM Studio:**\n\n");
                
                // Buscar todos los "id" en el JSON
                int index = 0;
                while ((index = jsonResponse.indexOf("\"id\":\"", index)) != -1) {
                    index += 6; // longitud de "id":"
                    int endIndex = jsonResponse.indexOf("\"", index);
                    if (endIndex != -1) {
                        String modelId = jsonResponse.substring(index, endIndex);
                        if (modelId.equals(MODEL_NAME)) {
                            models.append("✅ ").append(modelId).append(" (CONFIGURADO)\n");
                        } else {
                            models.append("- ").append(modelId).append("\n");
                        }
                        index = endIndex;
                    }
                }
                
                models.append("\n**Modelo actual configurado:** ").append(MODEL_NAME);
                return models.toString();
                
            } else {
                return "Error al obtener modelos. Código: " + responseCode;
            }
            
        } catch (Exception e) {
            return "Error al conectar con LM Studio: " + e.getMessage();
        }
    }
    
    /**
     * Cambia el modelo actualmente configurado
     */
    public static String changeModel(String newModelName) {
        if (newModelName == null || newModelName.trim().isEmpty()) {
            return "❌ **Error:** Debes especificar un nombre de modelo válido.\n\n" +
                   "**Uso:** `/modelo nuevo-modelo`\n" +
                   "**Ejemplo:** `/modelo microsoft/DialoGPT-medium`";
        }
        
        String oldModel = MODEL_NAME;
        MODEL_NAME = newModelName.trim();
        
        return "✅ **Modelo cambiado exitosamente**\n\n" +
               "**Modelo anterior:** " + oldModel + "\n" +
               "**Modelo nuevo:** " + MODEL_NAME + "\n\n" +
               "**Nota:** Asegúrate de que este modelo esté cargado y funcionando en LM Studio.\n" +
               "Puedes verificar la conexión con el comando `/estado`.";
    }
    
    /**
     * Obtiene el modelo actualmente configurado
     */
    public static String getCurrentModel() {
        return MODEL_NAME;
    }
}
