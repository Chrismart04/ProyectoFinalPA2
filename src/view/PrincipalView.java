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
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Font;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;

public class PrincipalView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JButton btnPaciente;
	public JButton btnCita;
	public JButton btnProducto;
	public JButton btnListaProducto;
	public JButton btnFactura; // <-- Botón nuevo
	public JButton btnLogout;
	
	// Login components
	public JPanel panelLogin;
	public JTextField txtUsername;
	public JPasswordField txtPassword;
	public JButton btnLogin;
	public JButton btnCancel;
	
	// Main interface panel
	public JPanel panelMain;
	
	public PacientePanelView panelPaciente;
	public CitaPanelView panelCita;
	public ProductoPanelView panelProducto;
	public ListaProductoPanelView panelListaProducto;
	public FacturaView panelFactura; // <-- Panel nuevo
	
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
		
		// Create login panel
		createLoginPanel();
		
		// Create main interface panel
		createMainPanel();
		
		// Initially show login panel
		showLoginPanel();
	}
	
	private void createLoginPanel() {
		panelLogin = new JPanel();
		panelLogin.setBackground(new Color(245, 245, 245));
		panelLogin.setBounds(0, 0, 1079, 589);
		contentPane.add(panelLogin);
		panelLogin.setLayout(null);
		
		// Logo
		JLabel lblLogo = new JLabel("");
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setIcon(new ImageIcon(PrincipalView.class.getResource("/img2/logo.png")));
		lblLogo.setBounds(439, 20, 200, 150);
		panelLogin.add(lblLogo);
		
		// Title
		JLabel lblTitle = new JLabel("Clínica Dental");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(new Color(60, 63, 65));
		lblTitle.setBounds(439, 180, 200, 30);
		panelLogin.add(lblTitle);
		
		// Subtitle
		JLabel lblSubtitle = new JLabel("Sistema de Gestión");
		lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
		lblSubtitle.setForeground(new Color(100, 100, 100));
		lblSubtitle.setBounds(439, 210, 200, 20);
		panelLogin.add(lblSubtitle);
		
		// Username label
		JLabel lblUsername = new JLabel("Usuario:");
		lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		lblUsername.setForeground(new Color(60, 63, 65));
		lblUsername.setBounds(439, 250, 200, 20);
		panelLogin.add(lblUsername);
		
		// Username field
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		txtUsername.setBounds(439, 275, 200, 35);
		panelLogin.add(txtUsername);
		txtUsername.setColumns(10);
		
		// Password label
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPassword.setForeground(new Color(60, 63, 65));
		lblPassword.setBounds(439, 320, 200, 20);
		panelLogin.add(lblPassword);
		
		// Password field
		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		txtPassword.setBounds(439, 345, 200, 35);
		panelLogin.add(txtPassword);
		
		// Login button
		btnLogin = new JButton("Iniciar Sesión");
		btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
		btnLogin.setForeground(new Color(255, 255, 255));
		btnLogin.setBackground(new Color(60, 63, 65));
		btnLogin.setBorder(null);
		btnLogin.setBounds(439, 400, 200, 40);
		panelLogin.add(btnLogin);
		
		// Cancel button
		btnCancel = new JButton("Cancelar");
		btnCancel.setFont(new Font("Arial", Font.PLAIN, 12));
		btnCancel.setForeground(new Color(100, 100, 100));
		btnCancel.setBackground(new Color(245, 245, 245));
		btnCancel.setBorder(null);
		btnCancel.setBounds(439, 450, 200, 30);
		panelLogin.add(btnCancel);
	}
	
	private void createMainPanel() {
		panelMain = new JPanel();
		panelMain.setBackground(new Color(245, 245, 245));
		panelMain.setBounds(0, 0, 1079, 589);
		contentPane.add(panelMain);
		panelMain.setLayout(null);
		panelMain.setVisible(false);
		
		JPanel panelLateral = new JPanel();
		panelLateral.setBackground(new Color(60, 63, 65));
		panelLateral.setBounds(0, 0, 179, 606);
		panelMain.add(panelLateral);
		panelLateral.setLayout(null);
		
		panelPaciente = new PacientePanelView();		
		panelPaciente.setBounds(176, 0, 903, 589);
		panelMain.add(panelPaciente);
		panelPaciente.setVisible(false);
		
		panelCita = new CitaPanelView();
		panelCita.setBounds(176, 0, 903, 589);
		panelMain.add(panelCita);
		panelCita.setVisible(false);
		
		panelProducto = new ProductoPanelView();
		panelProducto.setBounds(176, 0, 903, 589);
		panelMain.add(panelProducto);
		panelProducto.setVisible(false);
		
		panelListaProducto = new ListaProductoPanelView();
		panelListaProducto.setBounds(176, 0, 903, 589);
		panelMain.add(panelListaProducto);
		panelListaProducto.setVisible(false);

		panelFactura = new FacturaView(); // <-- Instanciar panel nuevo
		panelFactura.setBounds(176, 0, 903, 589);
		panelMain.add(panelFactura);
		panelFactura.setVisible(false);
				
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
		
		btnProducto = new JButton("Productos");
		btnProducto.setBorder(null);
		btnProducto.setFont(new Font("Arial", Font.PLAIN, 16));
		btnProducto.setForeground(new Color(255, 255, 255));
		btnProducto.setBorderPainted(false);
		btnProducto.setBackground(new Color(60, 63, 65));
		btnProducto.setBounds(4, 276, 169, 43);
		panelLateral.add(btnProducto);
		
		btnListaProducto = new JButton("Lista Productos");
		btnListaProducto.setBorder(null);
		btnListaProducto.setFont(new Font("Arial", Font.PLAIN, 16));
		btnListaProducto.setForeground(new Color(255, 255, 255));
		btnListaProducto.setBorderPainted(false);
		btnListaProducto.setBackground(new Color(60, 63, 65));
		btnListaProducto.setBounds(4, 320, 169, 43);
		panelLateral.add(btnListaProducto);
		
		btnLogout = new JButton("Cerrar Sesión");
		btnLogout.setBorder(null);
		btnLogout.setFont(new Font("Arial", Font.PLAIN, 14));
		btnLogout.setForeground(new Color(255, 255, 255));
		btnLogout.setBorderPainted(false);
		btnFactura = new JButton("Facturación"); // <-- Crear botón nuevo
		btnFactura.setBorder(null);
		btnFactura.setFont(new Font("Arial", Font.PLAIN, 16));
		btnFactura.setForeground(new Color(255, 255, 255));
		btnFactura.setBorderPainted(false);
		btnFactura.setBackground(new Color(60, 63, 65));
		btnFactura.setBounds(4, 364, 169, 43);
		panelLateral.add(btnFactura);

		btnLogout.setBackground(new Color(200, 50, 50));
		btnLogout.setBounds(4, 520, 169, 35);
		panelLateral.add(btnLogout);
	}
	
	public void showLoginPanel() {
		panelMain.setVisible(false);
		panelLogin.setVisible(true);
	}
	
	public void showMainPanel() {
		panelLogin.setVisible(false);
		panelMain.setVisible(true);
	}
}
