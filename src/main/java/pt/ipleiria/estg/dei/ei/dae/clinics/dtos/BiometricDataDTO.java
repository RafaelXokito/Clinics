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
    private String biometricDataTypeName;
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

    public BiometricDataDTO(long id, long biometricTypeId, double value, String notes, long patientId, Date created_at, long created_by, String patientName, String healthNo, String biometricDataTypeName, String valueUnit) {
        this.id = id;
        this.biometricTypeId = biometricTypeId;
        this.value = value;
        this.notes = notes;
        this.patientId = patientId;
        this.created_at = created_at;
        this.created_by = created_by;
        this.patientName = patientName;
        this.healthNo = healthNo;
        this.biometricDataTypeName = biometricDataTypeName;
        this.valueUnit = valueUnit;
    }

    public BiometricDataDTO(long biometricTypeId, double value, String notes, long patientId, Date created_at, long created_by) {
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

    public BiometricDataDTO(double value, String valueUnit, String biometricDataTypeName, long createdBy, Date createdAt) {
        this.value = value;
        this.valueUnit = valueUnit;
        this.biometricDataTypeName = biometricDataTypeName;
        this.created_by = createdBy;
        this.created_at = createdAt;
    }

    public BiometricDataDTO(long id, long patientId, String patientName, String healthNo, long biometricDataTypeId,
                            String biometricDataTypeName, double value, String valueUnit, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.healthNo = healthNo;
        this.biometricTypeId = biometricDataTypeId;
        this.biometricDataTypeName = biometricDataTypeName;
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
