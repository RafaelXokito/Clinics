package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Administrators")
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT a.username FROM Administrator a ORDER BY a.username"
        )
})
public class Administrator extends Person implements Serializable {

    @NotNull
    @OneToMany(mappedBy = "created_by", cascade = CascadeType.PERSIST)
    private List<HealthcareProfessional> doctors;

    public Administrator(String username, String email, String password, String name, String gender) {
        super(username, email, password, name, gender);
        this.doctors = new ArrayList<>();
    }

    public Administrator(String username) {
        super(username);
    }

    public Administrator() {
    }

    public HealthcareProfessional addDoctor(HealthcareProfessional doctor){
        if (doctor != null && !this.doctors.contains(doctor)) {
            doctors.add(doctor);
            return doctor;
        }
        return null;
    }

    public HealthcareProfessional removeDoctor(HealthcareProfessional doctor){
        return doctor != null && doctors.remove(doctor) ? doctor : null;
    }

    public List<HealthcareProfessional> getCreated_who() {
        return doctors;
    }

    public void setCreated_who(List<HealthcareProfessional> doctors) {
        this.doctors = doctors;
    }
}
