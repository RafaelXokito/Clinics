package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Person;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyUnauthorizedException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Stateless
public class HealthcareProfessionalBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private EmailBean emailBean;

    /***
     * Execute HealthcareProfessional query getAllHealthcareProfessionals getting all HealthcareProfessional Class
     * @return a list of All HealthcareProfessional
     */
    public List<HealthcareProfessional> getAllHealthcareProfessionalsClass() {
        return entityManager.createNamedQuery("getAllHealthcareProfessionals", HealthcareProfessional.class).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    /***
     * Execute HealthcareProfessional query getAllHealthcareProfessionalsWithTrashed getting all HealthcareProfessional Class
     * @return a list of All HealthcareProfessional
     */
    public List<HealthcareProfessional> getAllHealthcareProfessionalsClassWithTrashed() {
        return entityManager.createNamedQuery("getAllHealthcareProfessionalsWithTrashed", HealthcareProfessional.class).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    /***
     * Find HealthcareProfessional by given @Id:id
     * @param id @Id to find HealthcareProfessional
     * @return founded HealthcareProfessional or Null if dont
     */
    public HealthcareProfessional findHealthcareProfessional(long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, id);
        if (healthcareProfessional == null || healthcareProfessional.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + id + "\" does not exist");

        return healthcareProfessional;
    }

    /***
     * Find Person by given @Id:id
     * @param email @Id to find Person
     * @return founded Person or Null if dont
     */
    public Person findPerson(String email) {
        TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p WHERE p.email = '" + email + "'", Person.class);
        return query.getResultList().size() > 0 ? query.getSingleResult() : null;
    }

    /***
     * Creating a Doctor by a Administrator
     * @param email of doctor acc
     * @param password of doctor acc
     * @param name of doctor acc
     * @param gender of doctor acc
     * @param specialty of doctor acc
     * @param created_ById Administrator Username that is creating the current Doctor
     */
    public long create(String email, String password, String name, String gender, String specialty, long created_ById, Date birthDate) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException, MessagingException {
        //REQUIRED VALIDATION
        if (email == null || email.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"email\" is required");
        if (password == null || password.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"password\" is required");
        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");
        if (gender == null || gender.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"gender\" is required");
        if (specialty == null || specialty.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"specialty\" is required");
        if (birthDate == null)
            throw new MyIllegalArgumentException("Field \"birthDate\" is required");

        //CHECK VALUES
        Person person = findPerson(email.trim());
        if (person != null)
            throw new MyEntityExistsException("Person with an email of \"" + email.trim() + "\" already exist");

        Administrator created_by = entityManager.find(Administrator.class, created_ById);
        if (created_by == null || created_by.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Administrator \"" + created_ById + "\" does not exist");

        if (password.trim().length() < 4)
            throw new MyIllegalArgumentException("Field \"password\" must have at least 4 characters");
        if (name.trim().length() < 6)
            throw new MyIllegalArgumentException("Field \"name\" must have at least 6 characters");
        if (!gender.trim().equals("Male") && !gender.trim().equals("Female") && !gender.trim().equals("Other"))
            throw new MyIllegalArgumentException("Field \"gender\" needs to be one of the following \"Male\", \"Female\", \"Other\"");
        if (specialty.trim().length() < 3)
            throw new MyIllegalArgumentException("Field \"specialty\" must have at least 3 characters");
        if (Date.from(Instant.now()).compareTo(birthDate) < 0)
            throw new MyIllegalArgumentException("Field \"birthDate\" must be lower or equal to the current date");

        HealthcareProfessional newHealthcareProfessional = new HealthcareProfessional(email.trim(), password.trim(), name.trim(), gender.trim(), specialty.trim(), created_by, birthDate);
        try {
            entityManager.persist(newHealthcareProfessional);
            entityManager.flush();
        }catch (Exception ex){
            throw new MyIllegalArgumentException("Error persisting your data");
        }
        emailBean.send(newHealthcareProfessional.getEmail(), "Welcome to Clinics",
                "Welcome "+newHealthcareProfessional.getName()+"!\nYou are now registered in Clinics!\nContact support to get more information.");
        return newHealthcareProfessional.getId();
    }

    /***
     * Delete a Doctor by given @Id:username - Change deleted_at field to NOW() date
     * @param id @Id to find the proposal delete Doctor
     */
    public boolean delete(long id) throws MyEntityNotFoundException, MessagingException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);
        entityManager.lock(healthcareProfessional, LockModeType.PESSIMISTIC_WRITE);
        healthcareProfessional.setDeleted_at();
        emailBean.send(healthcareProfessional.getEmail(), "Your account from Clinics was deleted",
                "Your account from Clinics was deleted! Contact support to get more information.");
        return true;
    }

    /***
     * Update a Doctor by given @Id:username
     * @param email @Id to find the proposal update Doctor
     * @param name to update Doctor
     * @param gender to update Doctor
     * @param specialty to update Doctor
     */
    public void update(long id, String email, String name, String gender, String specialty, Date birthDate) throws MyEntityNotFoundException, MyIllegalArgumentException, MyEntityExistsException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);
        entityManager.lock(healthcareProfessional, LockModeType.PESSIMISTIC_FORCE_INCREMENT);

        //REQUIRED VALIDATION
        if (email == null || email.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"email\" is required");
        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");
        if (gender == null || gender.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"gender\" is required");
        if (specialty == null || specialty.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"specialty\" is required");
        if (birthDate == null)
            throw new MyIllegalArgumentException("Field \"birthDate\" is required");

        //CHECK VALUES
        Person person = findPerson(email.trim());
        if (person != null && person.getId() != id)
            throw new MyEntityExistsException("Person with an email of \"" + email.trim() + "\" already exist");
        if (name.trim().length() < 6)
            throw new MyIllegalArgumentException("Field \"name\" must have at least 6 characters");
        if (!gender.trim().equals("Male") && !gender.trim().equals("Female") && !gender.trim().equals("Other"))
            throw new MyIllegalArgumentException("Field \"gender\" needs to be one of the following \"Male\", \"Female\", \"Other\"");
        if (specialty.trim().length() < 3)
            throw new MyIllegalArgumentException("Field \"specialty\" must have at least 3 characters");
        if (Date.from(Instant.now()).compareTo(birthDate) < 0)
            throw new MyIllegalArgumentException("Field \"birthDate\" must be lower or equal to the current date");

        healthcareProfessional.setEmail(email.trim());
        healthcareProfessional.setName(name.trim());
        healthcareProfessional.setGender(gender.trim());
        healthcareProfessional.setSpecialty(specialty.trim());
        healthcareProfessional.setBirthDate(birthDate);
        try {
            entityManager.flush();
        }catch (Exception ex){
            throw new MyIllegalArgumentException("Error updating Administrator");
        }
    }

    public void updatePassword(long id, String oldPassword, String newPassword) throws MyEntityNotFoundException, MyIllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);
        entityManager.lock(healthcareProfessional, LockModeType.PESSIMISTIC_WRITE);

        //REQUIRED VALIDATION
        if (oldPassword == null || oldPassword.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"oldPassword\" is required");
        if (newPassword == null || newPassword.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"newPassword\" is required");

        //CHECK VALUES
        if (newPassword.trim().length() < 4)
            throw new MyIllegalArgumentException("Field \"newPassword\" must have at least 4 characters");

        if (!Person.validatePassword(oldPassword.trim(), healthcareProfessional.getPassword()))
            throw new MyIllegalArgumentException("Field \"oldPassword\" does not match with the current password");

        healthcareProfessional.setPassword(newPassword.trim());
    }


    /***
     * Restore a Healthcare professional by given @Id:id - Change deleted_at field to null date
     * @param id @Id to find the proposal restore Healthcare professional
     */
    public boolean restore(long id) throws MyEntityNotFoundException, MyEntityExistsException, MessagingException {
        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, id);
        if (healthcareProfessional == null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + id + "\" does not exist");
        if (healthcareProfessional.getDeleted_at() == null)
            throw new MyEntityExistsException("Healthcare Professional \"" + id + "\" already exist");
        healthcareProfessional.setDeleted_at(null);
        emailBean.send(healthcareProfessional.getEmail(), "Your account from Clinics was restored",
                "Your account from Clinics was restored! Contact support to get more information.");
        return true;
    }

    /***
     * Associate a patient to a given HealthcareProfessional @Id:id
     * @param id HealthcareProfessional @Id:id
     * @param patientId Patient @Id:id
     * @return  True if associate, false if dont
     * @throws MyEntityNotFoundException If some of the entities was not found
     * @throws MessagingException If Email of association was not sent
     */
    public Boolean associatePatient(long id, long patientId) throws MyEntityNotFoundException, MessagingException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);

        Patient patient = entityManager.find(Patient.class, patientId);
        if (patient == null || patient.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Patient \"" + id + "\" does not exist");

        healthcareProfessional.addPatient(patient);
        patient.addHealthcareProfessional(healthcareProfessional);
        emailBean.send(patient.getEmail(), "Great news from Clinics",
                "Congratulations "+patient.getName()+"!\nYour account from Clinics was associated to a new Healthcare Professional! Dr \n"+healthcareProfessional.getName()+" ("+healthcareProfessional.getSpecialty()+") \n"+healthcareProfessional.getEmail()+"\nContact support to get more information.");
        return true;
    }

    /***
     * Desassociate a patient to a given HealthcareProfessional @Id:id
     * @param id HealthcareProfessional @Id:id
     * @param patientId Patient @Id:id
     * @return True if desassociate, false if dont
     * @throws MyEntityNotFoundException If some of the entities was not found
     */
    public Boolean deassociatePatient(long id, long patientId) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);

        Patient patient = entityManager.find(Patient.class, patientId);
        if (patient == null || patient.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Patient \"" + id + "\" does not exist");

        healthcareProfessional.removePacient(patient);
        patient.removeHealthcareProfessional(healthcareProfessional);

        return true;
    }
}
