package hms.services;

import hms.dao.AppointmentDAO;
import hms.exceptions.AppointmentConflictException;
import hms.exceptions.DoctorNotFoundException;
import hms.exceptions.PatientNotFoundException;
import hms.models.Appointment;
import hms.models.Appointment.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentService {

    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private DoctorService  doctorService  = new DoctorService();
    private PatientService patientService = new PatientService();

    public boolean bookAppointment(int patientId, int doctorId,
                                   LocalDate date, LocalTime time, String notes) {

        // validate both exist — throws if not found
        patientService.getPatientById(patientId);
        doctorService.getDoctorById(doctorId);

        // check slot availability
        if (appointmentDAO.isSlotTaken(doctorId, date, time)) {
            throw new AppointmentConflictException(
                    "Dr. ID " + doctorId + " is already booked at " + date + " " + time
            );
        }

        Appointment a = new Appointment(0, patientId, doctorId, date, time, Status.SCHEDULED, notes);
        return appointmentDAO.addAppointment(a);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        return appointmentDAO.getAppointmentsByDoctor(doctorId);
    }

    public boolean cancelAppointment(int appointmentId) {
        return appointmentDAO.updateStatus(appointmentId, Status.CANCELLED);
    }

    public boolean completeAppointment(int appointmentId) {
        return appointmentDAO.updateStatus(appointmentId, Status.COMPLETED);
    }
}