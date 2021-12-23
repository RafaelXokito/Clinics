package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.PersonDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.JwtBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.PersonBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Person;
import pt.ipleiria.estg.dei.ei.dae.clinics.jwt.Jwt;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.logging.Logger;
@Path("/auth")
public class LoginService {
    private static final Logger log =
            Logger.getLogger(LoginService.class.getName());
    @EJB
    JwtBean jwtBean;
    @EJB
    PersonBean personBean;
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(AuthDTO authDTO) {
        System.out.println(authDTO);
        try {
            Person user = personBean.authenticate(authDTO.getEmail(), authDTO.getPassword());
            System.out.println("Login Service");
            if (user != null) {
                if (user.getId() > 0) {
                    log.info("Generating JWT for user " + user.getId());
                }
                String token = jwtBean.createJwt(String.valueOf(user.getId()), new
                        String[]{user.getClass().getSimpleName()});
                return Response.ok(new Jwt("Bearer",token)).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    @GET
    @Path("/user")
    public Response demonstrateClaims(@HeaderParam("Authorization") String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                JWT j = JWTParser.parse(auth.substring(7));
                j.getJWTClaimsSet().getClaims();
                Person person = personBean.findPerson(Long.valueOf(j.getJWTClaimsSet().getClaims().get("sub").toString()));
                return Response.ok(toDTO(person)).build();
                //return personBean.findPerson(j.getJWTClaimsSet().getClaims().get("sub");
                //return Response.ok(j.getJWTClaimsSet().getClaims()).build();
                //Note: nimbusds converts token expiration time to milliseconds
            } catch (ParseException e) {
                log.warning(e.toString());
                return Response.status(400).build();
            }
        }
        return Response.status(204).build(); //no jwt means no claims to extract
    }

    private PersonDTO toDTO(Person person) {
        return new PersonDTO(
                person.getId(),
                person.getEmail(),
                person.getName(),
                person.getGender(),
                person.getCreated_at(),
                person.getUpdated_at(),
                person.getDeleted_at());
    }
}
