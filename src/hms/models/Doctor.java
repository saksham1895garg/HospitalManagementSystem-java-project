package hms.models;

import hms.interfaces.Schedulable;
import hms.exceptions.AppointmentConflictException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person implements Schedulable {

    private String specialization;
    private String qualification;
    private double consultationFee;
    private List<LocalDateTime> schedule;

    public Doctor(int id, String name, int age, String gender,
                  String phone, String email,
                  String specialization, String qualification, double consultationFee) {

        super(id, name, age, gender, phone, email);
        this.specialization  = specialization;
        this.qualification   = qualification;
        this.consultationFee = consultationFee;
        this.schedule        = new ArrayList<>();
    }

    // Must implement from Person
    @Override
    public String getRole() {
        return "Doctor";
    }

    // Schedulable interface — check if slot is free
    @Override
    public boolean isAvailable(LocalDateTime dateTime) {
        return !schedule.contains(dateTime);
    }

    // Schedulable interface — get all booked slots
    @Override
    public List<LocalDateTime> getSchedule() {
        return new ArrayList<>(schedule); // return copy, not reference
    }

    // Book a slot — throws custom exception if already taken
    @Override
    public void addSchedule(LocalDateTime dateTime) {
        if (!isAvailable(dateTime)) {
            throw new AppointmentConflictException(
                    "Dr. " + getName() + " is already booked at " + dateTime
            );
        }
        schedule.add(dateTime);
    }

    // Cancel a slot
    @Override
    public void removeSchedule(LocalDateTime dateTime) {
        schedule.remove(dateTime);
    }

    // Doctor-specific getters
    public String getSpecialization()  { return specialization; }
    public String getQualification()   { return qualification; }
    public double getConsultationFee() { return consultationFee; }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public void setConsultationFee(double fee) {
        this.consultationFee = fee;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Specialization: " + specialization
                + " | Fee: Rs." + consultationFee;
    }
}