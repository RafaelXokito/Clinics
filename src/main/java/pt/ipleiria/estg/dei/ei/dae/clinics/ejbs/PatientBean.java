package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class PatientBean {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private EmailBean emailBean;

    public List<Patient> getAllPatientsClass() {
        return entityManager.createNamedQuery("getAllPatients", Patient.class).getResultList();
    }

    public List<Patient> getAllPatientsClassByHealthcareProfessional(long healthcareProfessionalId) {
        List<Patient> patients = new ArrayList<>();
        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, healthcareProfessionalId, LockModeType.OPTIMISTIC);

        if (healthcareProfessional == null) return patients;

        for (Patient patient : healthcareProfessional.getPatients()) {
            if (patient.getDeleted_at() == null) {
                patients.add(patient);
            }
        }

        return patients;
    }

    public List<Patient> getAllPatientsClassWithTrashed() {
        return entityManager.createNamedQuery("getAllPatientsWithTrashed", Patient.class).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    public Patient findPatient(long id) throws MyEntityNotFoundException {
        Patient patient = entityManager.find(Patient.class, id);
        if (patient == null || patient.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Patient \"" + id + "\" does not exist");

        return patient;
    }

    public Person findPerson(String email) {
        TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p WHERE p.email = '" + email + "'", Person.class);
        return query.getResultList().size() > 0 ? query.getSingleResult() : null;
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
    public long create(String email, String password, String name, String gender, int healthNo, long created_byId, Date birthDate)
            throws MyEntityExistsException, MyEntityNotFoundException, MyIllegalArgumentException, MessagingException {
        //REQUIRED VALIDATION
        if (email == null || email.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"email\" is required");
        if (password == null || password.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"password\" is required");
        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");
        if (gender == null || gender.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"gender\" is required");
        if (birthDate == null)
            throw new MyIllegalArgumentException("Field \"birthDate\" is required");

        //CHECK VALUES
        Person person = findPerson(email.trim());
        if (person != null)
            throw new MyEntityExistsException("Person with email of \"" + email.trim() + "\" already exist");

        Employee employee = entityManager.find(Employee.class, created_byId);
        if (employee == null || employee.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Employee \"" + created_byId + "\" don't exist");

        if (password.trim().length() < 4)
            throw new MyIllegalArgumentException("Field \"password\" must have at least 4 characters");
        if (name.trim().length() < 6)
            throw new MyIllegalArgumentException("Field \"name\" must have at least 6 characters");
        if (!gender.trim().equals("Male") && !gender.trim().equals("Female") && !gender.trim().equals("Other"))
            throw new MyIllegalArgumentException("Field \"gender\" needs to be one of the following \"Male\", \"Female\", \"Other\"");
        Patient patient = findPatientByHealthNo(healthNo);
        if (patient != null)
            throw new MyEntityExistsException("Patient with a health number of \"" + healthNo + "\" already exist");
        if (Date.from(Instant.now()).compareTo(birthDate) < 0)
            throw new MyIllegalArgumentException("Field \"birthDate\" must be lower or equal to the current date");

        Patient newPatient = new Patient(email.trim(), password.trim(), name.trim(), gender.trim(), healthNo, employee, birthDate);
        try {
            entityManager.persist(newPatient);
            entityManager.flush();
        }catch (Exception ex){
            throw new MyIllegalArgumentException("Error persisting your data");
        }
        emailBean.send(newPatient.getEmail(), "Welcome to Clinics",
                "Welcome "+newPatient.getName()+"!\nYou are now registered in Clinics!\nContact support to get more information.");
        return newPatient.getId();
    }

    /***
     * Delete a Patient by given @Id:username - Change deleted_at field to NOW()
     * date
     * 
     * @param id @Id to find the proposal delete Patient
     */
    public boolean delete(long id) throws MyEntityNotFoundException, MessagingException {
        Patient patient = findPatient(id);
        entityManager.lock(patient, LockModeType.PESSIMISTIC_WRITE);
        patient.setDeleted_at();
        emailBean.send(patient.getEmail(), "Your account from Clinics was deleted",
                "Your account from Clinics was deleted! Contact support to get more information.");
        return true;
    }

    /***
     * Update a Patient by given @Id:username
     * 
     * @param email    @Id to find the proposal update Patient
     * @param name     to update Patient
     * @param gender   to update Patient
     * @param healthNo to update Patient
     */
    public void update(long id, String email, String name, String gender, int healthNo, Date birthDate) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        Patient patient = findPatient(id);
        entityManager.lock(patient, LockModeType.PESSIMISTIC_FORCE_INCREMENT);

        //REQUIRED VALIDATION
        if (email == null || email.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"email\" is required");
        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");
        if (gender == null || gender.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"gender\" is required");
        if (birthDate == null)
            throw new MyIllegalArgumentException("Field \"birthDate\" is required");

        //CHECK VALUES
        Person person = findPerson(email.trim());
        if (person != null && person.getId() != id)
            throw new MyEntityExistsException("Person with email of \"" + email + "\" already exist");
        if (name.trim().length() < 6)
            throw new MyIllegalArgumentException("Field \"name\" must have at least 6 characters");
        if (!gender.trim().equals("Male") && !gender.trim().equals("Female") && !gender.trim().equals("Other"))
            throw new MyIllegalArgumentException("Field \"gender\" needs to be one of the following \"Male\", \"Female\", \"Other\"");
        Patient patientTest = findPatientByHealthNo(healthNo);
        if (patientTest != null && patientTest.getId() != id)
            throw new MyEntityExistsException("Patient with a health number of \"" + healthNo + "\" already exist");
        if (Date.from(Instant.now()).compareTo(birthDate) < 0)
            throw new MyIllegalArgumentException("Field \"birthDate\" must be lower or equal to the current date");

        patient.setEmail(email.trim());
        patient.setName(name.trim());
        patient.setGender(gender.trim());
        patient.setHealthNo(healthNo);
        patient.setBirthDate(birthDate);
        try {
            entityManager.flush();
        }catch (Exception ex){
            throw new MyIllegalArgumentException("Error updating Administrator");
        }
    }

    public void updatePassword(long id, String oldPassword, String newPassword) throws MyEntityNotFoundException, MyIllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {
        Patient patient = findPatient(id);
        entityManager.lock(patient, LockModeType.PESSIMISTIC_WRITE);

        //REQUIRED VALIDATION
        if (oldPassword == null || oldPassword.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"oldPassword\" is required");
        if (newPassword == null || newPassword.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"newPassword\" is required");

        //CHECK VALUES
        if (newPassword.trim().length() < 4)
            throw new MyIllegalArgumentException("Field \"newPassword\" must have at least 4 characters");

        if (!Person.validatePassword(oldPassword.trim(), patient.getPassword()))
            throw new MyIllegalArgumentException("Field \"oldPassword\" does not match with the current password");

        patient.setPassword(newPassword.trim());
    }

    /***
     * Restore a Patient by given @Id:id - Change deleted_at field to null date
     * @param id @Id to find the proposal restore Patient
     */
    public boolean restore(long id) throws MyEntityNotFoundException, MyEntityExistsException, MessagingException {
        Patient patient = entityManager.find(Patient.class, id);
        if (patient == null)
            throw new MyEntityNotFoundException("Patient \"" + id + "\" does not exist");
        if (patient.getDeleted_at() == null)
            throw new MyEntityExistsException("Patient \"" + id + "\" already exist");
        patient.setDeleted_at(null);
        emailBean.send(patient.getEmail(), "Your account from Clinics was restored",
                "Your account from Clinics was restored! Contact support to get more information.");
        return true;
    }

}
