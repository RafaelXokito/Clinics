package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Date;

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

    public PatientDTO(long id, String email, String password, String name, String gender, Date created_at, Date updated_at, Date deleted_at, int healthNo, long created_by) {
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
    }

    public PatientDTO(int healthNo, String email, String name, String gender) {
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.healthNo = healthNo;
    }

    public PatientDTO(long id, int healthNo, String email, String name, String gender) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.healthNo = healthNo;
    }

    public PatientDTO(long id,String email, String name, String gender, Date created_at, Date updated_at, Date deleted_at, int healthNo, long created_by) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.healthNo = healthNo;
        this.created_by = created_by;
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
    }

    public long getId() {
        return id;
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

    public long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(long created_by) {
        this.created_by = created_by;
    }
}
