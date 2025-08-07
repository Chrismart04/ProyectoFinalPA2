package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import model.ChatMessage;

public class ChatPanelView extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    // Componentes UI
    public JPanel chatPanel;
    public JScrollPane chatScrollPane;
    public JTextField messageField;
    public JButton sendButton;
    public JButton clearButton;
    public JLabel statusLabel;
    
    // Lista para mantener los mensajes
    private List<ChatMessage> messages;
    
    public ChatPanelView() {
        messages = new ArrayList<>();
        initializeComponents();
        setupLayout();
        setupStyles();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Panel principal del chat
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);
        
        // Scroll pane para el chat
        chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        chatScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // Campo de texto para escribir mensajes
        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.setPreferredSize(new Dimension(0, 35));
        
        // Botón para enviar
        sendButton = new JButton("Enviar");
        sendButton.setPreferredSize(new Dimension(80, 35));
        
        // Botón para limpiar chat
        clearButton = new JButton("Limpiar");
        clearButton.setPreferredSize(new Dimension(80, 35));
        
        // Label de estado
        statusLabel = new JLabel("Asistente de IA - Listo para ayudar");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);
        
        // Permitir enviar con Enter
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendButton.doClick();
                }
            }
        });
    }
    
    private void setupLayout() {
        // Panel superior con título y estado
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        JLabel titleLabel = new JLabel("💬 Asistente de IA - Clínica Dental");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Panel de entrada (campo de texto + botones)
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(sendButton);
        
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Agregar componentes al panel principal
        add(topPanel, BorderLayout.NORTH);
        add(chatScrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        // Agregar mensaje de bienvenida
        addWelcomeMessage();
    }
    
    private void setupStyles() {
        // Estilos para botones
        sendButton.setBackground(new Color(70, 130, 180));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        
        clearButton.setBackground(new Color(220, 53, 69));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        
        // Estilo para el campo de texto
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }
    
    private void addWelcomeMessage() {
        String welcomeText = "¡Hola! Soy tu asistente de IA para la clínica dental. " +
                           "Puedo ayudarte con información sobre pacientes, citas, productos y más. " +
                           "¿En qué puedo ayudarte hoy?\n\n" +
                           "Escribe '/ayuda' para ver los comandos disponibles.";
        
        ChatMessage welcomeMessage = new ChatMessage(welcomeText, ChatMessage.MessageType.ASSISTANT);
        addMessage(welcomeMessage);
    }
    
    /**
     * Agrega un mensaje al chat
     */
    public void addMessage(ChatMessage message) {
        messages.add(message);
        
        SwingUtilities.invokeLater(() -> {
            JPanel messagePanel = createMessagePanel(message);
            chatPanel.add(messagePanel);
            chatPanel.add(Box.createVerticalStrut(5));
            
            // Forzar actualización visual
            chatPanel.revalidate();
            chatPanel.repaint();
            
            // Scroll automático hacia abajo con un pequeño delay
            SwingUtilities.invokeLater(() -> {
                int max = chatScrollPane.getVerticalScrollBar().getMaximum();
                chatScrollPane.getVerticalScrollBar().setValue(max);
            });
        });
    }
    
    /**
     * Crea un panel visual para un mensaje
     */
    private JPanel createMessagePanel(ChatMessage message) {
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        
        // Área de texto para el mensaje
        JTextArea messageArea = new JTextArea(message.getContent());
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Panel para el timestamp
        JLabel timeLabel = new JLabel(message.getFormattedTimestamp());
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setForeground(Color.GRAY);
        
        // Estilos según el tipo de mensaje
        if (message.getType() == ChatMessage.MessageType.USER) {
            messagePanel.setBackground(new Color(230, 240, 255));
            messageArea.setBackground(new Color(230, 240, 255));
            timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            
            JLabel userLabel = new JLabel("👤 Tú");
            userLabel.setFont(new Font("Arial", Font.BOLD, 12));
            userLabel.setForeground(new Color(70, 130, 180));
            
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(230, 240, 255));
            headerPanel.add(userLabel, BorderLayout.WEST);
            headerPanel.add(timeLabel, BorderLayout.EAST);
            
            messagePanel.add(headerPanel, BorderLayout.NORTH);
            
        } else if (message.getType() == ChatMessage.MessageType.ASSISTANT) {
            messagePanel.setBackground(new Color(248, 249, 250));
            messageArea.setBackground(new Color(248, 249, 250));
            timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
            
            JLabel aiLabel = new JLabel("🤖 Asistente IA");
            aiLabel.setFont(new Font("Arial", Font.BOLD, 12));
            aiLabel.setForeground(new Color(40, 167, 69));
            
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(248, 249, 250));
            headerPanel.add(aiLabel, BorderLayout.WEST);
            headerPanel.add(timeLabel, BorderLayout.EAST);
            
            messagePanel.add(headerPanel, BorderLayout.NORTH);
        }
        
        messagePanel.add(messageArea, BorderLayout.CENTER);
        messagePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        return messagePanel;
    }
    
    /**
     * Limpia todos los mensajes del chat
     */
    public void clearChat() {
        messages.clear();
        chatPanel.removeAll();
        chatPanel.revalidate();
        chatPanel.repaint();
        addWelcomeMessage();
    }
    
    /**
     * Obtiene el texto del campo de entrada
     */
    public String getMessageText() {
        return messageField.getText().trim();
    }
    
    /**
     * Limpia el campo de entrada
     */
    public void clearMessageField() {
        messageField.setText("");
    }
    
    /**
     * Actualiza el estado mostrado
     */
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
    
    /**
     * Habilita o deshabilita la entrada de usuario
     */
    public void setInputEnabled(boolean enabled) {
        messageField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
        
        if (!enabled) {
            setStatus("Procesando...");
        } else {
            setStatus("Listo para ayudar");
        }
    }
    
    /**
     * Obtiene la lista de mensajes
     */
    public List<ChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }
    
    /**
     * Inicializa el panel (útil para refrescar cuando se muestra)
     */
    public void initializePanel() {
        if (messages.isEmpty()) {
            addWelcomeMessage();
        }
        revalidate();
        repaint();
    }
}
