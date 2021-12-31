package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class BiometricDataBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllBiometricData() {
        Query query = entityManager.createQuery("SELECT b.id, b.patient.name, b.patient.healthNo, b.biometric_data_type.name, b.value, b.biometric_data_type.unit, b.created_at FROM BiometricData b ORDER BY b.created_at DESC");
        List<Object[]> biometricDataList = query.getResultList();
        return biometricDataList;
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
     * @param notes given by healthcare professional about this specific Biometric Data
     * @param patientId @Id of Patient
     * @param personId @Id of Person who is creating this biometric data
     * @return BiometricData created
     *         null if Not found Biometric_Data_Type with this id
     *         null if Not found Patient with this username
     *         null if Not found Person with this username (Who is trying to create this biometric data)
     *         null if Value out of bounds for limits in Biometric_Data_Type
     */
    public BiometricData create(long biometricDataTypeId, double value, String notes, long patientId, long personId, String source, Date createdAt) throws MyEntityNotFoundException, MyIllegalArgumentException {
        //REQUIRED VALIDATION
        if (source == null || source.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"source\" is required");
        if (createdAt == null)
            throw new MyIllegalArgumentException("Field \"created_at\" is required");

        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId);
        if (biometricDataType == null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometricDataTypeId + "\" does not exist");

        Patient patient = entityManager.find(Patient.class, patientId);
        if (patient == null || patient.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Patient \"" + patientId + "\" does not exist");

        Person person = entityManager.find(Person.class, personId);
        if (person == null || person.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Person \"" + personId + "\" does not exist");

        if (value < biometricDataType.getMin() || value >= biometricDataType.getMax())
            throw new MyIllegalArgumentException("BiometricData \"" + value + "\" should be in bounds [" + biometricDataType.getMin() + ", " + biometricDataType.getMax() + "[");

        //CHECK VALUES
        if (!source.trim().equals("Exam") && !source.trim().equals("Sensor") && !source.trim().equals("Wearable"))
            throw new MyIllegalArgumentException("Field \"source\" needs to be one of the following \"Exam\", \"Sensor\", \"Wearable\"");

        BiometricDataIssue biometricDataIssue = null;
        for (BiometricDataIssue issue : biometricDataType.getIssues()) {
            if (value >= issue.getMin() && value <= issue.getMax()) {
                biometricDataIssue = issue;
                break;
            }
        }

        //REMOVE TO ALL PRESCRIPTIONS RELATED TO THE BIOMETRIC DATA TYPE
        for (BiometricDataIssue issue : biometricDataType.getIssues()) {
            for (Prescription prescription : issue.getPrescriptions()) {
                LocalDateTime startDate = prescription.getStart_date();
                LocalDateTime endDate = prescription.getEnd_date();
                LocalDateTime date = LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault());

                //IF BIOMETRIC DATA DATE IS IN BETWEEN START DATE AND END DATE OF PRESCRIPTION
                if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                    patient.removePrescription(prescription);
                }
            }
        }

        if (biometricDataIssue != null) {
            //FOREACH GLOBAL PRESCRIPTIONS OF THE ISSUE RELATED TO THE BIOMETRIC DATA
            for (Prescription prescription : biometricDataIssue.getPrescriptions()) {

                LocalDateTime startDate = prescription.getStart_date();
                LocalDateTime endDate = prescription.getEnd_date();
                LocalDateTime date = LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault());

                //IF BIOMETRIC DATA DATE IS IN BETWEEN START DATE AND END DATE OF PRESCRIPTION
                if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                    //ADD PRESCRIPTION TO PATIENT
                    patient.addPrescription(prescription);
                }
            }
        }

        BiometricData newBiometricData = new BiometricData(biometricDataType, value, notes.trim(), patient, person, source.trim(), biometricDataIssue, createdAt);
        entityManager.persist(newBiometricData);
        patient.addBiometricData(newBiometricData);
        entityManager.flush();

        return newBiometricData;
    }

    /***
     * Delete a Biometric Data by given @Id:id
     * @param id @Id to find the proposal delete Biometric Data
     */
    public boolean delete(long id) throws MyEntityNotFoundException {
        BiometricData biometricData = findBiometricData(id);
        biometricData.setDeleted_at();
        return true;
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
            throw new MyIllegalArgumentException("Biometric Data with id " + id + " does not belongs to you");

        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricTypeId);
        if (biometricDataType == null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + biometricTypeId + "\" does not exist");

        Patient patient = entityManager.find(Patient.class, patientId);
        if (patient == null || patient.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Patient \"" + patientId + "\" does not exist");

        if (value < biometricDataType.getMin() || value >= biometricDataType.getMax())
            throw new MyIllegalArgumentException("BiometricData \"" + value + "\" should be in bounds [" + biometricDataType.getMin() + ", " + biometricDataType.getMax() + "[");

        //CHECK VALUES
        if (!source.trim().equals("Exam") && !source.trim().equals("Sensor") && !source.trim().equals("Wearable"))
            throw new MyIllegalArgumentException("Field \"source\" needs to be one of the following \"Exam\", \"Sensor\", \"Wearable\"");

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
