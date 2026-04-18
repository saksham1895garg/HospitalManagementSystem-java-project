package hms.services;

import hms.dao.AdminDAO;
import hms.models.Admin;

public class AdminService {

    private AdminDAO adminDAO = new AdminDAO();

    public Admin login(String username, String password) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username and password cannot be empty");
        }
        return adminDAO.login(username, password);
        // returns null if credentials are wrong — UI handles that
    }
}