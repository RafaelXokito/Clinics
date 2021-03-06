package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Administrators")
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT a FROM Administrator a WHERE a.deleted_at IS NULL ORDER BY a.id"
        ),
        @NamedQuery(
                name = "getAllAdministratorsWithTrashed",
                query = "SELECT a FROM Administrator a ORDER BY a.id"
        )
})
public class Administrator extends Employee implements Serializable {

    @NotNull
    @OneToMany(mappedBy = "created_by", cascade = CascadeType.PERSIST)
    private List<HealthcareProfessional> healthcareProfessionals;

    public Administrator(String email, String password, String name, String gender, Date birthDate) {
        super(email, password, name, gender, birthDate);
        this.healthcareProfessionals = new ArrayList<>();
    }

    public Administrator() {
        this.healthcareProfessionals = new ArrayList<>();
    }

    public HealthcareProfessional addHealthcareProfessional(HealthcareProfessional healthcareProfessional){
        if (healthcareProfessional != null && !this.healthcareProfessionals.contains(healthcareProfessional)) {
            healthcareProfessionals.add(healthcareProfessional);
            return healthcareProfessional;
        }
        return null;
    }

    public HealthcareProfessional removeHealthcareProfessional(HealthcareProfessional healthcareProfessional){
        return healthcareProfessional != null && healthcareProfessionals.remove(healthcareProfessional) ? healthcareProfessional : null;
    }

    public List<HealthcareProfessional> getCreated_who() {
        return healthcareProfessionals;
    }

    public void setCreated_who(List<HealthcareProfessional> healthcareProfessionals) {
        this.healthcareProfessionals = healthcareProfessionals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Administrator that = (Administrator) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }
}
