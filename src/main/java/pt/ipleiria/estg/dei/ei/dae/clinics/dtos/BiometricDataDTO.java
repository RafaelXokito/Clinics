package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Date;

public class BiometricDataDTO {
    private long id;
    private long biometricTypeId;
    private double value;
    private String notes;
    private String patientUsername;
    private Date createdAt;
    private String createdByUsername;
    private String createdByName;
    private String patientName;
    private String healthNo;
    private String biometricDataTypeName;
    private String valueUnit;
    //Origem- exame, equipamento, consulta

    public BiometricDataDTO(long id, long biometricTypeId, double value, String notes, String patientUsername, Date createdAt, String createdByUsername) {
        this.id = id;
        this.biometricTypeId = biometricTypeId;
        this.value = value;
        this.notes = notes;
        this.patientUsername = patientUsername;
        this.createdAt = createdAt;
        this.createdByUsername = createdByUsername;
    }

    public BiometricDataDTO(long biometricTypeId, double value, String notes, String patientUsername, Date createdAt, String createdByUsername) {
        this.biometricTypeId = biometricTypeId;
        this.value = value;
        this.notes = notes;
        this.patientUsername = patientUsername;
        this.createdAt = createdAt;
        this.createdByUsername = createdByUsername;
    }

    public BiometricDataDTO() {
        id = 0;
        biometricTypeId = 0;
        value = 0;
        notes = "";
        patientUsername = "";
        createdAt = null;
        createdByUsername = "";
    }

    public BiometricDataDTO(double value, String valueUnit, String biometricDataTypeName, String createdByUsername, String createdByName, Date createdAt) {
        this.value = value;
        this.valueUnit = valueUnit;
        this.biometricDataTypeName = biometricDataTypeName;
        this.createdByUsername = createdByUsername;
        this.createdByName = createdByName;
        this.createdAt = createdAt;
    }

    public BiometricDataDTO(long id,String patientUsername, String patientName, String healthNo,long biometricDataTypeId, String biometricDataTypeName,double value, String valueUnit) {
        this.id = id;
        this.patientUsername = patientUsername;
        this.patientName = patientName;
        this.healthNo = healthNo;
        this.biometricTypeId = biometricDataTypeId;
        this.biometricDataTypeName = biometricDataTypeName;
        this.value = value;
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
        return biometricDataTypeName;
    }

    public void setBiometricDataTypeName(String biometricDataTypeName) {
        this.biometricDataTypeName = biometricDataTypeName;
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

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }
}
