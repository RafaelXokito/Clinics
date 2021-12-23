package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import javax.print.Doc;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HEALTHCAREPROFESSIONAL")
@NamedQueries({
        @NamedQuery(
                name = "getAllDoctors",
                query = "SELECT d FROM HealthcareProfessional d ORDER BY d.id"
        )
})
public class HealthcareProfessional extends Person implements Serializable {
    @NotNull
    private String specialty;

    @ManyToMany
    @JoinTable(name = "HEALTHCAREPROFESSIONAL_PATIENTS",
            joinColumns = @JoinColumn(name = "HEALTHCAREPROFESSIONAL_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PATIENT_ID", referencedColumnName =
                    "ID"))
    private List<Patient> patients;

    @ManyToOne
    @JoinColumn(name = "created_by") //Código do Administrador
    @NotNull
    private Administrator created_by;

    @NotNull
    @OneToMany(mappedBy = "healthcareProfessional", cascade = CascadeType.PERSIST)
    private List<Prescription> prescriptions;

    public HealthcareProfessional(String email, String password, String name, String gender, String specialty, Administrator created_by) {
        super(email, password, name, gender);
        this.specialty = specialty;
        this.created_by = created_by;
        this.patients = new ArrayList<Patient>();
        this.prescriptions = new ArrayList<Prescription>();
    }

    public HealthcareProfessional(long id) {
        super(id);
    }

    public HealthcareProfessional() {
        this.patients = new ArrayList<Patient>();
        this.prescriptions = new ArrayList<Prescription>();
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
