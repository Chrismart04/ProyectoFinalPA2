package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import view.LoginView;
import view.PrincipalView;

public class LoginController {
	
	private LoginView loginView;
	private PrincipalView principalView;
	
	// Default credentials (in a real application, these would be stored in a database)
	private static final String DEFAULT_USERNAME = "admin";
	private static final String DEFAULT_PASSWORD = "12345678";
	
	public LoginController(LoginView loginView) {
		this.loginView = loginView;
		initListeners();
	}
	
	private void initListeners() {
		// Login button action
		loginView.btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				authenticate();
			}
		});
		
		// Cancel button action
		loginView.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		// Enter key on password field
		loginView.txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					authenticate();
				}
			}
		});
		
		// Enter key on username field
		loginView.txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginView.txtPassword.requestFocus();
				}
			}
		});
	}
	
	private void authenticate() {
		String username = loginView.txtUsername.getText().trim();
		String password = new String(loginView.txtPassword.getPassword());
		
		// Validate input
		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(loginView, 
				"Por favor, complete todos los campos.", 
				"Error de Validación", 
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Check credentials
		if (username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD)) {
			// Successful login
			JOptionPane.showMessageDialog(loginView, 
				"¡Bienvenido al Sistema de Gestión de la Clínica Dental!", 
				"Login Exitoso", 
				JOptionPane.INFORMATION_MESSAGE);
			
			// Open main application
			openMainApplication();
		} else {
			// Failed login
			JOptionPane.showMessageDialog(loginView, 
				"Usuario o contraseña incorrectos.\n\nUsuario: admin\nContraseña: 12345678", 
				"Error de Autenticación", 
				JOptionPane.ERROR_MESSAGE);
			
			// Clear password field
			loginView.txtPassword.setText("");
			loginView.txtPassword.requestFocus();
		}
	}
	
	private void openMainApplication() {
		// Hide login window
		loginView.setVisible(false);
		
		// Create and show main application
		principalView = new PrincipalView();
		principalView.setVisible(true);
		principalView.setLocationRelativeTo(null);
		
		// Initialize main controller
		new PrincipalController(principalView);
	}
} 