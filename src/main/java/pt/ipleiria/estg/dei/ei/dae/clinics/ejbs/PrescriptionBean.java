package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Biometric_Data_Issue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Prescription;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;

@Stateless
public class PrescriptionBean {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(String doctor_username, Date start_date, Date end_date, String notes, long ...bio_data_issues_ids){

        Doctor doctor = entityManager.find(Doctor.class, doctor_username);
        if (doctor != null) {
            ArrayList<Biometric_Data_Issue> biometricDataIssues = new ArrayList<>();
            for (long bio_data_issues_id : bio_data_issues_ids) {
                Biometric_Data_Issue biometricDataIssue = entityManager.find(Biometric_Data_Issue.class, bio_data_issues_id);
                if (biometricDataIssue != null) {
                    biometricDataIssues.add(biometricDataIssue);
                    continue;
                }
                return -2; //Not found Biometric Data Issue with this id (bio_data_issues_id)
            }
            Prescription prescription = new Prescription(doctor,start_date,end_date,notes,biometricDataIssues);
            entityManager.persist(prescription);

            for (Biometric_Data_Issue biometricDataIssue: biometricDataIssues) {
                biometricDataIssue.addPrescription(prescription);
            }
        }
        return -1; //Not found Doctor with this username

    }

}
