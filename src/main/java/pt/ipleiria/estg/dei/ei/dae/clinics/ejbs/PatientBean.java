package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PatientBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllPatients() {
        Query query = entityManager.createQuery("SELECT p.id, p.healthNo, p.email, p.name, p.gender  FROM Patient p");
        List<Object[]> patientList = query.getResultList();
        return patientList;
        // return entityManager.createNamedQuery("getAllPatients",
        // Patient.class).getResultList();
    }

    public Patient findPatient(long id) throws MyEntityNotFoundException {
        Patient patient = entityManager.find(Patient.class, id);
        if (patient == null)
            throw new MyEntityNotFoundException("Patient \"" + id + "\" does not exist");

        return patient;
    }

    public Patient findPatient(String email) {
        Query query = entityManager.createNativeQuery("SELECT p FROM Persons p WHERE p.email = '" + email + "'",
                Patient.class);
        List<Patient> patientList = (List<Patient>) query.getResultList();
        return patientList.isEmpty() ? null : patientList.get(0);
    }

    /***
     * Creating a Patient by a Doctor
     * 
     * @param email        of doctor acc
     * @param password     of doctor acc
     * @param name         of doctor acc
     * @param gender       of doctor acc
     * @param healthNo     of doctor acc
     * @param created_byId Doctor Username that is creating the current Patient
     */
    public long create(String email, String password, String name, String gender, int healthNo, long created_byId)
            throws MyEntityExistsException, MyEntityNotFoundException {
        Patient patient = findPatient(email);
        if (patient != null)
            throw new MyEntityExistsException("Patient \"" + email + "\" already exist");

        HealthcareProfessional doctor = entityManager.find(HealthcareProfessional.class, created_byId);
        if (doctor == null)
            throw new MyEntityNotFoundException("Doctor \"" + created_byId + "\" already exist");

        Patient newPatient = new Patient(email, password, name, gender, healthNo, doctor);
        entityManager.persist(newPatient);
        doctor.addPatient(newPatient);
        entityManager.flush();
        return newPatient.getId();
    }

    /***
     * Delete a Patient by given @Id:username - Change deleted_at field to NOW()
     * date
     * 
     * @param id @Id to find the proposal delete Patient
     */
    public void delete(long id) throws MyEntityNotFoundException {
        Patient patient = findPatient(id);
        entityManager.remove(patient);
    }

    /***
     * Update a Patient by given @Id:username
     * 
     * @param email    @Id to find the proposal update Patient
     * @param password to update Patient
     * @param name     to update Patient
     * @param gender   to update Patient
     * @param healthNo to update Patient
     */
    public void update(long id, String email, String password, String name, String gender, int healthNo)
            throws MyEntityNotFoundException {
        Patient patient = findPatient(id);

        patient.setEmail(email);
        patient.setName(name);
        patient.setGender(gender);
        patient.setHealthNo(healthNo);
    }
}
