package hms.services;

import hms.dao.MedicineDAO;
import hms.exceptions.MedicineNotFoundException;
import hms.models.Medicine;

import java.util.List;
import java.util.stream.Collectors;

public class MedicineService {

    private MedicineDAO medicineDAO = new MedicineDAO();

    public boolean addMedicine(Medicine m) {
        return medicineDAO.addMedicine(m);
    }

    public List<Medicine> getAllMedicines() {
        return medicineDAO.getAllMedicines();
    }

    public List<Medicine> getExpiredMedicines() {
        return medicineDAO.getAllMedicines().stream()
                .filter(Medicine::isExpired)
                .collect(Collectors.toList());
    }

    public List<Medicine> getLowStockMedicines() {
        return medicineDAO.getAllMedicines().stream()
                .filter(Medicine::isLowStock)
                .collect(Collectors.toList());
    }

    public boolean updateStock(int medicineId, int newQty) {
        if (newQty < 0) throw new IllegalArgumentException("Stock cannot be negative");
        return medicineDAO.updateStock(medicineId, newQty);
    }

    public boolean deleteMedicine(int id) {
        return medicineDAO.deleteMedicine(id);
    }
}