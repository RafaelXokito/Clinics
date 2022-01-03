package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataIssueDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PersonBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyUnauthorizedException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("prescriptions")
@Produces({ MediaType.APPLICATION_JSON }) // injects header “Content-Type: application/json”
@Consumes({ MediaType.APPLICATION_JSON }) // injects header “Accept: application/json”

public class PrescriptionService {
    @EJB
    private PrescriptionBean prescriptionBean;

    @EJB
    private PersonBean personBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public Response getAllPrescriptionsWS(@HeaderParam("Authorization") String auth) throws Exception {
        Person person = personBean.getPersonByAuthToken(auth);

        if (securityContext.isUserInRole("HealthcareProfessional")) {
            return Response.status(Response.Status.OK)
                    .entity(toDTOsSimple(prescriptionBean.getAllPrescriptionByHealthcareProfessional(person.getId())))
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(toDTOsSimple(prescriptionBean.getAllPrescriptionByPatient(person.getId())))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getPrescriptionWS(@PathParam("id") long id, @HeaderParam("Authorization") String auth) throws Exception {
        Prescription prescription = prescriptionBean.findPrescription(id);
        long authId = personBean.getPersonByAuthToken(auth).getId();
        boolean isPresPatient = false;
        for (Patient patient : prescription.getPatients()) {
            if (patient.getId() == authId) {
                isPresPatient = true;
                break;
            }
        }
        if (prescription.getHealthcareProfessional().getId() != authId && !isPresPatient)
            throw new MyUnauthorizedException("You are not allowed to view this prescription");

        return Response.status(Response.Status.OK)
                .entity(toDTO(prescription))
                .build();
    }

    @POST
    @Path("/")
    public Response createPrescriptionWS(PrescriptionDTO prescriptionDTO) throws Exception {
        List<BiometricDataIssue> issues = fromDTOs(prescriptionDTO.getIssues());
        System.out.println(issues);
        long id = prescriptionBean.create(
                prescriptionDTO.getHealthcareProfessionalId(),
                prescriptionDTO.getStart_date(),
                prescriptionDTO.getEnd_date(),
                prescriptionDTO.getNotes(),
                issues);

        Prescription prescription = prescriptionBean.findPrescription(id);

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(prescription))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updatePrescriptionWS(@PathParam("id") long id, PrescriptionDTO prescriptionDTO, @HeaderParam("Authorization") String auth) throws Exception {
        List<BiometricDataIssue> issues = fromDTOs(prescriptionDTO.getIssues());

        prescriptionBean.update(id,
                prescriptionDTO.getStart_date(),
                prescriptionDTO.getEnd_date(),
                prescriptionDTO.getNotes(),
                issues,
                personBean.getPersonByAuthToken(auth).getId());

        Prescription prescription = prescriptionBean.findPrescription(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(prescription))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deletePrescriptionWS(@PathParam("id") long id, @HeaderParam("Authorization") String auth) throws Exception {
        if (prescriptionBean.delete(id, personBean.getPersonByAuthToken(auth).getId()))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    @POST
    @Path("{id}/restore")
    public Response restoreBiometricDataTypeWS(@PathParam("id") long id) throws Exception {
        if (prescriptionBean.restore(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    private List<BiometricDataIssue> fromDTOs(List<BiometricDataIssueDTO> issueDTOS) {
        List<BiometricDataIssue> issues = new ArrayList<>();
        for (BiometricDataIssueDTO issueDTO : issueDTOS) {
            BiometricDataIssue issue = prescriptionBean.findBiometricDataIssue(issueDTO.getId());
            issues.add(issue);
        }
        return issues;
    }

    private List<PrescriptionDTO> toDTOsSimple(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTOSimple).collect(Collectors.toList());
    }

    private PrescriptionDTO toDTOSimple(Prescription prescription) {
        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getHealthcareProfessional().getId(),
                prescription.getHealthcareProfessional().getName(),
                prescription.getStart_date().toString(),
                prescription.getEnd_date().toString(),
                prescription.getNotes(),
                prescription.getCreated_at(),
                prescription.getDeleted_at(),
                prescription.getObservation() == null);
    }

    private List<PrescriptionDTO> toDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PrescriptionDTO toDTO(Prescription prescription) {
        boolean isGlobal = prescription.getBiometric_data_issue() != null && prescription.getBiometric_data_issue().size() > 0;

        if (isGlobal) {
            return new PrescriptionDTO(
                    prescription.getId(),
                    prescription.getHealthcareProfessional().getId(),
                    prescription.getHealthcareProfessional().getName(),
                    prescription.getStart_date().toString(),
                    prescription.getEnd_date().toString(),
                    prescription.getNotes(),
                    issuesToDTOs(prescription.getBiometric_data_issue()));
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

    private List<PatientDTO> patientToDTOs(List<Patient> patients) {
        return patients.stream().map(this::patientToDTO).collect(Collectors.toList());
    }

    private PatientDTO patientToDTO(Patient patient) {
        return new PatientDTO(patient.getId(),
                patient.getName());
    }
}
