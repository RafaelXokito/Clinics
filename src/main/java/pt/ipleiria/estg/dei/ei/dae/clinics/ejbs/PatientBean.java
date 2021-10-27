package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PatientBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Patient> getAllPatients() {
        return (List<Patient>) entityManager.createNamedQuery("getAllPatients").getResultList();
    }

    public Patient findPatient(String username) throws MyEntityNotFoundException {
        Patient patient = entityManager.find(Patient.class, username);
        if (patient == null)
            throw new MyEntityNotFoundException("Patient \"" + username + "\" does not exist");

        return patient;
    }

    /***
     * Creating a Patient by a Doctor
     * @param username of doctor acc
     * @param email of doctor acc
     * @param password of doctor acc
     * @param name of doctor acc
     * @param gender of doctor acc
     * @param healthNo of doctor acc
     * @param created_byUsername Doctor Username that is creating the current Patient
     */
    public void create(String username, String email, String password, String name, String gender, int healthNo, String created_byUsername) throws MyEntityExistsException, MyEntityNotFoundException {
        Patient patient = entityManager.find(Patient.class, username);
        if (patient != null)
            throw new MyEntityExistsException("Patient \"" + username + "\" already exist");

        Doctor doctor = entityManager.find(Doctor.class, created_byUsername);
        if (doctor == null)
            throw new MyEntityNotFoundException("Doctor \"" + created_byUsername + "\" already exist");

        Patient newPatient = new Patient(username, email, password, name, gender, healthNo, doctor);
        entityManager.persist(newPatient);
        doctor.addPatient(newPatient);
    }

    /***
     * Delete a Patient by given @Id:username - Change deleted_at field to NOW() date
     * @param username @Id to find the proposal delete Patient
     */
    public void delete(String username) throws MyEntityNotFoundException {
        Patient patient = findPatient(username);
        entityManager.remove(patient);
    }

    /***
     * Update a Patient by given @Id:username
     * @param username @Id to find the proposal update Patient
     * @param email to update Patient
     * @param password to update Patient
     * @param name to update Patient
     * @param gender to update Patient
     * @param healthNo to update Patient
     */
    public void update(String username, String email, String password, String name, String gender, int healthNo) throws MyEntityNotFoundException {
        Patient patient = findPatient(username);

        patient.setEmail(email);
        patient.setPassword(password);
        patient.setName(name);
        patient.setGender(gender);
        patient.setHealthNo(healthNo);
    }
}
