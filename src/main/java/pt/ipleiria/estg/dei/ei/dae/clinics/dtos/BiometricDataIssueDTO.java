package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;


public class BiometricDataIssueDTO {
    private long id;
    private String name;
    private double min;
    private double max;
    private long biometricDataTypeId;
    private String biometricDataTypeName;

    public BiometricDataIssueDTO(long id, String name, double min, double max, long biometricDataTypeId, String biometricDataTypeName) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.biometricDataTypeId = biometricDataTypeId;
        this.biometricDataTypeName = biometricDataTypeName;
    }

    public BiometricDataIssueDTO(String name, String biometricDataTypeName) {
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
}
