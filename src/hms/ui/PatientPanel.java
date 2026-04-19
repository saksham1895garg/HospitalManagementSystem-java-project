package hms.ui;

import hms.models.Patient;
import hms.services.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {

    private PatientService     service = new PatientService();
    private DefaultTableModel  tableModel;
    private JTable             table;
    private JTextField         searchField;

    public PatientPanel() {
        setLayout(new BorderLayout());
        buildUI();
        loadTable(service.getAllPatients());
    }

    private void buildUI() {
        // Top bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Patient Management");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        top.add(title);

        top.add(Box.createHorizontalStrut(20));
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton showAll   = new JButton("Show All");
        JButton addBtn    = new JButton("+ Add Patient");
        addBtn.setBackground(new Color(33, 97, 140));
        addBtn.setForeground(Color.BLACK);
        addBtn.setFocusPainted(false);

        top.add(new JLabel("Search name:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(showAll);
        top.add(addBtn);
        add(top, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Name", "Age", "Gender", "Phone", "Blood Group", "Address"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton editBtn   = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(255, 0, 0));
        deleteBtn.setForeground(Color.RED);
        deleteBtn.setFocusPainted(false);
        bottom.add(editBtn);
        bottom.add(deleteBtn);
        add(bottom, BorderLayout.SOUTH);

        // Listeners
        searchBtn.addActionListener(e ->
                loadTable(service.searchByName(searchField.getText().trim())));
        showAll.addActionListener(e ->
                loadTable(service.getAllPatients()));
        addBtn.addActionListener(e -> openAddDialog());
        deleteBtn.addActionListener(e -> deleteSelected());
        editBtn.addActionListener(e -> openEditDialog());
    }

    private void loadTable(List<Patient> list) {
        tableModel.setRowCount(0);
        for (Patient p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getAge(), p.getGender(),
                    p.getPhone(), p.getBloodGroup(), p.getAddress()
            });
        }
    }

    private void openAddDialog() {
        JTextField name   = new JTextField();
        JTextField age    = new JTextField();
        JComboBox<String> gender = new JComboBox<>(new String[]{"Male","Female","Other"});
        JTextField phone  = new JTextField();
        JTextField email  = new JTextField();
        JTextField blood  = new JTextField();
        JTextField addr   = new JTextField();

        Object[] fields = {
                "Name:",    name,
                "Age:",     age,
                "Gender:",  gender,
                "Phone:",   phone,
                "Email:",   email,
                "Blood Group:", blood,
                "Address:", addr
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Add Patient", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Patient p = new Patient(0,
                        name.getText().trim(),
                        Integer.parseInt(age.getText().trim()),
                        (String) gender.getSelectedItem(),
                        phone.getText().trim(),
                        email.getText().trim(),
                        blood.getText().trim(),
                        addr.getText().trim()
                );
                if (service.addPatient(p)) {
                    loadTable(service.getAllPatients());
                    JOptionPane.showMessageDialog(this, "Patient added successfully!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a patient first."); return; }

        int    id    = (int)    tableModel.getValueAt(row, 0);
        Patient p    = service.getPatientById(id);

        JTextField name  = new JTextField(p.getName());
        JTextField age   = new JTextField(String.valueOf(p.getAge()));
        JTextField phone = new JTextField(p.getPhone());
        JTextField email = new JTextField(p.getEmail());
        JTextField blood = new JTextField(p.getBloodGroup());
        JTextField addr  = new JTextField(p.getAddress());

        Object[] fields = {
                "Name:",  name, "Age:", age,
                "Phone:", phone, "Email:", email,
                "Blood:", blood, "Address:", addr
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Edit Patient", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Patient updated = new Patient(id,
                        name.getText().trim(), Integer.parseInt(age.getText().trim()),
                        p.getGender(), phone.getText().trim(), email.getText().trim(),
                        blood.getText().trim(), addr.getText().trim()
                );
                if (service.updatePatient(updated)) {
                    loadTable(service.getAllPatients());
                    JOptionPane.showMessageDialog(this, "Patient updated!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a patient first."); return; }
        int id   = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete patient: " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (service.deletePatient(id)) {
                loadTable(service.getAllPatients());
                JOptionPane.showMessageDialog(this, "Deleted successfully.");
            }
        }
    }
}