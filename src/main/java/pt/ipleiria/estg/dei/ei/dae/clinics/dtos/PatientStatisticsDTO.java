package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.ArrayList;
import java.util.List;

public class PatientStatisticsDTO {
    BiometricDataDTO biometricData;
    List<PrescriptionDTO> prescriptions;
    List<HealthcareProfessionalDTO> healthcareProfessionals;
    List<BiometricDataDTO> biometricDatas;

    public PatientStatisticsDTO(BiometricDataDTO biometricData, List<PrescriptionDTO> prescriptions, List<HealthcareProfessionalDTO> healthcareProfessional, List<BiometricDataDTO> biometricDatas) {
        this.biometricData = biometricData;
        this.prescriptions = prescriptions;
        this.healthcareProfessionals = healthcareProfessional;
        this.biometricDatas = biometricDatas;
    }

    public PatientStatisticsDTO() {
        this.biometricData = new BiometricDataDTO();
        this.prescriptions = new ArrayList<>();
        this.healthcareProfessionals = new ArrayList<>();
        this.biometricDatas = new ArrayList<>();
    }

    public List<HealthcareProfessionalDTO> getHealthcareProfessionals() {
        return healthcareProfessionals;
    }

    public void setHealthcareProfessionals(List<HealthcareProfessionalDTO> healthcareProfessionals) {
        this.healthcareProfessionals = healthcareProfessionals;
    }

    public BiometricDataDTO getBiometricData() {
        return biometricData;
    }

    public void setBiometricData(BiometricDataDTO biometricData) {
        this.biometricData = biometricData;
    }

    public List<PrescriptionDTO> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescriptionDTO> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<BiometricDataDTO> getBiometricDatas() {
        return biometricDatas;
    }

    public void setBiometricDatas(List<BiometricDataDTO> biometricDatas) {
        this.biometricDatas = biometricDatas;
    }
}
