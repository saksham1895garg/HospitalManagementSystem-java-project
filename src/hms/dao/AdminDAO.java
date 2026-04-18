package hms.dao;

import hms.models.Admin;
import java.sql.*;

public class AdminDAO {

    private Connection conn = DBConnection.getConnection();

    public Admin login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getInt   ("admin_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println("login error: " + e.getMessage());
        }
        return null; // null = login failed
    }
}