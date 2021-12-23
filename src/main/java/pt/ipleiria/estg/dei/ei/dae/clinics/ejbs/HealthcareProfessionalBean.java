package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;
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

    public List<Object[]> getAllHealthcareProfessionals() {
        Query query = entityManager.createQuery("SELECT d.id, d.email, d.name, d.gender, d.specialty  FROM HealthcareProfessional d");
        List<Object[]> healthcareProfessionalList = query.getResultList();
        return healthcareProfessionalList;
        //return entityManager.createNamedQuery("getAllDoctors", Doctor.class).getResultList();
    }

    public HealthcareProfessional findHealthcareProfessional(long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, id);
        if (healthcareProfessional == null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + id + "\" does not exist");

        return healthcareProfessional;
    }

    public HealthcareProfessional findHealthcareProfessional(String email) {
        Query query = entityManager.createNativeQuery("SELECT p FROM Persons p WHERE p.email = '"+ email+"'", HealthcareProfessional.class);
        List<HealthcareProfessional> healthcareProfessionalList = (List<HealthcareProfessional>) query.getResultList();
        return healthcareProfessionalList.isEmpty() ? null : healthcareProfessionalList.get(0);
    }

    /***
     * Creating a Doctor by a Administrator
     * @param email of doctor acc
     * @param password of doctor acc
     * @param name of doctor acc
     * @param gender of doctor acc
     * @param specialty of doctor acc
     * @param created_ById Administrator Username that is creating the current Doctor
     */
    public long create(String email, String password, String name, String gender, String specialty,long created_ById) throws MyEntityNotFoundException, MyEntityExistsException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(email);
        if (healthcareProfessional != null)
            throw new MyEntityExistsException("Healthcare Professional with an email of \"" + email + "\" already exist");

        Administrator created_by = entityManager.find(Administrator.class, created_ById);
        if (created_by == null)
            throw new MyEntityNotFoundException("Administrator \"" + email + "\" does not exist");

        HealthcareProfessional newHealthcareProfessional = new HealthcareProfessional(email, password, name, gender, specialty, created_by);
        entityManager.persist(newHealthcareProfessional);
        entityManager.flush();
        return newHealthcareProfessional.getId();
    }

    /***
     * Delete a Doctor by given @Id:username - Change deleted_at field to NOW() date
     * @param id @Id to find the proposal delete Doctor
     */
    public boolean delete(long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);
        entityManager.remove(healthcareProfessional);
        return entityManager.find(HealthcareProfessional.class, id) == null;
    }

    /***
     * Update a Doctor by given @Id:username
     * @param email @Id to find the proposal update Doctor
     * @param name to update Doctor
     * @param gender to update Doctor
     * @param specialty to update Doctor
     */
    public void update(long id, String email, String name, String gender, String specialty) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = findHealthcareProfessional(id);

        healthcareProfessional.setEmail(email);
        healthcareProfessional.setName(name);
        healthcareProfessional.setGender(gender);
        healthcareProfessional.setSpecialty(specialty);
    }
}
