package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "EMPLOYEES")
public abstract class Employee extends Person implements Serializable {
    public Employee(String email, String password, String name, String gender) {
        super(email, password, name, gender);
    }

    public Employee() {
    }
}
