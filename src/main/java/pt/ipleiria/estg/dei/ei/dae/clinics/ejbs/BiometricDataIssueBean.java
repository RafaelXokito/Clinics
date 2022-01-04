package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BiometricDataIssueBean {
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Execute BiometricDataIssue query getAllBiometricDataIssues getting all BiometricDataIssue Class
     * @return a list of All BiometricDataIssue
     */
    public List<BiometricDataIssue> getAllBiometricDataIssuesClass() {
        return entityManager.createNamedQuery("getAllBiometricDataIssues", BiometricDataIssue.class).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    /***
     * Execute BiometricDataIssue query getAllBiometricDataIssuesWithTrashed getting all BiometricDataIssue Class
     * @return a list of All BiometricDataIssue
     */
    public List<BiometricDataIssue> getAllBiometricDataIssuesClassWithTrashed() {
        return entityManager.createNamedQuery("getAllBiometricDataIssuesWithTrashed", BiometricDataIssue.class).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    /***
     * Find BiometricDataIssue by given @Id:id
     * @param id @Id to find BiometricDataIssue
     * @return founded BiometricDataIssue or Null if dont
     */
    public BiometricDataIssue findBiometricDataIssue(long id) throws MyEntityNotFoundException {
        BiometricDataIssue biometricDataIssue = entityManager.find(BiometricDataIssue.class, id);
        if (biometricDataIssue == null || biometricDataIssue.getDeleted_at() != null)
            throw new MyEntityNotFoundException("BiometricDataIssue \"" + id + "\" does not exist");
        return biometricDataIssue;
    }

    /***
     * Creating a Biometric Data Issue based on a Biometric Data Type
     * @param name of the Biometric Data Issue ("Febre")
     * @param min value to be with this Issue (38)
     * @param max value to be with this Issue (45)
     * @param biometric_data_type @Id of Biometric_Data_Type (1:"Temperatura Corporal")
     * @return BiometricDataIssue created
     *         null if not found Biometric_Data_Type
     *         null if min or max out of bouds for limits in Biometric_Data_Type
     */
    public BiometricDataIssue create(String name, double min, double max, long biometric_data_type) throws MyEntityNotFoundException, MyIllegalArgumentException {
        if (biometric_data_type == 0)
            throw new MyIllegalArgumentException("Field \"biometricDataTypeId\" is required");

        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometric_data_type, LockModeType.PESSIMISTIC_READ);
        if (biometricDataType == null || biometricDataType.getDeleted_at() != null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometric_data_type + "\" does not exist");

        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");

        if (max <= min)
            throw new MyIllegalArgumentException("Field \"max\" must be greater than the field \"min\"");

        if (min < biometricDataType.getMin() || max > biometricDataType.getMax())
            throw new MyIllegalArgumentException("Both min \"" + min + "\"  and max \"" + max + "\" must be in bounds [" + biometricDataType.getMin() + ", " + biometricDataType.getMax() + "[");

        for (BiometricDataIssue issue : biometricDataType.getIssues()) {
            if ((min >= issue.getMin() && min < issue.getMax()) || (max > issue.getMin() && max <= issue.getMax()))
                throw new MyIllegalArgumentException("Interval [" + min + ", " + max + "[ collide with \"" + issue.getName() + "\" biometric data issue");
        }

        BiometricDataIssue newBiometricDataIssue = new BiometricDataIssue(name.trim(), min, max, biometricDataType);
        biometricDataType.addIssue(newBiometricDataIssue);

        entityManager.persist(newBiometricDataIssue);
        entityManager.flush();

        return newBiometricDataIssue;
    }

    /***
     * Delete a Biometric Data Issue by given @Id:id
     * @param id @Id to find the proposal delete Biometric Data Issue
     */
    public boolean delete(long id) throws MyEntityNotFoundException {
        BiometricDataIssue biometricDataIssue = findBiometricDataIssue(id);
        entityManager.lock(biometricDataIssue, LockModeType.PESSIMISTIC_WRITE);
        biometricDataIssue.setDeleted_at();
        return true;
    }

    /***
     * Restore a Biometric Data Issue by given @Id:id - Change deleted_at field to null date
     * @param id @Id to find the proposal restore Observation
     */
    public boolean restore(long id) throws MyEntityNotFoundException, MyEntityExistsException {
        BiometricDataIssue biometricDataIssue = entityManager.find(BiometricDataIssue.class, id);
        if (biometricDataIssue == null)
            throw new MyEntityNotFoundException("Biometric Data Issue \"" + id + "\" does not exist");
        if (biometricDataIssue.getDeleted_at() == null)
            throw new MyEntityExistsException("Biometric Data Issue \"" + id + "\" already exist");
        if (biometricDataIssue.getBiometric_data_type().getDeleted_at() != null)
            throw new MyEntityNotFoundException("Biometric Data Type \"" + biometricDataIssue.getBiometric_data_type().getName() + "\" does not exist");

        biometricDataIssue.setDeleted_at(null);
        return true;
    }

    /***
     * Update a Biometric Data Issue by given @Id:id
     * @param id @Id to find the proposal update Biometric Data Issue
     * @param biometricDataTypeId to update Biometric Data Issue
     * @param name to update Biometric Data Issue
     * @param min to update Biometric Data Issue
     * @param max to update Biometric Data Issue
     * @return Biometric Data
     */
    public BiometricDataIssue update(long id, String name, double min, double max, long biometricDataTypeId) throws MyEntityNotFoundException, MyIllegalArgumentException {
        BiometricDataIssue biometricDataIssue = findBiometricDataIssue(id);
        entityManager.lock(biometricDataIssue, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        if (biometricDataTypeId == 0)
            throw new MyIllegalArgumentException("Field \"biometricDataTypeId\" is required");

        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId, LockModeType.OPTIMISTIC);
        if (biometricDataType == null || biometricDataType.getDeleted_at() != null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometricDataTypeId + "\" does not exist");

        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");

        if (max <= min)
            throw new MyIllegalArgumentException("Field \"max\" must be greater than the field \"min\"");

        if (min < biometricDataType.getMin() || max > biometricDataType.getMax())
            //min or max out of bounds for limits in Biometric_Data_Type
            throw new MyIllegalArgumentException("Both min \"" + min + "\"  and max \"" + max + "\" must be in bounds [" + biometricDataType.getMin() + ", " + biometricDataType.getMax() + "[");

        for (BiometricDataIssue issue : biometricDataType.getIssues()) {
            if (issue.getId() == id) continue; //SKIP THE ISSUE TO UPDATE

            if (min >= issue.getMin() && max < issue.getMax())
                throw new MyIllegalArgumentException("Interval [" + min + ", " + max + "[ collide with another biometric data issue");
        }

        biometricDataIssue.getBiometric_data_type().removeIssue(biometricDataIssue);
        biometricDataIssue.setBiometric_data_type(biometricDataType);
        biometricDataType.addIssue(biometricDataIssue);
        biometricDataIssue.setName(name.trim());
        biometricDataIssue.setMin(min);
        biometricDataIssue.setMax(max);

        return biometricDataIssue;
    }
}
