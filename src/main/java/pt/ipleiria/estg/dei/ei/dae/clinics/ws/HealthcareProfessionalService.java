package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.HealthcareProfessionalBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("healthcareprofessionals") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class HealthcareProfessionalService {
    @EJB
    private HealthcareProfessionalBean healthcareProfessionalBean;

    @GET
    @Path("/")
    public Response getAllHealcareProfessionalsWS() {
        //List<Doctor> doctors = doctorBean.getAllDoctors();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<HealthcareProfessionalDTO>(toDTOAllHealthcareProfessionals(healthcareProfessionalBean.getAllHealthcareProfessionals()),
                        "id", "email", "name", "gender", "specialty"))
                .build();
    }


    private List<HealthcareProfessionalDTO> toDTOAllHealthcareProfessionals(List<Object[]> allHealthcareProfessionals) {
        List<HealthcareProfessionalDTO> healthcareProfessionalDTOList = new ArrayList<>();
        for (Object[] obj: allHealthcareProfessionals) {
            healthcareProfessionalDTOList.add(new HealthcareProfessionalDTO(
                    Long.parseLong(obj[0].toString()),
                    obj[1].toString(),
                    obj[2].toString(),
                    obj[3].toString(),
                    obj[4].toString()
            ));
        }
        return healthcareProfessionalDTOList;
    }

    @GET
    @Path("{id}")
    public Response getHealthcareProfessionalWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(healthcareProfessional))
                .build();
    }

    @POST
    @Path("/")
    public Response createHealthcareProfessionalWS(HealthcareProfessionalDTO healthcareProfessionalDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        long id = healthcareProfessionalBean.create(
            healthcareProfessionalDTO.getEmail(),
            healthcareProfessionalDTO.getPassword(),
            healthcareProfessionalDTO.getName(),
            healthcareProfessionalDTO.getGender(),
            healthcareProfessionalDTO.getSpecialty(),
            healthcareProfessionalDTO.getCreated_by());

        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(healthcareProfessional))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateHealthcareProfessionalWS(@PathParam("id") long id , HealthcareProfessionalDTO doctorDTO) throws MyEntityNotFoundException {
        healthcareProfessionalBean.update(
            id,
            doctorDTO.getEmail(),
            doctorDTO.getName(),
            doctorDTO.getGender(),
            doctorDTO.getSpecialty());

        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(healthcareProfessional))
                .build();
    }

    @PATCH
    @Path("{id}")
    public Response updateHealthcareProfessionalPasswordWS(@PathParam("id") long id, NewPasswordDTO newPasswordDTO) throws MyEntityNotFoundException, MyIllegalArgumentException {
        healthcareProfessionalBean.updatePassword(
                id,
                newPasswordDTO.getOldPassword(),
                newPasswordDTO.getNewPassword());

        return Response.status(Response.Status.OK)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteHealthcareProfessionalWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        if (healthcareProfessionalBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    @GET
    @Path("{id}/prescriptions")
    public Response getHealthcareProfessionalPrescriptionsWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(prescriptionToDTOs(healthcareProfessional.getPrescriptions()))
                .build();
    }

    @GET
    @Path("{id}/observations")
    public Response getHealthcareProfessionalObservationsWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(observationToDTOs(healthcareProfessional.getObservations()))
                .build();
    }

    @GET
    @Path("{id}/pacients")
    public Response getHealthcareProfessionalPacientsWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(pacientToDTOs(healthcareProfessional.getPatients()))
                .build();
    }

    private List<HealthcareProfessionalDTO> toDTOs(List<HealthcareProfessional> healthcareProfessionals) {
        return healthcareProfessionals.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private HealthcareProfessionalDTO toDTO(HealthcareProfessional healthcareProfessional) {
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
                prescriptionToDTOs(healthcareProfessional.getPrescriptions()),
                observationToDTOs(healthcareProfessional.getObservations()),
                pacientToDTOs(healthcareProfessional.getPatients()));
    }

    private List<PatientDTO> pacientToDTOs(List<Patient> patients) {
        return patients.stream().map(this::pacientToDTO).collect(Collectors.toList());
    }

    private PatientDTO pacientToDTO(Patient patient) {
        return new PatientDTO(patient.getId(),
                patient.getEmail(),
                patient.getName(),
                patient.getGender(),
                patient.getHealthNo());
    }

    private List<PrescriptionDTO> prescriptionToDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::prescriptionToDTO).collect(Collectors.toList());
    }

    private PrescriptionDTO prescriptionToDTO(Prescription prescription) {
        boolean hasPatient = prescription.getPatient() != null;

        if (hasPatient) {
            return new PrescriptionDTO(
                    prescription.getId(),
                    prescription.getHealthcareProfessional().getId(),
                    prescription.getHealthcareProfessional().getName(),
                    prescription.getPatient().getId(),
                    prescription.getPatient().getName(),
                    prescription.getStart_date().toString(),
                    prescription.getEnd_date().toString(),
                    prescription.getNotes());
        }

        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getHealthcareProfessional().getName(),
                prescription.getStart_date().toString(),
                prescription.getEnd_date().toString());
    }

    private List<ObservationDTO> observationToDTOs(List<Observation> observations) {
        return observations.stream().map(this::observationToDTO).collect(Collectors.toList());
    }

    private ObservationDTO observationToDTO(Observation observation) {
        return new ObservationDTO(observation.getId(),
                observation.getHealthcareProfessional().getId(),
                observation.getHealthcareProfessional().getName(),
                observation.getPatient().getId(),
                observation.getPatient().getName(),
                observation.getCreated_at());
    }
}
