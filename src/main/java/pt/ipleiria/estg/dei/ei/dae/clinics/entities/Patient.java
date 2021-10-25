package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import javax.print.Doc;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PATIENTS")
@NamedQueries({
        @NamedQuery(
                name = "getAllPatients",
                query = "SELECT p FROM Patient p ORDER BY p.username"
        )
})
public class Patient extends Person implements Serializable {

    @NotNull
    @Digits(integer = 9, fraction = 0)
    private int healthNo;

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

    public Patient(String username, String email, String password, String name, String gender, int healthNo, Doctor created_by) {
        super(username, email, password, name, gender);
        this.healthNo = healthNo;
        this.doctors = new ArrayList<>();
        this.created_by = created_by;
        this.biometric_data = new ArrayList<>();
        addDoctor(created_by);
    }

    public Patient() {
        this.biometric_data = new ArrayList<>();
    }

    public Doctor addDoctor(Doctor doctor){
        if (doctor != null && !this.doctors.contains(doctor)) {
            doctors.add(doctor);
            return doctor;
        }
        return null;
    }

    public Doctor removeDoctor(Doctor doctor){
        return doctor != null && doctors.remove(doctor) ? doctor : null;
    }

    public Biometric_Data addBiometricData(Biometric_Data biometric_data){
        if (biometric_data != null && !this.biometric_data.contains(biometric_data)) {
            this.biometric_data.add(biometric_data);
            return biometric_data;
        }
        return null;
    }

    public Biometric_Data removeBiometricData(Biometric_Data biometric_data){
        return biometric_data != null && this.biometric_data.remove(biometric_data) ? biometric_data : null;
    }

    public int getHealthNo() {
        return healthNo;
    }

    public void setHealthNo(int healthNo) {
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
