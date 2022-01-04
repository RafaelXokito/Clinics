package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Path("statistics") // relative url web path for this service
@Produces({ MediaType.APPLICATION_JSON }) // injects header “Content-Type: application/json”
@Consumes({ MediaType.APPLICATION_JSON }) // injects header “Accept: application/json”
public class StatisticService {

    @EJB
    private PersonBean personBean;

    @EJB
    private AdministratorBean administratorBean;

    @EJB
    private HealthcareProfessionalBean healthcareProfessionalBean;

    @EJB
    private PatientBean patientBean;

    @EJB
    private BiometricDataTypeBean biometricDataTypeBean;

    @EJB
    private BiometricDataBean biometricDataBean;

    @EJB
    private PrescriptionBean prescriptionBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public Response getStatistics(@HeaderParam("Authorization") String auth) throws ParseException {
        Person person = personBean.getPersonByAuthToken(auth);
        if (securityContext.isUserInRole("Administrator")) {
            /*
             * - Admin
             * numero total de administradores
             * numero total de healthcare professionals
             * numero total de pacientes
             * numero de biometric data types
             */
            long totalAdmin = administratorBean.getAllAdministratorsClass().size();
            long totalHealthcareProfessionals = healthcareProfessionalBean.getAllHealthcareProfessionalsClass().size();
            long totalPatients = patientBean.getAllPatientsClass().size();
            long totalBiometricDataTypes = biometricDataTypeBean.getAllBiometricDataTypes().size();
            return Response.ok(getAdministratorStatisticsDTO(totalAdmin, totalHealthcareProfessionals, totalPatients,
                    totalBiometricDataTypes)).build();
        }
        if (securityContext.isUserInRole("HealthcareProfessional")) {
            /*
             * - Healthcare professionals
             * ultima observação
             */
            List<Observation> observations = ((HealthcareProfessional) person).getObservations();
            Observation lastObservation = observations.size() > 0 ? observations.get(observations.size() - 1) : null;

            return Response.ok(getHealthcareProfessionalStatisticsDTO(lastObservation)).build();
        }
        if (securityContext.isUserInRole("Patient")) {
            /*
             * - Paciente
             * prescrições ativas (Entre datas da prescrição coincida com a atual)
             * healthcare professionals
             * ultima biometric data
             */
            List<BiometricData> biometricData = biometricDataBean.getAllBiometricDatasClassByPatient(person.getId());
            BiometricData lastBiometricData = biometricData.size() > 0 ? biometricData.get(biometricData.size() - 1) : null;
            List<Prescription> prescriptions = prescriptionBean.getActivePrescriptionsByPatient(person.getId());
            return Response.ok(getPatientStatisticsDTO(lastBiometricData, prescriptions, ((Patient) person).getHealthcareProfessionals(),biometricData)).build();
        }
        return Response.status(204).build(); // no jwt means no claims to extract
    }

    private PatientStatisticsDTO getPatientStatisticsDTO(BiometricData lastBiometricData,
                                                         List<Prescription> prescriptions, List<HealthcareProfessional> healthcareProfessionals, List<BiometricData> biometricData) {
        return new PatientStatisticsDTO(toDTOBiometricData(lastBiometricData), toDTOsPrescriptions(prescriptions), toDTOsHealthcareProfessional(healthcareProfessionals), biometricDataToDTOs(biometricData));
    }

    private HealthcareProfessionalStatisticsDTO getHealthcareProfessionalStatisticsDTO(Observation lastObservation) {
        return new HealthcareProfessionalStatisticsDTO(toDTOObservation(lastObservation));
    }

    private AdministratorStatisticsDTO getAdministratorStatisticsDTO(long totalAdmin, long totalHealthcareProfessionals,
            long totalPatients, long totalBiometricDataTypes) {
        return new AdministratorStatisticsDTO(totalAdmin, totalHealthcareProfessionals, totalPatients,
                totalBiometricDataTypes);
    }

    private PrescriptionDTO toDTOPrescription(Prescription prescription) {
        if (prescription == null)
            return new PrescriptionDTO();
        boolean isGlobal = prescription.getBiometric_data_issue() != null
                && prescription.getBiometric_data_issue().size() > 0;

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
                document.getFilepath());
    }

    private List<DocumentDTO> documentsToDTOs(List<Document> documents) {
        return documents.stream().map(this::documentToDTO).collect(Collectors.toList());
    }

    private ObservationDTO toDTOObservation(Observation observation) {
        if (observation == null)
            return new ObservationDTO();
        return new ObservationDTO(
                observation.getId(),
                observation.getHealthcareProfessional().getId(),
                observation.getHealthcareProfessional().getName(),
                observation.getPatient().getId(),
                observation.getPatient().getName(),
                observation.getNotes(),
                observation.getCreated_at(),
                toDTOPrescription(observation.getPrescription()),
                documentsToDTOs(observation.getDocuments()));
    }

    private List<BiometricDataDTO> biometricDataToDTOs(List<BiometricData> biometricData) {
        return biometricData.stream().map(this::toDTOBiometricData).collect(Collectors.toList());
    }

    private BiometricDataDTO toDTOBiometricData(BiometricData biometricData) {
        if (biometricData == null)
            return new BiometricDataDTO();
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
                biometricData.getBiometricDataIssue() == null ? null : biometricData.getBiometricDataIssue().getName(),
                biometricData.getBiometric_data_type().getMax(),
                biometricData.getBiometric_data_type().getMin());
    }

    private List<PrescriptionDTO> toDTOsPrescriptions(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTOPrescription).collect(Collectors.toList());
    }

    private List<HealthcareProfessionalDTO> toDTOsHealthcareProfessional(List<HealthcareProfessional> healthcareProfessionals) {
        return healthcareProfessionals.stream().map(this::toDTOHealthcareProfessional).collect(Collectors.toList());
    }

    private HealthcareProfessionalDTO toDTOHealthcareProfessional(HealthcareProfessional healthcareProfessional) {
        return new HealthcareProfessionalDTO(
                healthcareProfessional.getId(),
                healthcareProfessional.getEmail(),
                healthcareProfessional.getName(),
                healthcareProfessional.getGender(),
                healthcareProfessional.getCreated_at(),
                healthcareProfessional.getUpdated_at(),
                healthcareProfessional.getDeleted_at(),
                healthcareProfessional.getSpecialty(),
                healthcareProfessional.getCreated_by().getId(),
                healthcareProfessional.getBirthDate());
    }
}
