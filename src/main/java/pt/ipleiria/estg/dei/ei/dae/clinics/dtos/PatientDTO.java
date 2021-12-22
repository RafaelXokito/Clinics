package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientDTO {
    private String username;
    private String email;
    private String password;
    private String name;
    private String gender;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
    private int healthNo;
    private String createdByUsername;
    private String createdByName;
    private List<BiometricDataDTO> biometricDatas;
    private List<HealthcareProfessionalDTO> healthcareProfessionals;

    public PatientDTO(String username, String email, String password, String name, String gender, Date created_at, Date updated_at, Date deleted_at, int healthNo, String createdByUsername, String createdByName, List<BiometricDataDTO> biometricDatas, List<HealthcareProfessionalDTO> healthcareProfessionals) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.healthNo = healthNo;
        this.createdByUsername = createdByUsername;
        this.createdByName = createdByName;
        this.biometricDatas = biometricDatas;
        this.healthcareProfessionals = healthcareProfessionals;
    }

    public PatientDTO(String username, int healthNo, String email, String name, String gender) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.healthNo = healthNo;
        biometricDatas = new ArrayList<>();
    }

    public PatientDTO(String username, String email, String name, String gender, int healthNo, String createdByUsername, String createdByName, List<BiometricDataDTO> biometricDatas, List<HealthcareProfessionalDTO> healthcareProfessionals) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.healthNo = healthNo;
        this.createdByUsername = createdByUsername;
        this.createdByName = createdByName;
        this.biometricDatas = biometricDatas;
        this.healthcareProfessionals = healthcareProfessionals;
    }

    public PatientDTO() {
        username = "";
        email = "";
        password = "";
        name = "";
        gender = "";
        created_at = new Date();
        updated_at = new Date();
        deleted_at = new Date();
        healthNo = 0;
        createdByUsername = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getHealthNo() {
        return healthNo;
    }

    public void setHealthNo(int healthNo) {
        this.healthNo = healthNo;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public List<BiometricDataDTO> getBiometricDatas() {
        return biometricDatas;
    }

    public void setBiometricDatas(List<BiometricDataDTO> biometricDatas) {
        this.biometricDatas = biometricDatas;
    }

    public List<HealthcareProfessionalDTO> getHealthcareProfessionals() {
        return healthcareProfessionals;
    }

    public void setHealthcareProfessionals(List<HealthcareProfessionalDTO> healthcareProfessionals) {
        this.healthcareProfessionals = healthcareProfessionals;
    }
}
