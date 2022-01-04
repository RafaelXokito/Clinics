package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataIssueDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataIssueBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("biometricdataissues") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class BiometricDataIssueService {
    @EJB
    private BiometricDataIssueBean biometricDataIssueBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public Response getAllBiometricDataIssuesWS(@DefaultValue("false") @QueryParam("trashed") Boolean trashed) {
        if (securityContext.isUserInRole("HealthcareProfessional") && trashed)
            return Response.status(Response.Status.OK)
                    .entity(toDTOs(biometricDataIssueBean.getAllBiometricDataIssuesClassWithTrashed()))
                    .build();

        return Response.status(Response.Status.OK)
                .entity(toDTOs(biometricDataIssueBean.getAllBiometricDataIssuesClass()))
                .build();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"HealthcareProfessional"})
    public Response getBiometricDataIssueWS(@PathParam("id") long id) throws Exception {
        BiometricDataIssue biometricDataIssue = biometricDataIssueBean.findBiometricDataIssue(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricDataIssue))
                .build();
    }

    @POST
    @Path("/")
    public Response createBiometricDataIssueWS(BiometricDataIssueDTO biometricDataIssueDTO) throws Exception {
        BiometricDataIssue createdBiometricDataIssue = biometricDataIssueBean.create(
                biometricDataIssueDTO.getName(),
                biometricDataIssueDTO.getMin(),
                biometricDataIssueDTO.getMax(),
                biometricDataIssueDTO.getBiometricDataTypeId());

        BiometricDataIssue biometricDataIssue = biometricDataIssueBean.findBiometricDataIssue(createdBiometricDataIssue.getId());

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(biometricDataIssue))
                .build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed({"HealthcareProfessional"})
    public Response updateBiometricDataIssueWS(@PathParam("id") long id, BiometricDataIssueDTO biometricDataIssueDTO) throws Exception {
        biometricDataIssueBean.update(id,
            biometricDataIssueDTO.getName(),
            biometricDataIssueDTO.getMin(),
            biometricDataIssueDTO.getMax(),
            biometricDataIssueDTO.getBiometricDataTypeId());

        BiometricDataIssue biometricDataIssue = biometricDataIssueBean.findBiometricDataIssue(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricDataIssue))
                .build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({"HealthcareProfessional"})
    public Response deleteBiometricDataIssueWS(@PathParam("id") long id) throws Exception {
        if (biometricDataIssueBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    @POST
    @Path("{id}/restore")
    @RolesAllowed({"HealthcareProfessional"})
    public Response restoreBiometricDataIssueWS(@PathParam("id") long id) throws Exception {
        if (biometricDataIssueBean.restore(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    private List<BiometricDataIssueDTO> toDTOs(List<BiometricDataIssue> biometricDataIssues) {
        return biometricDataIssues.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private BiometricDataIssueDTO toDTO(BiometricDataIssue biometricDataIssue) {
        return new BiometricDataIssueDTO(biometricDataIssue.getId(),
                biometricDataIssue.getName(),
                biometricDataIssue.getMin(),
                biometricDataIssue.getMax(),
                biometricDataIssue.getBiometric_data_type().getId(),
                biometricDataIssue.getBiometric_data_type().getName(),
                biometricDataIssue.getBiometric_data_type().getUnit_name(),
                biometricDataIssue.getCreated_at(),
                biometricDataIssue.getDeleted_at());
    }
}
