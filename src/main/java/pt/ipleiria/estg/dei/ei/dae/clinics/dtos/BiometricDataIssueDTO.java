package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Date;

public class BiometricDataIssueDTO {
    private long id;
    private String name;
    private double min;
    private double max;
    private long biometricDataTypeId;
    private String biometricDataTypeName;
    private String biometricDataTypeUnitName;
    private Date created_at;
    private Date deleted_at;

    public BiometricDataIssueDTO(long id, String name, double min, double max, long biometricDataTypeId, String biometricDataTypeName, String biometricDataTypeUnitName, Date created_at, Date deleted_at) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.biometricDataTypeId = biometricDataTypeId;
        this.biometricDataTypeName = biometricDataTypeName;
        this.biometricDataTypeUnitName = biometricDataTypeUnitName;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
    }

    public BiometricDataIssueDTO(long id, String name, double min, double max, long biometricDataTypeId, String biometricDataTypeName, String biometricDataTypeUnitName) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.biometricDataTypeId = biometricDataTypeId;
        this.biometricDataTypeName = biometricDataTypeName;
        this.biometricDataTypeUnitName = biometricDataTypeUnitName;
    }

    public BiometricDataIssueDTO(long id, String name, double min, double max, long biometricDataTypeId, String biometricDataTypeName) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.biometricDataTypeId = biometricDataTypeId;
        this.biometricDataTypeName = biometricDataTypeName;
    }

    public BiometricDataIssueDTO(long id, String name, double min, double max) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public BiometricDataIssueDTO(long id, String name, String biometricDataTypeName) {
        this.id = id;
        this.name = name;
        this.biometricDataTypeName = biometricDataTypeName;
    }

    public BiometricDataIssueDTO() {
        id = 0;
        name = "";
        min = 0;
        max = 0;
        biometricDataTypeId = 0;
        biometricDataTypeName = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public long getBiometricDataTypeId() {
        return biometricDataTypeId;
    }

    public void setBiometricDataTypeId(long biometricDataTypeId) {
        this.biometricDataTypeId = biometricDataTypeId;
    }

    public String getBiometricDataTypeName() {
        return biometricDataTypeName;
    }

    public void setBiometricDataTypeName(String biometricDataTypeName) {
        this.biometricDataTypeName = biometricDataTypeName;
    }

    public String getBiometricDataTypeUnitName() {
        return biometricDataTypeUnitName;
    }

    public void setBiometricDataTypeUnitName(String biometricDataTypeUnitName) {
        this.biometricDataTypeUnitName = biometricDataTypeUnitName;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
}
