package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientDTO {
    private long id;
    private String email;
    private String password;
    private String name;
    private String gender;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
    private int healthNo;
    private long created_by;
    private List<BiometricDataDTO> biometricDatas;
    private List<ObservationDTO> observations;
    private Date birthDate;

    public PatientDTO(long id, String email, String password, String name, String gender, Date created_at,
            Date updated_at, Date deleted_at, int healthNo, long created_by, List<BiometricDataDTO> biometricDatas,
            List<ObservationDTO> observations) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.healthNo = healthNo;
        this.created_by = created_by;
        this.biometricDatas = biometricDatas;
        this.observations = observations;
    }

    public PatientDTO(long id, String email, String name, String gender, int healthNo) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.healthNo = healthNo;
        biometricDatas = new ArrayList<>();
        observations = new ArrayList<>();
    }

    public PatientDTO(long id, String email, String name, String gender, int healthNo, Date deleted_at) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.healthNo = healthNo;
        this.deleted_at = deleted_at;
        biometricDatas = new ArrayList<>();
        observations = new ArrayList<>();
    }

    public PatientDTO(long id, String name) {
        this.id = id;
        this.name = name;
        biometricDatas = new ArrayList<>();
        observations = new ArrayList<>();
    }



    public PatientDTO(long id, String email, String name, String gender, int healthNo, long created_by,
                      List<BiometricDataDTO> biometricDatas, List<ObservationDTO> observations, Date birthDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.healthNo = healthNo;
        this.created_by = created_by;
        this.biometricDatas = biometricDatas;
        this.observations = observations;
        this.birthDate = birthDate;
    }



    public PatientDTO(long id, String email, String name, String gender, Date created_at, Date updated_at,
            Date deleted_at, int healthNo, long created_by, List<BiometricDataDTO> biometricDatas,
            List<ObservationDTO> observations) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.healthNo = healthNo;
        this.created_by = created_by;
        this.biometricDatas = biometricDatas;
        this.observations = observations;
    }

    public PatientDTO() {
        email = "";
        password = "";
        name = "";
        gender = "";
        created_at = new Date();
        updated_at = new Date();
        deleted_at = new Date();
        healthNo = 0;
        created_by = -1;
        biometricDatas = new ArrayList<>();
        observations = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<BiometricDataDTO> getBiometricDatas() {
        return biometricDatas;
    }

    public void setBiometricDatas(List<BiometricDataDTO> biometricDatas) {
        this.biometricDatas = biometricDatas;
    }

    public List<ObservationDTO> getObservations() {
        return observations;
    }

    public void setObservations(List<ObservationDTO> observations) {
        this.observations = observations;
    }

    public long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(long created_by) {
        this.created_by = created_by;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
