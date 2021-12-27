package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PersonBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricData;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("biometricdata") // relative url web path for this service
@Produces({ MediaType.APPLICATION_JSON }) // injects header “Content-Type: application/json”
@Consumes({ MediaType.APPLICATION_JSON }) // injects header “Accept: application/json”

public class BiometricDataService {
    private static final Logger log = Logger.getLogger(AuthService.class.getName());

    @EJB
    private BiometricDataBean biometricDataBean;

    @EJB
    private PersonBean personBean;

    @GET
    @Path("/")
    public Response getAllBiometricDataWS() {
        // List<BiometricData> biometricData = biometricDataBean.getAllBiometricData();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<BiometricDataDTO>(
                        toDTOAllBiometricDatas(biometricDataBean.getAllBiometricData()),
                        "patientName", "healthNo", "biometricDataTypeName", "value", "valueUnit"))
                .build();
    }

    private List<BiometricDataDTO> toDTOAllBiometricDatas(List<Object[]> allBiometricDatas) {
        List<BiometricDataDTO> BiometricDataDTOList = new ArrayList<>();
        for (Object[] obj : allBiometricDatas) {
            BiometricDataDTOList.add(new BiometricDataDTO(
                    obj[0].toString(),
                    obj[1].toString(),
                    obj[2].toString(),
                    Double.parseDouble(obj[3].toString()),
                    obj[4].toString()));
        }
        return BiometricDataDTOList;
    }

    @GET
    @Path("{id}")
    public Response getBiometricDataWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        BiometricData biometricData = biometricDataBean.findBiometricData(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricData))
                .build();
    }

    @POST
    @Path("/")
    public Response createBiometricDataWS(BiometricDataDTO biometricDataDTO, @HeaderParam("Authorization") String auth)
            throws MyEntityNotFoundException, MyIllegalArgumentException {
        try {
            BiometricData createdBiometricData = biometricDataBean.create(
                    biometricDataDTO.getBiometricTypeId(),
                    biometricDataDTO.getValue(),
                    biometricDataDTO.getNotes(),
                    biometricDataDTO.getPatientId(),
                    personBean.getPersonByAuthToken(auth).getId());

            BiometricData biometricData = biometricDataBean.findBiometricData(createdBiometricData.getId());

            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(biometricData))
                    .build();
        } catch (Exception e) {
            log.warning(e.toString());
            return Response.status(400).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response updateBiometricDataWS(@PathParam("id") long id, BiometricDataDTO biometricDataDTO,
            @HeaderParam("Authorization") String auth) throws MyEntityNotFoundException, MyIllegalArgumentException {
        BiometricData createdBiometricData = biometricDataBean.update(
                id,
                biometricDataDTO.getBiometricTypeId(),
                biometricDataDTO.getValue(),
                biometricDataDTO.getNotes(),
                biometricDataDTO.getPatientId(),
                personBean.getPersonByAuthToken(auth).getId(),
                biometricDataDTO.getSource());

        BiometricData biometricData = biometricDataBean.findBiometricData(createdBiometricData.getId());

        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricData))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBiometricDataWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        if (biometricDataBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    private List<BiometricDataDTO> toDTOs(List<BiometricData> biometricData) {
        return biometricData.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private BiometricDataDTO toDTO(BiometricData biometricData) {
        return new BiometricDataDTO(
                biometricData.getId(),
                biometricData.getBiometric_data_type().getId(),
                biometricData.getBiometric_data_type().getName(),
                biometricData.getValue(),
                biometricData.getNotes(),
                biometricData.getPatient().getId(),
                biometricData.getPatient().getName(),
                biometricData.getCreated_at(),
                biometricData.getCreated_by().getId(),
                String.valueOf(biometricData.getPatient().getHealthNo()),
                biometricData.getBiometric_data_type().getUnit_name(),
                biometricData.getSource(),
                biometricData.getBiometricDataIssue().getId(),
                biometricData.getBiometricDataIssue().getName());
    }
}
