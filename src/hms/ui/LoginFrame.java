package hms.ui;

import hms.models.Admin;
import hms.services.AdminService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JButton        loginButton;
    private AdminService   adminService = new AdminService();

    public LoginFrame() {
        setTitle("Hospital Management System — Login");
        setSize(420, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout());

        // Header
        JLabel header = new JLabel("Hospital Management System", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(33, 97, 140));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        main.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        form.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        form.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(33, 97, 140));
        loginButton.setForeground(Color.BLUE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 35));
        form.add(loginButton, gbc);

        main.add(form, BorderLayout.CENTER);
        add(main);

        // Action
        loginButton.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        try {
            Admin admin = adminService.login(username, password);
            if (admin != null) {
                dispose();
                new DashboardFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}