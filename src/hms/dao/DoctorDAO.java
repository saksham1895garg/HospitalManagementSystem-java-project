package hms.dao;

import hms.models.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    private Connection conn = DBConnection.getConnection();

    public boolean addDoctor(Doctor d) {
        String sql = "INSERT INTO doctors (name, age, gender, phone, email, specialization, qualification, consultation_fee) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setInt   (2, d.getAge());
            ps.setString(3, d.getGender());
            ps.setString(4, d.getPhone());
            ps.setString(5, d.getEmail());
            ps.setString(6, d.getSpecialization());
            ps.setString(7, d.getQualification());
            ps.setDouble(8, d.getConsultationFee());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("addDoctor error: " + e.getMessage());
            return false;
        }
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("getAllDoctors error: " + e.getMessage());
        }
        return list;
    }

    public Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("getDoctorById error: " + e.getMessage());
        }
        return null;
    }

    public List<Doctor> searchBySpecialization(String spec) {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE specialization LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + spec + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("searchBySpec error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateDoctor(Doctor d) {
        String sql = "UPDATE doctors SET name=?, age=?, phone=?, email=?, specialization=?, consultation_fee=? WHERE doctor_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setInt   (2, d.getAge());
            ps.setString(3, d.getPhone());
            ps.setString(4, d.getEmail());
            ps.setString(5, d.getSpecialization());
            ps.setDouble(6, d.getConsultationFee());
            ps.setInt   (7, d.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("updateDoctor error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDoctor(int id) {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("deleteDoctor error: " + e.getMessage());
            return false;
        }
    }

    private Doctor mapRow(ResultSet rs) throws SQLException {
        return new Doctor(
                rs.getInt   ("doctor_id"),
                rs.getString("name"),
                rs.getInt   ("age"),
                rs.getString("gender"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("specialization"),
                rs.getString("qualification"),
                rs.getDouble("consultation_fee")
        );
    }
}