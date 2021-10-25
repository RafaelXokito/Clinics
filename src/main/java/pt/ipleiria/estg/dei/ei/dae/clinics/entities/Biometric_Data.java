package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Biometric_Data implements Serializable {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BIOMETRIC_DATA_TYPE_ID")
    @NotNull
    private Biometric_Data_Type biometric_data_type;

    @NotNull
    private int value;

    @NotNull
    private String notes;

    @ManyToOne
    @JoinColumn(name = "PATIENT_USERNAME")
    @NotNull
    private Patient patient;

    public Biometric_Data(Long id, Biometric_Data_Type biometric_data_type, int value, String notes) {
        this.id = id;
        this.biometric_data_type = biometric_data_type;
        this.value = value;
        this.notes = notes;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
