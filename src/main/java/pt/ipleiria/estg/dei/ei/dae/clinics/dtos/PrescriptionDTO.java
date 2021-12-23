package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionDTO {
    private long id;
    private long healthcareProfessionalId;
    private String healthcareProfessionalName;
    private String start_date;
    private String end_date;
    private String notes;
    private List<BiometricDataIssueDTO> issues;

    public PrescriptionDTO(long id, long healthcareProfessionalId, String doctorName, String start_date, String end_date, String notes, List<BiometricDataIssueDTO> issues) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.healthcareProfessionalName = doctorName;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;
        this.issues = issues;
    }

    public PrescriptionDTO() {
        id = -1;
        healthcareProfessionalId = -1;
        healthcareProfessionalName = "";
        start_date = "";
        end_date = "";
        notes = "";
        issues = new ArrayList<>();
    }

    public PrescriptionDTO(String healthcareProfessionalName, String start_date, String end_date, String notes) {
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;
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
}
