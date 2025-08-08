package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import javax.swing.SwingWorker;

import ClinicaDAO.ChatContextDAO;
import model.ChatMessage;
import view.ChatPanelView;
import view.PrincipalView;

public class ChatController implements AbstractPanelController, ActionListener {
    
    private ChatPanelView chatView;
    private LMStudioService lmStudioService;
    private ChatContextDAO chatContextDAO;
    private String systemContext;
    
    public ChatController(PrincipalView frame) {
        this.chatView = frame.panelChat;
        this.lmStudioService = new LMStudioService();
        this.chatContextDAO = new ChatContextDAO();
        
        // Listeners
        setupListeners();
        
        // Cargar contexto inicial
        loadSystemContext();
    }
    
    private void setupListeners() {
        chatView.sendButton.addActionListener(this);
        chatView.clearButton.addActionListener(this);
    }
    
    private void loadSystemContext() {
        // Contexto del sistema para la IA
        StringBuilder context = new StringBuilder();
        
        // Contexto base
        context.append("=== ASISTENTE INTELIGENTE DE CLÍNICA DENTAL ===\n");
        context.append("Eres un asistente especializado en análisis de datos de clínica dental.\n");
        context.append("Responde de forma NATURAL, DIRECTA y CONVERSACIONAL.\n");
        context.append("NUNCA repitas comandos, preguntas o información técnica innecesaria.\n\n");
        
        context.append("🎯 TIPOS DE RESPUESTA REQUERIDOS:\n");
        context.append("📦 INVENTARIO/PRODUCTOS:\n");
        context.append("• Para 'producto con menos stock/inventario': Identifica el primer producto en 'PRODUCTOS CON STOCK CRÍTICO' y responde: 'El producto con menos inventario es [NOMBRE] con [CANTIDAD] unidades disponibles.'\n");
        context.append("• Para 'productos con poco stock': Lista los productos de ambas secciones (crítico y bajo) con sus cantidades\n");
        context.append("• Siempre incluye números específicos de stock cuando sea relevante\n\n");
        
        context.append("👤 PACIENTES:\n");
        context.append("• Usa nombres reales de la base de datos\n");
        context.append("• Proporciona información específica y útil\n");
        context.append("• Mantén confidencialidad médica apropiada\n\n");
        
        context.append("📅 CITAS:\n");
        context.append("• Usa fechas, horas y procedimientos reales\n");
        context.append("• Proporciona detalles específicos cuando sea relevante\n\n");
        
        context.append("💡 ESTILO DE RESPUESTA:\n");
        context.append("• Responde como un asistente humano profesional\n");
        context.append("• Usa lenguaje natural y conversacional\n");
        context.append("• Sé conciso pero informativo\n");
        context.append("• Incluye emojis apropiados para claridad visual\n");
        context.append("• Si no tienes información específica, explica cómo obtenerla\n");
        context.append("• Proporciona consejos de salud dental cuando sea apropiado\n\n");
        
        context.append("🔍 EJEMPLOS DE RESPUESTAS CORRECTAS:\n");
        context.append("P: '¿Qué producto tiene menos inventario?'\n");
        context.append("R: 'El producto con menos inventario es [NOMBRE_PRODUCTO] con [X] unidades disponibles. Te recomiendo hacer un pedido pronto para evitar quedarte sin stock.'\n\n");
        context.append("P: '¿Qué productos necesitan reposición?'\n");
        context.append("R: 'Los productos que necesitan reposición urgente son: [lista con cantidades específicas]. Estos tienen stock crítico y deberían reponerse lo antes posible.'\n\n");
        
        // Contexto dinámico desde la BD
        try {
            // Pacientes
            context.append("=== DATOS ACTUALES DE LA CLÍNICA ===\n\n");
            context.append("📊 RESUMEN DE PACIENTES:\n");
            context.append(chatContextDAO.getPacientesContext()).append("\n");
            
            // Citas
            context.append("📅 RESUMEN DE CITAS:\n");
            context.append(chatContextDAO.getCitasContext()).append("\n");
            
            // Productos
            context.append("📦 RESUMEN DE INVENTARIO:\n");
            context.append(chatContextDAO.getProductosContext()).append("\n");
            
            // Información adicional
            context.append("=== INFORMACIÓN ADICIONAL ===\n");
            context.append("La clínica cuenta con sistema de gestión integral que incluye:\n");
            context.append("- Registro completo de pacientes con historial\n");
            context.append("- Sistema de citas programadas\n");
            context.append("- Control de inventario de productos dentales\n");
            context.append("- Generación de facturas\n");
            context.append("- Asistente de IA para consultas\n\n");
            
            // Consejos de salud dental
            context.append("=== CONSEJOS DE SALUD DENTAL COMUNES ===\n");
            context.append("- Cepillarse los dientes al menos 2 veces al día\n");
            context.append("- Usar hilo dental diariamente\n");
            context.append("- Visitar al dentista cada 6 meses\n");
            context.append("- Evitar alimentos azucarados en exceso\n");
            context.append("- Cambiar el cepillo de dientes cada 3-4 meses\n");
            context.append("- Usar enjuague bucal con flúor\n\n");
            
            System.out.println("✅ Contexto del sistema cargado exitosamente con datos de la base de datos");
            
        } catch (Exception e) {
            e.printStackTrace();
            context.append("❌ NOTA: No se pudo cargar el contexto completo de la base de datos.\n");
            context.append("Error: ").append(e.getMessage()).append("\n\n");
            System.out.println("❌ Error al cargar contexto: " + e.getMessage());
        }
        
        this.systemContext = context.toString();
        System.out.println("📝 Contexto del sistema preparado (" + context.length() + " caracteres)");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chatView.sendButton) {
            sendMessage();
        } else if (e.getSource() == chatView.clearButton) {
            clearChat();
        }
    }
    
    private void sendMessage() {
        String userMessage = chatView.getMessageText();
        
        if (userMessage.isEmpty()) {
            return;
        }
        
        // Agregar mensaje del usuario
        ChatMessage userChatMessage = new ChatMessage(userMessage, ChatMessage.MessageType.USER);
        chatView.addMessage(userChatMessage);
        chatView.clearMessageField();
        
        // Deshabilitar entrada mientras se procesa
        chatView.setInputEnabled(false);
        
        // Verificar comandos especiales
        String directResponse = processSpecialCommands(userMessage);
        if (directResponse != null) {
            ChatMessage directResponseMessage = new ChatMessage(directResponse, ChatMessage.MessageType.ASSISTANT);
            chatView.addMessage(directResponseMessage);
            chatView.setInputEnabled(true); // Rehabilitar
            return;
        }
        
        // Preguntas específicas de inventario
        directResponse = processInventoryQuestions(userMessage);
        if (directResponse != null) {
            ChatMessage inventoryResponseMessage = new ChatMessage(directResponse, ChatMessage.MessageType.ASSISTANT);
            chatView.addMessage(inventoryResponseMessage);
            chatView.setInputEnabled(true); // Rehabilitar
            return;
        }
        
        // Procesar en background si no es comando especial
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                System.out.println("DEBUG - Enviando mensaje a LM Studio: " + userMessage);
                
                // Verificar LM Studio
                if (!lmStudioService.isLMStudioAvailable()) {
                    System.out.println("DEBUG - LM Studio no disponible");
                    return "❌ No se puede conectar con LM Studio. Por favor:\n\n" +
                           "1. Asegúrate de que LM Studio esté ejecutándose\n" +
                           "2. Verifica que el servidor esté activo en http://localhost:1234\n" +
                           "3. Carga un modelo en LM Studio\n\n" +
                           "Una vez que LM Studio esté listo, podrás usar el asistente de IA.";
                }
                
                System.out.println("DEBUG - LM Studio disponible, enviando mensaje");
                
                // Filtrar conversación (sin bienvenida)
                List<ChatMessage> conversationMessages = filterConversationMessages(chatView.getMessages());
                
                // Enviar a LM Studio con contexto completo
                System.out.println("DEBUG - Enviando " + conversationMessages.size() + " mensajes con contexto de " + systemContext.length() + " caracteres");
                String aiResponse = lmStudioService.sendMessage(conversationMessages, systemContext);
                System.out.println("DEBUG - Respuesta de LM Studio (" + aiResponse.length() + " caracteres): " + 
                                 (aiResponse.length() > 200 ? aiResponse.substring(0, 200) + "..." : aiResponse));
                
                return aiResponse;
            }
            
            @Override
            protected void done() {
                try {
                    String response = get();
                    
                    // Agregar respuesta de la IA
                    ChatMessage aiResponse = new ChatMessage(response, ChatMessage.MessageType.ASSISTANT);
                    chatView.addMessage(aiResponse);
                    
                } catch (Exception e) {
                    // Manejar errores
                    String errorMessage = "Error al procesar el mensaje: " + e.getMessage();
                    ChatMessage errorResponse = new ChatMessage(errorMessage, ChatMessage.MessageType.ASSISTANT);
                    chatView.addMessage(errorResponse);
                } finally {
                    // Rehabilitar
                    chatView.setInputEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Procesa comandos especiales sin necesidad de IA
     */
    private String processSpecialCommands(String message) {
        String lowerMessage = message.toLowerCase().trim();
        
        // Solo procesar si empieza con /
        if (!lowerMessage.startsWith("/")) {
            return null; // No es un comando especial
        }
        
        // Comando de ayuda
        if (lowerMessage.equals("/ayuda") || lowerMessage.equals("/help")) {
            return "📋 **Comandos disponibles:**\n\n" +
                   "• `/ayuda` - Muestra esta ayuda\n" +
                   "• `/pacientes` - Información sobre pacientes\n" +
                   "• `/citas` - Información sobre citas\n" +
                   "• `/productos` - Información sobre productos\n" +
                   "• `/inventario` - Estado del inventario\n" +
                   "• `/stock` - Productos con stock bajo/crítico\n" +
                   "• `/buscar [nombre]` - Buscar paciente\n" +
                   "• `/estado` - Estado completo del sistema\n" +
                   "• `/refrescar` - Actualizar datos de la clínica\n" +
                   "• `/modelos` - Lista de modelos disponibles\n" +
                   "• `/modelo` - Ver modelo actual\n" +
                   "• `/modelo [nombre]` - Cambiar modelo de IA\n\n" +
                   "**También puedes hacer preguntas en lenguaje natural como:**\n" +
                   "• '¿Qué producto tiene menos inventario?'\n" +
                   "• '¿Qué tratamientos ofrecen?'\n" +
                   "• '¿Cuándo debo cambiar mi cepillo de dientes?'\n" +
                   "• 'Dime sobre el paciente Juan Pérez'\n" +
                   "• '¿Qué productos necesitan reposición?'";
        }
        
        // Información de pacientes
        if (lowerMessage.equals("/pacientes")) {
            try {
                return "👥 **Información de Pacientes:**\n\n" + chatContextDAO.getPacientesContext();
            } catch (Exception e) {
                return "Error al obtener información de pacientes: " + e.getMessage();
            }
        }
        
        // Información de citas
        if (lowerMessage.equals("/citas")) {
            try {
                return "📅 **Información de Citas:**\n\n" + chatContextDAO.getCitasContext();
            } catch (Exception e) {
                return "Error al obtener información de citas: " + e.getMessage();
            }
        }
        
        // Información de productos
        if (lowerMessage.equals("/productos") || lowerMessage.equals("/inventario")) {
            try {
                return "📦 **Información de Productos:**\n\n" + chatContextDAO.getProductosContext();
            } catch (Exception e) {
                return "Error al obtener información de productos: " + e.getMessage();
            }
        }
        
        // Buscar paciente
        if (lowerMessage.startsWith("/buscar ")) {
            String searchTerm = message.substring(8).trim();
            if (!searchTerm.isEmpty()) {
                try {
                    return "🔍 **Búsqueda de Pacientes:**\n\n" + chatContextDAO.buscarPaciente(searchTerm);
                } catch (Exception e) {
                    return "Error al buscar paciente: " + e.getMessage();
                }
            } else {
                return "Por favor proporciona un término de búsqueda. Ejemplo: `/buscar Juan`";
            }
        }
        
        // Estado de LM Studio
        if (lowerMessage.equals("/estado")) {
            boolean available = lmStudioService.isLMStudioAvailable();
            StringBuilder status = new StringBuilder();
            
            if (available) {
                status.append("✅ **LM Studio está disponible y listo para usar**\n\n");
                status.append("🤖 **Modelo configurado:** ").append(LMStudioService.getCurrentModel()).append("\n");
                status.append("📊 **Contexto cargado:** ").append(systemContext.length()).append(" caracteres\n");
                status.append("🔧 **Temperatura:** 0.1 (respuestas más precisas)\n");
                status.append("📝 **Tokens máximos:** 16,000\n\n");
                status.append("**Estado de la base de datos:**\n");
                try {
                    chatContextDAO.getPacientesContext(); // Test de conexión
                    status.append("✅ Base de datos conectada\n");
                } catch (Exception e) {
                    status.append("❌ Error en base de datos: ").append(e.getMessage()).append("\n");
                }
                status.append("\n💡 **Tip:** Usa `/refrescar` para actualizar los datos de la clínica");
            } else {
                status.append("❌ **LM Studio no está disponible**\n\n");
                status.append("**Verifica que:**\n");
                status.append("- LM Studio esté ejecutándose\n");
                status.append("- El servidor esté activo en http://localhost:1234\n");
                status.append("- Tengas un modelo cargado y funcionando\n");
            }
            
            return status.toString();
        }
        
        // Refrescar contexto de la base de datos
        if (lowerMessage.equals("/refrescar")) {
            try {
                loadSystemContext(); // Recargar contexto completo
                return "✅ **Contexto actualizado exitosamente**\n\n" +
                       "📊 Se han recargado los datos más recientes de:\n" +
                       "- Pacientes registrados\n" +
                       "- Citas programadas\n" +
                       "- Inventario de productos\n" +
                       "- Estadísticas de la clínica\n\n" +
                       "El asistente ya tiene acceso a la información más actualizada.";
            } catch (Exception e) {
                return "❌ **Error al refrescar el contexto**\n\n" +
                       "Error: " + e.getMessage() + "\n\n" +
                       "Verifica la conexión a la base de datos.";
            }
        }
        
        // Lista de modelos disponibles
        if (lowerMessage.equals("/modelos")) {
            return lmStudioService.getAvailableModels();
        }
        
        // Cambiar modelo de IA
        if (lowerMessage.startsWith("/modelo ")) {
            String newModel = message.substring(8).trim();
            return LMStudioService.changeModel(newModel);
        }
        
        // Stock bajo - comando directo para productos con bajo inventario
        if (lowerMessage.equals("/stock") || lowerMessage.equals("/stockbajo")) {
            try {
                return "📦 **PRODUCTOS CON STOCK BAJO/CRÍTICO:**\n\n" + chatContextDAO.getProductosContext();
            } catch (Exception e) {
                return "Error al obtener información de stock: " + e.getMessage();
            }
        }
        
        // Mostrar modelo actual
        if (lowerMessage.equals("/modelo")) {
            return "🤖 **Modelo actual:** " + LMStudioService.getCurrentModel() + "\n\n" +
                   "**Para cambiar el modelo:**\n" +
                   "`/modelo nombre-del-modelo`\n\n" +
                   "**Ejemplo:** `/modelo microsoft/DialoGPT-medium`\n\n" +
                   "Usa `/modelos` para ver todos los modelos disponibles.";
        }
        
        return null; // No es un comando especial
    }
    
    /**
     * Procesa preguntas específicas sobre inventario con respuestas directas y naturales
     */
    private String processInventoryQuestions(String message) {
        String lowerMessage = message.toLowerCase().trim();
        
        // Detectar preguntas sobre el producto con menos stock/inventario
        if (lowerMessage.contains("producto") && 
            (lowerMessage.contains("menos") || lowerMessage.contains("menor")) && 
            (lowerMessage.contains("stock") || lowerMessage.contains("inventario"))) {
            
            try {
                String productosContext = chatContextDAO.getProductosContext();
                
                // Buscar el primer producto en la sección de stock crítico
                String[] lines = productosContext.split("\n");
                for (String line : lines) {
                    if (line.contains("🔴 MENOR STOCK →")) {
                        // Extraer información del producto
                        String productInfo = line.substring(line.indexOf("→") + 1).trim();
                        String productName = productInfo.substring(0, productInfo.indexOf("→")).trim();
                        String stockInfo = productInfo.substring(productInfo.indexOf("→") + 1, productInfo.indexOf("unidades")).trim();
                        
                        return String.format("📦 El producto con menos inventario es **%s** con **%s unidades** disponibles.\n\n" +
                                           "⚠️ Este producto tiene stock crítico y necesita reposición urgente. " +
                                           "Te recomiendo hacer un pedido lo antes posible para evitar quedarte sin stock.\n\n" +
                                           "💡 ¿Te gustaría ver todos los productos que necesitan reposición?", 
                                           productName, stockInfo);
                    }
                }
                
                // Si no hay productos con stock crítico
                return "✅ ¡Buenas noticias! No tienes productos con stock crítico en este momento. " +
                       "Todos tus productos tienen un inventario adecuado (5+ unidades).\n\n" +
                       "💡 ¿Te gustaría ver el estado general del inventario?";
                       
            } catch (Exception e) {
                return "❌ No pude acceder a la información del inventario en este momento. " +
                       "Por favor, intenta de nuevo o usa el comando `/productos` para ver el inventario.";
            }
        }
        
        // Detectar preguntas sobre productos que necesitan reposición
        if ((lowerMessage.contains("productos") || lowerMessage.contains("producto")) && 
            (lowerMessage.contains("reposición") || lowerMessage.contains("reponer") || 
             lowerMessage.contains("comprar") || lowerMessage.contains("pedir") ||
             lowerMessage.contains("poco") || lowerMessage.contains("bajo"))) {
            
            try {
                String productosContext = chatContextDAO.getProductosContext();
                StringBuilder response = new StringBuilder();
                response.append("📦 **Productos que necesitan reposición:**\n\n");
                
                boolean foundCritical = false;
                boolean foundLow = false;
                
                // Buscar productos con stock crítico
                String[] lines = productosContext.split("\n");
                response.append("🚨 **URGENTE - Stock crítico (menos de 5 unidades):**\n");
                for (String line : lines) {
                    if (line.contains("🔴 MENOR STOCK →") || line.contains("⚠️") && line.contains("→") && line.contains("unidades")) {
                        if (line.contains("crítico") || 
                            (line.contains("→") && !line.contains("PRODUCTOS CON STOCK") && !line.contains("NOTA:"))) {
                            
                            String productInfo = line.substring(line.lastIndexOf("→") + 1).trim();
                            response.append("• ").append(productInfo).append("\n");
                            foundCritical = true;
                        }
                    }
                }
                
                if (!foundCritical) {
                    response.append("✅ No hay productos con stock crítico\n");
                }
                
                response.append("\n⚡ **PRONTO - Stock bajo (5-9 unidades):**\n");
                for (String line : lines) {
                    if (line.contains("⚡") && line.contains("→") && line.contains("unidades") && 
                        !line.contains("PRODUCTOS CON STOCK BAJO")) {
                        String productInfo = line.substring(line.lastIndexOf("→") + 1).trim();
                        response.append("• ").append(productInfo).append("\n");
                        foundLow = true;
                    }
                }
                
                if (!foundLow) {
                    response.append("✅ No hay productos con stock bajo\n");
                }
                
                response.append("\n💡 **Recomendación:** Prioriza la reposición de productos con stock crítico.");
                
                return response.toString();
                
            } catch (Exception e) {
                return "❌ No pude acceder a la información del inventario. " +
                       "Usa el comando `/productos` para ver el inventario completo.";
            }
        }
        
        return null; // No es una pregunta específica sobre inventario
    }
    
    private void clearChat() {
        int confirmation = javax.swing.JOptionPane.showConfirmDialog(
            chatView,
            "¿Estás seguro de que deseas limpiar el chat?",
            "Confirmar Limpieza",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
            chatView.clearChat();
        }
    }
    
    @Override
    public void init() {
        // Recargar contexto cuando se abre el panel
        loadSystemContext();
        
        // Inicializar el panel de chat
        chatView.initializePanel();
        
        // Verificar estado de LM Studio y mostrar mensaje de bienvenida
        StringBuilder welcomeMessage = new StringBuilder();
        
        welcomeMessage.append("🦷 **¡Bienvenido al Asistente de IA de la Clínica Dental!**\n\n");
        
        if (!lmStudioService.isLMStudioAvailable()) {
            welcomeMessage.append("⚠️ **LM Studio no está disponible** - Solo comandos básicos funcionarán\n\n");
            chatView.setStatus("LM Studio no disponible - Solo comandos básicos");
        } else {
            welcomeMessage.append("✅ **LM Studio conectado** - IA lista para ayudarte\n");
            welcomeMessage.append("🤖 **Modelo:** ").append(LMStudioService.getCurrentModel()).append("\n\n");
            chatView.setStatus("LM Studio conectado - IA lista");
        }
        
        // Agregar comandos disponibles
        welcomeMessage.append("📋 **Comandos disponibles:**\n\n");
        welcomeMessage.append("• `/ayuda` - Muestra la ayuda completa\n");
        welcomeMessage.append("• `/pacientes` - Información sobre pacientes\n");
        welcomeMessage.append("• `/citas` - Información sobre citas\n");
        welcomeMessage.append("• `/productos` - Información sobre productos\n");
        welcomeMessage.append("• `/inventario` - Estado del inventario\n");
        welcomeMessage.append("• `/buscar [nombre]` - Buscar paciente\n");
        welcomeMessage.append("• `/estado` - Estado completo del sistema\n");
        welcomeMessage.append("• `/refrescar` - Actualizar datos de la clínica\n");
        welcomeMessage.append("• `/modelos` - Lista de modelos de IA disponibles\n\n");
        
        welcomeMessage.append("💬 **También puedes hacer preguntas en lenguaje natural:**\n");
        welcomeMessage.append("• '¿Cómo puedo cuidar mis dientes?'\n");
        welcomeMessage.append("• '¿Qué tratamientos dentales ofrecen?'\n");
        welcomeMessage.append("• 'Dime sobre el paciente Juan Pérez'\n");
        welcomeMessage.append("• '¿Qué productos necesitan reposición?'\n\n");
        
        welcomeMessage.append("🚀 **¡Comienza escribiendo tu pregunta o comando!**");
        
        // Mostrar mensaje de bienvenida como si fuera del asistente
        ChatMessage welcomeChatMessage = new ChatMessage(welcomeMessage.toString(), ChatMessage.MessageType.ASSISTANT);
        chatView.addMessage(welcomeChatMessage);
    }
    
    /**
     * Filtra los mensajes para enviar solo la conversación real al modelo de IA
     * Excluye el mensaje de bienvenida y otros mensajes del sistema
     */
    private List<ChatMessage> filterConversationMessages(List<ChatMessage> allMessages) {
        List<ChatMessage> filtered = new ArrayList<>();
        
        for (ChatMessage message : allMessages) {
            // Excluir mensajes de bienvenida (que contienen "Bienvenido al Asistente")
            if (message.getType() == ChatMessage.MessageType.ASSISTANT && 
                message.getContent().contains("¡Bienvenido al Asistente de IA")) {
                continue;
            }
            
            // Excluir comandos que se procesan localmente
            if (message.getType() == ChatMessage.MessageType.USER && 
                message.getContent().trim().startsWith("/")) {
                continue;
            }
            
            // Incluir solo mensajes de conversación real
            filtered.add(message);
        }
        
        return filtered;
    }
}
