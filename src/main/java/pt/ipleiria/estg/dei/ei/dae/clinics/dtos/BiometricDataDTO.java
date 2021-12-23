package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Date;

public class BiometricDataDTO {
    private long id;
    private long biometricTypeId;
    private double value;
    private String notes;
    private long patientId;
    private Date created_at;
    private long created_by; //Person that created this entity
    private String patientName;
    private String healthNo;
    private String BiometricDataTypeName;
    private String valueUnit;
    //Origem- exame, equipamento, consulta

    public BiometricDataDTO(long id, long biometricTypeId, double value, String notes, long patientId, Date created_at, long created_by) {
        this.id = id;
        this.biometricTypeId = biometricTypeId;
        this.value = value;
        this.notes = notes;
        this.patientId = patientId;
        this.created_at = created_at;
        this.created_by = created_by;
    }

    public BiometricDataDTO() {
        id = -1;
        biometricTypeId = -1;
        value = 0;
        notes = "";
        patientId = -1;
        created_at = null;
        created_by = -1;
    }

    public BiometricDataDTO(String patientName, String healthNo, String biometricDataTypeName, String valueUnit) {
        this.patientName = patientName;
        this.healthNo = healthNo;
        this.BiometricDataTypeName = biometricDataTypeName;
        this.valueUnit = valueUnit;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getHealthNo() {
        return healthNo;
    }

    public void setHealthNo(String healthNo) {
        this.healthNo = healthNo;
    }

    public String getBiometricDataTypeName() {
        return BiometricDataTypeName;
    }

    public void setBiometricDataTypeName(String biometricDataTypeName) {
        BiometricDataTypeName = biometricDataTypeName;
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
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

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(long created_by) {
        this.created_by = created_by;
    }
}
