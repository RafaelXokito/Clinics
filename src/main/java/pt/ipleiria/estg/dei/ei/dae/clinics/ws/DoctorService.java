package pt.ipleiria.estg.dei.ei.dae.clinics.ws;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.DoctorDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.EntitiesDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.ejbs.DoctorBean;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Doctor;

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
    public Response getDoctorWS(@PathParam("username") String username) {
        Doctor doctor = doctorBean.findDoctor(username);

        if (doctor == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(toDTO(doctor))
                .build();
    }

    @POST
    @Path("/")
    public Response createDoctorWS(DoctorDTO doctorDTO) {
        Doctor createdDoctor = doctorBean.create(
                doctorDTO.getUsername(),
                doctorDTO.getEmail(),
                doctorDTO.getPassword(),
                doctorDTO.getName(),
                doctorDTO.getGender(),
                doctorDTO.getSpecialty(),
                doctorDTO.getCreated_by());

        Doctor doctor = doctorBean.findDoctor(createdDoctor.getUsername());

        if (doctor == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(createdDoctor)
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updateDoctorWS(@PathParam("username") String username, DoctorDTO doctorDTO) {
        Doctor updatedDoctor = doctorBean.update(
                doctorDTO.getUsername(),
                doctorDTO.getEmail(),
                doctorDTO.getPassword(),
                doctorDTO.getName(),
                doctorDTO.getGender(),
                doctorDTO.getSpecialty());

        Doctor doctor = doctorBean.findDoctor(updatedDoctor.getUsername());

        if (!updatedDoctor.equals(doctor))
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

        return Response.status(Response.Status.OK)
                .entity(updatedDoctor)
                .build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteDoctorWS(@PathParam("username") String username) {
        Doctor removedDoctor = doctorBean.delete(username);

        if (removedDoctor != null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();

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
