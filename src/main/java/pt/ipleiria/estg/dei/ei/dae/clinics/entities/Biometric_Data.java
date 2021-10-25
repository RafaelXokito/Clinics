package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Biometric_Data")
@NamedQueries({
        @NamedQuery(
                name = "getAllBiometricData",
                query = "SELECT bioData FROM Biometric_Data bioData ORDER BY bioData.id"
        )
})
public class Biometric_Data implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BIOMETRIC_DATA_TYPE_ID")
    @NotNull
    private Biometric_Data_Type biometric_data_type;

    @NotNull
    private double value;

    @NotNull
    private String notes;

    @ManyToOne
    @JoinColumn(name = "PATIENT_USERNAME")
    @NotNull
    private Patient patient;

    public Biometric_Data(Biometric_Data_Type biometric_data_type, double value, String notes, Patient patient) {
        this.biometric_data_type = biometric_data_type;
        this.value = value;
        this.notes = notes;
        this.patient = patient;
    }

    public Biometric_Data() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Biometric_Data_Type getBiometric_data_type() {
        return biometric_data_type;
    }

    public void setBiometric_data_type(Biometric_Data_Type biometric_data_type) {
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
