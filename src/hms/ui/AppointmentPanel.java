package hms.ui;

import hms.models.Appointment;
import hms.services.AppointmentService;
import hms.exceptions.AppointmentConflictException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentPanel extends JPanel {

    private AppointmentService service = new AppointmentService();
    private DefaultTableModel  tableModel;
    private JTable             table;

    public AppointmentPanel() {
        setLayout(new BorderLayout());
        buildUI();
        loadTable();
    }

    private void buildUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(new Color(245, 245, 245));
        JLabel title = new JLabel("Appointment Management");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        top.add(title);

        JButton addBtn      = new JButton("+ Book Appointment");
        JButton refreshBtn  = new JButton("Refresh");
        addBtn.setBackground(new Color(33, 97, 140));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        top.add(addBtn);
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        String[] cols = {"ID", "Patient", "Doctor", "Date", "Time", "Status", "Notes"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton cancelBtn   = new JButton("Cancel");
        JButton completeBtn = new JButton("Mark Complete");
        completeBtn.setBackground(new Color(39, 174, 96));
        completeBtn.setForeground(Color.GREEN);
        completeBtn.setFocusPainted(false);
        cancelBtn.setBackground(new Color(180, 50, 50));
        cancelBtn.setForeground(Color.RED);
        cancelBtn.setFocusPainted(false);
        bottom.add(completeBtn);
        bottom.add(cancelBtn);
        add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> openBookDialog());
        refreshBtn.addActionListener(e -> loadTable());
        cancelBtn.addActionListener(e -> updateStatus(false));
        completeBtn.addActionListener(e -> updateStatus(true));
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Appointment a : service.getAllAppointments()) {
            tableModel.addRow(new Object[]{
                    a.getAppointmentId(), a.getPatientName(), a.getDoctorName(),
                    a.getDate(), a.getTime(), a.getStatus(), a.getNotes()
            });
        }
    }

    private void openBookDialog() {
        JTextField patientId = new JTextField();
        JTextField doctorId  = new JTextField();
        JTextField date      = new JTextField(LocalDate.now().toString());
        JTextField time      = new JTextField("10:00:00");
        JTextField notes     = new JTextField();

        Object[] fields = {
                "Patient ID:", patientId,
                "Doctor ID:",  doctorId,
                "Date (YYYY-MM-DD):", date,
                "Time (HH:MM:SS):",  time,
                "Notes:", notes
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Book Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                boolean ok = service.bookAppointment(
                        Integer.parseInt(patientId.getText().trim()),
                        Integer.parseInt(doctorId.getText().trim()),
                        LocalDate.parse(date.getText().trim()),
                        LocalTime.parse(time.getText().trim()),
                        notes.getText().trim()
                );
                if (ok) {
                    loadTable();
                    JOptionPane.showMessageDialog(this, "Appointment booked!");
                }
            } catch (AppointmentConflictException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Slot Taken", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateStatus(boolean complete) {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment first."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        boolean ok = complete ? service.completeAppointment(id) : service.cancelAppointment(id);
        if (ok) { loadTable(); JOptionPane.showMessageDialog(this, "Status updated!"); }
    }
}