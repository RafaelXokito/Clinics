package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataIssueDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Prescription;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionService {
    @EJB
    private PrescriptionBean prescriptionBean;

    @GET
    @Path("/")
    public Response getAllPrescriptionsWS() {
        return Response.status(Response.Status.OK)
                .entity(toDTOs(prescriptionBean.getAllPrescriptions()))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getPrescriptionWS(@PathParam("id") long id) {
        Prescription prescription = prescriptionBean.findPrescription(id);

        if (prescription == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(toDTO(prescription))
                .build();
    }

    @POST
    @Path("/")
    public Response createPrescriptionWS(PrescriptionDTO prescriptionDTO) throws ParseException {
        List<BiometricDataIssue> issues = fromDTOs(prescriptionDTO.getIssues());
        Prescription createdPrescription = prescriptionBean.create(
                prescriptionDTO.getDoctorName(),
                prescriptionDTO.getStart_date(),
                prescriptionDTO.getEnd_date(),
                prescriptionDTO.getNotes(),
                issues);

        Prescription prescription = prescriptionBean.findPrescription(createdPrescription.getId());

        if (prescription == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(createdPrescription)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updatePrescriptionWS(@PathParam("id") long id, PrescriptionDTO prescriptionDTO) throws ParseException {
        Prescription updatedPrescription = prescriptionBean.update(
                prescriptionDTO.getId(),
                prescriptionDTO.getStart_date(),
                prescriptionDTO.getEnd_date(),
                prescriptionDTO.getNotes(),
                prescriptionDTO.getIssues());

        Prescription prescription = prescriptionBean.findPrescription(updatedPrescription.getId());

        if (!updatedPrescription.equals(prescription))
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(updatedPrescription)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deletePrescriptionWS(@PathParam("id") long id) {
        Prescription removedPrescription = prescriptionBean.delete(id);

        if (removedPrescription != null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
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
        return new PrescriptionDTO(prescription.getId(),
                prescription.getDoctor().getUsername(),
                prescription.getDoctor().getName(),
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
