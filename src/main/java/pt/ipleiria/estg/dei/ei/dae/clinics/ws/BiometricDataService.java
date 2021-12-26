package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricData;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("biometricdata") // relative url web path for this service
@Produces({ MediaType.APPLICATION_JSON }) // injects header “Content-Type: application/json”
@Consumes({ MediaType.APPLICATION_JSON }) // injects header “Accept: application/json”

public class BiometricDataService {
    @EJB
    private BiometricDataBean biometricDataBean;

    @GET
    @Path("/")
    public Response getAllBiometricDataWS() {
        // List<BiometricData> biometricData = biometricDataBean.getAllBiometricData();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<BiometricDataDTO>(
                        toDTOAllBiometricDatas(biometricDataBean.getAllBiometricData()),
                        "patientName", "healthNo", "biometricDataTypeName", "valueUnit"))
                .build();
    }

    private List<BiometricDataDTO> toDTOAllBiometricDatas(List<Object[]> allBiometricDatas) {
        List<BiometricDataDTO> BiometricDataDTOList = new ArrayList<>();
        for (Object[] obj : allBiometricDatas) {
            BiometricDataDTOList.add(new BiometricDataDTO(
                    Long.parseLong(obj[0].toString()), // BioData.id
                    Long.parseLong(obj[1].toString()), // bioData.patient.id
                    obj[2].toString(), // bioData.patient.name
                    obj[3].toString(), // bioData.patient.healthNo
                    Long.parseLong(obj[4].toString()), // bioData.biometric_data_type.id
                    obj[5].toString(), // bioData.biometric_data_type.name
                    Double.parseDouble(obj[6].toString()), // bioData.value
                    obj[6].toString() + " " + obj[7].toString(), // bioData.value + bioData.biometric_data_type.unit
                    obj[8].toString() // bioData.biometric_data_type.name
            ));
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
    public Response createBiometricDataWS(BiometricDataDTO biometricDataDTO) throws MyEntityNotFoundException, MyIllegalArgumentException {
        BiometricData createdBiometricData = biometricDataBean.create(
                biometricDataDTO.getBiometricTypeId(),
                biometricDataDTO.getValue(),
                biometricDataDTO.getNotes(),
                biometricDataDTO.getPatientId(),
                biometricDataDTO.getCreated_by());

        BiometricData biometricData = biometricDataBean.findBiometricData(createdBiometricData.getId());

        return Response.status(Response.Status.CREATED)
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
                biometricData.getValue(),
                biometricData.getNotes(),
                biometricData.getPatient().getId(),
                biometricData.getCreated_at(),
                biometricData.getCreated_by().getId());
    }
}
