package hms.dao;

import hms.models.Medicine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {

    private Connection conn = DBConnection.getConnection();

    public boolean addMedicine(Medicine m) {
        String sql = "INSERT INTO medicines (name, category, manufacturer, price, stock_quantity, expiry_date) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getCategory());
            ps.setString(3, m.getManufacturer());
            ps.setDouble(4, m.getPrice());
            ps.setInt   (5, m.getStockQuantity());
            ps.setDate  (6, Date.valueOf(m.getExpiryDate()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("addMedicine error: " + e.getMessage());
            return false;
        }
    }

    public List<Medicine> getAllMedicines() {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("getAllMedicines error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStock(int medicineId, int newQty) {
        String sql = "UPDATE medicines SET stock_quantity = ? WHERE medicine_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQty);
            ps.setInt(2, medicineId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("updateStock error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMedicine(int id) {
        String sql = "DELETE FROM medicines WHERE medicine_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("deleteMedicine error: " + e.getMessage());
            return false;
        }
    }

    private Medicine mapRow(ResultSet rs) throws SQLException {
        return new Medicine(
                rs.getInt   ("medicine_id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("manufacturer"),
                rs.getDouble("price"),
                rs.getInt   ("stock_quantity"),
                rs.getDate  ("expiry_date").toLocalDate()
        );
    }
}