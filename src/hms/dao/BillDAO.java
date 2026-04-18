package hms.dao;

import hms.models.Bill;
import hms.models.Bill.PaymentStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    private Connection conn = DBConnection.getConnection();

    public boolean addBill(Bill b) {
        String sql = "INSERT INTO bills (patient_id, appointment_id, consultation_fee, medicine_charge, total_amount, payment_status) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, b.getPatientId());
            ps.setInt   (2, b.getAppointmentId());
            ps.setDouble(3, b.getConsultationFee());
            ps.setDouble(4, b.getMedicineCharge());
            ps.setDouble(5, b.getTotalAmount());
            ps.setString(6, b.getPaymentStatus().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("addBill error: " + e.getMessage());
            return false;
        }
    }

    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT b.*, p.name AS patient_name " +
                "FROM bills b " +
                "JOIN patients p ON b.patient_id = p.patient_id " +
                "ORDER BY b.bill_date DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("getAllBills error: " + e.getMessage());
        }
        return list;
    }

    public boolean markAsPaid(int billId) {
        String sql = "UPDATE bills SET payment_status = 'PAID' WHERE bill_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, billId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("markAsPaid error: " + e.getMessage());
            return false;
        }
    }

    private Bill mapRow(ResultSet rs) throws SQLException {
        Bill b = new Bill(
                rs.getInt   ("bill_id"),
                rs.getInt   ("patient_id"),
                rs.getInt   ("appointment_id"),
                rs.getDouble("consultation_fee"),
                rs.getDouble("medicine_charge"),
                rs.getDouble("total_amount"),
                PaymentStatus.valueOf(rs.getString("payment_status").toUpperCase()),
                rs.getTimestamp("bill_date").toLocalDateTime()
        );
        try { b.setPatientName(rs.getString("patient_name")); }
        catch (SQLException ignored) {}
        return b;
    }
}