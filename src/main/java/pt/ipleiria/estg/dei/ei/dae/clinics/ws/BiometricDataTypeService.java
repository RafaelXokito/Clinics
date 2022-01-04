package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataIssueDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataTypeBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("biometricdatatypes") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class BiometricDataTypeService {
    @EJB
    private BiometricDataTypeBean biometricDataTypeBean;

    @Context
    private SecurityContext securityContext;
    @GET
    @Path("/")
    public Response getAllBiometricDataTypesWS() {
        if (securityContext.isUserInRole("Administrator"))
            return Response.status(Response.Status.OK)
                    .entity(toDTOs(biometricDataTypeBean.getAllBiometricDataTypeClassWithTrashed()))
                    .build();

        return Response.status(Response.Status.OK)
                .entity(toDTOs(biometricDataTypeBean.getAllBiometricDataTypeClass()))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getBiometricDataTypeWS(@PathParam("id") long id) throws Exception {
        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricDataType))
                .build();
    }

    @POST
    @Path("/")
    public Response createBiometricDataTypeWS(BiometricDataTypeDTO biometricDataTypeDTO) throws Exception {
        BiometricDataType createdBiometricDataType = biometricDataTypeBean.create(
                biometricDataTypeDTO.getName(),
                biometricDataTypeDTO.getMin(),
                biometricDataTypeDTO.getMax(),
                biometricDataTypeDTO.getUnit(),
                biometricDataTypeDTO.getUnit_name());

        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(createdBiometricDataType.getId());

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(biometricDataType))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateBiometricDataTypeWS(@PathParam("id") long id, BiometricDataTypeDTO biometricDataTypeDTO) throws Exception {
        biometricDataTypeBean.update(
            biometricDataTypeDTO.getId(),
            biometricDataTypeDTO.getName(),
            biometricDataTypeDTO.getMin(),
            biometricDataTypeDTO.getMax(),
            biometricDataTypeDTO.getUnit(),
            biometricDataTypeDTO.getUnit_name());

        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricDataType))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBiometricDataTypeWS(@PathParam("id") long id) throws Exception {
        if (biometricDataTypeBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    @POST
    @Path("{id}/restore")
    public Response restoreBiometricDataTypeWS(@PathParam("id") long id) throws Exception {
        if (biometricDataTypeBean.restore(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    @GET
    @Path("{id}/biometricdataissues")
    public Response getBiometricDataTypeBiometricDataIssuesWS(@PathParam("id") long id) throws Exception {
        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(id);

        return Response.status(Response.Status.OK)
                .entity(biometricDataIssueToDTOs(biometricDataType.getIssues()))
                .build();
    }


    private List<BiometricDataTypeDTO> toDTOs(List<BiometricDataType> biometricDataTypes) {
        return biometricDataTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private BiometricDataTypeDTO toDTO(BiometricDataType biometricDataType) {
        return new BiometricDataTypeDTO(biometricDataType.getId(),
                biometricDataType.getName(),
                biometricDataType.getMin(),
                biometricDataType.getMax(),
                biometricDataType.getUnit(),
                biometricDataType.getUnit_name(),
                biometricDataType.getDeleted_at());
    }

    private List<BiometricDataIssueDTO> biometricDataIssueToDTOs(List<BiometricDataIssue> biometricDataIssues) {
        return biometricDataIssues.stream().map(this::biometricDataIssueToDTO).collect(Collectors.toList());
    }

    private BiometricDataIssueDTO biometricDataIssueToDTO(BiometricDataIssue biometricDataIssue) {
        return new BiometricDataIssueDTO(biometricDataIssue.getId(),
                biometricDataIssue.getName(),
                biometricDataIssue.getMin(),
                biometricDataIssue.getMax());
    }
}
