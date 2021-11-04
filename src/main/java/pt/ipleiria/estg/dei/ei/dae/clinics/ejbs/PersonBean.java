package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PersonBean {
    @PersistenceContext
    EntityManager em;

    public Person authenticate(final String Personname, final String password) throws
            Exception {
        Person Person = em.find(Person.class, Personname);
        if (Person != null &&
                Person.getPassword().equals(Person.generateStorngPasswordHash(password))) {
            return Person;
        }
        throw new Exception("Failed logging in with Person username '" + Personname + "':unknown Person username or wrong password");
    }
}
