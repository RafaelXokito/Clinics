package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Date;

public class BiometricDataDTO {
    private long id;
    private long biometricTypeId;
    private double value;
    private String notes;
    private String patientUsername;
    private Date created_at;
    private String created_by;

    public BiometricDataDTO(long id, long biometricTypeId, double value, String notes, String patientUsername, Date created_at, String created_by) {
        this.id = id;
        this.biometricTypeId = biometricTypeId;
        this.value = value;
        this.notes = notes;
        this.patientUsername = patientUsername;
        this.created_at = created_at;
        this.created_by = created_by;
    }

    public BiometricDataDTO() {
        id = 0;
        biometricTypeId = 0;
        value = 0;
        notes = "";
        patientUsername = "";
        created_at = null;
        created_by = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBiometricTypeId() {
        return biometricTypeId;
    }

    public void setBiometricTypeId(long biometricTypeId) {
        this.biometricTypeId = biometricTypeId;
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

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}