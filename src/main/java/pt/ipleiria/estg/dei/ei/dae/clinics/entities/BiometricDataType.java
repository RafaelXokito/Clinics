package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import org.eclipse.persistence.annotations.AdditionalCriteria;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Biometric_Data_Types")
@NamedQueries({
        @NamedQuery(
                name = "getAllBiometricDataTypes",
                query = "SELECT b FROM BiometricDataType b WHERE b.deleted_at IS NULL ORDER BY b.id DESC"
        ),
        @NamedQuery(
                name = "getAllBiometricDataTypesWithTrashed",
                query = "SELECT b FROM BiometricDataType b ORDER BY b.id DESC"
        )
})
public class BiometricDataType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private double min;

    @NotNull
    private double max;

    @NotNull
    private String unit; //Measure Unit Short

    @NotNull
    private String unit_name; //Measure Unit Extended

    @NotNull
    @OneToMany(mappedBy = "biometric_data_type")
    private List<BiometricDataIssue> issues;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleted_at;

    @Version
    private int version;

    public BiometricDataType(String name, double min, double max, String unit, String unit_name) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.unit = unit;
        this.unit_name = unit_name;
        issues = new ArrayList<>();
    }

    public BiometricDataType() {
        issues = new ArrayList<>();
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

    public List<BiometricDataIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<BiometricDataIssue> issues) {
        this.issues = issues;
    }

    public void addIssue(BiometricDataIssue biometricDataIssue) {
        if (biometricDataIssue == null || issues.contains(biometricDataIssue))
            return;

        issues.add(biometricDataIssue);
    }

    public void removeIssue(BiometricDataIssue biometricDataIssue) {
        if (biometricDataIssue == null)
            return;

        issues.remove(biometricDataIssue);
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public void setDeleted_at() {
        this.deleted_at = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiometricDataType that = (BiometricDataType) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
