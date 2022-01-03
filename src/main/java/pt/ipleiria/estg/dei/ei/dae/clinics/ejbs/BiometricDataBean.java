package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyUnauthorizedException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

    public List<BiometricData> getAllBiometricDatasClass() {
        return entityManager.createNamedQuery("getAllBiometricData", BiometricData.class).getResultList();
    }

    public List<BiometricData> getAllBiometricDatasClassWithTrashed() {
        return entityManager.createNamedQuery("getAllBiometricDataWithTrashed", BiometricData.class).getResultList();
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
        if (biometricDataTypeId == 0)
            throw new MyIllegalArgumentException("Field \"biometricTypeId\" is required");
        if (patientId == 0)
            throw new MyIllegalArgumentException("Field \"patientId\" is required");

        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricDataTypeId);
        if (biometricDataType == null || biometricDataType.getDeleted_at() != null)
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

        //THE LATEST BIOMETRIC DATA OF THAT TYPE
        TypedQuery<BiometricData> query = entityManager.createQuery("SELECT b FROM BiometricData b WHERE b.biometric_data_type.id = :type_id AND b.patient.id = :patient_id ORDER BY b.created_at DESC", BiometricData.class);
        query.setParameter("type_id", biometricDataTypeId).setParameter("patient_id", patientId);
        List<BiometricData> latestBioDatas = query.setMaxResults(1).getResultList();

        //IF THE NEW BIOMETRIC DATA IS THE FIRST INSERT OR IS MORE RECENT
        if (latestBioDatas.size() == 0 || latestBioDatas.get(0).getCreated_at().compareTo(createdAt) < 0) {

            //REMOVE TO ALL PRESCRIPTIONS RELATED TO THE BIOMETRIC DATA TYPE
            for (BiometricDataIssue issue : biometricDataType.getIssues()) {
                for (Prescription prescription : issue.getPrescriptions()) {
                    LocalDateTime startDate = prescription.getStart_date();
                    LocalDateTime endDate = prescription.getEnd_date();
                    LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

                    //ONLY ACTIVE PRESCRIPTIONS WILL BE REMOVED
                    if (now.compareTo(startDate) >= 0 && now.compareTo(endDate) <= 0) {
                        patient.removePrescription(prescription);
                        prescription.removePatient(patient);
                    }
                }
            }

            if (biometricDataIssue != null) {
                //FOREACH GLOBAL PRESCRIPTIONS OF THE ISSUE RELATED TO THE BIOMETRIC DATA
                for (Prescription prescription : biometricDataIssue.getPrescriptions()) {
                    LocalDateTime startDate = prescription.getStart_date();
                    LocalDateTime endDate = prescription.getEnd_date();
                    LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

                    //ONLY ACTIVE PRESCRIPTIONS WILL BE REMOVED
                    if (now.compareTo(startDate) >= 0 && now.compareTo(endDate) <= 0) {
                        //ADD PRESCRIPTION TO PATIENT
                        patient.addPrescription(prescription);
                        prescription.addPatient(patient);
                    }
                }
            }
        }

        BiometricData newBiometricData = new BiometricData(biometricDataType, value, notes.trim(), patient, person, source.trim(), biometricDataIssue, createdAt);
        patient.addBiometricData(newBiometricData);
        entityManager.persist(newBiometricData);
        entityManager.flush();

        return newBiometricData;
    }

    /***
     * Delete a Biometric Data by given @Id:id
     * @param id @Id to find the proposal delete Biometric Data
     */
    public boolean delete(long id) throws MyEntityNotFoundException, MyIllegalArgumentException {
        BiometricData biometricData = findBiometricData(id);

        biometricData.setDeleted_at();
        return true;
    }

    /***
     * Restore a Biometric Data by given @Id:id - Change deleted_at field to null date
     * @param id @Id to find the proposal restore Biometric Data
     */
    public boolean restore(long id) throws MyEntityNotFoundException, MyEntityExistsException {
        BiometricData biometricData = entityManager.find(BiometricData.class, id);
        if (biometricData == null)
            throw new MyEntityNotFoundException("Biometric Data \"" + id + "\" does not exist");
        if (biometricData.getDeleted_at() == null)
            throw new MyEntityExistsException("Biometric Data \"" + id + "\" already exist");
        biometricData.setDeleted_at(null);
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
    public BiometricData update(long id, long biometricTypeId, double value, String notes, long patientId, String source, Date createdAt) throws MyEntityNotFoundException, MyIllegalArgumentException, MyUnauthorizedException {
        BiometricData biometricData = entityManager.find(BiometricData.class, id);

        //REQUIRED VALIDATION
        if (source == null || source.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"source\" is required");
        if (createdAt == null)
            throw new MyIllegalArgumentException("Field \"created_at\" is required");
        if (biometricTypeId == 0)
            throw new MyIllegalArgumentException("Field \"biometricTypeId\" is required");
        if (patientId == 0)
            throw new MyIllegalArgumentException("Field \"patientId\" is required");


        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, biometricTypeId);
        if (biometricDataType == null || biometricDataType.getDeleted_at() != null)
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
        biometricData.setSource(source.trim());
        biometricData.setCreated_at(createdAt);

        for (BiometricDataIssue issue : biometricDataType.getIssues()) {
            if (value >= issue.getMin() && value < issue.getMax()) {
                biometricData.setBiometricDataIssue(issue);
                break;
            }
        }

        return biometricData;
    }
}
