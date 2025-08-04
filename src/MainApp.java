import java.awt.EventQueue;
import view.LoginView;
import controller.LoginController;

public class MainApp {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Start with login screen
					LoginView loginView = new LoginView();
					loginView.setVisible(true);
					loginView.setLocationRelativeTo(null);
					
					// Initialize login controller
					new LoginController(loginView);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
} 