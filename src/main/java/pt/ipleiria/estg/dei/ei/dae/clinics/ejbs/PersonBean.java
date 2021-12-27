package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Person;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.clinics.ws.AuthService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class PersonBean {
    @PersistenceContext
    EntityManager em;

    private static final Logger log = Logger.getLogger(AuthService.class.getName());

    public Person findPerson(String email) {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.email = '" + email + "'",
                Person.class);
        em.flush();
        return query.getResultList().size() > 0 ? query.getSingleResult() : null;

        // System.out.println(em.find(Person.class, personList.get(0)).getEmail());
        // return em.find(Person.class, personList.get(0));
        // for (Object[] person : personList) {
        // if (person[1].toString().equals("Administrator")) {
        // return (Administrator) person[0];
        // } else if (person[1].toString().equals("HealthcareProfessional")) {
        // return (HealthcareProfessional) person[0];
        // } else if (person[1].toString().equals("Patient")) {
        // return new Patient(person[4].toString());
        // }
        // }
    }

    public Person findPerson(long id) {
        return em.find(Person.class, id);
    }

    public Person authenticate(final String email, final String password) throws Exception {
        System.out.println("Entrou no auth");

        Person person = findPerson(email);
        Thread.sleep(10);
        // System.out.println(person.toString());
        // System.out.println("@Id: "+person.getId());
        // System.out.println("@Password: "+person.getPassword());
        // System.out.println("@Input Password: "+password);
        // System.out.println(Person.validatePassword(password,person.getPassword()));
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
                Person person = findPerson(Long.valueOf(j.getJWTClaimsSet().getClaims().get("sub").toString()));
                return person;
                // return personBean.findPerson(j.getJWTClaimsSet().getClaims().get("sub");
                // return Response.ok(j.getJWTClaimsSet().getClaims()).build();
                // Note: nimbusds converts token expiration time to milliseconds
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

    public void updatePassword(long id, String oldPassword, String newPassword) throws MyEntityNotFoundException,
            MyIllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {

        Person person = findPerson(id);

        try {
            if (!Person.validatePassword(oldPassword, person.getPassword()))
                throw new MyIllegalArgumentException("Password does not match with the old one");
        } catch (Exception e) {
            log.warning(e.toString());
            throw e;
        }

        person.setPassword(newPassword);
    }
}
