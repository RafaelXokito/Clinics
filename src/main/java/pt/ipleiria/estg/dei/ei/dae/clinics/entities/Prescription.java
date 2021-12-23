package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private long id;

    @NotNull
    @ManyToMany(mappedBy = "prescriptions") //Precisa ser alterado para ManyToMany uma prescrição pode ter vários tipos biométricos
    private List<BiometricDataIssue> biometric_data_issues;

    @ManyToOne
    @JoinColumn(name = "HEALTHCAREPROFESSIONAL_ID")
    @NotNull
    private HealthcareProfessional healthcareProfessional;

    @NotNull
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start_date;

    @NotNull
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime end_date;

    @Nullable
    private String notes;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Prescription(HealthcareProfessional healthcareProfessional, String start_date, String end_date, String notes) throws ParseException {
        this.healthcareProfessional = healthcareProfessional;
        this.start_date = LocalDateTime.parse(start_date, formatter);
        this.end_date = LocalDateTime.parse(end_date, formatter);

        this.notes = notes;

        this.biometric_data_issues = new ArrayList<>();
    }

    public Prescription() {
        this.biometric_data_issues = new ArrayList<>();
    }

    public Prescription(HealthcareProfessional healthcareProfessional, String start_date, String end_date, String notes, List<BiometricDataIssue> biometricDataIssues) throws ParseException {
        this.healthcareProfessional = healthcareProfessional;
        this.start_date = LocalDateTime.parse(start_date, formatter);
        this.end_date = LocalDateTime.parse(end_date, formatter);
        this.notes = notes;

        this.biometric_data_issues = new ArrayList<>();

        for (BiometricDataIssue biometricDataIssue :
                biometricDataIssues) {
            addBiometricDataIssue(biometricDataIssue);
        }
    }

    public BiometricDataIssue addBiometricDataIssue(BiometricDataIssue biometricDataIssue){
        if (biometricDataIssue != null && !this.biometric_data_issues.contains(biometricDataIssue)) {
            biometric_data_issues.add(biometricDataIssue);
            return biometricDataIssue;
        }
        return null;
    }

    public BiometricDataIssue removeBiometricDataIssue(BiometricDataIssue biometricDataIssue){
        return biometricDataIssue != null && biometric_data_issues.remove(biometricDataIssue) ? biometricDataIssue : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HealthcareProfessional getHealthcareProfessional() {
        return healthcareProfessional;
    }

    public void setHealthcareProfessional(HealthcareProfessional healthcareProfessional) {
        this.healthcareProfessional = healthcareProfessional;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = LocalDateTime.parse(start_date, formatter);
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = LocalDateTime.parse(end_date, formatter);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<BiometricDataIssue> getBiometric_data_issue() {
        return biometric_data_issues;
    }

    public void setBiometric_data_issues(List<BiometricDataIssue> biometric_data_issue) {
        this.biometric_data_issues = biometric_data_issue;
    }
}
