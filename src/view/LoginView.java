package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;

public class LoginView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField txtUsername;
	public JPasswordField txtPassword;
	public JButton btnLogin;
	public JButton btnCancel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView frame = new LoginView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 500);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//Theme
		FlatArcIJTheme.setup();
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		defaults.putIfAbsent("Table.alternateRowColor", Color.WHITE);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelMain = new JPanel();
		panelMain.setBackground(new Color(245, 245, 245));
		panelMain.setBounds(0, 0, 390, 490);
		contentPane.add(panelMain);
		panelMain.setLayout(null);
		
		// Logo
		JLabel lblLogo = new JLabel("");
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setIcon(new ImageIcon(LoginView.class.getResource("/img2/logo.png")));
		lblLogo.setBounds(95, 20, 200, 150);
		panelMain.add(lblLogo);
		
		// Title
		JLabel lblTitle = new JLabel("Clínica Dental");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(new Color(60, 63, 65));
		lblTitle.setBounds(95, 180, 200, 30);
		panelMain.add(lblTitle);
		
		// Subtitle
		JLabel lblSubtitle = new JLabel("Sistema de Gestión");
		lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
		lblSubtitle.setForeground(new Color(100, 100, 100));
		lblSubtitle.setBounds(95, 210, 200, 20);
		panelMain.add(lblSubtitle);
		
		// Username label
		JLabel lblUsername = new JLabel("Usuario:");
		lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		lblUsername.setForeground(new Color(60, 63, 65));
		lblUsername.setBounds(95, 250, 200, 20);
		panelMain.add(lblUsername);
		
		// Username field
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		txtUsername.setBounds(95, 275, 200, 35);
		panelMain.add(txtUsername);
		txtUsername.setColumns(10);
		
		// Password label
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPassword.setForeground(new Color(60, 63, 65));
		lblPassword.setBounds(95, 320, 200, 20);
		panelMain.add(lblPassword);
		
		// Password field
		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		txtPassword.setBounds(95, 345, 200, 35);
		panelMain.add(txtPassword);
		
		// Login button
		btnLogin = new JButton("Iniciar Sesión");
		btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
		btnLogin.setForeground(new Color(255, 255, 255));
		btnLogin.setBackground(new Color(60, 63, 65));
		btnLogin.setBorder(null);
		btnLogin.setBounds(95, 400, 200, 40);
		panelMain.add(btnLogin);
		
		// Cancel button
		btnCancel = new JButton("Cancelar");
		btnCancel.setFont(new Font("Arial", Font.PLAIN, 12));
		btnCancel.setForeground(new Color(100, 100, 100));
		btnCancel.setBackground(new Color(245, 245, 245));
		btnCancel.setBorder(null);
		btnCancel.setBounds(95, 450, 200, 30);
		panelMain.add(btnCancel);
	}
} 