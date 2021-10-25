package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Biometric_Data;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PatientBean {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(String username, String email, String password, String name, String gender, int healthNo, String created_by){
        Doctor doctor = entityManager.find(Doctor.class, created_by);
        if (doctor != null){
            Patient newPatient = new Patient(username,email,password,name,gender,healthNo,doctor);
            entityManager.persist(newPatient);
        }
    }
}
