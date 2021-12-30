package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PersonBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

@Path("observations") // relative url web path for this service
@Produces({ MediaType.APPLICATION_JSON }) // injects header “Content-Type: application/json”
@Consumes({ MediaType.APPLICATION_JSON }) // injects header “Accept: application/json”
public class ObservationService {
    @EJB
    private ObservationBean observationBean;

    @EJB
    private PersonBean personBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public Response getAllObservationsWS(@HeaderParam("Authorization") String auth) throws Exception {
        Person person = personBean.getPersonByAuthToken(auth);

        if (securityContext.isUserInRole("HealthcareProfessional")) {
            return Response.status(Response.Status.OK)
                    .entity(toDTOs(((HealthcareProfessional) person).getObservations()))
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(toDTOs(((Patient) person).getObservations()))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getObservationWS(@PathParam("id") long id) throws Exception {
        Observation observation = observationBean.findObservation(id);

        return Response.status(Response.Status.OK)
                .entity(fullToDTO(observation))
                .build();
    }

    @POST
    @Path("/")
    public Response createObservationWS(ObservationDTO observationDTO, @HeaderParam("Authorization") String auth) throws Exception {
        long id = observationBean.create(
                    personBean.getPersonByAuthToken(auth).getId(),
                    observationDTO.getPatientId(),
                    observationDTO.getNotes(),
                    observationDTO.getPrescription().getStart_date(),
                    observationDTO.getPrescription().getEnd_date(),
                    observationDTO.getPrescription().getNotes());

            Observation observation = observationBean.findObservation(id);

            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(observation))
                    .build();
    }

    @PUT
    @Path("{id}")
    public Response updateObservationWS(@PathParam("id") long id , ObservationDTO observationDTO) throws Exception {
        System.out.println(observationDTO.getId());
        observationBean.update(
                id,
                observationDTO.getNotes(),
                observationDTO.getPrescription().getStart_date(),
                observationDTO.getPrescription().getEnd_date(),
                observationDTO.getPrescription().getNotes());

        Observation observation = observationBean.findObservation(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(observation))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteObservationWS(@PathParam("id") long id) throws Exception {
        if (observationBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    private List<ObservationDTO> toDTOs(List<Observation> observations) {
        return observations.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ObservationDTO toDTO(Observation observation) {
        return new ObservationDTO(observation.getId(),
                observation.getHealthcareProfessional().getId(),
                observation.getHealthcareProfessional().getName(),
                observation.getPatient().getId(),
                observation.getPatient().getName(),
                observation.getCreated_at());
    }

    private ObservationDTO fullToDTO(Observation observation) {
        return new ObservationDTO(observation.getId(),
                observation.getHealthcareProfessional().getId(),
                observation.getHealthcareProfessional().getName(),
                observation.getPatient().getId(),
                observation.getPatient().getName(),
                observation.getNotes(),
                observation.getCreated_at(),
                observation.getPrescription() != null ? prescriptionToDTO(observation.getPrescription()) : null,
                documentsToDTOs(observation.getDocuments())) ;
    }

    private PrescriptionDTO prescriptionToDTO(Prescription prescription) {
        boolean isGlobal = prescription.getBiometric_data_issue() != null && prescription.getBiometric_data_issue().size() > 0;

        if (isGlobal) {
            return new PrescriptionDTO(
                    prescription.getId(),
                    prescription.getHealthcareProfessional().getId(),
                    prescription.getHealthcareProfessional().getName(),
                    prescription.getStart_date().toString(),
                    prescription.getEnd_date().toString(),
                    prescription.getNotes(),
                    issuesToDTOs(prescription.getBiometric_data_issue()));
        }

        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getHealthcareProfessional().getId(),
                prescription.getHealthcareProfessional().getName(),
                patientToDTOs(prescription.getPatients()),
                prescription.getStart_date().toString(),
                prescription.getEnd_date().toString(),
                prescription.getNotes());
    }

    private List<BiometricDataIssueDTO> issuesToDTOs(List<BiometricDataIssue> biometricDataIssues) {
        return biometricDataIssues.stream().map(this::issueToDTO).collect(Collectors.toList());
    }

    private BiometricDataIssueDTO issueToDTO(BiometricDataIssue biometricDataIssue) {
        return new BiometricDataIssueDTO(biometricDataIssue.getId(),
                biometricDataIssue.getName(),
                biometricDataIssue.getMin(),
                biometricDataIssue.getMax(),
                biometricDataIssue.getBiometric_data_type().getId(),
                biometricDataIssue.getBiometric_data_type().getName());
    }

    private List<PatientDTO> patientToDTOs(List<Patient> patients) {
        return patients.stream().map(this::patientToDTO).collect(Collectors.toList());
    }

    private PatientDTO patientToDTO(Patient patient) {
        return new PatientDTO(patient.getId(),
                patient.getName());
    }
    
    private DocumentDTO documentToDTO(Document document) {
        return new DocumentDTO(
                document.getId(),
                document.getFilename(),
                document.getFilepath()
        );
    }

    private List<DocumentDTO> documentsToDTOs(List<Document> documents) {
        return documents.stream().map(this::documentToDTO).collect(Collectors.toList());
    }

}
