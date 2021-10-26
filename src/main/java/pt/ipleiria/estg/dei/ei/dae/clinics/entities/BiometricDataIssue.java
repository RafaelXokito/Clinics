package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BiometricDataIssue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int min;

    @NotNull
    private int max;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "biometric_data_type_id")
    private BiometricDataType biometric_data_type;

    @ManyToMany
    @JoinTable(name = "BIOMETRIC_DATA_ISSUES_PRESCRIPTIONS",
            joinColumns = @JoinColumn(name = "BIOMETRIC_DATA_ISSUE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRESCRIPTION_ID", referencedColumnName =
                    "ID"))
    private List<Prescription> prescriptions;

    public BiometricDataIssue(String name, int min, int max, BiometricDataType biometric_data_type) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.biometric_data_type = biometric_data_type;
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

    public BiometricDataIssue() {
        this.prescriptions = new ArrayList<>();
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

    public void setId(Long id) {
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

    public BiometricDataType getBiometric_data_type() {
        return biometric_data_type;
    }

    public void setBiometric_data_type(BiometricDataType biometric_data_type) {
        this.biometric_data_type = biometric_data_type;
    }
}
