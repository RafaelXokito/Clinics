package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("observations") // relative url web path for this service
@Produces({ MediaType.APPLICATION_JSON }) // injects header “Content-Type: application/json”
@Consumes({ MediaType.APPLICATION_JSON }) // injects header “Accept: application/json”
public class ObservationService {
    @EJB
    private ObservationBean observationBean;

    @GET
    @Path("/")
    public Response getAllObservationsWS() {
        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<>(toDTOAllObservations(observationBean.getAllObservations()),
                        "id", "healthcareProfessionalName", "patientName", "created_at"))
                .build();
    }

    private List<ObservationDTO> toDTOAllObservations(List<Object[]> observations) {
        List<ObservationDTO> observationDTOs = new ArrayList<>();
        for (Object[] obj : observations) {
            observationDTOs.add(new ObservationDTO(
                    Long.parseLong(obj[0].toString()),
                    Long.parseLong(obj[1].toString()),
                    obj[2].toString(),
                    Long.parseLong(obj[3].toString()),
                    obj[4].toString(),
                    (Date) obj[5]));
        }
        return observationDTOs;
    }

    @GET
    @Path("{id}")
    public Response getObservationWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        Observation observation = observationBean.findObservation(id);

        return Response.status(Response.Status.OK)
                .entity(fullToDTO(observation))
                .build();
    }

    @POST
    @Path("/")
    public Response createObservationWS(ObservationDTO observationDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        long id = observationBean.create(
                observationDTO.getHealthcareProfessionalId(),
                observationDTO.getPatientId(),
                observationDTO.getNotes(),
                observationDTO.getPrescription().getStart_date(),
                observationDTO.getPrescription().getEnd_date(),
                observationDTO.getPrescription().getNotes());

        Observation observation = observationBean.findObservation(id);

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(observation))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateObservationWS(@PathParam("id") long id , ObservationDTO observationDTO) throws MyEntityNotFoundException {
        observationBean.update(
                id,
                observationDTO.getNotes(),
                observationDTO.getPrescription().getStart_date(),
                observationDTO.getPrescription().getEnd_date(),
                observationDTO.getPrescription().getNotes());

        Observation observation = observationBean.findObservation(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(observation))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteObservationWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        if (observationBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    private List<ObservationDTO> toDTOs(List<Observation> observations) {
        return observations.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ObservationDTO toDTO(Observation observation) {
        return new ObservationDTO(observation.getId(),
                observation.getHealthcareProfessional().getId(),
                observation.getHealthcareProfessional().getName(),
                observation.getPatient().getId(),
                observation.getPatient().getName(),
                observation.getCreated_at());
    }

    private ObservationDTO fullToDTO(Observation observation) {
        return new ObservationDTO(observation.getId(),
                observation.getHealthcareProfessional().getId(),
                observation.getHealthcareProfessional().getName(),
                observation.getPatient().getId(),
                observation.getPatient().getName(),
                observation.getNotes(),
                observation.getCreated_at(),
                prescriptionToDTO(observation.getPrescription()));
    }

    private PrescriptionDTO prescriptionToDTO(Prescription prescription) {
        boolean hasPatient = prescription.getPatient() != null;

        if (hasPatient) {
            return new PrescriptionDTO(
                    prescription.getId(),
                    prescription.getHealthcareProfessional().getId(),
                    prescription.getHealthcareProfessional().getName(),
                    prescription.getPatient().getId(),
                    prescription.getPatient().getName(),
                    prescription.getStart_date().toString(),
                    prescription.getEnd_date().toString(),
                    prescription.getNotes());
        }

        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getHealthcareProfessional().getId(),
                prescription.getHealthcareProfessional().getName(),
                prescription.getStart_date().toString(),
                prescription.getEnd_date().toString(),
                prescription.getNotes(),
                issuesToDTOs(prescription.getBiometric_data_issue()));
    }

    private List<BiometricDataIssueDTO> issuesToDTOs(List<BiometricDataIssue> biometricDataIssues) {
        return biometricDataIssues.stream().map(this::issueToDTO).collect(Collectors.toList());
    }

    private BiometricDataIssueDTO issueToDTO(BiometricDataIssue biometricDataIssue) {
        return new BiometricDataIssueDTO(biometricDataIssue.getId(),
                biometricDataIssue.getName(),
                biometricDataIssue.getMin(),
                biometricDataIssue.getMax(),
                biometricDataIssue.getBiometric_data_type().getId(),
                biometricDataIssue.getBiometric_data_type().getName());
    }

}
