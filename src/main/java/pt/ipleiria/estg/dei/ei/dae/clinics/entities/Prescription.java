package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "prescriptions")
@NamedQueries({
        @NamedQuery(
                name = "getAllPrescriptions",
                query = "SELECT p FROM Prescription p ORDER BY p.id"
        )
})
public class Prescription implements Serializable {
    @Id
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "biometric_data_type_id")
    private Biometric_Data_Type biometric_data_type;

    @ManyToOne
    @JoinColumn(name = "DOCTOR_USERNAME")
    @NotNull
    private Doctor doctor;

    @NotNull
    private int value;

    @NotNull
    private Date start_date;

    @NotNull
    private Date end_date;

    @Nullable
    private String notes;

    public Prescription(Long id, Biometric_Data_Type biometric_data_type, Doctor doctor, int value, Date start_date, Date end_date, String notes) {
        this.id = id;
        this.biometric_data_type = biometric_data_type;
        this.doctor = doctor;
        this.value = value;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;
    }

    public Prescription() {
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
