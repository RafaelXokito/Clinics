package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "EMPLOYEES")
public abstract class Employee extends Person implements Serializable {
    public Employee(String email, String password, String name, String gender, Date birthDate) {
        super(email, password, name, gender, birthDate);
    }

    public Employee() {
    }
}
