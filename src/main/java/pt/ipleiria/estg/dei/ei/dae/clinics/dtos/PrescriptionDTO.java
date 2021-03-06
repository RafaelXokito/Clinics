package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescriptionDTO {
    private long id;
    private long healthcareProfessionalId;
    private String healthcareProfessionalName;
    private String start_date;
    private String end_date;
    private String notes;
    private List<PatientDTO> patients;
    private List<BiometricDataIssueDTO> issues;
    private Date created_at;
    private Date deleted_at;
    private Boolean isGlobal;

    public PrescriptionDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, String start_date, String end_date, String notes, List<PatientDTO> patients, List<BiometricDataIssueDTO> issues, Date created_at, Date deleted_at) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;
        this.patients = patients;
        this.issues = issues;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
    }

    public PrescriptionDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, String start_date,
                           String end_date, String notes, List<BiometricDataIssueDTO> issues) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;
        this.issues = issues;
    }

    public PrescriptionDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, List<PatientDTO> patients, String start_date, String end_date, String notes) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.patients = patients;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;
    }

    public PrescriptionDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, String start_date, String end_date, String notes, Date created_at, Date deleted_at, Boolean isGlobal) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
        this.isGlobal = isGlobal;
    }

    public PrescriptionDTO() {
        id = -1;
        healthcareProfessionalId = -1;
        healthcareProfessionalName = "";
        patients = new ArrayList<>();
        start_date = "";
        end_date = "";
        notes = "";
        issues = new ArrayList<>();
    }

    public PrescriptionDTO(long id, long healthcareProfessionalId, String healthcareProfessionalName, String start_date, String end_date) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.start_date = start_date;
        this.end_date = end_date;
        issues = new ArrayList<>();
        patients = new ArrayList<>();
    }

    public PrescriptionDTO(long id, String healthcareProfessionalName, String start_date, String end_date, String notes) {
        this.id = id;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;
        issues = new ArrayList<>();
        patients = new ArrayList<>();
    }

    public PrescriptionDTO(long id, String healthcareProfessionalName, String start_date, String end_date, Boolean isGlobal) {
        this.id = id;
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.start_date = start_date;
        this.end_date = end_date;
        this.isGlobal = isGlobal;
        issues = new ArrayList<>();
        patients = new ArrayList<>();
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<BiometricDataIssueDTO> getIssues() {
        return issues;
    }

    public void setIssues(List<BiometricDataIssueDTO> issues) {
        this.issues = issues;
    }

    public List<PatientDTO> getPatients() {
        return patients;
    }

    public void setPatients(List<PatientDTO> patients) {
        this.patients = patients;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean global) {
        isGlobal = global;
    }
}
