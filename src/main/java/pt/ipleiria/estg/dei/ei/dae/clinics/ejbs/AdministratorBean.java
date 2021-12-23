package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
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
        Query query = entityManager.createQuery("SELECT a.id, a.email, a.name, a.gender  FROM Administrator a");
        List<Object[]> administratorList = query.getResultList();
        return administratorList;
        //return entityManager.createNamedQuery("getAllAdministrators", Administrator.class).getResultList();
    }

    /***
     * Find Administrator by given @Id:id
     * @param id @Id to find Administrator
     * @return founded Administrator or Null if dont
     */
    public Administrator findAdministrator(long id) throws MyEntityNotFoundException {
        Administrator administrator = entityManager.find(Administrator.class, id);
        if (administrator == null)
            throw new MyEntityNotFoundException("Administrator \"" + id + "\" does not exist");
        return administrator;
    }

    /***
     * Find Administrator by given @Id:username
     * @param email @Id to find Administrator
     * @return founded Administrator or Null if dont
     */
    public Administrator findAdministrator(String email) {
        Query query = entityManager.createNativeQuery("SELECT a FROM Persons a WHERE a.email = '"+ email+"'", Administrator.class);
        List<Administrator> administratorList = (List<Administrator>) query.getResultList();
        System.out.println(administratorList);
        return administratorList.isEmpty() ? null : administratorList.get(0);
    }

    /***
     * Creating a Administrator Account
     * @param email of administrator acc
     * @param password of administrator acc
     * @param name of administrator acc
     * @param gender of administrator acc
     */
    public long create(String email, String password, String name, String gender) throws MyEntityExistsException {
        Administrator administrator = findAdministrator(email);
        if (administrator != null)
            throw new MyEntityExistsException("Administrator \"" + email + "\" already exist");

        Administrator newAdministrator = new Administrator(email, password, name, gender);
        entityManager.persist(newAdministrator);
        entityManager.flush();
        return newAdministrator.getId();
    }

    /***
     * Delete a Administrator by given @Id:username - Change deleted_at field to NOW() date
     * @param id @Id to find the proposal delete Administrator
     */
    public void delete(long id) throws MyEntityNotFoundException {
        Administrator administrator = findAdministrator(id);
        administrator.remove();
    }

    /***
     * Update a Administrator by given @Id:username
     * @param email @Id to find the proposal update Administrator
     * @param password to update Administrator
     * @param name to update Administrator
     * @param gender to update Administrator
     */
    public void update(long id, String email, String password, String name, String gender) throws MyEntityNotFoundException {
        Administrator administrator = findAdministrator(id);

        administrator.setEmail(email);
        administrator.setPassword(password);
        administrator.setName(name);
        administrator.setGender(gender);
    }
}
