package hms.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    public enum Status { SCHEDULED, COMPLETED, CANCELLED }

    private int       appointmentId;
    private int       patientId;
    private int       doctorId;
    private String    patientName;   // for display only
    private String    doctorName;    // for display only
    private LocalDate date;
    private LocalTime time;
    private Status    status;
    private String    notes;

    public Appointment(int appointmentId, int patientId, int doctorId,
                       LocalDate date, LocalTime time, Status status, String notes) {
        this.appointmentId = appointmentId;
        this.patientId     = patientId;
        this.doctorId      = doctorId;
        this.date          = date;
        this.time          = time;
        this.status        = status;
        this.notes         = notes;
    }

    // Getters
    public int       getAppointmentId() { return appointmentId; }
    public int       getPatientId()     { return patientId; }
    public int       getDoctorId()      { return doctorId; }
    public String    getPatientName()   { return patientName; }
    public String    getDoctorName()    { return doctorName; }
    public LocalDate getDate()          { return date; }
    public LocalTime getTime()          { return time; }
    public Status    getStatus()        { return status; }
    public String    getNotes()         { return notes; }

    // Setters
    public void setStatus(Status status)       { this.status = status; }
    public void setNotes(String notes)         { this.notes = notes; }
    public void setPatientName(String name)    { this.patientName = name; }
    public void setDoctorName(String name)     { this.doctorName = name; }

    @Override
    public String toString() {
        return "Appointment #" + appointmentId
                + " | Patient: " + patientName
                + " | Doctor: "  + doctorName
                + " | "          + date + " at " + time
                + " | Status: "  + status;
    }
}