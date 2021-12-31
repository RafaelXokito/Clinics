package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyUnauthorizedException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class ObservationBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Observation findObservation(long id) throws MyEntityNotFoundException {
        Observation observation = entityManager.find(Observation.class, id);
        if (observation == null)
            throw new MyEntityNotFoundException("Observation \"" + id + "\" does not exist");

        return observation;
    }

    public long create(long healthcareProfessionalId, long patientId, String notes, String start_date, String end_date, String notesPrescription) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, healthcareProfessionalId);
        if (healthcareProfessional == null || healthcareProfessional.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + healthcareProfessionalId + "\" does not exist");

        Patient patient = entityManager.find(Patient.class, patientId);
        if (patient == null || patient.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Patient \"" + patientId + "\" does not exist");

        boolean hasPrescription = start_date != null || end_date != null || notes != null;

        Observation newObservation = new Observation(healthcareProfessional, patient, notes);
        healthcareProfessional.addObservation(newObservation);
        healthcareProfessional.addPatient(patient);
        patient.addObservation(newObservation);

        if (hasPrescription) {
            if (start_date == null)
                throw new MyIllegalArgumentException("Field \"start_date\" is required");
            if (end_date == null)
                throw new MyIllegalArgumentException("Field \"end_date\" is required");

            if (compareDates(start_date, end_date) >= 0)
                throw new MyIllegalArgumentException("Fields \"start_date\" and \"end_date\" need to have a valid time difference");

            Prescription prescription = new Prescription(healthcareProfessional, patient, start_date.trim(), end_date.trim(), notesPrescription);
            healthcareProfessional.addPrescription(prescription);
            newObservation.setPrescription(prescription);
            patient.addPrescription(prescription);
            prescription.addPatient(patient);

            entityManager.persist(prescription);
        }
        entityManager.persist(newObservation);
        entityManager.flush();

        return newObservation.getId();
    }

    public void update(long id, String notesObservation, String start_date, String end_date, String notesPrescription, long personId) throws MyEntityNotFoundException, MyIllegalArgumentException, MyUnauthorizedException {
        Observation observation = findObservation(id);
        if (observation.getHealthcareProfessional().getId() != personId)
            throw new MyUnauthorizedException("You are not allowed to modify this observation");

        observation.setNotes(notesObservation);

        if (observation.getPrescription() == null) return;

        if (start_date == null)
            throw new MyIllegalArgumentException("Field \"start_date\" is required");
        if (end_date == null)
            throw new MyIllegalArgumentException("Field \"end_date\" is required");

        if (compareDates(start_date.trim(), end_date.trim()) >= 0)
            throw new MyIllegalArgumentException("Fields \"start_date\" and \"end_date\" need to have a valid time difference");

        Prescription prescription = entityManager.find(Prescription.class, observation.getPrescription().getId());
        prescription.setStart_date(start_date.trim());
        prescription.setEnd_date(end_date.trim());
        prescription.setNotes(notesPrescription);
    }

    public boolean delete(long id, long personId) throws MyEntityNotFoundException, MyIllegalArgumentException, MyUnauthorizedException {
        Observation observation = findObservation(id);
        if (observation.getHealthcareProfessional().getId() != personId)
            throw new MyUnauthorizedException("You are not allowed to delete this observation");

        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, observation.getHealthcareProfessional().getId());
        if (healthcareProfessional == null || healthcareProfessional.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + observation.getHealthcareProfessional().getId() + "\" does not exist");

        healthcareProfessional.removeObservation(observation);

        Patient patient = entityManager.find(Patient.class, observation.getPatient().getId());
        if (patient == null || patient.getDeleted_at() != null)
            throw new MyEntityNotFoundException("Patient \"" + observation.getPatient().getId() + "\" does not exist");

        patient.removeObservation(observation);

        entityManager.remove(observation);

        return entityManager.find(HealthcareProfessional.class, id) == null;
    }

    public List<Observation> getAllObservation() {
        return entityManager.createNamedQuery("getAllObservations", Observation.class).getResultList();
    }

    /**
     *
     * @param date1
     * @param date2
     * @return 0 if @date1 is equals @date2, 1 if @d1 is greater then @d2, -1 if @d2 is greater then @d1
     */
    private int compareDates(String date1,String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime d1 = LocalDateTime.parse(date1,formatter);
        LocalDateTime d2 = LocalDateTime.parse(date2,formatter);

        return d1.compareTo(d2); // -1 -> date1 < date2 | 0 -> date1 = date2 | 1 -> date1 > date2
    }
}
