package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.DocumentBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("documents")
public class DocumentService {

    @EJB
    private ObservationBean observationBean;
    @EJB
    private DocumentBean documentBean;

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA) //TODO FAlta fazer as restrições deste service
    public Response upload(MultipartFormDataInput input) throws Exception {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        // Get file data to save
        String id = uploadForm.get("id").get(0).getBodyAsString();
        Observation observation = observationBean.findObservation(Long.parseLong(id));
        if(observation == null) {
            throw new MyEntityNotFoundException("Observation with id " + id + " not found.");
        }
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
    public Response download(@PathParam("id") Long id) throws MyEntityNotFoundException {
        Document document = documentBean.findDocument(id);
        File fileDownload = new File(document.getFilepath() + File.separator + document.getFilename());
        Response.ResponseBuilder response = Response.ok((Object) fileDownload);
        response.header("Content-Disposition", "attachment;filename=" + document.getFilename());
        return response.build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response delete(@PathParam("id") Long id) throws Exception {
        Document document = documentBean.findDocument(id);
        File fileToDelete = new File(document.getFilepath() + File.separator + document.getFilename());
        if (fileToDelete.delete()) {
            if (!documentBean.deleteDocument(document.getObservation().getId(), document))
                return Response.status(400,"File deleted, with errors").build();

            return Response.ok("File deleted").build();
        }
        else
            return Response.status(400, "File was not deleted").build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DocumentDTO> getDocumentsByObservation(@PathParam("id") String id) throws MyEntityNotFoundException {
        Observation observation = observationBean.findObservation(Long.parseLong(id));
        if(observation == null) {
            throw new MyEntityNotFoundException("Observation with id " + id + " not found.");
        }
            return documentsToDTOs(documentBean.getObservationDocuments(Long.parseLong(id)));
    }

    @GET
    @Path("{id}/exists")
    public Response hasDocuments(@PathParam("id") String id) throws
    MyEntityNotFoundException {
        Observation observation = observationBean.findObservation(Long.parseLong(id)); if(observation == null) {
            throw new MyEntityNotFoundException("Observation with id " + id + " not found.");
        }
        return Response.status(Response.Status.OK).entity(new Boolean(!observation.getDocuments().isEmpty())).build();
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
