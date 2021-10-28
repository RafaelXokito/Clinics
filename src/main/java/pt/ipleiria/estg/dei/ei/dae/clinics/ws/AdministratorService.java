package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
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
        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<AdministratorDTO>(toDTOAllAdministrators(administratorBean.getAllAdministrators()),
                        "username","email","name","gender"))
                .build();
    }
    /*
    {
    columns: ["username", "name", "email", "gender"],
    data: [
        {
            username: "rafael.pereira",
            password: null,
            ...
            name: "Rafael Pereira"
        }
    ]
    }
     */

    private List<AdministratorDTO> toDTOAllAdministrators(List<Object[]> allAdministrators) {
        List<AdministratorDTO> administratorDTOList = new ArrayList<>();
        for (Object[] obj: allAdministrators) {
            administratorDTOList.add(new AdministratorDTO(
                obj[0].toString(),
                obj[1].toString(),
                obj[2].toString(),
                obj[3].toString()
            ));
        }
        return administratorDTOList;
    }

    @GET
    @Path("{username}")
    public Response getAdministratorWS(@PathParam("username") String username) throws MyEntityNotFoundException {
        Administrator administrator = administratorBean.findAdministrator(username);

        return Response.status(Response.Status.OK)
                .entity(toDTO(administrator))
                .build();
    }

    @POST
    @Path("/")
    public Response createAdministratorWS(AdministratorDTO administratorDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        administratorBean.create(
            administratorDTO.getUsername(),
            administratorDTO.getEmail(),
            administratorDTO.getPassword(),
            administratorDTO.getName(),
            administratorDTO.getGender());

        Administrator administrator = administratorBean.findAdministrator(administratorDTO.getUsername());

        return Response.status(Response.Status.CREATED)
                .entity(administrator)
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updateAdministratorWS(@PathParam("username") String username, AdministratorDTO administratorDTO) throws MyEntityNotFoundException {
        administratorBean.update(
                administratorDTO.getUsername(),
                administratorDTO.getEmail(),
                administratorDTO.getPassword(),
                administratorDTO.getName(),
                administratorDTO.getGender());

        Administrator administrator = administratorBean.findAdministrator(username);

        return Response.status(Response.Status.OK)
                .entity(administrator)
                .build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteAdministratorWS(@PathParam("username") String username) throws MyEntityNotFoundException {
        administratorBean.delete(username);

        return Response.status(Response.Status.OK)
                .build();
    }



    private List<AdministratorDTO> toDTOs(List<Administrator> administrators) {
        return administrators.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AdministratorDTO toDTO(Administrator administrator) {
        return new AdministratorDTO(administrator.getUsername(),
                administrator.getEmail(),
                administrator.getName(),
                administrator.getGender(),
                administrator.getCreated_at(),
                administrator.getUpdated_at(),
                administrator.getDeleted_at());
    }
}
