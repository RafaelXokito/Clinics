package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Date;

public class ObservationDTO {
    private long id;
    private long healthcareProfessionalId;
    private String healthcareProfessionalName;
    private long patientId;
    private String patientName;
    private String notes;
    private Date created_at;
    private PrescriptionDTO prescription;

    public ObservationDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, long patientId, String patientName, String notes, Date created_at, PrescriptionDTO prescription) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.notes = notes;
        this.created_at = created_at;
        this.prescription = prescription;
    }

    public ObservationDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, long patientId, String patientName, Date created_at) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.created_at = created_at;
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
}
