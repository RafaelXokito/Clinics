package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Document create(long observation_id, String filepath, String filename) throws MyEntityNotFoundException, MyIllegalArgumentException {
        Observation observation = entityManager.find(Observation.class, observation_id);
        if (observation == null)
            throw new MyEntityNotFoundException("Observation \"" + observation_id + "\" does not exist");

        if (filepath == null || filepath.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"filepath\" is required");
        if (filename == null || filename.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"filepath\" is required");

        Document newDocument = new Document(observation, filename.trim(), filepath.trim());

        observation.addDocument(newDocument);

        entityManager.persist(newDocument);
        entityManager.flush();

        return newDocument;
    }

    public Document findDocument(long id) throws MyEntityNotFoundException {
        Document document = entityManager.find(Document.class, id);
        if (document == null)
            throw new MyEntityNotFoundException("Document with id " + id + " was not found");

        return document;
    }

    public List<Document> getObservationDocuments(long id){
        return (List<Document>) entityManager.createNamedQuery("getObservationDocuments").setParameter("id",id).getResultList();
    }


    public boolean deleteDocument(Document document) throws MyIllegalArgumentException, MyEntityNotFoundException {
        long observationId = document.getObservation().getId();
        Observation observation = entityManager.find(Observation.class, observationId);
        if (observation == null)
            throw new MyEntityNotFoundException("Observation \"" + observationId + "\" does not exist");

        if (!entityManager.contains(document)) {
            document = entityManager.merge(document);
        }
        entityManager.remove(document);
        entityManager.flush();

        if (observation.removeDocument(document) == null)
            throw new MyIllegalArgumentException("Error removing document from observation");

        return true;
    }
}
