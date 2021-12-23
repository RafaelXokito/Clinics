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
        Query query = entityManager.createQuery("SELECT bioData.id, bioData.name, bioData.biometric_data_type.name as BiometricDataType, bioData.biometric_data_type.id as BiometricDataTypeId, bioData.min, bioData.max FROM BiometricDataIssue bioData");
        List<Object[]> biometricDataIssueList = query.getResultList();
        return biometricDataIssueList;
        //return entityManager.createNamedQuery("getAllBiometricDataIssues", BiometricDataIssue.class).getResultList();
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

        if (min < biometricDataType.getMin() || max > biometricDataType.getMax())
            //min or max out of bounds for limits in Biometric_Data_Type
            throw new MyIllegalArgumentException("Both min \"" + min + "\"  and max \"" + max + "\" must be in bounds [" + biometricDataType.getMin() + ", " + biometricDataType.getMax() + "]");

        BiometricDataIssue newBiometricDataIssue = new BiometricDataIssue(name, min, max, biometricDataType);
        entityManager.persist(newBiometricDataIssue);
        entityManager.flush();

        return newBiometricDataIssue;
    }

    /***
     * Delete a Biometric Data Issue by given @Id:id
     * @param id @Id to find the proposal delete Biometric Data Issue
     */
    public void delete(long id) throws MyEntityNotFoundException {
        BiometricDataIssue biometricDataIssue = findBiometricDataIssue(id);
        entityManager.remove(biometricDataIssue);
    }

    /***
     * Update a Biometric Data Issue by given @Id:id
     * @param id @Id to find the proposal update Biometric Data Issue
     * @param biometricDataTypeId to update Biometric Data Issue
     * @param name to update Biometric Data Issue
     * @param min to update Biometric Data Issue
     * @param max to update Biometric Data Issue
     * @return Biometric Data
     * @throw TODO - Acrescentar os throws e a descrição
     */
    public void update(long id, String name, double min, double max, long biometricDataTypeId) throws MyEntityNotFoundException {
        BiometricDataIssue biometricDataIssue = findBiometricDataIssue(id);

        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId);
        if (biometricDataType == null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometricDataTypeId + "\" does not exist");

        biometricDataIssue.setBiometric_data_type(biometricDataType);
        biometricDataIssue.setName(name);
        biometricDataIssue.setMin(min);
        biometricDataIssue.setMax(max);
    }
}
