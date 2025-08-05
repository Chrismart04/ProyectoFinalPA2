
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import view.PrincipalView;

public class PrincipalController implements ActionListener {

    PrincipalView frame;
    CitaController citaController;
    PacienteController pacienteController;
    ProductoController productoController;
    ListaProductoController listaProductoController;
    
    // Login credentials
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "12345678";

    //Constructor
    public PrincipalController(PrincipalView frame) {
        super();
        this.frame = frame;

        // Login listeners
        this.frame.btnLogin.addActionListener(this);
        this.frame.btnCancel.addActionListener(this);
        
        // Main interface listeners
        this.frame.btnCita.addActionListener(this);
        this.frame.btnPaciente.addActionListener(this);
        this.frame.btnProducto.addActionListener(this);
        this.frame.btnListaProducto.addActionListener(this);
        this.frame.btnLogout.addActionListener(this);
        
        // Keyboard listeners for login
        this.frame.txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    authenticate();
                }
            }
        });
        
        this.frame.txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.txtPassword.requestFocus();
                }
            }
        });

        citaController = new CitaController(frame);
        pacienteController = new PacienteController(frame);
        productoController = new ProductoController(frame);
        listaProductoController = new ListaProductoController(frame);
    }

    //Metodos
    public void actionPerformed(ActionEvent e) {
        // Login actions
        if (e.getSource() == this.frame.btnLogin) {
            authenticate();
        }
        if (e.getSource() == this.frame.btnCancel) {
            System.exit(0);
        }
        
        // Main interface actions
        if (e.getSource() == this.frame.btnCita) {
            cambiarPaneles(this.frame.panelCita, citaController);
        }
        if (e.getSource() == this.frame.btnPaciente) {
            cambiarPaneles(this.frame.panelPaciente, pacienteController);
        }
        if (e.getSource() == this.frame.btnProducto) {
            cambiarPaneles(this.frame.panelProducto, productoController);
        }
        if (e.getSource() == this.frame.btnListaProducto) {
            cambiarPaneles(this.frame.panelListaProducto, listaProductoController);
        }
        if (e.getSource() == this.frame.btnLogout) {
            logout();
        }
    }
    
    private void authenticate() {
        String username = frame.txtUsername.getText().trim();
        String password = new String(frame.txtPassword.getPassword());
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "Por favor, complete todos los campos.", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD)) {
            JOptionPane.showMessageDialog(frame, 
                "¡Bienvenido al Sistema", 
                "Login Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear login fields
            frame.txtUsername.setText("");
            frame.txtPassword.setText("");
            
            // Show main interface
            frame.showMainPanel();
        } else {
            JOptionPane.showMessageDialog(frame, 
                "Usuario o contraseña incorrectos.\n\nUsuario: admin\nContraseña: 12345678", 
                "Error de Autenticación", 
                JOptionPane.ERROR_MESSAGE);
            
            frame.txtPassword.setText("");
            frame.txtPassword.requestFocus();
        }
    }

    public void cambiarPaneles(JPanel panelActivar, AbstractPanelController panelController) {
        this.frame.panelCita.setVisible(false);
        this.frame.panelPaciente.setVisible(false);
        this.frame.panelProducto.setVisible(false);
        this.frame.panelListaProducto.setVisible(false);
        panelActivar.setVisible(true);
        panelController.init();
    }
    
    private void logout() {
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            frame, 
            "¿Está seguro que desea cerrar sesión?", 
            "Confirmar Cierre de Sesión", 
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            // Hide all panels
            this.frame.panelCita.setVisible(false);
            this.frame.panelPaciente.setVisible(false);
            this.frame.panelProducto.setVisible(false);
            this.frame.panelListaProducto.setVisible(false);
            
            // Show login panel
            frame.showLoginPanel();
        }
    }
}
