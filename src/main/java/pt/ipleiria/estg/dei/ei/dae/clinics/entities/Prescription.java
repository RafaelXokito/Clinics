package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private List<BiometricDataIssue> biometricDataIssues;

    @ManyToOne
    @JoinColumn(name = "HEALTHCARE_PROFESSIONAL_USERNAME")
    @NotNull
    private HealthcareProfessional healthcareProfessional;

    @NotNull
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startDate;

    @NotNull
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endDate;

    @Nullable
    private String notes;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Prescription(HealthcareProfessional healthcareProfessional, String startDate, String endDate, String notes) throws ParseException {
        this.healthcareProfessional = healthcareProfessional;
        this.startDate = LocalDateTime.parse(startDate, formatter);
        this.endDate = LocalDateTime.parse(endDate, formatter);

        this.notes = notes;

        this.biometricDataIssues = new ArrayList<>();
    }

    public Prescription() {
        this.biometricDataIssues = new ArrayList<>();
    }

    public Prescription(HealthcareProfessional healthcareProfessional, String startDate, String endDate, String notes, List<BiometricDataIssue> biometricDataIssues) throws ParseException {
        this.healthcareProfessional = healthcareProfessional;
        this.startDate = LocalDateTime.parse(startDate, formatter);
        this.endDate = LocalDateTime.parse(endDate, formatter);
        this.notes = notes;

        this.biometricDataIssues = new ArrayList<>();

        for (BiometricDataIssue biometricDataIssue :
                biometricDataIssues) {
            addBiometricDataIssue(biometricDataIssue);
        }
    }

    public BiometricDataIssue addBiometricDataIssue(BiometricDataIssue biometricDataIssue){
        if (biometricDataIssue != null && !this.biometricDataIssues.contains(biometricDataIssue)) {
            biometricDataIssues.add(biometricDataIssue);
            return biometricDataIssue;
        }
        return null;
    }

    public BiometricDataIssue removeBiometricDataIssue(BiometricDataIssue biometricDataIssue){
        return biometricDataIssue != null && biometricDataIssues.remove(biometricDataIssue) ? biometricDataIssue : null;
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

    public void setHealthcareProfessional(HealthcareProfessional doctor) {
        this.healthcareProfessional = doctor;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(String start_date) {
        this.startDate = LocalDateTime.parse(start_date, formatter);
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(String end_date) {
        this.endDate = LocalDateTime.parse(end_date, formatter);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<BiometricDataIssue> getBiometric_data_issue() {
        return biometricDataIssues;
    }

    public void setBiometricDataIssues(List<BiometricDataIssue> biometric_data_issue) {
        this.biometricDataIssues = biometric_data_issue;
    }
}
