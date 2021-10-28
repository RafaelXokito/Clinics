package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

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
        Query query = entityManager.createQuery("SELECT a.username, a.email, a.name, a.gender  FROM Administrator a");
        List<Object[]> administratorList = query.getResultList();
        return administratorList;
        //return entityManager.createNamedQuery("getAllAdministrators", Administrator.class).getResultList();
    }

    /***
     * Find Administrator by given @Id:username
     * @param username @Id to find Administrator
     * @return founded Administrator or Null if dont
     */
    public Administrator findAdministrator(String username) throws MyEntityNotFoundException {
        Administrator administrator = entityManager.find(Administrator.class, username);
        if (administrator == null)
            throw new MyEntityNotFoundException("Administrator \"" + username + "\" does not exist");
        return administrator;
    }

    /***
     * Creating a Administrator Account
     * @param username of administrator acc
     * @param email of administrator acc
     * @param password of administrator acc
     * @param name of administrator acc
     * @param gender of administrator acc
     */
    public void create(String username, String email, String password, String name, String gender) throws MyEntityExistsException {
        Administrator administrator = entityManager.find(Administrator.class, username);
        if (administrator != null)
            throw new MyEntityExistsException("Administrator \"" + username + "\" already exist");

        Administrator newAdministrator = new Administrator(username, email, password, name, gender);
        entityManager.persist(newAdministrator);
    }

    /***
     * Delete a Administrator by given @Id:username - Change deleted_at field to NOW() date
     * @param username @Id to find the proposal delete Administrator
     */
    public void delete(String username) throws MyEntityNotFoundException {
        Administrator administrator = findAdministrator(username);
        administrator.remove();
    }

    /***
     * Update a Administrator by given @Id:username
     * @param username @Id to find the proposal update Administrator
     * @param email to update Administrator
     * @param password to update Administrator
     * @param name to update Administrator
     * @param gender to update Administrator
     */
    public void update(String username, String email, String password, String name, String gender) throws MyEntityNotFoundException {
        Administrator administrator = findAdministrator(username);

        administrator.setEmail(email);
        administrator.setPassword(password);
        administrator.setName(name);
        administrator.setGender(gender);
    }
}
