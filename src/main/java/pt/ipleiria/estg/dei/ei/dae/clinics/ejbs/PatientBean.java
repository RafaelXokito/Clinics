package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Employee;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PatientBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllPatients() {
        Query query = entityManager.createQuery("SELECT p.id, p.email, p.name, p.gender, p.healthNo  FROM Patient p ORDER BY p.id DESC");
        List<Object[]> patientList = query.getResultList();
        return patientList;
    }

    public Patient findPatient(long id) throws MyEntityNotFoundException {
        Patient patient = entityManager.find(Patient.class, id);
        if (patient == null)
            throw new MyEntityNotFoundException("Patient \"" + id + "\" does not exist");

        return patient;
    }

    public Patient findPatient(String email) {
        TypedQuery<Patient> query = entityManager.createQuery("SELECT p FROM Person p WHERE p.email = '"+ email+"'", Patient.class);
        return query.getResultList().size()>0 ? query.getSingleResult() : null;
    }

    public Patient findPatientByHealthNo(long healthNo) {
        TypedQuery<Patient> query = entityManager.createQuery("SELECT p FROM Patient p WHERE p.healthNo = '"+ healthNo+"'", Patient.class);
        return query.getResultList().size()>0 ? query.getSingleResult() : null;
    }

    /***
     * Creating a Patient by a Administrator
     *
     * @param email        of patient acc
     * @param password     of patient acc
     * @param name         of patient acc
     * @param gender       of patient acc
     * @param healthNo     of patient acc
     * @param created_byId Administrator Username that is creating the current Patient
     */
    public long create(String email, String password, String name, String gender, int healthNo, long created_byId)
            throws MyEntityExistsException, MyEntityNotFoundException {
        Patient patient = findPatient(email);
        if (patient != null)
            throw new MyEntityExistsException("Patient \"" + email + "\" already exist");

        Employee employee = entityManager.find(Employee.class, created_byId);
        if (employee == null)
            throw new MyEntityNotFoundException("Employee \"" + created_byId + "\" don't exist");

        Patient newPatient = new Patient(email, password, name, gender, healthNo, employee);
        entityManager.persist(newPatient);
        entityManager.flush();
        return newPatient.getId();
    }

    /***
     * Delete a Patient by given @Id:username - Change deleted_at field to NOW()
     * date
     * 
     * @param id @Id to find the proposal delete Patient
     */
    public boolean delete(long id) throws MyEntityNotFoundException {
        Patient patient = findPatient(id);
        entityManager.remove(patient); //TODO soft delete
        return entityManager.find(Patient.class, id) == null;
    }

    /***
     * Update a Patient by given @Id:username
     * 
     * @param email    @Id to find the proposal update Patient
     * @param name     to update Patient
     * @param gender   to update Patient
     * @param healthNo to update Patient
     */
    public void update(long id, String email, String name, String gender, int healthNo) throws MyEntityNotFoundException {
        Patient patient = findPatient(id);

        patient.setEmail(email);
        patient.setName(name);
        patient.setGender(gender);
        patient.setHealthNo(healthNo);
    }

    public void updatePassword(long id, String oldPassword, String newPassword) throws MyEntityNotFoundException, MyIllegalArgumentException {
        Patient patient = findPatient(id);

        Administrator administratorOldPassword = new Administrator();
        administratorOldPassword.setPassword(oldPassword);

        if (!patient.getPassword().equals(administratorOldPassword.getPassword()))
            throw new MyIllegalArgumentException("Password does not match with the old one");

        patient.setPassword(newPassword);
    }

}
