package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Stateless
public class BiometricDataBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllBiometricData() {
        Query query = entityManager.createQuery("SELECT b.id, b.patient.name, b.patient.healthNo, b.biometric_data_type.name, b.value, b.biometric_data_type.unit FROM BiometricData b");
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
    public BiometricData create(long biometricDataTypeId, double value, String notes, long patientId, long personId, String source, Date createdAt) throws MyEntityNotFoundException, MyIllegalArgumentException {
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

        BiometricDataIssue biometricDataIssue = null;
        for (BiometricDataIssue issue : biometricDataType.getIssues()) {
            if (value >= issue.getMin() && value <= issue.getMax()) {
                biometricDataIssue = issue;
                break;
            }
        }

        BiometricData newBiometricData = new BiometricData(biometricDataType, value, notes, patient, person, source, biometricDataIssue, createdAt);
        entityManager.persist(newBiometricData);
        entityManager.flush();

        return newBiometricData;
    }

    /***
     * Delete a Biometric Data by given @Id:id
     * @param id @Id to find the proposal delete Biometric Data
     */
    public boolean delete(long id) throws MyEntityNotFoundException {
        BiometricData biometricData = findBiometricData(id);
        entityManager.remove(biometricData);
        return entityManager.find(BiometricData.class, id) == null;
    }

    /***
     * Creating a Biometric Data of a Biometric_Data_Type for a Patient
     * @param id @Id of Biometric_Data to be updated
     * @param biometricTypeId @Id of Biometric_Data_Type
     * @param value that Patient got in this Biometric_Data_Type
     * @param notes given by doctor about this specific Biometric Data
     * @param patientId @Id of Patient
     * @return BiometricData created
     *         null if Not found Biometric_Data_Type with this id
     *         null if Not found Patient with this username
     *         null if Not found Person with this username (Who is trying to create this biometric data)
     *         null if Value out of bounds for limits in Biometric_Data_Type
     */
    public BiometricData update(long id, long biometricTypeId, double value, String notes, long patientId, long personId, String source, Date createdAt)  throws MyEntityNotFoundException, MyIllegalArgumentException{
        BiometricData biometricData = entityManager.find(BiometricData.class, id);

        if (personId != biometricData.getCreated_by().getId())
            throw new MyIllegalArgumentException("BiometricData " + id + " was not created by you");

        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricTypeId);
        if (biometricDataType == null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometricTypeId + "\" does not exist");

        Patient patient = entityManager.find(Patient.class, patientId);
        if (patient == null)
            throw new MyEntityNotFoundException("Patient \"" + patientId + "\" does not exist");

        if (value < biometricDataType.getMin() || value > biometricDataType.getMax())
            throw new MyIllegalArgumentException("BiometricData \"" + value + "\" should be in bounds [" + biometricDataType.getMin() + ", " + biometricDataType.getMax() + "]");

        biometricData.setBiometric_data_type(biometricDataType);
        biometricData.setValue(value);
        biometricData.setPatient(patient);
        biometricData.setNotes(notes);
        biometricData.setSource(source);
        biometricData.setCreated_at(createdAt);

        for (BiometricDataIssue issue : biometricDataType.getIssues()) {
            if (value >= issue.getMin() && value <= issue.getMax()) {
                biometricData.setBiometricDataIssue(issue);
                break;
            }
        }

        return biometricData;
    }
}
