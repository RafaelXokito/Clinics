package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import org.jboss.resteasy.spi.touri.MappedBy;
import pt.ipleiria.estg.dei.ei.dae.clinics.dtos.DocumentDTO;

import javax.persistence.*;
import javax.print.Doc;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "OBSERVATIONS")
@NamedQueries({
        @NamedQuery(name = "getAllObservations", query = "SELECT o FROM Observation o WHERE o.deleted_at IS NULL ORDER BY o.id"),
        @NamedQuery(name = "getAllObservationsWithTrashed", query = "SELECT o FROM Observation o ORDER BY o.id"),
        @NamedQuery(name= "getAllObservationsByPatient", query = "SELECT o FROM Observation o WHERE o.deleted_at IS NULL AND o.patient.id = :id ORDER BY o.id"),
        @NamedQuery(name= "getAllObservationsByHealthcareProfessional", query = "SELECT o FROM Observation o WHERE o.healthcareProfessional.id = :id ORDER BY o.id")
})
public class Observation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "HEALTHCAREPROFESSIONAL_ID")
    @NotNull
    private HealthcareProfessional healthcareProfessional;

    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    @NotNull
    private Patient patient;

    private String notes;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleted_at;

    @OneToOne
    private Prescription prescription;

    @OneToMany(mappedBy = "observation", cascade = CascadeType.REMOVE)
    private List<Document> documents;

    @Version
    private int version;

    public Observation(HealthcareProfessional healthcareProfessional, Patient patient, String notes, Prescription prescription) {
        this.healthcareProfessional = healthcareProfessional;
        this.patient = patient;
        this.notes = notes;
        this.created_at = new Date();
        this.prescription = prescription;
        this.documents = new ArrayList<>();
    }

    public Observation(HealthcareProfessional healthcareProfessional, Patient patient, String notes) {
        this.healthcareProfessional = healthcareProfessional;
        this.patient = patient;
        this.notes = notes;
        this.created_at = new Date();
        this.prescription = null;
        this.documents = new ArrayList<>();
    }

    public Observation() {
        this.documents = new ArrayList<>();
    }

    public Document addDocument(Document document){
        if (this.documents.contains(document))
            return null;

        this.documents.add(document);
        return document;
    }

    public Document removeDocument(Document document){
        if (document == null)
            return null;

        documents.remove(document);
        return documents.contains(document) ? null : document;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HealthcareProfessional getHealthcareProfessional() {
        return healthcareProfessional;
    }

    public void setHealthcareProfessional(HealthcareProfessional healthcareProfessional) {
        this.healthcareProfessional = healthcareProfessional;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at() {
        this.deleted_at = new Date();
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Observation that = (Observation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
