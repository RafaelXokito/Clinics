package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataIssueDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Prescription;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PrescriptionBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Prescription> getAllPrescriptions() {
        return (List<Prescription>) entityManager.createNamedQuery("getAllPrescriptions").getResultList();
    }

    public Prescription findPrescription(long id) {
        return entityManager.find(Prescription.class, id);
    }

    /***
     * Creating a Prescription by a Doctor given some Biometric Data Issues
     * @param doctor_username Doctor Username that is creating the current Prescription
     * @param start_date that Prescription is starting
     * @param end_date that Prescription is ending
     * @param notes given by Doctor, the content of Prescription
     * @param biometricDataIssuesDTO Biometric Data Issues that this Prescription will affect
     * @return @Id generated by autoincrement
     *         null if Not a found Doctor with this username
     *         null if Not found Biometric Data Issue with this id (bio_data_issues_id)
     */
    public Prescription create(String doctor_username, String start_date, String end_date, String notes, List<BiometricDataIssueDTO> biometricDataIssuesDTO) throws ParseException {

        Doctor doctor = entityManager.find(Doctor.class, doctor_username);
        if (doctor != null) {

            ArrayList<BiometricDataIssue> biometricDataIssues = new ArrayList<>();
            for (BiometricDataIssueDTO issue : biometricDataIssuesDTO) {
                BiometricDataIssue biometricDataIssue = entityManager.find(BiometricDataIssue.class, issue.getId());
                if (biometricDataIssue != null) {
                    biometricDataIssues.add(biometricDataIssue);
                    continue;
                }
                return null; //Not found Biometric Data Issue with this id (bio_data_issues_id)
            }
            Prescription prescription = new Prescription(doctor,start_date,end_date,notes,biometricDataIssues);
            entityManager.persist(prescription);

            for (BiometricDataIssue biometricDataIssue: biometricDataIssues) {
                biometricDataIssue.addPrescription(prescription);
            }

            entityManager.flush();
            return prescription;

        }
        return null; //Not found Doctor with this username
    }

    /***
     * Delete a Prescription by given @Id:id
     * @param id @Id to find the proposal delete Prescription
     * @return Prescription deleted or null if dont find the Prescription with @Id:id given
     */
    public Prescription delete(long id) {
        entityManager.remove(entityManager.find(Prescription.class,id));
        return entityManager.find(Prescription.class,id);
    }

    /***
     * Update a Biometric Data Issue by given @Id:id
     * @param id @Id to find the proposal update Biometric Data Issue
     * @param start_date to update Biometric Data Issue
     * @param end_date to update Biometric Data Issue
     * @param notes to update Biometric Data Issue
     * @param biometricDataIssuesDTO to update Biometric Data Issue
     * @return Prescription
     * @throw TODO - Acrescentar os throws e a descrição
     */
    public Prescription update(long id, String start_date, String end_date, String notes, List<BiometricDataIssueDTO> biometricDataIssuesDTO) throws ParseException {
        Prescription prescription = entityManager.find(Prescription.class, id);
        if (prescription != null) {

            ArrayList<BiometricDataIssue> biometricDataIssues = new ArrayList<>();
            for (BiometricDataIssueDTO issue : biometricDataIssuesDTO) {
                BiometricDataIssue biometricDataIssue = entityManager.find(BiometricDataIssue.class, issue.getId());
                if (biometricDataIssue != null) {
                    biometricDataIssues.add(biometricDataIssue);
                    continue;
                }
                return null; //Not found Biometric Data Issue with this id (bio_data_issues_id)
            }
            prescription.setStart_date(start_date);
            prescription.setEnd_date(end_date);
            prescription.setNotes(notes);

            for (BiometricDataIssue biometricDataIssue: prescription.getBiometric_data_issue()) {
                //Se as Issues antigas ainda estiverem presentes nesta, a prescrição não é removida das mesmas
                //Se já não estiverem presentes, são removidas
                if (!biometricDataIssues.contains(biometricDataIssue))
                    biometricDataIssue.removePrescription(prescription);
            }

            prescription.setBiometric_data_issues(biometricDataIssues);

            for (BiometricDataIssue biometricDataIssue: biometricDataIssues) {
                // São adicionadas às novas Issues esta prescrição
                if (!biometricDataIssue.getPrescriptions().contains(prescription))
                    biometricDataIssue.addPrescription(prescription);
            }

            return prescription;

        }
        return null; //Not found BiometricData with the given id
    }
}
