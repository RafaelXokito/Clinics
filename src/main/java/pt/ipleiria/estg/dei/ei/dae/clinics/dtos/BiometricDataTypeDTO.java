package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Objects;

public class BiometricDataTypeDTO {
    private long id;
    private String name;
    private int min;
    private int max;
    private String unit;
    private String unit_name;

    public BiometricDataTypeDTO(long id, String name, int min, int max, String unit, String unit_name) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.unit = unit;
        this.unit_name = unit_name;
    }

    public BiometricDataTypeDTO() {
        id = 0;
        name = "";
        min = 0;
        max = 0;
        unit = "";
        unit_name = "";
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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiometricDataTypeDTO that = (BiometricDataTypeDTO) o;
        return id == that.id && min == that.min && max == that.max && Objects.equals(name, that.name) && Objects.equals(unit, that.unit) && Objects.equals(unit_name, that.unit_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, min, max, unit, unit_name);
    }
}
