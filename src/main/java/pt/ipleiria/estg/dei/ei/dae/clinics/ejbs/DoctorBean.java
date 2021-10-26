package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DoctorBean {
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Creating a Doctor by a Administrator
     * @param username of doctor acc
     * @param email of doctor acc
     * @param password of doctor acc
     * @param name of doctor acc
     * @param gender of doctor acc
     * @param specialty of doctor acc
     * @param created_ByUsername Administrator Username that is creating the current Doctor
     * @return @Id passed as 'username' as confirmation
     *         -1 if Not Found a Administrator with this created_ByUsername
     */
    public String create(String username, String email, String password, String name, String gender, String specialty,String created_ByUsername){
        Administrator created_by = entityManager.find(Administrator.class, created_ByUsername);
        if (created_by != null) {
            Doctor newDoctor = new Doctor(username, email, password, name, gender, specialty, created_by);
            entityManager.persist(newDoctor);
            entityManager.flush();
            return newDoctor.getUsername();
        }
        return "-1"; //Not Found a Administrator with this created_ByUsername
    }

    public Doctor delete(String username) {
        entityManager.find(Doctor.class,username).remove();
        return entityManager.find(Doctor.class,username);
    }
}
