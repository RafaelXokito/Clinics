package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PatientBean {

    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Creating a Patient by a Doctor
     * @param username of doctor acc
     * @param email of doctor acc
     * @param password of doctor acc
     * @param name of doctor acc
     * @param gender of doctor acc
     * @param healthNo of doctor acc
     * @param created_byUsername Doctor Username that is creating the current Patient
     * @return @Id passed as 'username' as confirmation
     *         -1 if Not Found a Doctor with this created_byUsername
     */
    public String create(String username, String email, String password, String name, String gender, int healthNo, String created_byUsername){
        Doctor doctor = entityManager.find(Doctor.class, created_byUsername);
        if (doctor != null){
            Patient newPatient = new Patient(username,email,password,name,gender,healthNo,doctor);
            entityManager.persist(newPatient);
            entityManager.flush();
            doctor.addPatient(newPatient);
            return newPatient.getUsername();
        }
        return "-1";
    }

    public Patient delete(String username) {
        entityManager.find(Patient.class,username).remove();
        return entityManager.find(Patient.class,username);
    }
}
