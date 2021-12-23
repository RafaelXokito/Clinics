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
    private String patientName;
    private String healthNo;
    private String BiometricDataTypeName;
    private String valueUnit;
    //Origem- exame, equipamento, consulta

    public BiometricDataDTO(long id, long biometricTypeId, double value, String notes, String patientUsername, Date created_at, String created_by) {
        this.id = id;
        this.biometricTypeId = biometricTypeId;
        this.value = value;
        this.notes = notes;
        this.patientUsername = patientUsername;
        this.created_at = created_at;
        this.created_by = created_by;
    }

    public BiometricDataDTO(long biometricTypeId, double value, String notes, String patientUsername, Date created_at, String created_by) {
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

    public BiometricDataDTO(long id,String patientUsername, String patientName, String healthNo,long biometricDataTypeId, String biometricDataTypeName,double value, String valueUnit, String notes) {
        this.id = id;
        this.patientUsername = patientUsername;
        this.patientName = patientName;
        this.healthNo = healthNo;
        this.biometricTypeId = biometricDataTypeId;
        this.BiometricDataTypeName = biometricDataTypeName;
        this.value = value;
        this.valueUnit = valueUnit;
        this.notes = notes;
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
