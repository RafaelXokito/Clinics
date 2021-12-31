package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

public class AdministratorStatisticsDTO {
    long totalAdmin;
    long totalHealthcareProfessionals;
    long totalPatients;
    long totalBiometricDataTypes;

    public AdministratorStatisticsDTO(long totalAdmin, long totalHealthcareProfessionals, long totalPatients, long totalBiometricDataTypes) {
        this.totalAdmin = totalAdmin;
        this.totalHealthcareProfessionals = totalHealthcareProfessionals;
        this.totalPatients = totalPatients;
        this.totalBiometricDataTypes = totalBiometricDataTypes;
    }

    public AdministratorStatisticsDTO() {
        this.totalAdmin = 0;
        this.totalHealthcareProfessionals = 0;
        this.totalPatients = 0;
        this.totalBiometricDataTypes = 0;
    }

    public long getTotalAdmin() {
        return totalAdmin;
    }

    public void setTotalAdmin(long totalAdmin) {
        this.totalAdmin = totalAdmin;
    }

    public long getTotalHealthcareProfessionals() {
        return totalHealthcareProfessionals;
    }

    public void setTotalHealthcareProfessionals(long totalHealthcareProfessionals) {
        this.totalHealthcareProfessionals = totalHealthcareProfessionals;
    }

    public long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(long totalPatients) {
        this.totalPatients = totalPatients;
    }

    public long getTotalBiometricDataTypes() {
        return totalBiometricDataTypes;
    }

    public void setTotalBiometricDataTypes(long totalBiometricDataTypes) {
        this.totalBiometricDataTypes = totalBiometricDataTypes;
    }
}
