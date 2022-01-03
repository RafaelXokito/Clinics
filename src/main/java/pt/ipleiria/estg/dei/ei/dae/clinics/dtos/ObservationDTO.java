package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ObservationDTO {
    private long id;
    private long healthcareProfessionalId;
    private String healthcareProfessionalName;
    private long patientId;
    private String patientName;
    private String notes;
    private Date created_at;
    private PrescriptionDTO prescription;
    private List<DocumentDTO> documents;
    private Date deleted_at;

    private boolean hasPrescription;
    private long nDocuments;

    public ObservationDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, long patientId, String patientName, String notes, Date created_at, PrescriptionDTO prescription, List<DocumentDTO> documents) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.notes = notes;
        this.created_at = created_at;
        this.prescription = prescription;
        this.documents = documents;
        this.nDocuments = documents.size();
    }

    public ObservationDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, long patientId, String patientName, String notes, Date created_at, PrescriptionDTO prescription) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.notes = notes;
        this.created_at = created_at;
        this.prescription = prescription;
        this.documents = new ArrayList<>();
    }

    public ObservationDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, long patientId, String patientName, Date created_at, String notes, PrescriptionDTO prescription, long nDocuments) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.created_at = created_at;
        this.documents = new ArrayList<>();
        this.prescription = prescription;
        this.nDocuments = nDocuments;
        this.notes = notes;
    }

    public ObservationDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, long patientId, String patientName, Date created_at) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.created_at = created_at;
        this.documents = new ArrayList<>();
    }

    public ObservationDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, long patientId, String patientName, Date created_at, Date deleted_at) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
        this.documents = new ArrayList<>();
    }

    public ObservationDTO() {
        this.id = -1;
        this.healthcareProfessionalId = -1;
        this.healthcareProfessionalName = "";
        this.patientId = -1;
        this.patientName = "";
        this.notes = "";
        this.created_at = null;
        this.prescription = new PrescriptionDTO();
        this.documents = new ArrayList<>();
    }

    public List<DocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDTO> documents) {
        this.documents = documents;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHealthcareProfessionalId() {
        return healthcareProfessionalId;
    }

    public void setHealthcareProfessionalId(long healthcareProfessionalId) {
        this.healthcareProfessionalId = healthcareProfessionalId;
    }

    public String getHealthcareProfessionalName() {
        return healthcareProfessionalName;
    }

    public void setHealthcareProfessionalName(String healthcareProfessionalName) {
        this.healthcareProfessionalName = healthcareProfessionalName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public PrescriptionDTO getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionDTO prescription) {
        this.prescription = prescription;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public boolean isHasPrescription() {
        return hasPrescription;
    }

    public void setHasPrescription(boolean hasPrescription) {
        this.hasPrescription = hasPrescription;
    }

    public long getnDocuments() {
        return nDocuments;
    }

    public void setnDocuments(long nDocuments) {
        this.nDocuments = nDocuments;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
}
