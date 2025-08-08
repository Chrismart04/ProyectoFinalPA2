package controller;

import model.UserModel;

public class SessionController {
    private static SessionController instance;
    private UserModel currentUser;

    private SessionController() {
    }

    public static SessionController getInstance() {
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }

    public void setCurrentUser(UserModel user) {
        this.currentUser = user;
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public boolean hasRole(String role) {
        return currentUser != null && role.equals(currentUser.getRole());
    }

    public boolean canDeleteProducts() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }

    public void logout() {
        currentUser = null;
    }
}


