package hms.ui;

import hms.models.Medicine;
import hms.services.MedicineService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MedicinePanel extends JPanel {

    private MedicineService   service = new MedicineService();
    private DefaultTableModel tableModel;
    private JTable            table;

    public MedicinePanel() {
        setLayout(new BorderLayout());
        buildUI();
        loadTable(service.getAllMedicines());
    }

    private void buildUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(new Color(245, 245, 245));
        JLabel title = new JLabel("Pharmacy / Medicine Inventory");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        top.add(title);

        JButton addBtn      = new JButton("+ Add Medicine");
        JButton expiredBtn  = new JButton("Show Expired");
        JButton lowStockBtn = new JButton("Low Stock");
        JButton showAll     = new JButton("Show All");
        addBtn.setBackground(new Color(33, 97, 140));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        top.add(addBtn);
        top.add(expiredBtn);
        top.add(lowStockBtn);
        top.add(showAll);
        add(top, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Category", "Manufacturer", "Price", "Stock", "Expiry"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // highlight expired rows in red
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                String expiry = (String) tableModel.getValueAt(row, 6);
                if (!sel && LocalDate.parse(expiry).isBefore(LocalDate.now()))
                    c.setBackground(new Color(255, 200, 200));
                else if (!sel)
                    c.setBackground(Color.WHITE);
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton updateStockBtn = new JButton("Update Stock");
        JButton deleteBtn      = new JButton("Delete");
        deleteBtn.setBackground(new Color(180, 50, 50));
        deleteBtn.setForeground(Color.RED);
        deleteBtn.setFocusPainted(false);
        bottom.add(updateStockBtn);
        bottom.add(deleteBtn);
        add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> openAddDialog());
        showAll.addActionListener(e -> loadTable(service.getAllMedicines()));
        expiredBtn.addActionListener(e -> loadTable(service.getExpiredMedicines()));
        lowStockBtn.addActionListener(e -> loadTable(service.getLowStockMedicines()));
        deleteBtn.addActionListener(e -> deleteSelected());
        updateStockBtn.addActionListener(e -> updateStock());
    }

    private void loadTable(List<Medicine> list) {
        tableModel.setRowCount(0);
        for (Medicine m : list) {
            tableModel.addRow(new Object[]{
                    m.getMedicineId(), m.getName(), m.getCategory(),
                    m.getManufacturer(), "Rs." + m.getPrice(),
                    m.getStockQuantity(), m.getExpiryDate().toString()
            });
        }
    }

    private void openAddDialog() {
        JTextField name   = new JTextField();
        JTextField cat    = new JTextField();
        JTextField mfr    = new JTextField();
        JTextField price  = new JTextField();
        JTextField stock  = new JTextField();
        JTextField expiry = new JTextField(LocalDate.now().plusYears(1).toString());

        Object[] fields = {
                "Name:", name, "Category:", cat, "Manufacturer:", mfr,
                "Price:", price, "Stock:", stock, "Expiry (YYYY-MM-DD):", expiry
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Medicine", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Medicine m = new Medicine(0,
                        name.getText().trim(), cat.getText().trim(),
                        mfr.getText().trim(), Double.parseDouble(price.getText().trim()),
                        Integer.parseInt(stock.getText().trim()),
                        LocalDate.parse(expiry.getText().trim())
                );
                if (service.addMedicine(m)) {
                    loadTable(service.getAllMedicines());
                    JOptionPane.showMessageDialog(this, "Medicine added!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateStock() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a medicine first."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String input = JOptionPane.showInputDialog(this, "Enter new stock quantity:");
        if (input != null) {
            try {
                if (service.updateStock(id, Integer.parseInt(input.trim()))) {
                    loadTable(service.getAllMedicines());
                    JOptionPane.showMessageDialog(this, "Stock updated!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a medicine first."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        if (service.deleteMedicine(id)) {
            loadTable(service.getAllMedicines());
            JOptionPane.showMessageDialog(this, "Deleted!");
        }
    }
}