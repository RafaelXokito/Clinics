package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.BiometricDataDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.BiometricDataBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PersonBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricData;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Person;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("biometricdata") // relative url web path for this service
@Produces({ MediaType.APPLICATION_JSON }) // injects header “Content-Type: application/json”
@Consumes({ MediaType.APPLICATION_JSON }) // injects header “Accept: application/json”

public class BiometricDataService {

    @EJB
    private BiometricDataBean biometricDataBean;

    @EJB
    private PersonBean personBean;

    @EJB
    private PatientBean patientBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public Response getAllBiometricDataWS(@HeaderParam("Authorization") String auth) throws Exception {
        Person person = personBean.getPersonByAuthToken(auth);

        if (securityContext.isUserInRole("HealthcareProfessional")) {
            return Response.status(Response.Status.OK)
                    .entity(toDTOAllBiometricDatas(biometricDataBean.getAllBiometricData()))
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(biometricDataToDTOs(((Patient) person).getBiometric_data()))
                .build();
    }

    private List<BiometricDataDTO> toDTOAllBiometricDatas(List<Object[]> allBiometricDatas) {
        List<BiometricDataDTO> BiometricDataDTOList = new ArrayList<>();
        for (Object[] obj : allBiometricDatas) {
            BiometricDataDTOList.add(new BiometricDataDTO(
                    Long.parseLong(obj[0].toString()),
                    obj[1].toString(),
                    obj[2].toString(),
                    obj[3].toString(),
                    Double.parseDouble(obj[4].toString()),
                    obj[5].toString(),
                    (Date) obj[6]
                )
            );
        }
        return BiometricDataDTOList;
    }

    @GET
    @Path("{id}")
    public Response getBiometricDataWS(@PathParam("id") long id) throws Exception {
        BiometricData biometricData = biometricDataBean.findBiometricData(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricData))
                .build();
    }

    @POST
    @Path("/")
    public Response createBiometricDataWS(BiometricDataDTO biometricDataDTO, @HeaderParam("Authorization") String auth)
            throws Exception {
        Person person = personBean.getPersonByAuthToken(auth);

        long patientId;
        if (person.getClass().getSimpleName().equals("Patient")) {
            patientId = person.getId();
        }
        else {
            patientId = biometricDataDTO.getPatientId();
        }

        BiometricData createdBiometricData = biometricDataBean.create(
            biometricDataDTO.getBiometricTypeId(),
            biometricDataDTO.getValue(),
            biometricDataDTO.getNotes(),
            patientId,
            person.getId(),
            biometricDataDTO.getSource(),
            biometricDataDTO.getCreated_at());

        BiometricData biometricData = biometricDataBean.findBiometricData(createdBiometricData.getId());

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(biometricData))
                .build();
    }

    @POST
    @Path("import")
    @Consumes(MediaType.MULTIPART_FORM_DATA) //TODO FAlta fazer as restrições deste service
    public Response importFile(MultipartFormDataInput input, @HeaderParam("Authorization") String auth) throws Exception {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        for (InputPart inputPart : inputParts) {
                MultivaluedMap<String, String> header = inputPart.getHeaders(); String filename = getFilename(header);
                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);

                String[] lines = new String(bytes).split("\\r?\\n");

                List<Integer> failedRows = new ArrayList<>();
                int successRows = 0;
                //Skip first row
                for (int i = 1; i < lines.length; i++) {
                    String[] cols = lines[i].split(";");
                    try {
                        BiometricData createdBiometricData = biometricDataBean.create(
                                Long.parseLong(cols[0]), //Biometric Data Type
                                Double.parseDouble(cols[1]), //Value
                                cols[2], //Notes
                                patientBean.findPatientByHealthNo(Long.parseLong(cols[3])).getId(), //Patient Health Number
                                personBean.getPersonByAuthToken(auth).getId(),
                                cols[4], //Source
                                Date.from(Instant.parse(cols[5]))); //Created At
                        successRows++;
                    }catch (Exception e){
                        e.printStackTrace();
                        failedRows.add(i);
                    }
                }
                return Response.status(200).entity("Imported file, name : " + filename+"\nTotal success rows: "+successRows+"\nFailed rows: "+failedRows).build();
        }
        return null;
    }

    @PUT
    @Path("{id}")
    public Response updateBiometricDataWS(@PathParam("id") long id, BiometricDataDTO biometricDataDTO, @HeaderParam("Authorization") String auth) throws Exception {
        BiometricData createdBiometricData = biometricDataBean.update(
                id,
                biometricDataDTO.getBiometricTypeId(),
                biometricDataDTO.getValue(),
                biometricDataDTO.getNotes(),
                biometricDataDTO.getPatientId(),
                personBean.getPersonByAuthToken(auth).getId(),
                biometricDataDTO.getSource(),
                biometricDataDTO.getCreated_at());

        BiometricData biometricData = biometricDataBean.findBiometricData(createdBiometricData.getId());

        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricData))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBiometricDataWS(@PathParam("id") long id) throws Exception {
        if (biometricDataBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    private List<BiometricDataDTO> biometricDataToDTOs(List<BiometricData> biometricData) {
        return biometricData.stream().map(this::biometricDataToDTO).collect(Collectors.toList());
    }

    private BiometricDataDTO biometricDataToDTO(BiometricData biometricData) {
        return new BiometricDataDTO(
                biometricData.getId(),
                biometricData.getPatient().getName(),
                String.valueOf(biometricData.getPatient().getHealthNo()),
                biometricData.getBiometric_data_type().getName(),
                biometricData.getValue(),
                biometricData.getBiometric_data_type().getUnit_name(),
                biometricData.getCreated_at());
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
                biometricData.getBiometricDataIssue() == null ? 0 : biometricData.getBiometricDataIssue().getId(),
                biometricData.getBiometricDataIssue() == null ? null : biometricData.getBiometricDataIssue().getName());
    }

    private String getFilename(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";"); for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", ""); return finalFileName;
            } }
        return "unknown";
    }
}
