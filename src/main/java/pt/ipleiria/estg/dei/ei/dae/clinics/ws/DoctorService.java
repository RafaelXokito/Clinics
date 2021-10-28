package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.DoctorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.DoctorBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("doctors") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class DoctorService {
    @EJB
    private DoctorBean doctorBean;

    @GET
    @Path("/")
    public Response getAllDoctorsWS() {
        List<Doctor> doctors = doctorBean.getAllDoctors();

        return Response.status(Response.Status.OK)
                .entity(new EntitiesDTO<DoctorDTO>(toDTOAllDoctors(doctorBean.getAllDoctors()),
                        "username","email","name","gender","specialty"))
                .build();
    }


    private List<DoctorDTO> toDTOAllDoctors(List<Object[]> allDoctors) {
        List<DoctorDTO> doctorDTOList = new ArrayList<>();
        for (Object[] obj: allDoctors) {
            doctorDTOList.add(new DoctorDTO(
                    obj[0].toString(),
                    obj[1].toString(),
                    obj[2].toString(),
                    obj[3].toString(),
                    obj[4].toString()
            ));
        }
        return doctorDTOList;
    }

    @GET
    @Path("{username}")
    public Response getDoctorWS(@PathParam("username") String username) throws MyEntityNotFoundException {
        Doctor doctor = doctorBean.findDoctor(username);

        return Response.status(Response.Status.OK)
                .entity(toDTO(doctor))
                .build();
    }

    @POST
    @Path("/")
    public Response createDoctorWS(DoctorDTO doctorDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        doctorBean.create(doctorDTO.getUsername(),
            doctorDTO.getEmail(),
            doctorDTO.getPassword(),
            doctorDTO.getName(),
            doctorDTO.getGender(),
            doctorDTO.getSpecialty(),
            doctorDTO.getCreated_by());

        Doctor doctor = doctorBean.findDoctor(doctorDTO.getUsername());

        return Response.status(Response.Status.CREATED)
                .entity(doctor)
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updateDoctorWS(@PathParam("username") String username, DoctorDTO doctorDTO) throws MyEntityNotFoundException {
        doctorBean.update(username,
            doctorDTO.getEmail(),
            doctorDTO.getPassword(),
            doctorDTO.getName(),
            doctorDTO.getGender(),
            doctorDTO.getSpecialty());

        Doctor doctor = doctorBean.findDoctor(username);

        return Response.status(Response.Status.OK)
                .entity(doctor)
                .build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteDoctorWS(@PathParam("username") String username) throws MyEntityNotFoundException {
        doctorBean.delete(username);

        return Response.status(Response.Status.OK)
                .build();
    }


    private List<DoctorDTO> toDTOs(List<Doctor> doctors) {
        return doctors.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private DoctorDTO toDTO(Doctor doctor) {
        return new DoctorDTO(doctor.getUsername(),
                doctor.getEmail(),
                doctor.getName(),
                doctor.getGender(),
                doctor.getCreated_at(),
                doctor.getUpdated_at(),
                doctor.getDeleted_at(),
                doctor.getSpecialty(),
                doctor.getCreated_by().getUsername());
    }
}
