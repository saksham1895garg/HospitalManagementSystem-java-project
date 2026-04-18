package hms.services;

import hms.dao.BillDAO;
import hms.models.Bill;
import hms.models.Bill.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public class BillService {

    private BillDAO billDAO = new BillDAO();

    public boolean generateBill(int patientId, int appointmentId,
                                double consultationFee, double medicineCharge) {
        double total = consultationFee + medicineCharge;
        Bill b = new Bill(0, patientId, appointmentId,
                consultationFee, medicineCharge,
                total, PaymentStatus.PENDING, LocalDateTime.now());
        return billDAO.addBill(b);
    }

    public List<Bill> getAllBills() {
        return billDAO.getAllBills();
    }

    public boolean markAsPaid(int billId) {
        return billDAO.markAsPaid(billId);
    }

    public double getTotalRevenue() {
        return billDAO.getAllBills().stream()
                .filter(Bill::isPaid)
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }
}