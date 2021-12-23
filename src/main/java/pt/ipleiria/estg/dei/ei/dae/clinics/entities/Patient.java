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
        @NamedQuery(
                name = "getAllPatients",
                query = "SELECT p FROM Patient p ORDER BY p.id"
        )
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

    public Patient(String email, String password, String name, String gender, int healthNo, HealthcareProfessional created_by) {
        super(email, password, name, gender);
        this.healthNo = healthNo;
        this.healthcareProfessionals = new ArrayList<>();
        this.created_by = created_by;
        this.biometric_data = new ArrayList<>();
        addDoctor(created_by);
    }

    public Patient(long id) {
        super(id);
    }

    public Patient() {
        this.biometric_data = new ArrayList<>();
    }

    public HealthcareProfessional addDoctor(HealthcareProfessional doctor){
        if (doctor != null && !this.healthcareProfessionals.contains(doctor)) {
            healthcareProfessionals.add(doctor);
            return doctor;
        }
        return null;
    }

    public HealthcareProfessional removeDoctor(HealthcareProfessional doctor){
        return doctor != null && healthcareProfessionals.remove(doctor) ? doctor : null;
    }

    public BiometricData addBiometricData(BiometricData biometric_data){
        if (biometric_data != null && !this.biometric_data.contains(biometric_data)) {
            this.biometric_data.add(biometric_data);
            return biometric_data;
        }
        return null;
    }

    public BiometricData removeBiometricData(BiometricData biometric_data){
        return biometric_data != null && this.biometric_data.remove(biometric_data) ? biometric_data : null;
    }

    public int getHealthNo() {
        return healthNo;
    }

    public void setHealthNo(int healthNo) {
        this.healthNo = healthNo;
    }

    public List<HealthcareProfessional> getDoctors() {
        return healthcareProfessionals;
    }

    public void setDoctors(List<HealthcareProfessional> doctors) {
        this.healthcareProfessionals = doctors;
    }

    public List<BiometricData> getBiometric_data() {
        return biometric_data;
    }

    public void setBiometric_data(List<BiometricData> biometric_data) {
        this.biometric_data = biometric_data;
    }

    public HealthcareProfessional getCreated_by() {
        return created_by;
    }

    public void setCreated_by(HealthcareProfessional created_by) {
        this.created_by = created_by;
    }
}
