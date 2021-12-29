package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Document create(long observation_id, String filepath, String filename) throws MyEntityNotFoundException {
        Observation observation = entityManager.find(Observation.class, observation_id);
        if (observation == null)
            throw new MyEntityNotFoundException("Observation \"" + observation + "\" does not exist");

        Document newDocument = new Document(observation, filename, filepath);

        observation.addDocument(newDocument);

        entityManager.persist(newDocument);
        entityManager.flush();

        return newDocument;
    }

    public Document findDocument(long id){
        return entityManager.find(Document.class, id);
    }

    public List<Document> getObservationDocuments(long id){
        return (List<Document>) entityManager.createNamedQuery("getObservationDocuments").setParameter("id",id).getResultList();
    }
}
