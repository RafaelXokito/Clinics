package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataIssueDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataTypeBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("biometricdatatypes") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class BiometricDataTypeService {
    @EJB
    private BiometricDataTypeBean biometricDataTypeBean;

    @GET
    @Path("/")
    public Response getAllBiometricDataTypesWS() {
        //List<BiometricDataType> biometricDataTypes = biometricDataTypeBean.getAllBiometricDataTypes();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<BiometricDataTypeDTO>(toDTOAllBiometricDataTypes(biometricDataTypeBean.getAllBiometricDataTypes()),
                        "name", "unit_name"))
                .build();
    }

    private List<BiometricDataTypeDTO> toDTOAllBiometricDataTypes(List<Object[]> allBiometricDataTypes) {
        List<BiometricDataTypeDTO> BiometricDataTypeDTOList = new ArrayList<>();
        for (Object[] obj: allBiometricDataTypes) {
            BiometricDataTypeDTOList.add(new BiometricDataTypeDTO(
                    obj[0].toString(),
                    obj[1].toString() + " " + obj[2].toString()
            ));
        }
        return BiometricDataTypeDTOList;
    }

    @GET
    @Path("{id}")
    public Response getBiometricDataTypeWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricDataType))
                .build();
    }

    @POST
    @Path("/")
    public Response createBiometricDataTypeWS(BiometricDataTypeDTO biometricDataTypeDTO) throws MyEntityNotFoundException {
        BiometricDataType createdBiometricDataType = biometricDataTypeBean.create(
                biometricDataTypeDTO.getName(),
                biometricDataTypeDTO.getMin(),
                biometricDataTypeDTO.getMax(),
                biometricDataTypeDTO.getUnit(),
                biometricDataTypeDTO.getUnit_name());

        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(createdBiometricDataType.getId());

        return Response.status(Response.Status.CREATED)
                .entity(createdBiometricDataType)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateBiometricDataTypeWS(@PathParam("id") long id, BiometricDataTypeDTO biometricDataTypeDTO) throws MyEntityNotFoundException {
        biometricDataTypeBean.update(
            biometricDataTypeDTO.getId(),
            biometricDataTypeDTO.getName(),
            biometricDataTypeDTO.getMin(),
            biometricDataTypeDTO.getMax(),
            biometricDataTypeDTO.getUnit(),
            biometricDataTypeDTO.getUnit_name());

        BiometricDataType biometricDataType = biometricDataTypeBean.findBiometricDataType(id);

        return Response.status(Response.Status.OK)
                .entity(biometricDataType)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBiometricDataTypeWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        biometricDataTypeBean.delete(id);

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
