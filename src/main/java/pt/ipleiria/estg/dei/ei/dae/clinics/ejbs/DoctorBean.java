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

    public void create(String username, String email, String password, String name, String gender, String specialty,String created_ByUsername){
        Administrator created_by = entityManager.find(Administrator.class, created_ByUsername);
        if (created_by != null) {
            Doctor doctor = new Doctor(username, email, password, name, gender, specialty, created_by);
            entityManager.persist(doctor);
        }
    }
}
