package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class BiometricDataBean {
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Creating a Biometric Data of a Biometric_Data_Type for a Patient
     * @param biometricDataTypeId @Id of Biometric_Data_Type
     * @param value that Patient got in this Biometric_Data_Type
     * @param notes given by doctor about this specific Biometric Data
     * @param patientUsername @Id of Patient
     * @param personUsername @Id of Person who is creating this biometric data
     * @return BiometricData created
     *         null if Not found Biometric_Data_Type with this id
     *         null if Not found Patient with this username
     *         null if Not found Person with this username (Who is trying to create this biometric data)
     *         null if Value out of bounds for limits in Biometric_Data_Type
     */
    public BiometricData create(long biometricDataTypeId, double value, String notes, String patientUsername, String personUsername){
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId);
        if (biometricDataType != null) {
            Patient patient = entityManager.find(Patient.class,patientUsername);
            if (patient != null){
                Person person = entityManager.find(Person.class,personUsername);
                if (person != null) {
                    if (value < biometricDataType.getMin() || value > biometricDataType.getMax())
                        return null; //value out of bounds for limits in Biometric_Data_Type

                    BiometricData newBiometricData = new BiometricData(biometricDataType, value, notes, patient, person);
                    entityManager.persist(newBiometricData);
                    entityManager.flush();
                    return newBiometricData;
                }
                return null; //Not found Person with this username (Who is trying to create this biometric data)
            }
            return null; //Not found Patient with this username
        }
        return null; //Not found Biometric_Data_Type with this id
    }


    /***
     * Delete a Biometric Data by given @Id:id
     * @param id @Id to find the proposal delete Biometric Data
     * @return Biometric Data deleted or null if dont find the Biometric Data with @Id:id given
     */
    public BiometricData delete(long id) {
        entityManager.remove(entityManager.find(BiometricData.class,id));
        return entityManager.find(BiometricData.class,id);
    }

}
