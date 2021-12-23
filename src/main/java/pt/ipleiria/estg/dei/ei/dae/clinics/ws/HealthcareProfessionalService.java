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
    private HealthcareProfessionalBean healthcareProfessionalBean;

    @GET
    @Path("/")
    public Response getAllHealcareProfessionalsWS() {
        //List<Doctor> doctors = doctorBean.getAllDoctors();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<HealthcareProfessionalDTO>(toDTOAllHealcareProfessionals(healthcareProfessionalBean.getAllHealthcareProfessionals()),
                        "id", "email", "name", "gender", "specialty"))
                .build();
    }


    private List<HealthcareProfessionalDTO> toDTOAllHealcareProfessionals(List<Object[]> allHealthcareProfessionals) {
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
        healthcareProfessionalBean.create(
            healthcareProfessionalDTO.getEmail(),
            healthcareProfessionalDTO.getPassword(),
            healthcareProfessionalDTO.getName(),
            healthcareProfessionalDTO.getGender(),
            healthcareProfessionalDTO.getSpecialty(),
            healthcareProfessionalDTO.getCreated_by());

        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(healthcareProfessionalDTO.getEmail());

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
            doctorDTO.getPassword(),
            doctorDTO.getName(),
            doctorDTO.getGender(),
            doctorDTO.getSpecialty());

        HealthcareProfessional healthcareProfessional = healthcareProfessionalBean.findHealthcareProfessional(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(healthcareProfessional))
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
                healthcareProfessional.getCreated_by().getId());
    }
}