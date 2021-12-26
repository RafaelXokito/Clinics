package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Observation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "HEALTHCAREPROFESSIONAL_ID")
    @NotNull
    private HealthcareProfessional healthcareProfessional;

    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    @NotNull
    private Patient patient;

    private String notes;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @NotNull
    @OneToOne
    private Prescription prescription;

    public Observation(HealthcareProfessional healthcareProfessional, Patient patient, String notes, Prescription prescription) {
        this.healthcareProfessional = healthcareProfessional;
        this.patient = patient;
        this.notes = notes;
        this.created_at = new Date();
        this.prescription = prescription;
    }

    public Observation() {
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
