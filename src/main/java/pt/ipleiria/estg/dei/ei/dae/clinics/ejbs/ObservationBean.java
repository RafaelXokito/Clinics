package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyUnauthorizedException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class ObservationBean {
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Find Observation by given @Id:id
     * @param id @Id to find Observation
     * @return founded Observation or Null if dont
     */
    public Observation findObservation(long id) throws MyEntityNotFoundException {
        Observation observation = entityManager.find(Observation.class, id);
        if (observation == null || observation.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Observation \"" + id + "\" does not exist");

        return observation;
    }

    /***
     * Creating Observation
     * @param healthcareProfessionalId @Id:id of the HealthcareProfessional
     * @param patientId @Id:id of the Patient
     * @param notes given by HealthcareProfessional, the content of Observation
     * @param start_date that Prescription is starting
     * @param end_date that Prescription is ending
     * @param notesPrescription given by HealthcareProfessional, the content of Prescription
     * @return @Id generated by autoincrement
     *      null if Not a found HealthcareProfessional with this username
     *      null if Not found Patient with this id
     * @throws MyEntityNotFoundException
     * @throws MyEntityExistsException
     * @throws MyIllegalArgumentException
     * @throws MyUnauthorizedException
     */
    public long create(long healthcareProfessionalId, long patientId, String notes, String start_date, String end_date, String notesPrescription) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException, MyUnauthorizedException {
        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, healthcareProfessionalId);
        if (healthcareProfessional == null || healthcareProfessional.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + healthcareProfessionalId + "\" does not exist");

        Patient patient = entityManager.find(Patient.class, patientId, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        if (patient == null || patient.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Patient \"" + patientId + "\" does not exist");

        if (!healthcareProfessional.getPatients().contains(patient))
            throw new MyUnauthorizedException("Patient \"" + patientId + "\" does not belongs to this healthcare professional");

        if (notes == null || notes.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"notes\" of observation is required");

        Observation newObservation = new Observation(healthcareProfessional, patient, notes.trim());
        healthcareProfessional.addObservation(newObservation);
        patient.addObservation(newObservation);

        boolean hasPrescription = notesPrescription != null && !notesPrescription.trim().isEmpty();
        if (hasPrescription) {
            if (start_date == null)
                throw new MyIllegalArgumentException("Field \"start_date\" is required");
            if (compareDates(start_date, LocalDateTime.now().atZone(ZoneId.systemDefault()).minusMinutes(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))) < 0)
                throw new MyIllegalArgumentException("Field \"start_date\" should be higher or equal than the current date " + LocalDateTime.now().atZone(ZoneId.systemDefault()).minusMinutes(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " | "+start_date);
            if (end_date == null)
                throw new MyIllegalArgumentException("Field \"end_date\" is required");
            if (compareDates(start_date, end_date) >= 0)
                throw new MyIllegalArgumentException("Fields \"start_date\" and \"end_date\" need to have a valid time difference");

            Prescription prescription = new Prescription(healthcareProfessional, patient, start_date.trim(), end_date.trim(), notesPrescription.trim());
            healthcareProfessional.addPrescription(prescription);
            newObservation.setPrescription(prescription);
            patient.addPrescription(prescription);
            prescription.addPatient(patient);
            prescription.setObservation(newObservation);

            entityManager.persist(prescription);
        }
        entityManager.persist(newObservation);
        entityManager.flush();

        return newObservation.getId();
    }

    /***
     * Updating Observation
     * @param id @Id to find the proposal delete Observation
     * @param notesObservation given by HealthcareProfessional, the content of Observation
     * @param start_date that Prescription is starting
     * @param end_date that Prescription is ending
     * @param notesPrescription given by HealthcareProfessional, the content of Prescription
     * @param personId who is updating
     * @throws MyEntityNotFoundException
     * @throws MyIllegalArgumentException
     * @throws MyUnauthorizedException
     */
    public void update(long id, String notesObservation, String start_date, String end_date, String notesPrescription, long personId) throws MyEntityNotFoundException, MyIllegalArgumentException, MyUnauthorizedException {
        Observation observation = findObservation(id);
        if (observation.getHealthcareProfessional().getId() != personId)
            throw new MyUnauthorizedException("You are not allowed to modify this observation");

        entityManager.lock(observation, LockModeType.PESSIMISTIC_FORCE_INCREMENT);

        if (notesObservation == null || notesObservation.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"notes\" of observation is required");

        observation.setNotes(notesObservation.trim());

        if (observation.getPrescription() == null) return;

        if (start_date == null)
            throw new MyIllegalArgumentException("Field \"start_date\" is required");
        if (end_date == null)
            throw new MyIllegalArgumentException("Field \"end_date\" is required");
        if (notesPrescription == null || notesPrescription.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"notes\" of prescription is required");

        if (compareDates(start_date.trim(), end_date.trim()) >= 0)
            throw new MyIllegalArgumentException("Fields \"start_date\" and \"end_date\" need to have a valid time difference");

        Prescription prescription = entityManager.find(Prescription.class, observation.getPrescription().getId());
        if (prescription == null || prescription.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Prescription \"" + id + "\" does not exist");

        prescription.setStart_date(start_date.trim());
        prescription.setEnd_date(end_date.trim());
        prescription.setNotes(notesPrescription.trim());
    }

    /***
     * Delete a Observation by given @Id:id
     * @param id Delete a Observation by given @Id:id
     * @param personId who is deleting
     * @return true if deleted, throws if dont
     * @throws MyEntityNotFoundException
     * @throws MyIllegalArgumentException
     * @throws MyUnauthorizedException
     */
    public boolean delete(long id, long personId) throws MyEntityNotFoundException, MyIllegalArgumentException, MyUnauthorizedException {
        Observation observation = findObservation(id);
        if (observation.getHealthcareProfessional().getId() != personId)
            throw new MyUnauthorizedException("You are not allowed to delete this observation");

        entityManager.lock(observation, LockModeType.PESSIMISTIC_WRITE);

        Prescription prescription = observation.getPrescription();
        if (prescription != null)
            prescription.setDeleted_at();

        observation.setDeleted_at();

        return true;
    }

    /***
     * Restore a Observation by given @Id:id - Change deleted_at field to null date
     * @param id @Id to find the proposal restore Observation
     */
    public boolean restore(long id) throws MyEntityNotFoundException, MyEntityExistsException {
        Observation observation = entityManager.find(Observation.class, id);
        if (observation == null)
            throw new MyEntityNotFoundException("Observation \"" + id + "\" does not exist");
        if (observation.getDeleted_at() == null)
            throw new MyEntityExistsException("Observation \"" + id + "\" already exist");
        observation.setDeleted_at(null);

        Prescription prescription = observation.getPrescription();
        if (prescription != null)
            prescription.setDeleted_at();
        return true;
    }

    /***
     * Execute Observation query getAllObservationsByPatient getting all Observation Class by Patient @Id:id
     * @param id Patient @Id:id
     * @return List of Observations based on given Patient @Id:id
     */
    public List<Observation> getAllObservationByPatient(long id) {
        return entityManager.createNamedQuery("getAllObservationsByPatient", Observation.class).setParameter("id", id).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    /***
     * Execute Observation query getAllObservationsByHealthcareProfessional getting all Observation Class by HealthcareProfessioanl @Id:id
     * @param id HealthcareProfessioanl @Id:id
     * @return List of Observations based on given HealthcareProfessioanl @Id:id
     */
    public List<Observation> getAllObservationByHealthcareProfessional(long id) {
        return entityManager.createNamedQuery("getAllObservationsByHealthcareProfessional", Observation.class).setParameter("id", id).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    /**
     *
     * @param date1
     * @param date2
     * @return 0 if @date1 is equals @date2, 1 if @d1 is greater then @d2, -1 if @d2 is greater then @d1
     */
    private int compareDates(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime d1 = LocalDateTime.parse(date1,formatter);
        LocalDateTime d2 = LocalDateTime.parse(date2,formatter);

        return d1.compareTo(d2); // -1 -> date1 < date2 | 0 -> date1 = date2 | 1 -> date1 > date2
    }

}
