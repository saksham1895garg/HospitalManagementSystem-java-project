package hms.services;

import hms.dao.PatientDAO;
import hms.exceptions.PatientNotFoundException;
import hms.models.Patient;

import java.util.List;

public class PatientService {

    private PatientDAO patientDAO = new PatientDAO();

    public boolean addPatient(Patient p) {
        return patientDAO.addPatient(p);
    }

    public List<Patient> getAllPatients() {
        return patientDAO.getAllPatients();
    }

    public Patient getPatientById(int id) {
        Patient p = patientDAO.getPatientById(id);
        if (p == null) throw new PatientNotFoundException("No patient found with ID: " + id);
        return p;
    }

    public List<Patient> searchByName(String name) {
        return patientDAO.searchByName(name);
    }

    public boolean updatePatient(Patient p) {
        getPatientById(p.getId()); // throws if not found
        return patientDAO.updatePatient(p);
    }

    public boolean deletePatient(int id) {
        getPatientById(id); // throws if not found
        return patientDAO.deletePatient(id);
    }
}