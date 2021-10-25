package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Administrators")
public class Administrator extends Person implements Serializable {

    @NotNull
    @OneToMany(mappedBy = "created_by", cascade = CascadeType.PERSIST)
    private List<Doctor> created_who;

    public Administrator(String username, String email, String password, String name, String gender, List<Doctor> created_who) {
        super(username, email, password, name, gender);
        this.created_who = new ArrayList<>();
    }

    public Administrator() {
    }

    public List<Doctor> getCreated_who() {
        return created_who;
    }

    public void setCreated_who(List<Doctor> created_who) {
        this.created_who = created_who;
    }
}
