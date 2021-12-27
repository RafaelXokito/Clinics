package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PATIENTS")
@NamedQueries({
        @NamedQuery(name = "getAllPatients", query = "SELECT p FROM Patient p ORDER BY p.id")
})
public class Patient extends Person implements Serializable {

    @NotNull
    @Digits(integer = 9, fraction = 0)
    private int healthNo;

    @NotNull
    @OneToMany(mappedBy = "patient", cascade = CascadeType.PERSIST)
    private List<Observation> observations;

    @NotNull
    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private List<BiometricData> biometric_data;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CREATED_BY")
    private Employee created_by;

    public Patient(String email, String password, String name, String gender, int healthNo,
            Employee created_by) {
        super(email, password, name, gender);
        this.healthNo = healthNo;
        this.observations = new ArrayList<>();
        this.created_by = created_by;
        this.biometric_data = new ArrayList<>();
    }

    public Patient() {
        this.observations = new ArrayList<>();
        this.biometric_data = new ArrayList<>();
    }

    public BiometricData addBiometricData(BiometricData biometricData) {
        if (biometricData != null && !this.biometric_data.contains(biometricData)) {
            this.biometric_data.add(biometricData);
            return biometricData;
        }
        return null;
    }

    public BiometricData removeBiometricData(BiometricData biometricData) {
        return biometricData != null && this.biometric_data.remove(biometricData) ? biometricData : null;
    }

    public int getHealthNo() {
        return healthNo;
    }

    public void setHealthNo(int healthNo) {
        this.healthNo = healthNo;
    }

    public List<BiometricData> getBiometric_data() {
        return biometric_data;
    }

    public void setBiometric_data(List<BiometricData> biometricData) {
        this.biometric_data = biometricData;
    }

    public Employee getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Employee createdBy) {
        this.created_by = createdBy;
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
