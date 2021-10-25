package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Biometric_Data_Types")
@NamedQueries({
        @NamedQuery(
                name = "getAllBiometricDataTypes",
                query = "SELECT bioDataTypes FROM Biometric_Data_Type bioDataTypes ORDER BY bioDataTypes.id"
        )
})
public class Biometric_Data_Type implements Serializable {
    @Id
    private int id;
    @NotNull
    private String name;
    @NotNull
    private int min;
    @NotNull
    private int max;
    @NotNull
    private String measurement_unit;

    public Biometric_Data_Type(int id, String name, int min, int max, String measurement_unit) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.measurement_unit = measurement_unit;
    }

    public Biometric_Data_Type() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getMeasurement_unit() {
        return measurement_unit;
    }

    public void setMeasurement_unit(String measurement_unit) {
        this.measurement_unit = measurement_unit;
    }
}
