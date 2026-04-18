package hms.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JPanel     contentPanel;
    private CardLayout cardLayout;

    public DashboardFrame() {
        setTitle("Hospital Management System — Dashboard");
        setSize(1100, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(25, 42, 86));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel logo = new JLabel("HMS", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        sidebar.add(logo);

        String[] menus = {"Patients", "Doctors", "Appointments", "Pharmacy", "Billing", "Reports"};
        for (String menu : menus) {
            sidebar.add(sidebarButton(menu));
            sidebar.add(Box.createVerticalStrut(8));
        }

        sidebar.add(Box.createVerticalGlue());
        JButton logout = sidebarButton("Logout");
        logout.setBackground(new Color(180, 50, 50));
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(logout);

        add(sidebar, BorderLayout.WEST);

        // Content area with CardLayout
        cardLayout    = new CardLayout();
        contentPanel  = new JPanel(cardLayout);
        contentPanel.add(new PatientPanel(),     "Patients");
        contentPanel.add(new DoctorPanel(),      "Doctors");
        contentPanel.add(new AppointmentPanel(), "Appointments");
        contentPanel.add(new MedicinePanel(),    "Pharmacy");
        contentPanel.add(new BillingPanel(),     "Billing");
        contentPanel.add(new ReportsPanel(),     "Reports");
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Patients");
    }

    private JButton sidebarButton(String label) {
        JButton btn = new JButton(label);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setBackground(new Color(44, 62, 110));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> cardLayout.show(contentPanel, label));
        return btn;
    }
}