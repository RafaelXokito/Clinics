package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataIssueDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataIssueBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("biometricdataissue") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class BiometricDataIssueService {
    @EJB
    private BiometricDataIssueBean biometricDataIssueBean;

    @GET
    @Path("/")
    public Response getAllBiometricDataIssuesWS() {
        return Response.status(Response.Status.FOUND)
                .entity(toDTOs(biometricDataIssueBean.getAllBiometricDataIssues()))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getBiometricDataIssueWS(@PathParam("id") long id) {
        BiometricDataIssue biometricDataIssue = biometricDataIssueBean.findBiometricDataIssue(id);

        if (biometricDataIssue == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .build();

        return Response.status(Response.Status.FOUND)
                .entity(toDTO(biometricDataIssue))
                .build();
    }

    @POST
    @Path("/")
    public Response createBiometricDataIssueWS(BiometricDataIssueDTO biometricDataIssueDTO) {
        BiometricDataIssue createdBiometricDataIssue = biometricDataIssueBean.create(
                biometricDataIssueDTO.getName(),
                biometricDataIssueDTO.getMin(),
                biometricDataIssueDTO.getMax(),
                biometricDataIssueDTO.getBiometricDataTypeId());

        BiometricDataIssue biometricDataIssue = biometricDataIssueBean.findBiometricDataIssue(createdBiometricDataIssue.getId());

        if (biometricDataIssue == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(createdBiometricDataIssue)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateBiometricDataIssueWS(@PathParam("id") long id, BiometricDataIssueDTO biometricDataIssueDTO) {
        BiometricDataIssue updatedBiometricDataIssue = biometricDataIssueBean.update(
                biometricDataIssueDTO.getId(),
                biometricDataIssueDTO.getName(),
                biometricDataIssueDTO.getMin(),
                biometricDataIssueDTO.getMax(),
                biometricDataIssueDTO.getBiometricDataTypeId());

        BiometricDataIssue biometricDataIssue = biometricDataIssueBean.findBiometricDataIssue(updatedBiometricDataIssue.getId());

        if (!updatedBiometricDataIssue.equals(biometricDataIssue))
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(updatedBiometricDataIssue)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBiometricDataIssueWS(@PathParam("id") long id) {
        BiometricDataIssue removedBiometricDataIssue = biometricDataIssueBean.delete(id);

        if (removedBiometricDataIssue != null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
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
                biometricDataIssue.getBiometric_data_type().getName());
    }
}
