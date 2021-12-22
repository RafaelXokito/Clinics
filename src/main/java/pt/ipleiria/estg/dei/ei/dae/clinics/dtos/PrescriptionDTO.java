package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionDTO {
    private long id;
    private String healthProfessionalUsername;
    private String healthProfessionalName;
    private String startDate;
    private String endDate;
    private String notes;
    private List<BiometricDataIssueDTO> issues;

    public PrescriptionDTO(long id, String healthProfessionalUsername, String healthProfessionalName, String startDate, String endDate, String notes, List<BiometricDataIssueDTO> issues) {
        this.id = id;
        this.healthProfessionalUsername = healthProfessionalUsername;
        this.healthProfessionalName = healthProfessionalName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.issues = issues;
    }

    public PrescriptionDTO(long id, String healthProfessionalName, String startDate, String endDate) {
        this.id = id;
        this.healthProfessionalName = healthProfessionalName;
        this.startDate = startDate;
        this.endDate = endDate;
        issues = new ArrayList<>();
    }

    public PrescriptionDTO() {
        issues = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHealthProfessionalUsername() {
        return healthProfessionalUsername;
    }

    public void setHealthProfessionalUsername(String healthProfessionalUsername) {
        this.healthProfessionalUsername = healthProfessionalUsername;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getHealthProfessionalName() {
        return healthProfessionalName;
    }

    public void setHealthProfessionalName(String healthProfessionalName) {
        this.healthProfessionalName = healthProfessionalName;
    }
}
