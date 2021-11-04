package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class HealthcareProfessionalBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllDoctors() {
        Query query = entityManager.createQuery("SELECT d.username, d.email, d.name, d.gender, d.specialty  FROM HealthcareProfessional d");
        List<Object[]> doctorList = query.getResultList();
        return doctorList;
        //return entityManager.createNamedQuery("getAllDoctors", Doctor.class).getResultList();
    }

    public HealthcareProfessional findDoctor(String username) throws MyEntityNotFoundException {
        HealthcareProfessional doctor = entityManager.find(HealthcareProfessional.class, username);
        if (doctor == null)
            throw new MyEntityNotFoundException("Doctor \"" + username + "\" does not exist");

        return doctor;
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
     */
    public void create(String username, String email, String password, String name, String gender, String specialty,String created_ByUsername) throws MyEntityNotFoundException, MyEntityExistsException {
        HealthcareProfessional doctor = findDoctor(username);
        if (doctor != null)
            throw new MyEntityExistsException("Doctor \"" + username + "\" already exist");

        Administrator created_by = entityManager.find(Administrator.class, created_ByUsername);
        if (created_by == null)
            throw new MyEntityNotFoundException("Administrator \"" + username + "\" does not exist");

        HealthcareProfessional newDoctor = new HealthcareProfessional(username, email, password, name, gender, specialty, created_by);
        entityManager.persist(newDoctor);
    }

    /***
     * Delete a Doctor by given @Id:username - Change deleted_at field to NOW() date
     * @param username @Id to find the proposal delete Doctor
     */
    public void delete(String username) throws MyEntityNotFoundException {
        HealthcareProfessional doctor = findDoctor(username);
        entityManager.remove(doctor);
    }

    /***
     * Update a Doctor by given @Id:username
     * @param username @Id to find the proposal update Doctor
     * @param email to update Doctor
     * @param password to update Doctor
     * @param name to update Doctor
     * @param gender to update Doctor
     * @param specialty to update Doctor
     */
    public void update(String username, String email, String password, String name, String gender, String specialty) throws MyEntityNotFoundException {
        HealthcareProfessional doctor = findDoctor(username);

        doctor.setEmail(email);
        doctor.setPassword(password);
        doctor.setName(name);
        doctor.setGender(gender);
        doctor.setSpecialty(specialty);
    }
}
