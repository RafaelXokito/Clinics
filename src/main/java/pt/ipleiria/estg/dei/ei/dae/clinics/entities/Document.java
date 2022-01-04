package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NamedQuery(name = "getObservationDocuments", query = "SELECT doc FROM Document doc WHERE doc.observation.id = :id")

@Entity
@Table(name = "Documents")
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "observation_id") // ID da Observação
    private Observation observation;

    @NotNull
    private String filename;

    @NotNull
    private String filepath;

    @Version
    private int version;

    public Document(Observation observation, String filename, String filepath) {
        this.observation = observation;
        this.filename = filename;
        this.filepath = filepath;
    }

    public Document() {
        this.observation = new Observation();
        this.filename = "";
        this.filepath = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Observation getObservation() {
        return observation;
    }

    public void setObservation(Observation observation) {
        this.observation = observation;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
