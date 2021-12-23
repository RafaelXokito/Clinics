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
        issues = new ArrayList<>();
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

    public String getStart_Date() {
        return startDate;
    }

    public void setStart_Date(String startDate) {
        this.startDate = startDate;
    }

    public String getEnd_Date() {
        return endDate;
    }

    public void setEnd_Date(String endDate) {
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
