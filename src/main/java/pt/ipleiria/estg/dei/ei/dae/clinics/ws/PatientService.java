package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

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

    @PATCH
    @Path("{id}")
    public Response updatePatientPasswordWS(@PathParam("id") long id, NewPasswordDTO newPasswordDTO) throws MyEntityNotFoundException, MyIllegalArgumentException {
        patientBean.updatePassword(
                id,
                newPasswordDTO.getOldPassword(),
                newPasswordDTO.getNewPassword());

        return Response.status(Response.Status.OK)
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

    @GET
    @Path("{id}/observations")
    public Response getPatientObservationsWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        Patient patient = patientBean.findPatient(id);

        return Response.status(Response.Status.OK)
                .entity(observationToDTOs(patient.getObservations()))
                .build();
    }

    @GET
    @Path("{id}/biometricdata")
    public Response getPatientBiometricDatasWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        Patient patient = patientBean.findPatient(id);

        return Response.status(Response.Status.OK)
                .entity(biometricDataToDTOs(patient.getBiometric_data()))
                .build();
    }

    @GET
    @Path("{id}/healthcareprofessionals")
    public Response getPatientHealthcareProfessionalsWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        Patient patient = patientBean.findPatient(id);

        return Response.status(Response.Status.OK)
                .entity(healthcareProfessionalToDTOs(patient.getHealthcareProfessionals()))
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
                observationToDTOs(patient.getObservations()));
    }

    private List<HealthcareProfessionalDTO> healthcareProfessionalToDTOs(List<HealthcareProfessional> healthcareProfessionals) {
        return healthcareProfessionals.stream().map(this::healthcareProfessionalToDTO).collect(Collectors.toList());
    }

    private HealthcareProfessionalDTO healthcareProfessionalToDTO(HealthcareProfessional healthcareProfessional) {
        return new HealthcareProfessionalDTO(
                healthcareProfessional.getId(),
                healthcareProfessional.getEmail(),
                healthcareProfessional.getName(),
                healthcareProfessional.getGender(),
                healthcareProfessional.getSpecialty());
    }

    private List<ObservationDTO> observationToDTOs(List<Observation> observations) {
        return observations.stream().map(this::observationToDTO).collect(Collectors.toList());
    }

    private ObservationDTO observationToDTO(Observation observation) {
        return new ObservationDTO(observation.getId(),
                observation.getHealthcareProfessional().getId(),
                observation.getHealthcareProfessional().getName(),
                observation.getPatient().getId(),
                observation.getPatient().getName(),
                observation.getCreated_at());
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
