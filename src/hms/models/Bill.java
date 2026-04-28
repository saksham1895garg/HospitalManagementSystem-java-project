package hms.models;

import java.time.LocalDateTime;

public class Bill {

    public enum PaymentStatus { PENDING, PAID }

    private int           billId;
    private int           patientId;
    private int           appointmentId;
    private String        patientName;     // for display
    private double        consultationFee;
    private double        medicineCharge;
    private double        totalAmount;
    private PaymentStatus paymentStatus;
    private LocalDateTime billDate;

    public Bill(int billId, int patientId, int appointmentId,
                double consultationFee, double medicineCharge,
                double totalAmount, PaymentStatus paymentStatus,
                LocalDateTime billDate) {
        this.billId          = billId;
        this.patientId       = patientId;
        this.appointmentId   = appointmentId;
        this.consultationFee = consultationFee;
        this.medicineCharge  = medicineCharge;
        this.totalAmount     = totalAmount;
        this.paymentStatus   = paymentStatus;
        this.billDate        = billDate;
    }

    // Getters
    public int           getBillId()          { return billId; }
    public int           getPatientId()       { return patientId; }
    public int           getAppointmentId()   { return appointmentId; }
    public String        getPatientName()     { return patientName; }
    public double        getConsultationFee() { return consultationFee; }
    public double        getMedicineCharge()  { return medicineCharge; }
    public double        getTotalAmount()     { return totalAmount; }
    public PaymentStatus getPaymentStatus()   { return paymentStatus; }
    public LocalDateTime getBillDate()        { return billDate; }

    // Setters
    public void setPatientName(String name)          { this.patientName = name; }
    public void setPaymentStatus(PaymentStatus status) { this.paymentStatus = status; }
    public void setMedicineCharge(double charge) {
        this.medicineCharge = charge;
        this.totalAmount    = consultationFee + charge;
    }

    public boolean isPaid() {
        return paymentStatus == PaymentStatus.PAID;
    }

    @Override
    public String toString() {
        return "Bill #" + billId
                + " | Patient: "      + patientName
                + " | Consultation: Rs." + consultationFee
                + " | Medicine: Rs."  + medicineCharge
                + " | Total: Rs."     + totalAmount
                + " | "               + paymentStatus;
    }
}