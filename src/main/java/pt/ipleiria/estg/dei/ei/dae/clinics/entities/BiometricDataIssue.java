package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="Biometric_Data_Issues")
@NamedQueries({
        @NamedQuery(
                name = "getAllBiometricDataIssues",
                query = "SELECT b FROM BiometricDataIssue b WHERE b.deleted_at IS NULL AND b.biometric_data_type.deleted_at IS NULL ORDER BY b.id"
        ),
        @NamedQuery(
                name = "getAllBiometricDataIssuesWithTrashed",
                query = "SELECT b FROM BiometricDataIssue b WHERE b.biometric_data_type.deleted_at IS NULL ORDER BY b.id"
        )
})
public class BiometricDataIssue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private double min;

    @NotNull
    private double max;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleted_at;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "biometric_data_type_id")
    private BiometricDataType biometric_data_type;

    @ManyToMany
    @JoinTable(name = "BIOMETRIC_DATA_ISSUES_PRESCRIPTIONS",
            joinColumns = @JoinColumn(name = "BIOMETRIC_DATA_ISSUE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRESCRIPTION_ID", referencedColumnName = "ID"))
    private List<Prescription> prescriptions;

    @Version
    private int version;

    public BiometricDataIssue(String name, double min, double max, BiometricDataType biometric_data_type) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.created_at = new Date();
        this.biometric_data_type = biometric_data_type;
        this.prescriptions = new ArrayList<>();
    }

    public BiometricDataIssue() {
        this.prescriptions = new ArrayList<>();
    }

    public Prescription addPrescription(Prescription prescription){
        if (prescription != null && !this.prescriptions.contains(prescription)) {
            prescriptions.add(prescription);
            return prescription;
        }
        return null;
    }

    public Prescription removePrescription(Prescription prescription){
        return prescription != null && prescriptions.remove(prescription) ? prescription : null;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public Long getId() {
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

    public BiometricDataType getBiometric_data_type() {
        return biometric_data_type;
    }

    public void setBiometric_data_type(BiometricDataType biometric_data_type) {
        this.biometric_data_type = biometric_data_type;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at() {
        this.deleted_at = new Date();
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiometricDataIssue issue = (BiometricDataIssue) o;
        return id == issue.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
