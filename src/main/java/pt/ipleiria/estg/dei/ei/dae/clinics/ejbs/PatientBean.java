package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

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

    public List<Patient> getAllPatients() {
        return (List<Patient>) entityManager.createNamedQuery("getAllPatients").getResultList();
    }

    public Patient findPatient(String username) {
        return entityManager.find(Patient.class, username);
    }

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
     *         null if Not Found a Doctor with this created_byUsername
     */
    public Patient create(String username, String email, String password, String name, String gender, int healthNo, String created_byUsername){
        Doctor doctor = entityManager.find(Doctor.class, created_byUsername);
        if (doctor != null){
            Patient newPatient = new Patient(username,email,password,name,gender,healthNo,doctor);
            entityManager.persist(newPatient);
            entityManager.flush();
            doctor.addPatient(newPatient);
            return newPatient;
        }
        return null; //Not Found a Doctor with this created_byUsername
    }

    /***
     * Delete a Patient by given @Id:username - Change deleted_at field to NOW() date
     * @param username @Id to find the proposal delete Patient
     * @return Patient deleted or null if dont find the Patient with @Id:username given
     */
    public Patient delete(String username) {
        entityManager.find(Patient.class,username).remove();
        return entityManager.find(Patient.class,username);
    }

    /***
     * Update a Patient by given @Id:username
     * @param username @Id to find the proposal update Patient
     * @param email to update Patient
     * @param password to update Patient
     * @param name to update Patient
     * @param gender to update Patient
     * @param healthNo to update Patient
     * @return Patient updated or null if dont find the Patient with @Id:username given
     */
    public Patient update(String username, String email, String password, String name, String gender, int healthNo) {
        Patient patient = entityManager.find(Patient.class, username);
        if (patient != null){
            patient.setEmail(email);
            patient.setPassword(password);
            patient.setName(name);
            patient.setGender(gender);
            patient.setHealthNo(healthNo);
        }
        return patient;
    }
}
