package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Person;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyUnauthorizedException;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Stateless
public class HealthcareProfessionalBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllHealthcareProfessionals() {
        Query query = entityManager.createQuery("SELECT d.id, d.email, d.name, d.gender, d.specialty  FROM HealthcareProfessional d ORDER BY d.id DESC");
        List<Object[]> healthcareProfessionalList = query.getResultList();
        return healthcareProfessionalList;
    }

    public List<HealthcareProfessional> getAllHealthcareProfessionalsClass() {
        return entityManager.createNamedQuery("getAllHealthcareProfessionals", HealthcareProfessional.class).getResultList();
    }

    public List<HealthcareProfessional> getAllHealthcareProfessionalsClassWithTrashed() {
        return entityManager.createNamedQuery("getAllHealthcareProfessionalsWithTrashed", HealthcareProfessional.class).getResultList();
    }

    public HealthcareProfessional findHealthcareProfessional(long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, id);
        if (healthcareProfessional == null || healthcareProfessional.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + id + "\" does not exist");

        return healthcareProfessional;
    }

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
    public long create(String email, String password, String name, String gender, String specialty, long created_ById) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
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

        HealthcareProfessional newHealthcareProfessional = new HealthcareProfessional(email.trim(), password.trim(), name.trim(), gender.trim(), specialty.trim(), created_by);
        try {
            entityManager.persist(newHealthcareProfessional);
            entityManager.flush();
        }catch (Exception ex){
            throw new MyIllegalArgumentException("Error persisting your data");
        }
        return newHealthcareProfessional.getId();
    }

    /***
     * Delete a Doctor by given @Id:username - Change deleted_at field to NOW() date
     * @param id @Id to find the proposal delete Doctor
     */
    public boolean delete(long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);
        healthcareProfessional.setDeleted_at();
        return true;
    }

    /***
     * Update a Doctor by given @Id:username
     * @param email @Id to find the proposal update Doctor
     * @param name to update Doctor
     * @param gender to update Doctor
     * @param specialty to update Doctor
     */
    public void update(long id, String email, String name, String gender, String specialty) throws MyEntityNotFoundException, MyIllegalArgumentException, MyEntityExistsException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);

        //REQUIRED VALIDATION
        if (email == null || email.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"email\" is required");
        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");
        if (gender == null || gender.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"gender\" is required");
        if (specialty == null || specialty.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"specialty\" is required");

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

        healthcareProfessional.setEmail(email.trim());
        healthcareProfessional.setName(name.trim());
        healthcareProfessional.setGender(gender.trim());
        healthcareProfessional.setSpecialty(specialty.trim());
        try {
            entityManager.flush();
        }catch (Exception ex){
            throw new MyIllegalArgumentException("Error updating Administrator");
        }
    }

    public void updatePassword(long id, String oldPassword, String newPassword) throws MyEntityNotFoundException, MyIllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);

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
    public boolean restore(long id) throws MyEntityNotFoundException, MyEntityExistsException {
        HealthcareProfessional administrator = entityManager.find(HealthcareProfessional.class, id);
        if (administrator == null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + id + "\" does not exist");
        if (administrator.getDeleted_at() == null)
            throw new MyEntityExistsException("Healthcare Professional \"" + id + "\" already exist");
        administrator.setDeleted_at(null);
        return true;
    }
}
