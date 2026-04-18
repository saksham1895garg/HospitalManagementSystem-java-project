package hms.ui;

import hms.services.*;

import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {

    private DoctorService      doctorService      = new DoctorService();
    private PatientService     patientService     = new PatientService();
    private AppointmentService appointmentService = new AppointmentService();
    private BillService        billService        = new BillService();
    private MedicineService    medicineService    = new MedicineService();

    public ReportsPanel() {
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(new Color(245, 245, 245));
        JLabel title = new JLabel("Reports & Summary");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        top.add(title);

        JButton refreshBtn = new JButton("Refresh");
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 3, 20, 20));
        cards.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        cards.setBackground(Color.WHITE);

        JLabel totalDoctors      = statCard("Total Doctors",      "0", new Color(52, 152, 219));
        JLabel totalPatients     = statCard("Total Patients",     "0", new Color(39, 174, 96));
        JLabel totalAppointments = statCard("Appointments",       "0", new Color(155, 89, 182));
        JLabel totalRevenue      = statCard("Total Revenue",      "Rs.0", new Color(230, 126, 34));
        JLabel expiredMeds       = statCard("Expired Medicines",  "0", new Color(231, 76, 60));
        JLabel lowStock          = statCard("Low Stock Items",    "0", new Color(241, 196, 15));

        cards.add(totalDoctors);
        cards.add(totalPatients);
        cards.add(totalAppointments);
        cards.add(totalRevenue);
        cards.add(expiredMeds);
        cards.add(lowStock);
        add(cards, BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> {
            ((JLabel) totalDoctors     .getClientProperty("val")).setText(String.valueOf(doctorService.getAllDoctors().size()));
            ((JLabel) totalPatients    .getClientProperty("val")).setText(String.valueOf(patientService.getAllPatients().size()));
            ((JLabel) totalAppointments.getClientProperty("val")).setText(String.valueOf(appointmentService.getAllAppointments().size()));
            ((JLabel) totalRevenue     .getClientProperty("val")).setText("Rs." + billService.getTotalRevenue());
            ((JLabel) expiredMeds      .getClientProperty("val")).setText(String.valueOf(medicineService.getExpiredMedicines().size()));
            ((JLabel) lowStock         .getClientProperty("val")).setText(String.valueOf(medicineService.getLowStockMedicines().size()));
        });

        refreshBtn.doClick();
    }

    private JLabel statCard(String label, String value, Color color) {
        JLabel card = new JLabel();
        card.setLayout(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl.setForeground(Color.WHITE);

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("Arial", Font.BOLD, 36));
        val.setForeground(Color.WHITE);

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        card.putClientProperty("val", val);
        return card;
    }
}