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
     * @return @Id generated by autoincrement
     *         -1 if Not found Biometric_Data_Type with this id
     *         -2 if Not found Patient with this username
     *         -3 if Not found Person with this username (Who is trying to create this biometric data)
     *         -4 if Value out of bounds for limits in Biometric_Data_Type
     */
    public long create(long biometricDataTypeId, double value, String notes, String patientUsername, String personUsername){
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId);
        if (biometricDataType != null) {
            Patient patient = entityManager.find(Patient.class,patientUsername);
            if (patient != null){
                Person person = entityManager.find(Person.class,personUsername);
                if (person != null) {
                    if (value < biometricDataType.getMin() || value > biometricDataType.getMax())
                        return -4; //value out of bounds for limits in Biometric_Data_Type

                    BiometricData newBiometricData = new BiometricData(biometricDataType, value, notes, patient, person);
                    entityManager.persist(newBiometricData);
                    entityManager.flush();
                    return newBiometricData.getId();
                }
                return -3;
            }
            return -2; //Not found Patient with this username
        }
        return -1; //Not found Biometric_Data_Type with this id
    }


    public BiometricData delete(long id) {
        entityManager.remove(entityManager.find(BiometricData.class,id));
        return entityManager.find(BiometricData.class,id);
    }

}
