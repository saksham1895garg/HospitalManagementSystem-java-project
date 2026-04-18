package hms.services;

import hms.dao.DoctorDAO;
import hms.exceptions.DoctorNotFoundException;
import hms.models.Doctor;

import java.util.List;

public class DoctorService {

    private DoctorDAO doctorDAO = new DoctorDAO();

    public boolean addDoctor(Doctor d) {
        return doctorDAO.addDoctor(d);
    }

    public List<Doctor> getAllDoctors() {
        return doctorDAO.getAllDoctors();
    }

    public Doctor getDoctorById(int id) {
        Doctor d = doctorDAO.getDoctorById(id);
        if (d == null) throw new DoctorNotFoundException("No doctor found with ID: " + id);
        return d;
    }

    public List<Doctor> searchBySpecialization(String spec) {
        return doctorDAO.searchBySpecialization(spec);
    }

    public boolean updateDoctor(Doctor d) {
        getDoctorById(d.getId()); // throws if not found
        return doctorDAO.updateDoctor(d);
    }

    public boolean deleteDoctor(int id) {
        getDoctorById(id); // throws if not found
        return doctorDAO.deleteDoctor(id);
    }
}