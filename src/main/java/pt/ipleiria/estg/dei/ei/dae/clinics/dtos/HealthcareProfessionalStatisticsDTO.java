package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

public class HealthcareProfessionalStatisticsDTO {
    ObservationDTO observation;

    public HealthcareProfessionalStatisticsDTO(ObservationDTO observation) {
        this.observation = observation;
    }

    public HealthcareProfessionalStatisticsDTO() {
        this.observation = new ObservationDTO();
    }

    public ObservationDTO getObservation() {
        return observation;
    }

    public void setObservation(ObservationDTO observation) {
        this.observation = observation;
    }
}
