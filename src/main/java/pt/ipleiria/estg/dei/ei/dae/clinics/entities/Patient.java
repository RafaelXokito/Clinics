package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient extends Person implements Serializable {

    @NotNull
    @Digits(integer = 9, fraction = 0)
    private Long healthNo;

    @NotNull
    @ManyToMany(mappedBy = "patients")
    private List<Doctor> doctors;

    @NotNull
    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private List<Biometric_Data> biometric_data;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "DOCTOR_USERNAME")
    private Doctor created_by;

    public Patient(String username, String email, String password, String name, String gender, Long healthNo, List<Doctor> doctors, List<Biometric_Data> biometric_data, Doctor created_by) {
        super(username, email, password, name, gender);
        this.healthNo = healthNo;
        this.doctors = doctors;
        this.biometric_data = biometric_data;
        this.created_by = created_by;
    }

    public Patient() {
    }

    public Long getHealthNo() {
        return healthNo;
    }

    public void setHealthNo(Long healthNo) {
        this.healthNo = healthNo;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Biometric_Data> getBiometric_data() {
        return biometric_data;
    }

    public void setBiometric_data(List<Biometric_Data> biometric_data) {
        this.biometric_data = biometric_data;
    }

    public Doctor getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Doctor created_by) {
        this.created_by = created_by;
    }
}
