
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import view.PrincipalView;

public class PrincipalController implements ActionListener {

    PrincipalView frame;
    CitaController citaController;
    PacienteController pacienteController;

    //Constructor
    public PrincipalController(PrincipalView frame) {
        super();
        this.frame = frame;

        this.frame.btnCita.addActionListener(this);
        this.frame.btnPaciente.addActionListener(this);

        citaController = new CitaController(frame);
        pacienteController = new PacienteController(frame);
    }

    //Metodos
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.frame.btnCita) {
            cambiarPaneles(this.frame.panelCita, citaController);
        }
        if (e.getSource() == this.frame.btnPaciente) {
            cambiarPaneles(this.frame.panelPaciente, pacienteController);
        }
    }

    public void cambiarPaneles(JPanel panelActivar, AbstractPanelController panelController) {
        this.frame.panelCita.setVisible(false);
        this.frame.panelPaciente.setVisible(false);
        panelActivar.setVisible(true);
        panelController.init();
    }
}
