package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HEALTHCARE_PROFESSIONALS")
@NamedQueries({
        @NamedQuery(name = "getAllHealthcareProfessionals", query = "SELECT d FROM HealthcareProfessional d WHERE d.deleted_at IS NULL ORDER BY d.id"),
        @NamedQuery(name = "getAllHealthcareProfessionalsWithTrashed", query = "SELECT d FROM HealthcareProfessional d ORDER BY d.id")
})
public class HealthcareProfessional extends Employee implements Serializable {
    @NotNull
    private String specialty;

    @ManyToOne
    @JoinColumn(name = "created_by") // Código do Administrador
    @NotNull
    private Administrator created_by;

    @NotNull
    @OneToMany(mappedBy = "healthcareProfessional", cascade = CascadeType.PERSIST)
    private List<Prescription> prescriptions;

    @NotNull
    @OneToMany(mappedBy = "healthcareProfessional", cascade = CascadeType.PERSIST)
    private List<Observation> observations;

    @NotNull
    @ManyToMany
    @JoinTable(name = "HEALTHCARE_PROFESSIONALS_PATIENTS",
            joinColumns = @JoinColumn(name = "HEALTHCAREPROFESSIONAL_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PATIENT_ID", referencedColumnName = "ID"))
    private List<Patient> patients;

    public HealthcareProfessional(String email, String password, String name, String gender, String specialty,
                                  Administrator created_by) {
        super(email, password, name, gender);
        this.specialty = specialty;
        this.created_by = created_by;
        this.observations = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.patients = new ArrayList<>();
    }

    public HealthcareProfessional() {
        this.observations = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.patients = new ArrayList<>();
    }

    public Prescription addPrescription(Prescription prescription) {
        if (prescription != null && !this.prescriptions.contains(prescription)) {
            prescriptions.add(prescription);
            return prescription;
        }
        return null;
    }

    public Prescription removePrescription(Prescription prescription) {
        return prescription != null && prescriptions.remove(prescription) ? prescription : null;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public Administrator getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Administrator created_by) {
        this.created_by = created_by;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    public void addObservation(Observation observation) {
        if (observation == null || observations.contains(observation))
            return;

        observations.add(observation);
    }

    public void removeObservation(Observation observation) {
        if (observation == null)
            return;

        observations.remove(observation);
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void addPatient(Patient patient) {
        if (patient == null || patients.contains(patient))
            return;

        patients.add(patient);
    }

    public void removePacient(Patient patient) {
        if (patient == null)
            return;

        patients.remove(patient);
    }

}
