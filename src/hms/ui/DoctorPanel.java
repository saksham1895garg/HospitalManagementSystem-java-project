package hms.ui;

import hms.models.Doctor;
import hms.services.DoctorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPanel extends JPanel {

    private DoctorService     service = new DoctorService();
    private DefaultTableModel tableModel;
    private JTable            table;
    private JTextField        searchField;

    public DoctorPanel() {
        setLayout(new BorderLayout());
        buildUI();
        loadTable(service.getAllDoctors());
    }

    private void buildUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Doctor Management");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        top.add(title);
        top.add(Box.createHorizontalStrut(20));

        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search Spec.");
        JButton showAll   = new JButton("Show All");
        JButton addBtn    = new JButton("+ Add Doctor");
        addBtn.setBackground(new Color(33, 97, 140));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);

        top.add(new JLabel("Specialization:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(showAll);
        top.add(addBtn);
        add(top, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Age", "Gender", "Specialization", "Qualification", "Fee", "Phone"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(180, 50, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        bottom.add(deleteBtn);
        add(bottom, BorderLayout.SOUTH);

        searchBtn.addActionListener(e ->
                loadTable(service.searchBySpecialization(searchField.getText().trim())));
        showAll.addActionListener(e ->
                loadTable(service.getAllDoctors()));
        addBtn.addActionListener(e -> openAddDialog());
        deleteBtn.addActionListener(e -> deleteSelected());
    }

    private void loadTable(List<Doctor> list) {
        tableModel.setRowCount(0);
        for (Doctor d : list) {
            tableModel.addRow(new Object[]{
                    d.getId(), d.getName(), d.getAge(), d.getGender(),
                    d.getSpecialization(), d.getQualification(),
                    "Rs." + d.getConsultationFee(), d.getPhone()
            });
        }
    }

    private void openAddDialog() {
        JTextField name   = new JTextField();
        JTextField age    = new JTextField();
        JComboBox<String> gender = new JComboBox<>(new String[]{"Male","Female","Other"});
        JTextField phone  = new JTextField();
        JTextField email  = new JTextField();
        JTextField spec   = new JTextField();
        JTextField qual   = new JTextField();
        JTextField fee    = new JTextField();

        Object[] fields = {
                "Name:", name, "Age:", age, "Gender:", gender,
                "Phone:", phone, "Email:", email,
                "Specialization:", spec, "Qualification:", qual,
                "Consultation Fee:", fee
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Doctor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Doctor d = new Doctor(0,
                        name.getText().trim(), Integer.parseInt(age.getText().trim()),
                        (String) gender.getSelectedItem(),
                        phone.getText().trim(), email.getText().trim(),
                        spec.getText().trim(), qual.getText().trim(),
                        Double.parseDouble(fee.getText().trim())
                );
                if (service.addDoctor(d)) {
                    loadTable(service.getAllDoctors());
                    JOptionPane.showMessageDialog(this, "Doctor added successfully!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age and Fee must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a doctor first."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete doctor: " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (service.deleteDoctor(id)) {
                loadTable(service.getAllDoctors());
                JOptionPane.showMessageDialog(this, "Deleted successfully.");
            }
        }
    }
}