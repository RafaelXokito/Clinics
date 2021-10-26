package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("administrators") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class AdministratorService {
    @EJB
    private AdministratorBean administratorBean;

    @GET
    @Path("/")
    public Response getAllAdministratorsWS() {
        return Response.status(Response.Status.FOUND)
                .entity(toDTOs(administratorBean.getAllAdministrators()))
                .build();
    }

    @GET
    @Path("{username}")
    public Response getAdministratorWS(@PathParam("username") String username) {
        Administrator administrator = administratorBean.findAdministrator(username);

        if (administrator == null)
            return Response.status(Response.Status.NOT_FOUND)
                .build();

        return Response.status(Response.Status.FOUND)
                .entity(toDTO(administrator))
                .build();
    }

    @POST
    @Path("/")
    public Response createAdministratorWS(AdministratorDTO administratorDTO) {
        Administrator createdAdministrator = administratorBean.create(
                administratorDTO.getUsername(),
                administratorDTO.getEmail(),
                administratorDTO.getPassword(),
                administratorDTO.getName(),
                administratorDTO.getGender());

        Administrator administrator = administratorBean.findAdministrator(createdAdministrator.getUsername());

        if (administrator == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(createdAdministrator)
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updateAdministratorWS(@PathParam("username") String username, AdministratorDTO administratorDTO) {
        Administrator updatedAdministrator = administratorBean.update(
                administratorDTO.getUsername(),
                administratorDTO.getEmail(),
                administratorDTO.getPassword(),
                administratorDTO.getName(),
                administratorDTO.getGender());

        Administrator administrator = administratorBean.findAdministrator(updatedAdministrator.getUsername());

        if (!updatedAdministrator.equals(administrator))
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(updatedAdministrator)
                .build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteAdministratorWS(@PathParam("username") String username) {
        Administrator removedAdministrator = administratorBean.delete(username);

        if (removedAdministrator != null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
                .build();
    }



    private List<AdministratorDTO> toDTOs(List<Administrator> administrators) {
        return administrators.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AdministratorDTO toDTO(Administrator administrator) {
        return new AdministratorDTO(administrator.getUsername(),
                administrator.getEmail(),
                administrator.getPassword(),
                administrator.getName(),
                administrator.getGender(),
                administrator.getCreated_at(),
                administrator.getUpdated_at(),
                administrator.getDeleted_at());
    }
}
