package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AdministratorBean {
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Creating a Administrator Account
     * @param username of administrator acc
     * @param email of administrator acc
     * @param password of administrator acc
     * @param name of administrator acc
     * @param gender of administrator acc
     * @return @Id passed as 'username' as confirmation
     */
    public String create(String username, String email, String password, String name, String gender) {
        Administrator newAdministrator = new Administrator(username, email, password, name, gender);
        entityManager.persist(newAdministrator);
        entityManager.flush();
        return newAdministrator.getUsername();
    }

    /***
     * Execute Administrator query getAllAdministrators getting all Administrators
     * @return a list of All Administrators
     */
    public List<Administrator> getAllAdministrators() {
        return (List<Administrator>) entityManager.createNamedQuery("getAllAdministrators").getResultList();
    }

    /***
     * Find Administrator by given @Id:username
     * @param username @Id to find Administrator
     * @return founded Administrator or Null if dont
     */
    public Administrator findAdministrator(String username) {
        return entityManager.find(Administrator.class, username);
    }

    /***
     * Delete a Administrator by given @Id:username - Change deleted_at field to NOW() date
     * @param username @Id to find the proposal delete Administrator
     * @return Administrator deleted or null if dont find the Administrator with @Id:username given
     */
    public Administrator delete(String username) {
        entityManager.find(Administrator.class,username).remove();
        return entityManager.find(Administrator.class,username);
    }

    /***
     * Update a Administrator by given @Id:username
     * @param username @Id to find the proposal update Administrator
     * @param email to update Administrator
     * @param password to update Administrator
     * @param name to update Administrator
     * @param gender to update Administrator
     * @return Administrator updated or null if dont find the Administrator with @Id:username given
     */
    public Administrator update(String username, String email, String password, String name, String gender) {
        Administrator administrator = entityManager.find(Administrator.class, username);
        if (administrator != null){
            administrator.setEmail(email);
            administrator.setPassword(password);
            administrator.setName(name);
            administrator.setGender(gender);
        }
        return administrator;
    }
}
