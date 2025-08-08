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

// Servicio para conectar con LM Studio u otros modelos locales
// API compatible con OpenAI en http://localhost:1234/v1/chat/completions
public class LMStudioService {
    
    private static final String LM_STUDIO_URL = "http://localhost:1234/v1/chat/completions";
    private static String MODEL_NAME = "google/gemma-3-1b"; // Modelo Gemma 3 1B
    
    
    private static final String[] RECOMMENDED_MODELS = {
        "microsoft/DialoGPT-medium",
        "microsoft/DialoGPT-large", 
        "huggingface/CodeBERTa-small-v1",
        "llama2:7b-chat",
        "codellama:7b",
        "mistral:7b"
    };
    
    // Envía un mensaje al modelo de IA con estrategias para distintos modelos
    public String sendMessage(List<ChatMessage> conversationHistory, String systemContext) {
     
        String[] strategies = {"system_message", "embedded_context", "simple", "alternating_strict"};
        
        System.out.println("DEBUG - Iniciando envío con contexto de " + (systemContext != null ? systemContext.length() : 0) + " caracteres");
        
        for (String strategy : strategies) {
            try {
                String result = sendMessageWithStrategy(conversationHistory, systemContext, strategy);
                if (!result.contains("Error rendering prompt with jinja template")) {
                    return result;
                }
            } catch (Exception e) {
                System.out.println("Estrategia " + strategy + " falló: " + e.getMessage());
            }
        }
        
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
    
    // Enviar mensaje usando una estrategia específica
    private String sendMessageWithStrategy(List<ChatMessage> conversationHistory, String systemContext, String strategy) {
        try {
            URL url = new URL(LM_STUDIO_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000); // 30s
            connection.setReadTimeout(60000); // 60s
            
            
            String jsonRequest = buildJsonRequestWithStrategy(conversationHistory, systemContext, strategy);
            System.out.println("Estrategia: " + strategy + " | JSON Request: " + jsonRequest); 
            
            
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            
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
                
                StringBuilder errorResponse = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                } catch (Exception e) {
                    
                }
                
                String error = "Error del servidor (código " + responseCode + "): " + errorResponse.toString();
                
                
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
    
    // Construye JSON para diferentes estrategias
    private String buildJsonRequestWithStrategy(List<ChatMessage> conversationHistory, String systemContext, String strategy) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"model\": \"").append(MODEL_NAME).append("\",");
        json.append("\"messages\": [");
        
        boolean hasMessages = false;
        
        switch (strategy) {
            case "system_message":
                
                hasMessages = buildSystemMessageStrategy(json, conversationHistory, systemContext);
                break;
                
            case "embedded_context":
                
                hasMessages = buildEmbeddedContextStrategy(json, conversationHistory, systemContext);
                break;
                
            case "simple":
                
                hasMessages = buildSimpleStrategy(json, conversationHistory, systemContext);
                break;
                
            case "alternating_strict":
                
                hasMessages = buildAlternatingStrictStrategy(json, conversationHistory, systemContext);
                break;
                
            default:
                
                hasMessages = buildSystemMessageStrategy(json, conversationHistory, systemContext);
        }
        
        json.append("],");
        json.append("\"temperature\": 0.1,");
        json.append("\"max_tokens\": 16000,");
        json.append("\"stream\": false");
        json.append("}");
        
        return json.toString();
    }
    
