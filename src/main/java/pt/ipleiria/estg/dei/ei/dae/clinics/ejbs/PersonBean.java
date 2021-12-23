package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.HealthcareProfessional;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Person;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PersonBean {
    @PersistenceContext
    EntityManager em;

    public Person findPerson(String email) {
        Query query = em.createQuery("SELECT p.id as Id, p.email as Email, p.password as Password FROM Person p WHERE p.email = '"+ email+"'");
        List<Object[]> personList = (List<Object[]>) query.getResultList();
        for (Object[] person:personList) {
            Person person1 = em.find(Person.class,Long.valueOf(person[0].toString()));
            person1.setEmail(person[1].toString());
            person1.setPassword(person[2].toString());
            return person1;
        }
        return null;
        //System.out.println(em.find(Person.class, personList.get(0)).getEmail());
        //return em.find(Person.class, personList.get(0));
        //for (Object[] person : personList) {
        //    if (person[1].toString().equals("Administrator")) {
        //        return (Administrator) person[0];
        //    } else if (person[1].toString().equals("HealthcareProfessional")) {
        //        return (HealthcareProfessional) person[0];
        //     } else if (person[1].toString().equals("Patient")) {
        //        return new Patient(person[4].toString());
        //    }
        //}
    }

    public Person findPerson(long id) {
        return em.find(Person.class, id);
    }

    public Person authenticate(final String email, final String password) throws
            Exception {
        System.out.println("Entrou no auth");

        Person person = findPerson(email);
        Thread.sleep(10);
        //System.out.println(person.toString());
        //System.out.println("@Id: "+person.getId());
        //System.out.println("@Password: "+person.getPassword());
        //System.out.println("@Input Password: "+password);
        //System.out.println(Person.validatePassword(password,person.getPassword()));
        if (person != null && Person.validatePassword(password,person.getPassword())) {
            return person;
        }
        throw new Exception("Failed logging in with Person email '" + email + "':unknown Person email or wrong password");
    }
}
