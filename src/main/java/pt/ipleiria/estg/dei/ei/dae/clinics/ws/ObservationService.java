package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.HealthcareProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.NewPasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.ObservationDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
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
        List<Observation> observations = observationBean.getAllObservations();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<>(toDTOs(observations),
                        "healthcareProfessionalId", "healthcareProfessionalName", "patientId", "patientName", "created_at"))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getObservationWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        Observation observation = observationBean.findObservation(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(observation))
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

}
