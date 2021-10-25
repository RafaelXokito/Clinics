package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataIssueBean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "prescriptions")
@NamedQueries({
        @NamedQuery(
                name = "getAllPrescriptions",
                query = "SELECT p FROM Prescription p ORDER BY p.id"
        )
})
public class Prescription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToMany(mappedBy = "prescriptions") //Precisa ser alterado para ManyToMany uma prescrição pode ter vários tipos biométricos
    private List<Biometric_Data_Issue> biometric_data_issues;

    @ManyToOne
    @JoinColumn(name = "DOCTOR_USERNAME")
    @NotNull
    private Doctor doctor;

    @NotNull
    private Date start_date;

    @NotNull
    private Date end_date;

    @Nullable
    private String notes;

    public Prescription(Doctor doctor, Date start_date, Date end_date, String notes, List<Biometric_Data_Issue> biometric_data_issues) {
        this.doctor = doctor;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;

        this.biometric_data_issues = biometric_data_issues;
    }

    public Prescription() {
    }

    public Biometric_Data_Issue addBiometricDataIssue(Biometric_Data_Issue biometricDataIssue){
        if (biometricDataIssue != null && !this.biometric_data_issues.contains(biometricDataIssue)) {
            biometric_data_issues.add(biometricDataIssue);
            return biometricDataIssue;
        }
        return null;
    }

    public Biometric_Data_Issue removeBiometricDataIssue(Biometric_Data_Issue biometricDataIssue){
        return biometricDataIssue != null && biometric_data_issues.remove(biometricDataIssue) ? biometricDataIssue : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Biometric_Data_Issue> getBiometric_data_issue() {
        return biometric_data_issues;
    }

    public void setBiometric_data_issues(List<Biometric_Data_Issue> biometric_data_issue) {
        this.biometric_data_issues = biometric_data_issue;
    }
}
