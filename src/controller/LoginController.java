package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import model.UserModel;
import view.LoginView;
import view.PrincipalView;

public class LoginController {
	
	private LoginView loginView;
	private PrincipalView principalView;
	
	private static final String ADMIN_USERNAME = "admin";
	private static final String USER_USERNAME = "usuario";
	private static final String DEFAULT_PASSWORD = "12345678";
	
	public LoginController(LoginView loginView) {
		this.loginView = loginView;
		initListeners();
	}
	
	private void initListeners() {
		loginView.btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				authenticate();
			}
		});
		
		loginView.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
        // Enter en contraseña
		loginView.txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					authenticate();
				}
			}
		});
		
        // Enter en usuario
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
		
		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(loginView, 
				"Por favor, complete todos los campos.", 
				"Error de Validación", 
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		UserModel authenticatedUser = authenticateUser(username, password);
        if (authenticatedUser != null) {
            // Configurar sesión
            SessionController.getInstance().setCurrentUser(authenticatedUser);
			
			String welcomeMessage = "admin".equals(authenticatedUser.getRole()) ? 
				"Bienvenido, Administrador" : "Bienvenido, Usuario";
			
			JOptionPane.showMessageDialog(loginView, 
				welcomeMessage, 
				"Login Exitoso", 
				JOptionPane.INFORMATION_MESSAGE);
			
			openMainApplication();
		} else {
			JOptionPane.showMessageDialog(loginView, 
				"Usuario o contraseña incorrectos.\n\nUsuarios válidos:\n• admin / 12345678 (Administrador)\n• usuario / 12345678 (Usuario)", 
				"Error de Autenticación", 
				JOptionPane.ERROR_MESSAGE);
			
			loginView.txtPassword.setText("");
			loginView.txtPassword.requestFocus();
		}
	}
	
	private UserModel authenticateUser(String username, String password) {
		if (!password.equals(DEFAULT_PASSWORD)) {
			return null;
		}
		
		if (username.equals(ADMIN_USERNAME)) {
			return new UserModel(1, ADMIN_USERNAME, DEFAULT_PASSWORD, "Administrador", "admin", true);
		} else if (username.equals(USER_USERNAME)) {
			return new UserModel(2, USER_USERNAME, DEFAULT_PASSWORD, "Usuario", "user", true);
		}
		
		return null;
	}
	
	private void openMainApplication() {
		loginView.setVisible(false);
		
		principalView = new PrincipalView();
		principalView.setVisible(true);
		principalView.setLocationRelativeTo(null);
		
		new PrincipalController(principalView);
	}
} 