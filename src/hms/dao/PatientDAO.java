package hms.dao;

import hms.models.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    private Connection conn = DBConnection.getConnection();

    public boolean addPatient(Patient p) {
        String sql = "INSERT INTO patients (name, age, gender, phone, email, blood_group, address) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt   (2, p.getAge());
            ps.setString(3, p.getGender());
            ps.setString(4, p.getPhone());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getBloodGroup());
            ps.setString(7, p.getAddress());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("addPatient error: " + e.getMessage());
            return false;
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("getAllPatients error: " + e.getMessage());
        }
        return list;
    }

    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("getPatientById error: " + e.getMessage());
        }
        return null;
    }

    public List<Patient> searchByName(String name) {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE name LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("searchByName error: " + e.getMessage());
        }
        return list;
    }

    public boolean updatePatient(Patient p) {
        String sql = "UPDATE patients SET name=?, age=?, phone=?, email=?, blood_group=?, address=? WHERE patient_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt   (2, p.getAge());
            ps.setString(3, p.getPhone());
            ps.setString(4, p.getEmail());
            ps.setString(5, p.getBloodGroup());
            ps.setString(6, p.getAddress());
            ps.setInt   (7, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("updatePatient error: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("deletePatient error: " + e.getMessage());
            return false;
        }
    }

    private Patient mapRow(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getInt   ("patient_id"),
                rs.getString("name"),
                rs.getInt   ("age"),
                rs.getString("gender"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("blood_group"),
                rs.getString("address")
        );
    }
}