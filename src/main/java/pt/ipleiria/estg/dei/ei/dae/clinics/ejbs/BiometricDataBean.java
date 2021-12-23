package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class BiometricDataBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllBiometricData() {
        Query query = entityManager.createQuery("SELECT bioData.id, bioData.patient.username as PatientUsername, bioData.patient.name as Patient, bioData.patient.healthNo as HealthNumber, bioData.biometric_data_type.id, bioData.biometric_data_type.name as BiometricDataTypeName, bioData.value, bioData.biometric_data_type.unit as Unit, bioData.notes FROM BiometricData bioData");
        List<Object[]> biometricDataList = query.getResultList();
        return biometricDataList;
        //return (List<BiometricData>) entityManager.createNamedQuery("getAllBiometricData").getResultList();
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
     * @param patientId @Id of Patient
     * @param personId @Id of Person who is creating this biometric data
     * @return BiometricData created
     *         null if Not found Biometric_Data_Type with this id
     *         null if Not found Patient with this username
     *         null if Not found Person with this username (Who is trying to create this biometric data)
     *         null if Value out of bounds for limits in Biometric_Data_Type
     */
    public BiometricData create(long biometricDataTypeId, double value, String notes, long patientId, long personId) throws MyEntityNotFoundException, MyIllegalArgumentException {
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId);
        if (biometricDataType == null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometricDataTypeId + "\" does not exist");

        Patient patient = entityManager.find(Patient.class, patientId);
        if (patient == null)
            throw new MyEntityNotFoundException("Patient \"" + patientId + "\" does not exist");

        Person person = entityManager.find(Person.class, personId);
        if (person == null)
            throw new MyEntityNotFoundException("Person \"" + personId + "\" does not exist");

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
