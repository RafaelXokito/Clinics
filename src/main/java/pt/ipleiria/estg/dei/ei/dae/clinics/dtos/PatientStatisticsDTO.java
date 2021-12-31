package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.ArrayList;
import java.util.List;

public class PatientStatisticsDTO {
    BiometricDataDTO biometricData;
    List<PrescriptionDTO> prescriptions;

    public PatientStatisticsDTO(BiometricDataDTO biometricData, List<PrescriptionDTO> prescriptions) {
        this.biometricData = biometricData;
        this.prescriptions = prescriptions;
    }

    public PatientStatisticsDTO() {
        this.biometricData = new BiometricDataDTO();
        this.prescriptions = new ArrayList<>();
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
}
