
package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.PrincipalController;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.Font;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;

public class PrincipalView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JButton btnPaciente;
	public JButton btnCita;
	
	public PacientePanelView panelPaciente;
	public CitaPanelView panelCita;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrincipalView frame = new PrincipalView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					new PrincipalController(frame);				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PrincipalView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1093, 626);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//Theme
		FlatArcIJTheme.setup();
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		defaults.putIfAbsent("Table.alternateRowColor", Color.WHITE);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelBack = new JPanel();
		panelBack.setBackground(new Color(245, 245, 245));
		panelBack.setBounds(0, 0, 1079, 589);
		contentPane.add(panelBack);
		panelBack.setLayout(null);
		
		JPanel panelLateral = new JPanel();
		panelLateral.setBackground(new Color(60, 63, 65));
		panelLateral.setBounds(0, 0, 179, 606);
		panelBack.add(panelLateral);
		panelLateral.setLayout(null);
		
		panelPaciente = new PacientePanelView();		
		panelPaciente.setBounds(176, 0, 903, 589);
		panelBack.add(panelPaciente);
		panelPaciente.setVisible(false);
		
		panelCita = new CitaPanelView();
		panelCita.setBounds(176, 0, 903, 589);
		panelBack.add(panelCita);
		panelCita.setVisible(true);
				
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(PrincipalView.class.getResource("/img2/logo.png")));
		lblNewLabel.setBounds(-14, 0, 208, 177);
		panelLateral.add(lblNewLabel);
		
		btnPaciente = new JButton("Pacientes");		
		btnPaciente.setBorder(null);
		btnPaciente.setFont(new Font("Arial", Font.PLAIN, 16));
		btnPaciente.setForeground(new Color(255, 255, 255));
		btnPaciente.setBorderPainted(false);
		btnPaciente.setBackground(new Color(60, 63, 65));
		btnPaciente.setBounds(4, 188, 169, 43);
		panelLateral.add(btnPaciente);
		
		btnCita = new JButton("Citas");
		btnCita.setBorder(null);
		btnCita.setFont(new Font("Arial", Font.PLAIN, 16));
		btnCita.setForeground(new Color(255, 255, 255));
		btnCita.setBorderPainted(false);
		btnCita.setBackground(new Color(60, 63, 65));
		btnCita.setBounds(4, 232, 169, 43);
		panelLateral.add(btnCita);
	}
}
