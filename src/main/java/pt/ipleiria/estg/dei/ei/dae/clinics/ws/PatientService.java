package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.HealthcareProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricData;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("patients") // relative url web path for this service
@Produces({ MediaType.APPLICATION_JSON }) // injects header “Content-Type: application/json”
@Consumes({ MediaType.APPLICATION_JSON }) // injects header “Accept: application/json”

public class PatientService {
    @EJB
    private PatientBean patientBean;

    @GET
    @Path("/")
    public Response getAllPatientsWS() {
        // List<Patient> patients = patientBean.getAllPatients();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<PatientDTO>(toDTOAllPatients(patientBean.getAllPatients()),
                        "id", "email", "name", "gender", "healthNo"))
                .build();
    }

    private List<PatientDTO> toDTOAllPatients(List<Object[]> allPatients) {
        List<PatientDTO> patientDTOList = new ArrayList<>();
        for (Object[] obj : allPatients) {
            patientDTOList.add(new PatientDTO(
                    Long.parseLong(obj[0].toString()),
                    obj[1].toString(),
                    obj[2].toString(),
                    obj[3].toString(),
                    Integer.parseInt(obj[4].toString())));
        }
        return patientDTOList;
    }

    @GET
    @Path("{id}")
    public Response getPatientWS(@PathParam("id") int id) throws MyEntityNotFoundException {
        Patient patient = patientBean.findPatient(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(patient))
                .build();
    }

    @POST
    @Path("/")
    public Response createPatientWS(PatientDTO patientDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        long id = patientBean.create(
                patientDTO.getEmail(),
                patientDTO.getPassword(),
                patientDTO.getName(),
                patientDTO.getGender(),
                patientDTO.getHealthNo(),
                patientDTO.getCreated_by());

        Patient patient = patientBean.findPatient(id);

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(patient))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updatePatientWS(@PathParam("id") long id, PatientDTO patientDTO) throws MyEntityNotFoundException {
        patientBean.update(
                id,
                patientDTO.getEmail(),
                patientDTO.getName(),
                patientDTO.getGender(),
                patientDTO.getHealthNo());

        Patient patient = patientBean.findPatient(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(patient))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deletePatientWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        if (patientBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    private List<PatientDTO> toDTOs(List<Patient> patients) {
        return patients.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PatientDTO toDTO(Patient patient) {
        return new PatientDTO(patient.getId(),
                patient.getEmail(),
                patient.getName(),
                patient.getGender(),
                patient.getHealthNo(),
                patient.getCreated_by().getId(),
                biometricDataToDTOs(patient.getBiometric_data()),
                healthcareProfessionalToDTOs(patient.getHealthcareProfessionals()));
    }

    private List<BiometricDataDTO> biometricDataToDTOs(List<BiometricData> biometricData) {
        return biometricData.stream().map(this::biometricDataToDTO).collect(Collectors.toList());
    }

    private BiometricDataDTO biometricDataToDTO(BiometricData biometricData) {
        return new BiometricDataDTO(biometricData.getValue(),
                biometricData.getBiometric_data_type().getUnit_name(),
                biometricData.getBiometric_data_type().getName(),
                biometricData.getCreated_by().getId(),
                biometricData.getCreated_at());
    }

    private HealthcareProfessionalDTO healthcareProfessionalToDTO(HealthcareProfessional healthcareProfessional) {
        return new HealthcareProfessionalDTO(
                healthcareProfessional.getId(),
                healthcareProfessional.getEmail(),
                healthcareProfessional.getName(),
                healthcareProfessional.getGender(),
                healthcareProfessional.getSpecialty());
    }

    private List<HealthcareProfessionalDTO> healthcareProfessionalToDTOs(
            List<HealthcareProfessional> healthcareProfessionals) {
        return healthcareProfessionals.stream().map(this::healthcareProfessionalToDTO).collect(Collectors.toList());
    }
}
