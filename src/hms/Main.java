package hms;

import hms.dao.DBConnection;
import hms.ui.LoginFrame;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        DBConnection.getConnection();
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new LoginFrame().setVisible(true);
        });

    }

}