package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Person;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyUnauthorizedException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Email;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Stateless
public class AdministratorBean {
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Execute Administrator query getAllAdministrators getting all Administrators
     * @return a list of All Administrators
     */
    public List<Object[]> getAllAdministrators() {
        Query query = entityManager.createQuery("SELECT a.id, a.email, a.name, a.gender, a.deleted_at  FROM Administrator a ORDER BY a.id DESC");
        List<Object[]> administratorList = query.getResultList();
        return administratorList;
    }

    /***
     * Execute Administrator query getAllAdministrators getting all Administrators Class
     * @return a list of All Administrators
     */
    public List<Administrator> getAllAdministratorsClass() {
        return entityManager.createNamedQuery("getAllAdministrators", Administrator.class).getResultList();
    }

    /***
     * Execute Administrator query getAllAdministrators getting all Administrators Class
     * @return a list of All Administrators
     */
    public List<Administrator> getAllAdministratorsClassWithTrashed() {
        return entityManager.createNamedQuery("getAllAdministratorsWithTrashed", Administrator.class).getResultList();
    }

    /***
     * Find Administrator by given @Id:id
     * @param id @Id to find Administrator
     * @return founded Administrator or Null if dont
     */
    public Administrator findAdministrator(long id) throws MyEntityNotFoundException {
        Administrator administrator = entityManager.find(Administrator.class, id);
        if (administrator == null || administrator.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Administrator \"" + id + "\" does not exist");
        return administrator;
    }

    /***
     * Find Administrator by given @Id:username
     * @param email @Id to find Administrator
     * @return founded Administrator or Null if dont
     */
    public Person findPerson(String email) {
        TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p WHERE p.email = '" + email + "'", Person.class);
        return query.getResultList().size() > 0 ? query.getSingleResult() : null;
    }

    /***
     * Creating a Administrator Account
     * @param email of administrator acc
     * @param password of administrator acc
     * @param name of administrator acc
     * @param gender of administrator acc
     */
    public long create(String email, String password, String name, String gender) throws MyEntityExistsException, MyIllegalArgumentException {
        //REQUIRED VALIDATION
        if (email == null || email.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"email\" is required");
        if (password == null || password.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"password\" is required");
        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");
        if (gender == null || gender.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"gender\" is required");

        //CHECK VALUES
        Person person = findPerson(email);
        if (person != null)
            throw new MyIllegalArgumentException("Person with email of \"" + email + "\" already exist");
        if (password.trim().length() < 4)
            throw new MyIllegalArgumentException("Field \"password\" must have at least 4 characters");
        if (name.trim().length() < 6)
            throw new MyIllegalArgumentException("Field \"name\" must have at least 6 characters");
        if (!gender.trim().equals("Male") && !gender.trim().equals("Female") && !gender.trim().equals("Other"))
            throw new MyIllegalArgumentException("Field \"gender\" needs to be one of the following \"Male\", \"Female\", \"Other\"");

        Administrator newAdministrator = new Administrator(email.trim(), password.trim(), name.trim(), gender.trim());
        try {
            entityManager.persist(newAdministrator);
            entityManager.flush();
        }catch (Exception ex){
            throw new MyIllegalArgumentException("Error persisting your data");
        }
        return newAdministrator.getId();
    }

    /***
     * Delete a Administrator by given @Id:id - Change deleted_at field to NOW() date
     * @param id @Id to find the proposal delete Administrator
     */
    public boolean delete(long id) throws MyEntityNotFoundException {
        Administrator administrator = findAdministrator(id);
        administrator.setDeleted_at();
        return true;
        //entityManager.remove(administrator);
        //return entityManager.find(Administrator.class, id) == null;
    }

    /***
     * Update a Administrator by given @Id:username
     * @param email @Id to find the proposal update Administrator
     * @param name to update Administrator
     * @param gender to update Administrator
     */
    public void update(long id, String email, String name, String gender) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        Administrator administrator = findAdministrator(id);

        //REQUIRED VALIDATION
        if (email == null || email.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"email\" is required");
        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");
        if (gender == null || gender.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"gender\" is required");

        //CHECK VALUES
        Person person = findPerson(email);
        if (person != null && person.getId() != id)
            throw new MyIllegalArgumentException("Person with email of \"" + email + "\" already exist");
        if (name.trim().length() < 6)
            throw new MyIllegalArgumentException("Field \"name\" must have at least 6 characters");
        if (!gender.trim().equals("Male") && !gender.trim().equals("Female") && !gender.trim().equals("Other"))
            throw new MyIllegalArgumentException("Field \"gender\" needs to be one of the following \"Male\", \"Female\", \"Other\"");

        administrator.setEmail(email.trim());
        administrator.setName(name.trim());
        administrator.setGender(gender.trim());

        try {
            entityManager.flush();
        }catch (Exception ex){
            throw new MyIllegalArgumentException("Error updating Administrator");
        }
    }

    /***
     * Update a Administrator password by given @Id:username
     * @param id @Id to find the proposal update Administrator
     * @param oldPassword to update Administrator
     * @param newPassword to update Administrator
     * @throws MyEntityNotFoundException
     */
    public void updatePassword(long id, String oldPassword, String newPassword, long personId) throws MyEntityNotFoundException, MyUnauthorizedException, MyIllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {
        Administrator administrator = findAdministrator(id);

        if (administrator.getId() != personId)
            throw new MyUnauthorizedException("You are not allowed to modify the password of this administrator");

        //REQUIRED VALIDATION
        if (oldPassword == null || oldPassword.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"oldPassword\" is required");
        if (newPassword == null || newPassword.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"newPassword\" is required");

        //CHECK VALUES
        if (newPassword.trim().length() < 4)
            throw new MyIllegalArgumentException("Field \"newPassword\" must have at least 4 characters");

        if (!Person.validatePassword(oldPassword.trim(), administrator.getPassword()))
            throw new MyIllegalArgumentException("Field \"oldPassword\" does not match with the current password");

        administrator.setPassword(newPassword.trim());
    }

    /***
     * Restore a Administrator by given @Id:id - Change deleted_at field to null date
     * @param id @Id to find the proposal restore Administrator
     */
    public boolean restore(long id) throws MyEntityNotFoundException, MyEntityExistsException {
        Administrator administrator = entityManager.find(Administrator.class, id);
        if (administrator == null)
            throw new MyEntityNotFoundException("Administrator \"" + id + "\" does not exist");
        if (administrator.getDeleted_at() == null)
            throw new MyEntityExistsException("Administrator \"" + id + "\" already exist");
        administrator.setDeleted_at(null);
        return true;
    }
}
