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
		
		if (username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD)) {
			JOptionPane.showMessageDialog(loginView, 
				"¡Bienvenido al Sistema", 
				"Login Exitoso", 
				JOptionPane.INFORMATION_MESSAGE);
			
			openMainApplication();
		} else {
			JOptionPane.showMessageDialog(loginView, 
				"Usuario o contraseña incorrectos.\n\nUsuario: admin\nContraseña: 12345678", 
				"Error de Autenticación", 
				JOptionPane.ERROR_MESSAGE);
			
			loginView.txtPassword.setText("");
			loginView.txtPassword.requestFocus();
		}
	}
	
	private void openMainApplication() {
		loginView.setVisible(false);
		
		principalView = new PrincipalView();
		principalView.setVisible(true);
		principalView.setLocationRelativeTo(null);
		
		new PrincipalController(principalView);
	}
} 