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

    public void create(String username, String email, String password, String name, String gender) {
        Administrator administrator = new Administrator(username, email, password, name, gender);
        entityManager.persist(administrator);
    }

    public List<Administrator> getAllAdministrators() {
        return (List<Administrator>) entityManager.createNamedQuery("getAllAdministrators").getResultList();
    }

    public Administrator findAdministrator(String username) {
        return entityManager.find(Administrator.class, username);
    }
}
