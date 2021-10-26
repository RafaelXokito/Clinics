package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DoctorBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Doctor> getAllDoctors() {
        return (List<Doctor>) entityManager.createNamedQuery("getAllDoctors").getResultList();
    }

    public Doctor findDoctor(String username) {
        return entityManager.find(Doctor.class, username);
    }

    /***
     * Creating a Doctor by a Administrator
     * @param username of doctor acc
     * @param email of doctor acc
     * @param password of doctor acc
     * @param name of doctor acc
     * @param gender of doctor acc
     * @param specialty of doctor acc
     * @param created_ByUsername Administrator Username that is creating the current Doctor
     * @return Doctor created
     *         null if Not Found a Administrator with this created_ByUsername
     */
    public Doctor create(String username, String email, String password, String name, String gender, String specialty,String created_ByUsername){
        Administrator created_by = entityManager.find(Administrator.class, created_ByUsername);
        if (created_by != null) {
            Doctor newDoctor = new Doctor(username, email, password, name, gender, specialty, created_by);
            entityManager.persist(newDoctor);
            entityManager.flush();
            return newDoctor;
        }
        return null; //Not Found a Administrator with this created_ByUsername
    }

    /***
     * Delete a Doctor by given @Id:username - Change deleted_at field to NOW() date
     * @param username @Id to find the proposal delete Doctor
     * @return Doctor deleted or null if dont find the Doctor with @Id:username given
     */
    public Doctor delete(String username) {
        entityManager.find(Doctor.class,username).remove();
        return entityManager.find(Doctor.class,username);
    }

    /***
     * Update a Doctor by given @Id:username
     * @param username @Id to find the proposal update Doctor
     * @param email to update Doctor
     * @param password to update Doctor
     * @param name to update Doctor
     * @param gender to update Doctor
     * @param specialty to update Doctor
     * @return Doctor updated or null if dont find the Doctor with @Id:username given
     */
    public Doctor update(String username, String email, String password, String name, String gender, String specialty) {
        Doctor doctor = entityManager.find(Doctor.class, username);
        if (doctor != null){
            doctor.setEmail(email);
            doctor.setPassword(password);
            doctor.setName(name);
            doctor.setGender(gender);
            doctor.setSpecialty(specialty);
        }
        return doctor;
    }
}
