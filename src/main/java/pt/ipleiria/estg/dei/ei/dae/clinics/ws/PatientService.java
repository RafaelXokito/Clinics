package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.DoctorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.DoctorBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
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
        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<PatientDTO>(toDTOAllPatients(patientBean.getAllPatients()),
                        "username","healthNo","email","name","gender"))
                .build();
    }

    private List<PatientDTO> toDTOAllPatients(List<Object[]> allPatients) {
        List<PatientDTO> patientDTOList = new ArrayList<>();
        for (Object[] obj: allPatients) {
            patientDTOList.add(new PatientDTO(
                    obj[0].toString(),
                    (Integer) obj[1],
                    obj[2].toString(),
                    obj[3].toString(),
                    obj[4].toString()
            ));
        }
        return patientDTOList;
    }

    @GET
    @Path("{username}")
    public Response getPatientWS(@PathParam("username") String username) {
        Patient patient = patientBean.findPatient(username);

        if (patient == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(toDTO(patient))
                .build();
    }

    @POST
    @Path("/")
    public Response createPatientWS(PatientDTO patientDTO) {
        Patient createdPatient = patientBean.create(
                patientDTO.getUsername(),
                patientDTO.getEmail(),
                patientDTO.getPassword(),
                patientDTO.getName(),
                patientDTO.getGender(),
                patientDTO.getHealthNo(),
                patientDTO.getCreated_by());

        Patient patient = patientBean.findPatient(createdPatient.getUsername());

        if (patient == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(createdPatient)
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updatePatientWS(@PathParam("username") String username, PatientDTO patientDTO) {
        Patient updatedPatient = patientBean.update(
                patientDTO.getUsername(),
                patientDTO.getEmail(),
                patientDTO.getPassword(),
                patientDTO.getName(),
                patientDTO.getGender(),
                patientDTO.getHealthNo());

        Patient patient = patientBean.findPatient(updatedPatient.getUsername());

        if (!updatedPatient.equals(patient))
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(updatedPatient)
                .build();
    }

    @DELETE
    @Path("{username}")
    public Response deletePatientWS(@PathParam("username") String username) {
        Patient removedPatient = patientBean.delete(username);

        if (removedPatient != null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

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
