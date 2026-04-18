package hms.dao;

import hms.models.Appointment;
import hms.models.Appointment.Status;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private Connection conn = DBConnection.getConnection();

    public boolean addAppointment(Appointment a) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, a.getPatientId());
            ps.setInt   (2, a.getDoctorId());
            ps.setDate  (3, Date.valueOf(a.getDate()));
            ps.setTime  (4, Time.valueOf(a.getTime()));
            ps.setString(5, a.getStatus().name());
            ps.setString(6, a.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Slot already booked!");
            return false;
        } catch (SQLException e) {
            System.out.println("addAppointment error: " + e.getMessage());
            return false;
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS patient_name, d.name AS doctor_name " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "ORDER BY a.appointment_date, a.appointment_time";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("getAllAppointments error: " + e.getMessage());
        }
        return list;
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS patient_name, d.name AS doctor_name " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "WHERE a.doctor_id = ? " +
                "ORDER BY a.appointment_date, a.appointment_time";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("getByDoctor error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(int appointmentId, Status status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt   (2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("updateStatus error: " + e.getMessage());
            return false;
        }
    }

    public boolean isSlotTaken(int doctorId, LocalDate date, LocalTime time) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=? AND appointment_time=? AND status='Scheduled'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt  (1, doctorId);
            ps.setDate (2, Date.valueOf(date));
            ps.setTime (3, Time.valueOf(time));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("isSlotTaken error: " + e.getMessage());
        }
        return false;
    }

    private Appointment mapRow(ResultSet rs) throws SQLException {
        Appointment a = new Appointment(
                rs.getInt   ("appointment_id"),
                rs.getInt   ("patient_id"),
                rs.getInt   ("doctor_id"),
                rs.getDate  ("appointment_date").toLocalDate(),
                rs.getTime  ("appointment_time").toLocalTime(),
                Status.valueOf(rs.getString("status").toUpperCase()),
                rs.getString("notes")
        );
        try {
            a.setPatientName(rs.getString("patient_name"));
            a.setDoctorName (rs.getString("doctor_name"));
        } catch (SQLException ignored) {}
        return a;
    }
}