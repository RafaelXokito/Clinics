package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.HealthcareProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.HealthcareProfessionalBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("healthcareprofessional") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class HealthcareProfessionalService {
    @EJB
    private HealthcareProfessionalBean doctorBean;

    @GET
    @Path("/")
    public Response getAllDoctorsWS() {
        //List<Doctor> doctors = doctorBean.getAllDoctors();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<HealthcareProfessionalDTO>(toDTOAllDoctors(doctorBean.getAllDoctors()),
                        "id","email","name","gender","specialty"))
                .build();
    }


    private List<HealthcareProfessionalDTO> toDTOAllDoctors(List<Object[]> allDoctors) {
        List<HealthcareProfessionalDTO> doctorDTOList = new ArrayList<>();
        for (Object[] obj: allDoctors) {
            doctorDTOList.add(new HealthcareProfessionalDTO(
                    obj[0].toString(),
                    obj[1].toString(),
                    obj[2].toString(),
                    obj[3].toString()
            ));
        }
        return doctorDTOList;
    }

    @GET
    @Path("{id}")
    public Response getDoctorWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        HealthcareProfessional doctor = doctorBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(doctor))
                .build();
    }

    @POST
    @Path("/")
    public Response createDoctorWS(HealthcareProfessionalDTO healthcareProfessionalDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        doctorBean.create(
            healthcareProfessionalDTO.getEmail(),
            healthcareProfessionalDTO.getPassword(),
            healthcareProfessionalDTO.getName(),
            healthcareProfessionalDTO.getGender(),
            healthcareProfessionalDTO.getSpecialty(),
            healthcareProfessionalDTO.getCreated_by());

        HealthcareProfessional doctor = doctorBean.findHealthcareProfessional(healthcareProfessionalDTO.getEmail());

        return Response.status(Response.Status.CREATED)
                .entity(doctor)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateDoctorWS(@PathParam("id") long id , HealthcareProfessionalDTO doctorDTO) throws MyEntityNotFoundException {
        doctorBean.update(
            id,
            doctorDTO.getEmail(),
            doctorDTO.getPassword(),
            doctorDTO.getName(),
            doctorDTO.getGender(),
            doctorDTO.getSpecialty());

        HealthcareProfessional doctor = doctorBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(doctor)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteDoctorWS(@PathParam("id") long id) throws MyEntityNotFoundException {
        doctorBean.delete(id);

        return Response.status(Response.Status.OK)
                .build();
    }


    private List<HealthcareProfessionalDTO> toDTOs(List<HealthcareProfessional> healthcareProfessionals) {
        return healthcareProfessionals.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private HealthcareProfessionalDTO toDTO(HealthcareProfessional doctor) {
        return new HealthcareProfessionalDTO(
                doctor.getEmail(),
                doctor.getName(),
                doctor.getGender(),
                doctor.getCreated_at(),
                doctor.getUpdated_at(),
                doctor.getDeleted_at(),
                doctor.getSpecialty(),
                doctor.getCreated_by().getId());
    }
}
