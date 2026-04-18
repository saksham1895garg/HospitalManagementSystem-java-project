package hms.ui;

import hms.models.Bill;
import hms.services.BillService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BillingPanel extends JPanel {

    private BillService       service = new BillService();
    private DefaultTableModel tableModel;
    private JTable            table;

    public BillingPanel() {
        setLayout(new BorderLayout());
        buildUI();
        loadTable();
    }

    private void buildUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(new Color(245, 245, 245));
        JLabel title = new JLabel("Billing Management");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        top.add(title);

        JButton generateBtn = new JButton("+ Generate Bill");
        JButton refreshBtn  = new JButton("Refresh");
        generateBtn.setBackground(new Color(33, 97, 140));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFocusPainted(false);
        top.add(generateBtn);
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        String[] cols = {"Bill ID", "Patient", "Appointment ID", "Consult Fee", "Medicine", "Total", "Status", "Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JLabel revenueLabel = new JLabel("Total Revenue: Rs.0.00");
        revenueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JButton paidBtn = new JButton("Mark as Paid");
        paidBtn.setBackground(new Color(39, 174, 96));
        paidBtn.setForeground(Color.WHITE);
        paidBtn.setFocusPainted(false);
        bottom.add(revenueLabel);
        bottom.add(Box.createHorizontalStrut(20));
        bottom.add(paidBtn);
        add(bottom, BorderLayout.SOUTH);

        generateBtn.addActionListener(e -> openGenerateDialog());
        refreshBtn.addActionListener(e -> { loadTable(); revenueLabel.setText("Total Revenue: Rs." + service.getTotalRevenue()); });
        paidBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a bill first."); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            if (service.markAsPaid(id)) {
                loadTable();
                revenueLabel.setText("Total Revenue: Rs." + service.getTotalRevenue());
                JOptionPane.showMessageDialog(this, "Marked as paid!");
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Bill b : service.getAllBills()) {
            tableModel.addRow(new Object[]{
                    b.getBillId(), b.getPatientName(), b.getAppointmentId(),
                    "Rs." + b.getConsultationFee(), "Rs." + b.getMedicineCharge(),
                    "Rs." + b.getTotalAmount(), b.getPaymentStatus(), b.getBillDate()
            });
        }
    }

    private void openGenerateDialog() {
        JTextField patientId     = new JTextField();
        JTextField appointmentId = new JTextField();
        JTextField consultFee    = new JTextField();
        JTextField medCharge     = new JTextField("0.00");

        Object[] fields = {
                "Patient ID:", patientId,
                "Appointment ID:", appointmentId,
                "Consultation Fee:", consultFee,
                "Medicine Charge:", medCharge
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Generate Bill", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                if (service.generateBill(
                        Integer.parseInt(patientId.getText().trim()),
                        Integer.parseInt(appointmentId.getText().trim()),
                        Double.parseDouble(consultFee.getText().trim()),
                        Double.parseDouble(medCharge.getText().trim()))) {
                    loadTable();
                    JOptionPane.showMessageDialog(this, "Bill generated!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}