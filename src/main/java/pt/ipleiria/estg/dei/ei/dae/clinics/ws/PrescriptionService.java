package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataIssueDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @GET
    @Path("/")
    public Response getAllPrescriptionsWS() {
        // List<Prescription> prescriptions = prescriptionBean.getAllPrescriptions();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<PrescriptionDTO>(toDTOAllPrescriptions(prescriptionBean.getAllPrescriptions()),
                        "id", "healthcareProfessionalId", "healthcareProfessionalName", "start_date", "end_date"))
                .build();
    }

    private List<PrescriptionDTO> toDTOAllPrescriptions(List<Object[]> allPrescriptions) {
        List<PrescriptionDTO> prescriptionDTOList = new ArrayList<>();
        for (Object[] obj : allPrescriptions) {
            prescriptionDTOList.add(new PrescriptionDTO(
                    Long.parseLong(obj[0].toString()),
                    Long.parseLong(obj[1].toString()),
                    obj[2].toString(),
                    obj[3].toString(),
                    obj[4].toString()));
        }
        return prescriptionDTOList;
    }

    @GET
    @Path("{id}")
    public Response getPrescriptionWS(@PathParam("id") long id) throws Exception {
        Prescription prescription = prescriptionBean.findPrescription(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(prescription))
                .build();
    }

    @POST
    @Path("/")
    public Response createPrescriptionWS(PrescriptionDTO prescriptionDTO) throws Exception {
        List<BiometricDataIssue> issues = fromDTOs(prescriptionDTO.getIssues());
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
    public Response updatePrescriptionWS(@PathParam("id") long id, PrescriptionDTO prescriptionDTO)
            throws ParseException, MyEntityNotFoundException {
        List<BiometricDataIssue> issues = fromDTOs(prescriptionDTO.getIssues());
        prescriptionBean.update(id,
                prescriptionDTO.getStart_date(),
                prescriptionDTO.getEnd_date(),
                prescriptionDTO.getNotes(),
                issues);

        Prescription prescription = prescriptionBean.findPrescription(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(prescription))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deletePrescriptionWS(@PathParam("id") long id) {
        if (prescriptionBean.delete(id))
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

    private List<PrescriptionDTO> toDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PrescriptionDTO toDTO(Prescription prescription) {
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
