package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Person;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.List;

@Stateless
public class PersonBean {
    @PersistenceContext
    EntityManager em;

    public Person findPerson(String email) {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.email = '" + email + "'",
                Person.class);
        em.flush();
        return query.getResultList().size() > 0 ? query.getSingleResult() : null;
    }

    public Person findPerson(long id) {
        Person person = em.find(Person.class, id);
        return person.getDeleted_at() == null ? person : null;
    }

    public List<Person> getAllPersons() {
        return em.createNamedQuery("getAllPersons", Person.class).getResultList();
    }

    public Person authenticate(final String email, final String password) throws Exception {
        System.out.println("Entrou no auth");

        Person person = findPerson(email);
        Thread.sleep(10);
        if (person != null && Person.validatePassword(password, person.getPassword())) {
            return person;
        }
        throw new Exception(
                "Failed logging in with Person email '" + email + "':unknown Person email or wrong password");
    }

    public Person getPersonByAuthToken(String auth) throws ParseException {
        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                JWT j = JWTParser.parse(auth.substring(7));
                j.getJWTClaimsSet().getClaims();
                Person person = findPerson(Long.parseLong(j.getJWTClaimsSet().getClaims().get("sub").toString()));
                return person;
            } catch (ParseException e) {
                throw e;
            }
        }
        return null;
    }

    /***
     * Update a Person by given @Id:username
     * 
     * @param email  @Id to find the proposal update Person
     * @param name   to update Person
     * @param gender to update Person
     */
    public void update(long id, String email, String name, String gender) throws MyEntityNotFoundException {
        Person person = findPerson(id);

        person.setEmail(email);
        person.setName(name);
        person.setGender(gender);
    }

    public void updatePassword(long id, String oldPassword, String newPassword) throws
            MyIllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {

        Person person = findPerson(id);

        if (!Person.validatePassword(oldPassword, person.getPassword()))
            throw new MyIllegalArgumentException("Password does not match with the old one");

        person.setPassword(newPassword);
    }
}
