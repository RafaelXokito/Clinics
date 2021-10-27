package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataTypeBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("biometricdatatype") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class BiometricDataTypeService {
    @EJB
    private BiometricDataTypeBean biometricDataTypeBean;

    @GET
    @Path("/")
    public Response getAllBiometricDataTypesWS() {
        return Response.status(Response.Status.FOUND)
                .entity(toDTOs(biometricDataTypeBean.getAllBiometricDataTypes()))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getBiometricDataTypeWS(@PathParam("id") long id) {
        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(id);

        if (biometricDataType == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .build();

        return Response.status(Response.Status.FOUND)
                .entity(toDTO(biometricDataType))
                .build();
    }

    @POST
    @Path("/")
    public Response createBiometricDataTypeWS(BiometricDataTypeDTO biometricDataTypeDTO) {
        BiometricDataType createdBiometricDataType = biometricDataTypeBean.create(
                biometricDataTypeDTO.getName(),
                biometricDataTypeDTO.getMin(),
                biometricDataTypeDTO.getMax(),
                biometricDataTypeDTO.getUnit(),
                biometricDataTypeDTO.getUnit_name());

        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(createdBiometricDataType.getId());

        if (biometricDataType == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(createdBiometricDataType)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateBiometricDataTypeWS(@PathParam("id") long id, BiometricDataTypeDTO biometricDataTypeDTO) {
        BiometricDataType updatedBiometricDataType = biometricDataTypeBean.update(
                biometricDataTypeDTO.getId(),
                biometricDataTypeDTO.getName(),
                biometricDataTypeDTO.getMin(),
                biometricDataTypeDTO.getMax(),
                biometricDataTypeDTO.getUnit(),
                biometricDataTypeDTO.getUnit_name());

        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(updatedBiometricDataType.getId());

        if (!updatedBiometricDataType.equals(biometricDataType))
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(updatedBiometricDataType)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBiometricDataTypeWS(@PathParam("id") long id) {
        BiometricDataType removedBiometricDataType = biometricDataTypeBean.delete(id);

        if (removedBiometricDataType != null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
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
                biometricDataType.getUnit_name());
    }
}
