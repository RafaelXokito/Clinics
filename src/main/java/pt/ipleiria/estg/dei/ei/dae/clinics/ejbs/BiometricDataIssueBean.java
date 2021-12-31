package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BiometricDataIssueBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllBiometricDataIssues() {
        Query query = entityManager.createQuery("SELECT b.id, b.name, b.biometric_data_type.name FROM BiometricDataIssue b ORDER BY b.id DESC");
        List<Object[]> biometricDataIssueList = query.getResultList();
        return biometricDataIssueList;
    }

    public BiometricDataIssue findBiometricDataIssue(long id) throws MyEntityNotFoundException {
        BiometricDataIssue biometricDataIssue = entityManager.find(BiometricDataIssue.class, id);
        if (biometricDataIssue == null)
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
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometric_data_type);
        if (biometricDataType == null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometric_data_type + "\" does not exist");

        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");

        if (max < min)
            throw new MyIllegalArgumentException("Field \"max\" must be greater or equal to the field \"min\"");

        if (min < biometricDataType.getMin() || max > biometricDataType.getMax())
            //min or max out of bounds for limits in Biometric_Data_Type
            throw new MyIllegalArgumentException("Both min \"" + min + "\"  and max \"" + max + "\" must be in bounds [" + biometricDataType.getMin() + ", " + biometricDataType.getMax() + "[");

        for (BiometricDataIssue issue : biometricDataType.getIssues()) {
            if (min >= issue.getMin() && max < issue.getMax())
                throw new MyIllegalArgumentException("Interval [" + min + ", " + max + "[ collide with another biometric data issue");
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
        biometricDataIssue.getBiometric_data_type().removeIssue(biometricDataIssue);
        entityManager.remove(biometricDataIssue);
        return entityManager.find(BiometricDataIssue.class, id) == null;
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

        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId);
        if (biometricDataType == null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometricDataTypeId + "\" does not exist");

        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");

        if (max < min)
            throw new MyIllegalArgumentException("Field \"max\" must be greater or equal to the field \"min\"");

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
