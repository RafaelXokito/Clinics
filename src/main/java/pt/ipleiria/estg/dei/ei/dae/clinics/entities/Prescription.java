package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescriptions")
@NamedQueries({
        @NamedQuery(name = "getAllPrescriptions", query = "SELECT p FROM Prescription p ORDER BY p.id"),
        @NamedQuery(name = "getActivePrescriptions", query = "SELECT p FROM Prescription p WHERE p.start_date < CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP < p.end_date ORDER BY p.id")
})
public class Prescription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToMany(mappedBy = "prescriptions")
    private List<BiometricDataIssue> biometricDataIssues;

    @ManyToOne
    @JoinColumn(name = "HEALTHCAREPROFESSIONAL_ID")
    @NotNull
    private HealthcareProfessional healthcareProfessional;

    @NotNull
    @ManyToMany
    @JoinTable(name = "PRESCRIPTIONS_PATIENTS",
            joinColumns = @JoinColumn(name = "PRESCRIPTION_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PATIENT_ID", referencedColumnName = "ID"))
    private List<Patient> patients;

    @NotNull
    // @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start_date;

    @NotNull
    // @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime end_date;

    private String notes;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Prescription(HealthcareProfessional healthcareProfessional, Patient patient, String start_date, String end_date, String notes) {
        this.healthcareProfessional = healthcareProfessional;
        this.start_date = LocalDateTime.parse(start_date, formatter);
        this.end_date = LocalDateTime.parse(end_date, formatter);

        this.notes = notes;

        this.biometricDataIssues = new ArrayList<>();
        this.patients = new ArrayList<>();
    }

    public Prescription() {
        this.biometricDataIssues = new ArrayList<>();
        this.patients = new ArrayList<>();
    }

    public Prescription(HealthcareProfessional healthcareProfessional, String start_date, String end_date, String notes,
            List<BiometricDataIssue> biometricDataIssues) {
        this.healthcareProfessional = healthcareProfessional;
        this.start_date = LocalDateTime.parse(start_date, formatter);
        this.end_date = LocalDateTime.parse(end_date, formatter);
        this.notes = notes;

        this.biometricDataIssues = new ArrayList<>();
        this.patients = new ArrayList<>();

        for (BiometricDataIssue biometricDataIssue : biometricDataIssues) {
            addBiometricDataIssue(biometricDataIssue);
        }
    }

    public BiometricDataIssue addBiometricDataIssue(BiometricDataIssue biometricDataIssue) {
        if (biometricDataIssue != null && !this.biometricDataIssues.contains(biometricDataIssue)) {
            biometricDataIssues.add(biometricDataIssue);
            return biometricDataIssue;
        }
        return null;
    }

    public BiometricDataIssue removeBiometricDataIssue(BiometricDataIssue biometricDataIssue) {
        return biometricDataIssue != null && biometricDataIssues.remove(biometricDataIssue) ? biometricDataIssue : null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return biometricDataIssues;
    }

    public void setBiometricDataIssues(List<BiometricDataIssue> biometric_data_issue) {
        this.biometricDataIssues = biometric_data_issue;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void addPatient(Patient patient) {
        if (patient == null || this.patients.contains(patient)) return;

        this.patients.add(patient);
    }

    public void removePatient(Patient patient) {
        if (patient == null) return;

        this.patients.remove(patient);
    }
}
