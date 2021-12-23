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
    @ManyToMany(mappedBy = "patients")
    private List<HealthcareProfessional> healthcareProfessionals;

    @NotNull
    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private List<BiometricData> biometric_data;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "HEALTHCAREPROFESSIONAL_ID")
    private HealthcareProfessional created_by;

    public Patient(String email, String password, String name, String gender, int healthNo,
            HealthcareProfessional created_by) {
        super(email, password, name, gender);
        this.healthNo = healthNo;
        this.healthcareProfessionals = new ArrayList<>();
        this.created_by = created_by;
        this.biometric_data = new ArrayList<>();
        addHealthcareProfessional(createdBy);
    }

    public Patient(long id) {
        super(id);
    }

    public Patient() {
        this.biometric_data = new ArrayList<>();
    }

    public HealthcareProfessional addHealthcareProfessional(HealthcareProfessional healthcareProfessional) {
        if (healthcareProfessional != null && !this.healthcareProfessionals.contains(healthcareProfessional)) {
            healthcareProfessionals.add(healthcareProfessional);
            return healthcareProfessional;
        }
        return null;
    }

    public HealthcareProfessional removeHealthcareProfessional(HealthcareProfessional healthcareProfessional) {
        return healthcareProfessional != null && healthcareProfessionals.remove(healthcareProfessional)
                ? healthcareProfessional
                : null;
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

    public List<HealthcareProfessional> getHealthcareProfessionals() {
        return healthcareProfessionals;
    }

    public void setHealthcareProfessionals(List<HealthcareProfessional> healthcareProfessionals) {
        this.healthcareProfessionals = healthcareProfessionals;
    }

    public List<BiometricData> getBiometric_data() {
        return biometric_data;
    }

    public void setBiometric_data(List<BiometricData> biometricData) {
        this.biometric_data = biometricData;
    }

    public HealthcareProfessional getCreated_by() {
        return created_by;
    }

    public void setCreated_by(HealthcareProfessional createdBy) {
        this.created_by = createdBy;
    }
}
