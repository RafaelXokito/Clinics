package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescriptionDTO {
    private long id;
    private String doctorUsername;
    private String doctorName;
    private String start_date;
    private String end_date;
    private String notes;
    private List<BiometricDataIssueDTO> issues;

    public PrescriptionDTO(long id, String doctorUsername, String doctorName, String start_date, String end_date, String notes, List<BiometricDataIssueDTO> issues) {
        this.id = id;
        this.doctorUsername = doctorUsername;
        this.doctorName = doctorName;
        this.start_date = start_date;
        this.end_date = end_date;
        this.notes = notes;
        this.issues = issues;
    }

    public PrescriptionDTO() {
        id = 0;
        doctorUsername = "";
        doctorName = "";
        start_date = "";
        end_date = "";
        notes = "";
        issues = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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
