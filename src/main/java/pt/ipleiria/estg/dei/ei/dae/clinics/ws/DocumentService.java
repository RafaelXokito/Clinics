package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.DocumentBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PersonBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyUnauthorizedException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("documents")
public class DocumentService {

    @EJB
    private ObservationBean observationBean;

    @EJB
    private DocumentBean documentBean;

    @EJB
    private PersonBean personBean;

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(MultipartFormDataInput input, @HeaderParam("Authorization") String auth) throws Exception {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        // Get file data to save
        String id = uploadForm.get("id").get(0).getBodyAsString();
        Observation observation = observationBean.findObservation(Long.parseLong(id));
        if(observation == null)
            throw new MyEntityNotFoundException("Observation with id " + id + " not found.");

        if (personBean.getPersonByAuthToken(auth).getId() != observation.getHealthcareProfessional().getId())
            throw new MyUnauthorizedException("You are not allowed to upload files to this observation");

        List<InputPart> inputParts = uploadForm.get("file");
        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders(); String filename = getFilename(header);
                // convert the uploaded file to inputstream

                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                String path = System.getProperty("user.home") + File.separator + "uploads"; File customDir = new File(path);
                if (!customDir.exists()) {
                    customDir.mkdir();
                }
                String filepath =  customDir.getCanonicalPath() + File.separator + filename;
                writeFile(bytes, filepath);
                documentBean.create(observation.getId(), path, filename);
                return Response.status(200).entity("Uploaded file name : " + filename).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GET
    @Path("download/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("id") long id, @HeaderParam("Authorization") String auth) throws Exception {
        Document document = documentBean.findDocument(id);

        long authId = personBean.getPersonByAuthToken(auth).getId();
        if (authId != document.getObservation().getHealthcareProfessional().getId() && authId != document.getObservation().getPatient().getId())
            throw new MyUnauthorizedException("You are not allowed to download files of this observation");

        File fileDownload = new File(document.getFilepath() + File.separator + document.getFilename());
        Response.ResponseBuilder response = Response.ok(fileDownload);
        response.header("Content-Disposition", "attachment;filename=" + document.getFilename());
        return response.build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response delete(@PathParam("id") long id, @HeaderParam("Authorization") String auth) throws Exception {
        Document document = documentBean.findDocument(id);

        if (personBean.getPersonByAuthToken(auth).getId() != document.getObservation().getHealthcareProfessional().getId())
            throw new MyUnauthorizedException("You are not allowed to delete files of this observation");

        File fileToDelete = new File(document.getFilepath() + File.separator + document.getFilename());
        if (fileToDelete.delete()) {
            if (!documentBean.deleteDocument(document))
                return Response.status(400,"File deleted, with errors").build();

            return Response.ok("File deleted").build();
        }

        return Response.status(400, "File was not deleted").build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DocumentDTO> getDocumentsByObservation(@PathParam("id") long id, @HeaderParam("Authorization") String auth) throws Exception {
        Observation observation = observationBean.findObservation(id);

        long authId = personBean.getPersonByAuthToken(auth).getId();
        if (authId != observation.getHealthcareProfessional().getId() && authId != observation.getPatient().getId())
            throw new MyUnauthorizedException("You are not allowed to view files of this observation");

        return documentsToDTOs(documentBean.getObservationDocuments(id));
    }

    @GET
    @Path("{id}/exists")
    public Response hasDocuments(@PathParam("id") long id, @HeaderParam("Authorization") String auth) throws Exception {
        Observation observation = observationBean.findObservation(id);

        return Response.status(Response.Status.OK).entity(!observation.getDocuments().isEmpty()).build();
    }

    DocumentDTO toDTO(Document document) { return new DocumentDTO(
            document.getId(),
            document.getFilepath(),
            document.getFilename());
    }

    List<DocumentDTO> documentsToDTOs(List<Document> documents) {
        return documents.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private String getFilename(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";"); for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", ""); return finalFileName;
            } }
        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
        System.out.println("Written: " + filename);
    }
}
