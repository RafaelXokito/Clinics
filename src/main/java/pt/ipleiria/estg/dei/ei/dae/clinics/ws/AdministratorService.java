package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.NewPasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PersonBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;

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

    @EJB
    private PersonBean personBean;

    @GET
    @Path("/")
    public Response getAllAdministratorsWS() {
        return Response.status(Response.Status.OK)
                .entity(toDTOAllAdministrators(administratorBean.getAllAdministrators()))
                .build();
    }

    private List<AdministratorDTO> toDTOAllAdministrators(List<Object[]> allAdministrators) {
        List<AdministratorDTO> administratorDTOList = new ArrayList<>();
        for (Object[] obj: allAdministrators) {
            administratorDTOList.add(new AdministratorDTO(
                Long.parseLong(obj[0].toString()),
                obj[1].toString(),
                obj[2].toString(),
                obj[3].toString()
            ));
        }
        return administratorDTOList;
    }

    @GET
    @Path("{id}")
    public Response getAdministratorWS(@PathParam("id") long id) throws Exception {
        Administrator administrator = administratorBean.findAdministrator(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(administrator))
                .build();
    }

    @POST
    @Path("/")
    public Response createAdministratorWS(AdministratorDTO administratorDTO) throws Exception {
        long id = administratorBean.create(
            administratorDTO.getEmail(),
            administratorDTO.getPassword(),
            administratorDTO.getName(),
            administratorDTO.getGender());

        Administrator administrator = administratorBean.findAdministrator(id);

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(administrator))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateAdministratorWS(@PathParam("id") long id,AdministratorDTO administratorDTO) throws Exception {
        administratorBean.update(
            id,
            administratorDTO.getEmail(),
            administratorDTO.getName(),
            administratorDTO.getGender());

        Administrator administrator = administratorBean.findAdministrator(id);

        return Response.status(Response.Status.OK)
                .entity(toDTO(administrator))
                .build();
    }

    @PATCH
    @Path("{id}")
    public Response updateAdministratorPasswordWS(@PathParam("id") long id, NewPasswordDTO newPasswordDTO, @HeaderParam("Authorization") String auth) throws Exception {
        administratorBean.updatePassword(
            id,
            newPasswordDTO.getOldPassword(),
            newPasswordDTO.getNewPassword(),
            personBean.getPersonByAuthToken(auth).getId());

        return Response.status(Response.Status.OK)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteAdministratorWS(@PathParam("id") long id) throws Exception {
        if (administratorBean.delete(id))
            return Response.status(Response.Status.OK)
                    .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }



    private List<AdministratorDTO> toDTOs(List<Administrator> administrators) {
        return administrators.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AdministratorDTO toDTO(Administrator administrator) {
        return new AdministratorDTO(administrator.getId(),
                administrator.getEmail(),
                administrator.getName(),
                administrator.getGender(),
                administrator.getCreated_at(),
                administrator.getUpdated_at(),
                administrator.getDeleted_at());
    }
}
