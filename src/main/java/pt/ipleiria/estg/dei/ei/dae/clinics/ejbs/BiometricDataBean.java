package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BiometricDataBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<BiometricData> getAllBiometricData() {
        return (List<BiometricData>) entityManager.createNamedQuery("getAllBiometricData").getResultList();
    }

    public BiometricData findBiometricData(long id) throws MyEntityNotFoundException {
        BiometricData biometricData = entityManager.find(BiometricData.class, id);
        if (biometricData == null)
            throw new MyEntityNotFoundException("BiometricData \"" + id + "\" does not exist");

        return biometricData;
    }

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
    public BiometricData create(long biometricDataTypeId, double value, String notes, String patientUsername, String personUsername) throws MyEntityNotFoundException, MyIllegalArgumentException {
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId);
        if (biometricDataType == null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometricDataTypeId + "\" does not exist");

        Patient patient = entityManager.find(Patient.class, patientUsername);
        if (patient == null)
            throw new MyEntityNotFoundException("Patient \"" + patientUsername + "\" does not exist");

        Person person = entityManager.find(Person.class, personUsername);
        if (person == null)
            throw new MyEntityNotFoundException("Person \"" + personUsername + "\" does not exist");

        if (value < biometricDataType.getMin() || value > biometricDataType.getMax())
            throw new MyIllegalArgumentException("BiometricData \"" + value + "\" should be in bounds [" + biometricDataType.getMin() + ", " + biometricDataType.getMax() + "]");

        BiometricData newBiometricData = new BiometricData(biometricDataType, value, notes, patient, person);
        entityManager.persist(newBiometricData);
        entityManager.flush();

        return newBiometricData;
    }


    /***
     * Delete a Biometric Data by given @Id:id
     * @param id @Id to find the proposal delete Biometric Data
     */
    public void delete(long id) throws MyEntityNotFoundException {
        BiometricData biometricData = findBiometricData(id);
        entityManager.remove(biometricData);
    }

}
