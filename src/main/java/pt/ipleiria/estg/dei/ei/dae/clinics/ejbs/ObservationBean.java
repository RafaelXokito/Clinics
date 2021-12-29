package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.*;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public long create(long healthcareProfessionalId, long patientId, String notes, String start_date, String end_date, String notesPrescription) throws MyEntityNotFoundException, MyEntityExistsException {
        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, healthcareProfessionalId);
        if (healthcareProfessional == null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + healthcareProfessionalId + "\" does not exist");

        Patient patient = entityManager.find(Patient.class, patientId);
        if (patient == null)
            throw new MyEntityNotFoundException("Patient \"" + patientId + "\" does not exist");

        Observation newObservation = new Observation(healthcareProfessional, patient, notes);
        healthcareProfessional.addObservation(newObservation);
        patient.addObservation(newObservation);

        if (!(Objects.equals(start_date, "") || Objects.equals(end_date, ""))) {
            Prescription prescription = new Prescription(healthcareProfessional, patient, start_date, end_date, notesPrescription);
            healthcareProfessional.addPrescription(prescription);
            newObservation.setPrescription(prescription);
            patient.addPrescription(prescription);

            entityManager.persist(prescription);
        }
        entityManager.persist(newObservation);
        entityManager.flush();

        return newObservation.getId();
    }

    public void update(long id, String notesObservation, String start_date, String end_date, String notesPrescription) throws MyEntityNotFoundException {
        Observation observation = findObservation(id);
        observation.setNotes(notesObservation);

        if (observation.getPrescription() == null) return;

        Prescription prescription = entityManager.find(Prescription.class, observation.getPrescription().getId());
        prescription.setStart_date(start_date);
        prescription.setEnd_date(end_date);
        prescription.setNotes(notesPrescription);
    }

    public boolean delete(long id) throws MyEntityNotFoundException {
        Observation observation = findObservation(id);

        HealthcareProfessional healthcareProfessional = entityManager.find(HealthcareProfessional.class, observation.getHealthcareProfessional().getId());
        if (healthcareProfessional == null)
            throw new MyEntityNotFoundException("Healthcare Professional \"" + observation.getHealthcareProfessional().getId() + "\" does not exist");

        healthcareProfessional.removeObservation(observation);

        Patient patient = entityManager.find(Patient.class, observation.getPatient().getId());
        if (patient == null)
            throw new MyEntityNotFoundException("Patient \"" + observation.getPatient().getId() + "\" does not exist");

        patient.removeObservation(observation);

        entityManager.remove(observation);

        return entityManager.find(HealthcareProfessional.class, id) == null;
    }

}
