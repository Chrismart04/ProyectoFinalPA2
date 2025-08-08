
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import view.PrincipalView;

public class PrincipalController implements ActionListener {

    PrincipalView frame;
    CitaController citaController;
    PacienteController pacienteController;
    ProductoController productoController;
    ListaProductoController listaProductoController;
    ChatController chatController;

    //Constructor
    public PrincipalController(PrincipalView frame) {
        super();
        this.frame = frame;
        
        // Listeners de la interfaz principal
        this.frame.btnCita.addActionListener(this);
        this.frame.btnPaciente.addActionListener(this);
        this.frame.btnProducto.addActionListener(this);
        this.frame.btnListaProducto.addActionListener(this);
        this.frame.btnFactura.addActionListener(this);
        this.frame.btnChat.addActionListener(this);
        this.frame.btnLogout.addActionListener(this);

        citaController = new CitaController(frame);
        pacienteController = new PacienteController(frame);
        productoController = new ProductoController(frame);
        listaProductoController = new ListaProductoController(frame);
        chatController = new ChatController(frame);
        
        // Abrir Chat al iniciar
        abrirChatIA();
    }
    
   
    private void abrirChatIA() {
        cambiarPaneles(this.frame.panelChat, chatController);
    }

    //Metodos
    public void actionPerformed(ActionEvent e) {
        // Acciones de la interfaz principal
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
        if (e.getSource() == this.frame.btnFactura) {
            mostrarPanelFactura();
        }
        if (e.getSource() == this.frame.btnChat) {
            cambiarPaneles(this.frame.panelChat, chatController);
        }
        if (e.getSource() == this.frame.btnLogout) {
            logout();
        }
    }

    public void cambiarPaneles(JPanel panelActivar, AbstractPanelController panelController) {
        this.frame.panelCita.setVisible(false);
        this.frame.panelPaciente.setVisible(false);
        this.frame.panelProducto.setVisible(false);
        this.frame.panelListaProducto.setVisible(false);
        this.frame.panelFactura.setVisible(false);
        this.frame.panelChat.setVisible(false);
        panelActivar.setVisible(true);
        panelController.init();
    }
    
    private void mostrarPanelFactura() {
        this.frame.panelCita.setVisible(false);
        this.frame.panelPaciente.setVisible(false);
        this.frame.panelProducto.setVisible(false);
        this.frame.panelListaProducto.setVisible(false);
        this.frame.panelFactura.setVisible(true);
        this.frame.panelChat.setVisible(false);
    }

    private void logout() {
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(frame, 
            "¿Está seguro que desea cerrar sesión?", 
            "Confirmar Cierre de Sesión", 
            javax.swing.JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            // cerrar sesion
            SessionController.getInstance().logout();
            
            frame.setVisible(false);
            frame.dispose();
            
            // Abrir ventana de login
            view.LoginView loginView = new view.LoginView();
            loginView.setVisible(true);
            new LoginController(loginView);
        }
    }
}
