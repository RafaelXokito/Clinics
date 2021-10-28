package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PatientDTO;
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
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class BiometricDataService {
    @EJB
    private BiometricDataBean biometricDataBean;

    @GET
    @Path("/")
    public Response getAllBiometricDataWS() {
        List<BiometricData> biometricData = biometricDataBean.getAllBiometricData();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<BiometricDataDTO>(toDTOAllBiometricDatas(biometricDataBean.getAllBiometricData()),
                        "patientName", "healthNo", "biometricDataTypeName", "valueUnit"))
                .build();
    }

    private List<BiometricDataDTO> toDTOAllBiometricDatas(List<Object[]> allBiometricDatas) {
        List<BiometricDataDTO> BiometricDataDTOList = new ArrayList<>();
        for (Object[] obj: allBiometricDatas) {
            BiometricDataDTOList.add(new BiometricDataDTO(
                    obj[0].toString(),
                    obj[1].toString(),
                    obj[2].toString(),
                    obj[3].toString() + " " + obj[4].toString()
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
                biometricDataDTO.getPatientUsername(),
                biometricDataDTO.getCreated_by());

        BiometricData biometricData = biometricDataBean.findBiometricData(createdBiometricData.getId());

        return Response.status(Response.Status.CREATED)
                .entity(biometricData)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBiometricDataWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        biometricDataBean.delete(id);

        return Response.status(Response.Status.OK)
                .build();
    }


    private List<BiometricDataDTO> toDTOs(List<BiometricData> biometricData) {
        return biometricData.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private BiometricDataDTO toDTO(BiometricData biometricData) {
        return new BiometricDataDTO(biometricData.getId(),
                biometricData.getBiometric_data_type().getId(),
                biometricData.getValue(),
                biometricData.getNotes(),
                biometricData.getPatient().getUsername(),
                biometricData.getCreated_at(),
                biometricData.getCreated_by().getUsername());
    }
}
