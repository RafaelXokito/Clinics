package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class BiometricDataIssueBean {
    @PersistenceContext
    private EntityManager entityManager;

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
    public BiometricDataIssue create(String name, int min, int max, long biometric_data_type){
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometric_data_type);
        if (biometricDataType != null){

            if (min < biometricDataType.getMin() || max > biometricDataType.getMax())
                return null; //min or max out of bouds for limits in Biometric_Data_Type

            BiometricDataIssue newBiometricDataIssue = new BiometricDataIssue(name, min, max, biometricDataType);
            entityManager.persist(newBiometricDataIssue);
            entityManager.flush();
            return newBiometricDataIssue;
        }
        return null; //Not found Biometric_Data_Type with this id
    }

    /***
     * Delete a Biometric Data Issue by given @Id:id
     * @param id @Id to find the proposal delete Biometric Data Issue
     * @return Biometric Data Issue deleted or null if dont find the Biometric Data Issue with @Id:id given
     */
    public BiometricDataIssue delete(long id) {
        entityManager.remove(entityManager.find(BiometricDataIssue.class,id));
        return entityManager.find(BiometricDataIssue.class,id);
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
    public BiometricDataIssue update(long id, String name, int min, int max, long biometricDataTypeId){
        BiometricDataIssue biometricDataIssue = entityManager.find(BiometricDataIssue.class, id);
        if (biometricDataIssue != null) {

            BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId);
            if (biometricDataType != null) {

                biometricDataIssue.setBiometric_data_type(biometricDataType);
                biometricDataIssue.setName(name);
                biometricDataIssue.setMin(min);
                biometricDataIssue.setMax(max);

                return biometricDataIssue;

            }
            return null; //Not found BiometricDataType with the given id

        }
        return null; //Not found BiometricData with the given id
    }
}
