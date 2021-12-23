package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="Biometric_Data")
@NamedQueries({
        @NamedQuery(
                name = "getAllBiometricData",
                query = "SELECT bioData FROM BiometricData bioData ORDER BY bioData.id"
        )
})
public class BiometricData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "BIOMETRIC_DATA_TYPE_ID")
    @NotNull
    private BiometricDataType biometric_data_type;

    @NotNull
    private double value;

    @NotNull
    private String notes;

    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    @NotNull
    private Patient patient;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @NotNull
    @ManyToOne
    private Person created_by;

    public BiometricData(BiometricDataType biometric_data_type, double value, String notes, Patient patient, Person person) {
        this.biometric_data_type = biometric_data_type;
        this.value = value;
        this.notes = notes;
        this.patient = patient;
        this.created_at = new Date();
        this.created_by = person;
    }

    public BiometricData() {
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Person getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Person created_by) {
        this.created_by = created_by;
    }

    public Long getId() {
        return id;
    }

    public BiometricDataType getBiometric_data_type() {
        return biometric_data_type;
    }

    public void setBiometric_data_type(BiometricDataType biometric_data_type) {
        this.biometric_data_type = biometric_data_type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
