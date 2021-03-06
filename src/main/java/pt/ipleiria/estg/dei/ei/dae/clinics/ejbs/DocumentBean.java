package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentBean {
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Creating Document
     * @param observation_id @Id:id of the Observation
     * @param filepath file path where file was gonna be stored
     * @param filename file name
     * @return Document created
     */
    public Document create(long observation_id, String filepath, String filename) throws MyEntityNotFoundException, MyIllegalArgumentException {
        Observation observation = entityManager.find(Observation.class, observation_id);

        if (observation == null || observation.getDeleted_at() != null)
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

    /***
     * Find Document by given @Id:id
     * @param id @Id to find Document
     * @return founded Document or Null if dont
     */
    public Document findDocument(long id) throws MyEntityNotFoundException {
        Document document = entityManager.find(Document.class, id);
        if (document == null)
            throw new MyEntityNotFoundException("Document with id " + id + " was not found");

        return document;
    }

    /***
     * Execute Document query getObservationDocuments getting all Document Class by Observation @Id:id
     * @param id Observation @Id:id
     * @return List of documents based on given Observation @Id:id
     */
    public List<Document> getObservationDocuments(long id){
        return (List<Document>) entityManager.createNamedQuery("getObservationDocuments").setParameter("id",id).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    /***
     * Delete a Document by given @Id:id
     * @param document Document to be removed
     * @return true or exceptions
     */
    public boolean deleteDocument(Document document) throws MyIllegalArgumentException, MyEntityNotFoundException {
        long observationId = document.getObservation().getId();
        Observation observation = entityManager.find(Observation.class, observationId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        if (observation == null || observation.getDeleted_at() != null)
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
