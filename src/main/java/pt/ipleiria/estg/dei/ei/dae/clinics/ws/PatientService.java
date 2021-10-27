package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("patients") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class PatientService {
    @EJB
    private PatientBean patientBean;

    @GET
    @Path("/")
    public Response getAllPatientsWS() {
        List<Patient> patients = patientBean.getAllPatients();

        return Response.status(Response.Status.OK)
                .entity(toDTOs(patients))
                .build();
    }

    @GET
    @Path("{username}")
    public Response getPatientWS(@PathParam("username") String username) throws MyEntityNotFoundException {
        Patient patient = patientBean.findPatient(username);

        return Response.status(Response.Status.OK)
                .entity(toDTO(patient))
                .build();
    }

    @POST
    @Path("/")
    public Response createPatientWS(PatientDTO patientDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        patientBean.create(patientDTO.getUsername(),
            patientDTO.getEmail(),
            patientDTO.getPassword(),
            patientDTO.getName(),
            patientDTO.getGender(),
            patientDTO.getHealthNo(),
            patientDTO.getCreated_by());

        Patient patient = patientBean.findPatient(patientDTO.getUsername());

        return Response.status(Response.Status.CREATED)
                .entity(patient)
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updatePatientWS(@PathParam("username") String username, PatientDTO patientDTO) throws MyEntityNotFoundException {
        patientBean.update(username,
            patientDTO.getEmail(),
            patientDTO.getPassword(),
            patientDTO.getName(),
            patientDTO.getGender(),
            patientDTO.getHealthNo());

        Patient patient = patientBean.findPatient(username);

        return Response.status(Response.Status.OK)
                .entity(patient)
                .build();
    }

    @DELETE
    @Path("{username}")
    public Response deletePatientWS(@PathParam("username") String username) throws MyEntityNotFoundException {
        patientBean.delete(username);

        return Response.status(Response.Status.OK)
                .build();
    }


    private List<PatientDTO> toDTOs(List<Patient> patients) {
        return patients.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PatientDTO toDTO(Patient patient) {
        return new PatientDTO(patient.getUsername(),
                patient.getEmail(),
                patient.getName(),
                patient.getGender(),
                patient.getCreated_at(),
                patient.getUpdated_at(),
                patient.getDeleted_at(),
                patient.getHealthNo(),
                patient.getCreated_by().getUsername());
    }
}
