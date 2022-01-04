package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.HealthcareProfessionalBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PersonBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyUnauthorizedException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("healthcareprofessionals") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class HealthcareProfessionalService {
    @EJB
    private HealthcareProfessionalBean healthcareProfessionalBean;

    @EJB
    private PersonBean personBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public Response getAllHealthcareProfessionalsWS() {
        if (securityContext.isUserInRole("Administrator"))
            return Response.status(Response.Status.OK)
                    .entity(toDTOs(healthcareProfessionalBean.getAllHealthcareProfessionalsClassWithTrashed()))
                    .build();
        return Response.status(Response.Status.OK)
                .entity(toDTOs(healthcareProfessionalBean.getAllHealthcareProfessionalsClass()))
                .build();
    }


    private List<HealthcareProfessionalDTO> toDTOAllHealthcareProfessionals(List<Object[]> allHealthcareProfessionals) {
        List<HealthcareProfessionalDTO> healthcareProfessionalDTOList = new ArrayList<>();
        for (Object[] obj: allHealthcareProfessionals) {
            healthcareProfessionalDTOList.add(new HealthcareProfessionalDTO(
                Long.parseLong(obj[0].toString()),
                obj[1].toString(),
                obj[2].toString(),
                obj[3].toString(),
                obj[4].toString()
            ));
        }
        return healthcareProfessionalDTOList;
    }

    @GET
    @Path("{id}")
    public Response getHealthcareProfessionalWS(@PathParam("id") long id, @HeaderParam("Authorization") String auth) throws Exception {
        if (!securityContext.isUserInRole("Administrator") && id != personBean.getPersonByAuthToken(auth).getId())
            throw new MyUnauthorizedException("You are not allowed to view this healthcare professional");

        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(healthcareProfessional))
                .build();
    }

    @POST
    @Path("/")
    public Response createHealthcareProfessionalWS(HealthcareProfessionalDTO healthcareProfessionalDTO, @HeaderParam("Authorization") String auth) throws Exception {
        long id = healthcareProfessionalBean.create(
            healthcareProfessionalDTO.getEmail(),
            healthcareProfessionalDTO.getPassword(),
            healthcareProfessionalDTO.getName(),
            healthcareProfessionalDTO.getGender(),
            healthcareProfessionalDTO.getSpecialty(),
            personBean.getPersonByAuthToken(auth).getId(),
            healthcareProfessionalDTO.getBirthDate());

        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(healthcareProfessional))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateHealthcareProfessionalWS(@PathParam("id") long id , HealthcareProfessionalDTO healthcareProfessionalDTO, @HeaderParam("Authorization") String auth) throws Exception {
        if (!securityContext.isUserInRole("Administrator") && id != personBean.getPersonByAuthToken(auth).getId())
            throw new MyUnauthorizedException("You are not allowed to modify this healthcare professional");

        healthcareProfessionalBean.update(
            id,
            healthcareProfessionalDTO.getEmail(),
            healthcareProfessionalDTO.getName(),
            healthcareProfessionalDTO.getGender(),
            healthcareProfessionalDTO.getSpecialty(),
            healthcareProfessionalDTO.getBirthDate());

        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(healthcareProfessional))
                .build();
    }

    @PATCH
    @Path("{id}")
    public Response updateHealthcareProfessionalPasswordWS(@PathParam("id") long id, NewPasswordDTO newPasswordDTO, @HeaderParam("Authorization") String auth) throws Exception {
        if (personBean.getPersonByAuthToken(auth).getId() != id)
            throw new MyUnauthorizedException("You are not allowed to modify the password of this healthcare professional");

        healthcareProfessionalBean.updatePassword(
                id,
                newPasswordDTO.getOldPassword(),
                newPasswordDTO.getNewPassword());

        return Response.status(Response.Status.OK)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteHealthcareProfessionalWS(@PathParam("id") long id) throws Exception {
        if (healthcareProfessionalBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    @POST
    @Path("{id}/restore")
    public Response restoreHealthCareProfessionalWS(@PathParam("id") long id) throws Exception {
        if (healthcareProfessionalBean.restore(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    @GET
    @Path("{id}/prescriptions")
    public Response getHealthcareProfessionalPrescriptionsWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(prescriptionToDTOs(healthcareProfessional.getPrescriptions()))
                .build();
    }

    @GET
    @Path("{id}/observations")
    public Response getHealthcareProfessionalObservationsWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(observationToDTOs(healthcareProfessional.getObservations()))
                .build();
    }

    @GET
    @Path("{id}/pacients")
    public Response getHealthcareProfessionalPacientsWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(pacientToDTOs(healthcareProfessional.getPatients()))
                .build();
    }

    private List<HealthcareProfessionalDTO> toDTOs(List<HealthcareProfessional> healthcareProfessionals) {
        return healthcareProfessionals.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private HealthcareProfessionalDTO toDTO(HealthcareProfessional healthcareProfessional) {
        return new HealthcareProfessionalDTO(
                healthcareProfessional.getId(),
                healthcareProfessional.getEmail(),
                healthcareProfessional.getName(),
                healthcareProfessional.getGender(),
                healthcareProfessional.getCreated_at(),
                healthcareProfessional.getUpdated_at(),
                healthcareProfessional.getDeleted_at(),
                healthcareProfessional.getSpecialty(),
                healthcareProfessional.getCreated_by().getId(),
                prescriptionToDTOs(healthcareProfessional.getPrescriptions()),
                observationToDTOs(healthcareProfessional.getObservations()),
                pacientToDTOs(healthcareProfessional.getPatients()),
                healthcareProfessional.getBirthDate());
    }

    private List<PatientDTO> pacientToDTOs(List<Patient> patients) {
        return patients.stream().map(this::pacientToDTO).collect(Collectors.toList());
    }

    private PatientDTO pacientToDTO(Patient patient) {
        return new PatientDTO(patient.getId(),
                patient.getEmail(),
                patient.getName(),
                patient.getGender(),
                patient.getHealthNo());
    }

    private List<PrescriptionDTO> prescriptionToDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::prescriptionToDTO).collect(Collectors.toList());
    }

    private PrescriptionDTO prescriptionToDTO(Prescription prescription) {
        boolean isGlobal = prescription.getBiometric_data_issue() != null && prescription.getBiometric_data_issue().size() > 0;

        if (isGlobal) {
            return new PrescriptionDTO(
                    prescription.getId(),
                    prescription.getHealthcareProfessional().getName(),
                    prescription.getStart_date().toString(),
                    prescription.getEnd_date().toString(),
                    true);
        }

        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getHealthcareProfessional().getId(),
                prescription.getHealthcareProfessional().getName(),
                patientToDTOs(prescription.getPatients()),
                prescription.getStart_date().toString(),
                prescription.getEnd_date().toString(),
                prescription.getNotes());

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

    private List<PatientDTO> patientToDTOs(List<Patient> patients) {
        return patients.stream().map(this::patientToDTO).collect(Collectors.toList());
    }

    private PatientDTO patientToDTO(Patient patient) {
        return new PatientDTO(patient.getId(),
                patient.getName());
    }
}
