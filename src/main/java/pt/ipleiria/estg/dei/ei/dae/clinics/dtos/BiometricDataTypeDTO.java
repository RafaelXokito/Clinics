package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Date;
import java.util.Objects;

public class BiometricDataTypeDTO {
    private long id;
    private String name;
    private double min;
    private double max;
    private String unit;
    private String unit_name;
    private Date deleted_at;

    public BiometricDataTypeDTO(long id, String name, String unit, String unit_name, double min, double max) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.unit_name = unit_name;
        this.min = min;
        this.max = max;
    }

    public BiometricDataTypeDTO(String name, String unit, String unit_name, double min, double max) {
        this.name = name;
        this.unit = unit;
        this.unit_name = unit_name;
        this.min = min;
        this.max = max;
    }

    public BiometricDataTypeDTO(long id, String name, double min, double max, String unit, String unit_name, Date deleted_at) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.unit = unit;
        this.unit_name = unit_name;
        this.deleted_at = deleted_at;
    }

    public BiometricDataTypeDTO(long id, String name, String unit, String unit_name) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.unit_name = unit_name;
    }

    public BiometricDataTypeDTO() {
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }
}
