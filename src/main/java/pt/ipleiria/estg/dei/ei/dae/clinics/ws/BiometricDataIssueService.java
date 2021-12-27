package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataIssueDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataIssueBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("biometricdataissues") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class BiometricDataIssueService {
    @EJB
    private BiometricDataIssueBean biometricDataIssueBean;

    @GET
    @Path("/")
    public Response getAllBiometricDataIssuesWS() {
        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<BiometricDataIssueDTO>(toDTOAllBiometricDataIssues(biometricDataIssueBean.getAllBiometricDataIssues()),
                        "id", "name", "biometricDataTypeName"))
                .build();
    }

    private List<BiometricDataIssueDTO> toDTOAllBiometricDataIssues(List<Object[]> allBiometricDataIssues) {
        List<BiometricDataIssueDTO> BiometricDataIssueDTOList = new ArrayList<>();
        for (Object[] obj: allBiometricDataIssues) {
            BiometricDataIssueDTOList.add(new BiometricDataIssueDTO(
                    Integer.parseInt(obj[0].toString()), //id
                    obj[1].toString(), //name
                    Double.parseDouble(obj[4].toString()), //min
                    Double.parseDouble(obj[5].toString()),  //max
                    Integer.parseInt(obj[3].toString()), //id
                    obj[2].toString() //biometric_data_type.name
            ));
        }
        return BiometricDataIssueDTOList;
    }

    @GET
    @Path("{id}")
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
    public Response deleteBiometricDataIssueWS(@PathParam("id") long id) throws Exception {
        if (biometricDataIssueBean.delete(id))
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
                biometricDataIssue.getBiometric_data_type().getName());
    }
}