    // Estrategia 0: usar rol "system"
    private boolean buildSystemMessageStrategy(StringBuilder json, List<ChatMessage> conversationHistory, String systemContext) {
        boolean hasMessages = false;
        
        // Agregar mensaje del sistema si existe contexto
        if (systemContext != null && !systemContext.trim().isEmpty()) {
            json.append("{");
            json.append("\"role\": \"system\",");
            json.append("\"content\": \"").append(escapeJson(systemContext)).append("\"");
            json.append("}");
            hasMessages = true;
            System.out.println("DEBUG - System message strategy con contexto (" + systemContext.length() + ")");
        }
        
        // Agregar mensajes de la conversación
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
    
    // Estrategia 1: contexto completo en el primer mensaje del usuario
    private boolean buildEmbeddedContextStrategy(StringBuilder json, List<ChatMessage> conversationHistory, String systemContext) {
        boolean hasMessages = false;
        
        if (!conversationHistory.isEmpty()) {
            ChatMessage firstMessage = conversationHistory.get(0);
            
            // Primer mensaje con contexto embebido
            String contextualizedContent = "";
            if (systemContext != null && !systemContext.trim().isEmpty()) {
                // Usar todo el contexto
                contextualizedContent = systemContext + "\n\n=== PREGUNTA DEL USUARIO ===\n";
                System.out.println("DEBUG - Enviando contexto completo (" + systemContext.length() + " caracteres)");
            }
            contextualizedContent += firstMessage.getContent();
            
            json.append("{");
            json.append("\"role\": \"user\",");
            json.append("\"content\": \"").append(escapeJson(contextualizedContent)).append("\"");
            json.append("}");
            hasMessages = true;
            
            // Resto del historial
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
    
    // Estrategia 2: mensajes simples con contexto en el primer mensaje
    private boolean buildSimpleStrategy(StringBuilder json, List<ChatMessage> conversationHistory, String systemContext) {
        boolean hasMessages = false;
        boolean isFirstUserMessage = true;
        
        for (ChatMessage message : conversationHistory) {
            if (hasMessages) json.append(",");
            
            json.append("{");
            json.append("\"role\": \"").append(getRoleForApi(message.getType())).append("\",");
            
            String content = message.getContent();
            
            // En el primer mensaje del usuario, agregar contexto
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
    
    // Estrategia 3: alternancia estricta user/assistant (p.ej. Gemma)
    private boolean buildAlternatingStrictStrategy(StringBuilder json, List<ChatMessage> conversationHistory, String systemContext) {
        boolean hasMessages = false;
        String lastRole = null;
        boolean isFirstMessage = true;
        
        for (ChatMessage message : conversationHistory) {
            String currentRole = getRoleForApi(message.getType());
            
            // Saltar mensajes del sistema
            if ("system".equals(currentRole)) {
                continue;
            }
            
            // Forzar que inicie con "user"
            if (isFirstMessage && !"user".equals(currentRole)) {
                // Crear uno artificial con contexto
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
                
                // Si el actual es "assistant", agregarlo
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
            
            // Saltar si es el mismo rol (forzar alternancia)
            if (currentRole.equals(lastRole)) {
                continue;
            }
            
            if (hasMessages) json.append(",");
            
            json.append("{");
            json.append("\"role\": \"").append(currentRole).append("\",");
            
            // En el primer mensaje del usuario, agregar contexto
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
        
        // Si no hay mensajes, agregar uno de usuario
        if (!hasMessages) {
            json.append("{");
            json.append("\"role\": \"user\",");
            json.append("\"content\": \"Hola, ¿puedes ayudarme?\"");
            json.append("}");
            hasMessages = true;
        }
        
        return hasMessages;
    }
    
    // Convierte el tipo interno al formato de la API
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
    
    // Escapa caracteres especiales JSON
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
    
    // Extrae la respuesta del JSON de la API
    private String parseResponse(String jsonResponse) {
        try {
            System.out.println("DEBUG - JSON Response recibido: " + jsonResponse);
            
            // Extraer contenido con regex
            if (jsonResponse != null && jsonResponse.contains("\"content\"")) {
                // Buscar patrón "content"
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
            
            // Fallback manual
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
    
    // Limpia contenido para respuestas naturales
    private String cleanContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "No pude generar una respuesta. ¿Podrías reformular tu pregunta?";
        }
        
        String cleaned = content.trim();
        
        // Remover comandos al inicio
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
        
        // Limpiar patrones robóticos
        cleaned = cleaned.replaceAll("(?i)\\bpara tu consulta sobre\\b", "")
                        .replaceAll("(?i)\\ben base a tu pregunta\\b", "")
                        .replaceAll("(?i)\\bcon respecto a tu pregunta\\b", "")
                        .replaceAll("(?i)\\brespecto a tu consulta\\b", "")
                        .trim();
        
        // Mejorar formato natural
        cleaned = cleaned.replace("\\n", "\n")
                        .replace("\\*", "*")
                        .replace("\\\"", "\"")
                        .replaceAll("\\s+", " ") // Normalizar espacios múltiples pero mantener saltos de línea
                        .replaceAll("(?m)^\\s+", "") // Remover espacios al inicio de líneas
                        .replaceAll("(?m)\\s+$", "") // Remover espacios al final de líneas
                        .replaceAll("\n{3,}", "\n\n") // Máximo 2 saltos de línea consecutivos
                        .trim();
        
        // Si la respuesta queda muy corta, mensaje útil
        if (cleaned.length() < 10 || cleaned.matches("^[\\s\\p{Punct}]*$")) {
            return "No pude generar una respuesta apropiada. ¿Podrías ser más específico en tu pregunta?";
        }
        
        // Asegurar cierre adecuado
        if (!cleaned.matches(".*[.!?]\\s*$")) {
            cleaned += ".";
        }
        
        return cleaned;
    }
    
    // Fin de cadena JSON considerando escapes
    private int findEndOfJsonString(String json, int startIndex) {
        for (int i = startIndex; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"') {
                // Verificar si está escapado
                int backslashes = 0;
                for (int j = i - 1; j >= startIndex && json.charAt(j) == '\\'; j--) {
                    backslashes++;
                }
                // Par de backslashes: comilla no escapada
                if (backslashes % 2 == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    // Decodifica caracteres escapados JSON
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
    
    // Verifica si LM Studio está disponible
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
    
    // Lista de modelos disponibles en LM Studio
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
                
                // Parsear modelos
                String jsonResponse = response.toString();
                StringBuilder models = new StringBuilder("📋 **Modelos disponibles en LM Studio:**\n\n");
                
                // Buscar "id" en el JSON
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
    
    // Cambia el modelo configurado
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
    
    // Obtiene el modelo configurado
    public static String getCurrentModel() {
        return MODEL_NAME;
    }
}
