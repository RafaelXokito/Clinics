package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Biometric_Data_Types")
@NamedQueries({
        @NamedQuery(
                name = "getAllBiometricDataTypes",
                query = "SELECT bioDataTypes FROM BiometricDataType bioDataTypes ORDER BY bioDataTypes.id"
        )
})
public class BiometricDataType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;
    @NotNull
    private int min;
    @NotNull
    private int max;
    @NotNull
    private String unit; //Measure Unit Short
    @NotNull
    private String unit_name; //Measure Unit Extended

    public BiometricDataType(String name, int min, int max, String unit, String unit_name) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.unit = unit;
        this.unit_name = unit_name;
    }

    public BiometricDataType() {
    }

    public long getId() {
        return id;
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
}
