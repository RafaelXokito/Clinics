package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PrescriptionBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllPrescriptions() {
        Query query = entityManager
                .createQuery("SELECT p.id, p.healthcareProfessional.id, p.healthcareProfessional.name, p.start_date, p.end_date FROM Prescription p");
        List<Object[]> prescriptionList = query.getResultList();
        return prescriptionList;
        // return entityManager.createNamedQuery("getAllPrescriptions",
        // Prescription.class).getResultList();
    }

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
            List<BiometricDataIssue> biometricDataIssues) throws ParseException, MyEntityNotFoundException {

        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, healthcareProfessionalId);
        if (healthcareProfessional == null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + healthcareProfessionalId + "\" does not exist");

        Prescription prescription = new Prescription(healthcareProfessional, start_date, end_date, notes, biometricDataIssues);

        healthcareProfessional.addPrescription(prescription);

        for (BiometricDataIssue biometricDataIssue : biometricDataIssues) {
            biometricDataIssue.addPrescription(prescription);
        }

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
                       List<BiometricDataIssue> biometricDataIssues) throws ParseException, MyEntityNotFoundException {
        Prescription prescription = findPrescription(id);

        prescription.setStart_date(start_date);
        prescription.setEnd_date(end_date);
        prescription.setNotes(notes);

        if (prescription.getPatient() != null)
            return;

        for (BiometricDataIssue biometricDataIssue : prescription.getBiometric_data_issue()) {
            // Se as Issues antigas ainda estiverem presentes nesta, a prescrição não é
            // removida das mesmas
            // Se já não estiverem presentes, são removidas
            if (!biometricDataIssues.contains(biometricDataIssue))
                biometricDataIssue.removePrescription(prescription);
        }

        prescription.setBiometricDataIssues(biometricDataIssues);

        for (BiometricDataIssue biometricDataIssue : biometricDataIssues) {
            // São adicionadas às novas Issues esta prescrição
            if (!biometricDataIssue.getPrescriptions().contains(prescription))
                biometricDataIssue.addPrescription(prescription);
        }
    }
}
