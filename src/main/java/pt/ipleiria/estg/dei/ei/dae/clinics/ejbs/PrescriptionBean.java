package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Stateless
public class PrescriptionBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Prescription findPrescription(long id) throws MyEntityNotFoundException {
        Prescription prescription = entityManager.find(Prescription.class, id);
        if (prescription == null)
            throw new MyEntityNotFoundException("Prescription \"" + id + "\" does not exist");

        return prescription;
    }

    public BiometricDataIssue findBiometricDataIssue(long id) {
        return entityManager.find(BiometricDataIssue.class, id);
    }

    /***
     * Creating a Prescription by a Doctor given some Biometric Data Issues
     * 
     * @param healthcareProfessionalId   Healthcare Professional Id that is
     *                                   creating the current Prescription
     * @param start_date                 that Prescription is starting
     * @param end_date                   that Prescription is ending
     * @param notes                      given by Doctor, the content of
     *                                   Prescription
     * @param biometricDataIssues        Biometric Data Issues that this
     *                                   Prescription will affect
     * @return @Id generated by autoincrement
     *         null if Not a found Doctor with this username
     *         null if Not found Biometric Data Issue with this id
     *         (bio_data_issues_id)
     */
    public long create(long healthcareProfessionalId, String start_date, String end_date, String notes,
            List<BiometricDataIssue> biometricDataIssues) throws ParseException, MyEntityNotFoundException, MyIllegalArgumentException {

        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, healthcareProfessionalId);
        if (healthcareProfessional == null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + healthcareProfessionalId + "\" does not exist");

        if (compareDates(start_date, end_date) != 2)
            throw new MyIllegalArgumentException("Error when validating dates");

        if (biometricDataIssues == null || biometricDataIssues.size() == 0)
            throw new MyIllegalArgumentException("You need to have at least 1 biometric data issue");

        Prescription prescription = new Prescription(healthcareProfessional, start_date, end_date, notes, biometricDataIssues);

        healthcareProfessional.addPrescription(prescription);

        entityManager.persist(prescription);
        entityManager.flush();

        return prescription.getId();
    }

    /***
     * Delete a Prescription by given @Id:id
     * 
     * @param id @Id to find the proposal delete Prescription
     */
    public boolean delete(long id) {
        Prescription prescription = entityManager.find(Prescription.class, id);

        //REMOVE DEPENDENCIES
        HealthcareProfessional healthcareProfessional = prescription.getHealthcareProfessional();
        List<Patient> patients = prescription.getPatients();
        List<BiometricDataIssue> biometricDataIssues = prescription.getBiometric_data_issue();

        healthcareProfessional.removePrescription(prescription);
        for (Patient patient : patients) {
            patient.removePrescription(prescription);
        }
        for (BiometricDataIssue biometricDataIssue : biometricDataIssues) {
            biometricDataIssue.removePrescription(prescription);
        }

        entityManager.remove(prescription);
        return entityManager.find(Prescription.class, id) == null;
    }

    /***
     * Update a Biometric Data Issue by given @Id:id
     * 
     * @param id                  @Id to find the proposal update Biometric Data
     *                            Issue
     * @param start_date          to update Biometric Data Issue
     * @param end_date            to update Biometric Data Issue
     * @param notes               to update Biometric Data Issue
     * @param biometricDataIssues to update Biometric Data Issue
     * @throw TODO - Acrescentar os throws e a descrição
     */
    public void update(long id, String start_date, String end_date, String notes,
                       List<BiometricDataIssue> biometricDataIssues) throws ParseException, MyEntityNotFoundException, MyIllegalArgumentException {

        if (compareDates(start_date, end_date) != 2)
            throw new MyIllegalArgumentException("Error when validating dates");

        Prescription prescription = findPrescription(id);

        boolean isGlobalPrescription = prescription.getBiometric_data_issue().size() > 0;
        if (isGlobalPrescription) {
            if (biometricDataIssues == null || biometricDataIssues.size() == 0)
                throw new MyIllegalArgumentException("You need to have at least 1 biometric data issue");
        }

        prescription.setStart_date(start_date);
        prescription.setEnd_date(end_date);
        prescription.setNotes(notes);

        if (!isGlobalPrescription)
            return;

        for (BiometricDataIssue biometricDataIssue : prescription.getBiometric_data_issue()) {
            // Se as Issues antigas ainda estiverem presentes nesta, a prescrição não é
            // removida das mesmas
            // Se já não estiverem presentes, são removidas
            biometricDataIssue.removePrescription(prescription);
        }

        prescription.setBiometricDataIssues(biometricDataIssues);

        for (BiometricDataIssue biometricDataIssue : biometricDataIssues) {
            // São adicionadas às novas Issues esta prescrição
            biometricDataIssue.addPrescription(prescription);
        }
    }

    /**
     *
     * @param d1
     * @param d2
     * @return 0 if @d2 is equals @d1
     * @return 1 if @d2 is greater then @d1
     * @return 2 if @d1 is greater then @d2
     * @return -1 if a error happened
     */
    public static int compareDates(String d1,String d2) throws ParseException {
        //1
        // Create 2 dates starts
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date1 = LocalDateTime.parse(d1,formatter);
        LocalDateTime date2 = LocalDateTime.parse(d2,formatter);

        // Create 2 dates ends
        //1

        // Date object is having 3 methods namely after,before and equals for comparing
        // after() will return true if and only if date1 is after date 2
        if(date1.isAfter(date2)){
            return 1;
        }
        // before() will return true if and only if date1 is before date2
        if(date1.isBefore(date2)){
            return 2;
        }

        //equals() returns true if both the dates are equal
        if(date1.equals(date2)){
            return 0;
        }
        return -1;
    }

    public List<Prescription> getAllPrescriptions() {
        return entityManager.createNamedQuery("getAllPrescriptions", Prescription.class).getResultList();
    }

    public List<Prescription> getActivePrescriptionsByPatient(long patientId) {
        TypedQuery<Prescription> query = entityManager.createQuery("SELECT p FROM Prescription p JOIN p.patients p2 WHERE p2.id = :patientId AND p.start_date < CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP < p.end_date ORDER BY p.id",Prescription.class)
                .setParameter("patientId", patientId);
        entityManager.flush();
        return query.getResultList();

    }
}
