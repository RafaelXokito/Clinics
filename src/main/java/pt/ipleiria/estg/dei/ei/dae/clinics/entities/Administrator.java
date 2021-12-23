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
                query = "SELECT a FROM Administrator a ORDER BY a.id"
        )
})
public class Administrator extends Person implements Serializable {

    @NotNull
    @OneToMany(mappedBy = "created_by", cascade = CascadeType.PERSIST)
    private List<HealthcareProfessional> healthcareProfessionals;

    public Administrator(String email, String password, String name, String gender) {
        super(email, password, name, gender);
        this.healthcareProfessionals = new ArrayList<>();
    }

    public Administrator(long id) {
        super(id);
        this.healthcareProfessionals = new ArrayList<>();
    }

    public Administrator() {
        this.healthcareProfessionals = new ArrayList<>();
    }

    public HealthcareProfessional addDoctor(HealthcareProfessional doctor){
        if (doctor != null && !this.healthcareProfessionals.contains(doctor)) {
            healthcareProfessionals.add(doctor);
            return doctor;
        }
        return null;
    }

    public HealthcareProfessional removeDoctor(HealthcareProfessional doctor){
        return doctor != null && healthcareProfessionals.remove(doctor) ? doctor : null;
    }

    public List<HealthcareProfessional> getCreated_who() {
        return healthcareProfessionals;
    }

    public void setCreated_who(List<HealthcareProfessional> doctors) {
        this.healthcareProfessionals = doctors;
    }
}
