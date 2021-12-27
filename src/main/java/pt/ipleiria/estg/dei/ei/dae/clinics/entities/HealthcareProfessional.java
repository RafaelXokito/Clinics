package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HEALTHCAREPROFESSIONAL")
@NamedQueries({
        @NamedQuery(name = "getAllDoctors", query = "SELECT d FROM HealthcareProfessional d ORDER BY d.id")
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

    public HealthcareProfessional(String email, String password, String name, String gender, String specialty,
            Administrator created_by) {
        super(email, password, name, gender);
        this.specialty = specialty;
        this.created_by = created_by;
        this.observations = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    public HealthcareProfessional() {
        this.observations = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
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
}
