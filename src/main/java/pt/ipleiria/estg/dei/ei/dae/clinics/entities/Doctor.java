package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "DOCTORS")
@NamedQueries({
        @NamedQuery(
                name = "getAllDoctors",
                query = "SELECT d FROM Doctor d ORDER BY d.username"
        )
})
public class Doctor extends Person implements Serializable {
    @NotNull
    private String specialty;

    @ManyToMany
    @JoinTable(name = "DOCTORS_PATIENTS",
            joinColumns = @JoinColumn(name = "DOCTOR_USERNAME", referencedColumnName = "USERNAME"),
            inverseJoinColumns = @JoinColumn(name = "PATIENT_USERNAME", referencedColumnName =
                    "USERNAME"))
    private List<Patient> patients;

    @ManyToOne
    @JoinColumn(name = "created_by") //Código do Administrador
    @NotNull
    private Administrator created_by;

    @NotNull
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.PERSIST)
    private List<Prescription> prescriptions;

    public Doctor(String username, String email, String password, String name, String gender, String specialty, Administrator created_by) {
        super(username, email, password, name, gender);
        this.specialty = specialty;
        this.patients = patients;
        this.created_by = created_by;
        this.prescriptions = prescriptions;
    }

    public Doctor() {
    }

    public Patient addPatient(Patient patient){
        if (patient != null && !this.patients.contains(patient)) {
            patients.add(patient);
            return patient;
        }
        return null;
    }

    public Patient removePatient(Patient patient){
        return patient != null && patients.remove(patient) ? patient : null;
    }

    public Prescription addPrescription(Prescription prescription){
        if (prescription != null && !this.prescriptions.contains(prescription)) {
            prescriptions.add(prescription);
            return prescription;
        }
        return null;
    }

    public Prescription removePrescription(Prescription prescription){
        return prescription != null && prescriptions.remove(prescription) ? prescription : null;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
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
}
