package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
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
public class AdministratorBean {
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Execute Administrator query getAllAdministrators getting all Administrators
     * @return a list of All Administrators
     */
    public List<Object[]> getAllAdministrators() {
        Query query = entityManager.createQuery("SELECT a.id, a.email, a.name, a.gender  FROM Administrator a ORDER BY a.id DESC");
        List<Object[]> administratorList = query.getResultList();
        return administratorList;
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
    public Administrator findAdministrator(String email) {
        TypedQuery<Administrator> query = entityManager.createQuery("SELECT a FROM Person a WHERE a.email = '"+ email+"'", Administrator.class);
        return query.getResultList().size() > 0 ? query.getSingleResult() : null;
    }

    /***
     * Creating a Administrator Account
     * @param email of administrator acc
     * @param password of administrator acc
     * @param name of administrator acc
     * @param gender of administrator acc
     */
    public long create(String email, String password, String name, String gender) throws MyEntityExistsException {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || gender.isEmpty()) {
            throw new IllegalArgumentException("Invalid data given.");
        }
        Administrator administrator = findAdministrator(email);
        if (administrator != null)
            throw new MyEntityExistsException("Administrator with email of \"" + email + "\" already exist");

        Administrator newAdministrator = new Administrator(email, password, name, gender);
        entityManager.persist(newAdministrator);
        entityManager.flush();
        return newAdministrator.getId();
    }

    /***
     * Delete a Administrator by given @Id:id - Change deleted_at field to NOW() date
     * @param id @Id to find the proposal delete Administrator
     */
    public boolean delete(long id) throws MyEntityNotFoundException {
        Administrator administrator = findAdministrator(id);
        entityManager.remove(administrator);
        return entityManager.find(Administrator.class, id) == null;
    }

    /***
     * Update a Administrator by given @Id:username
     * @param email @Id to find the proposal update Administrator
     * @param name to update Administrator
     * @param gender to update Administrator
     */
    public void update(long id, String email, String name, String gender) throws MyEntityNotFoundException {
        Administrator administrator = findAdministrator(id);

        administrator.setEmail(email);
        administrator.setName(name);
        administrator.setGender(gender);
    }

    public void updatePassword(long id, String oldPassword, String newPassword) throws MyEntityNotFoundException, MyIllegalArgumentException {
        Administrator administrator = findAdministrator(id);

        Administrator administratorOldPassword = new Administrator();
        administratorOldPassword.setPassword(oldPassword);

        if (!administrator.getPassword().equals(administratorOldPassword.getPassword()))
            throw new MyIllegalArgumentException("Password does not match with the old one");

        administrator.setPassword(newPassword);
    }
}
