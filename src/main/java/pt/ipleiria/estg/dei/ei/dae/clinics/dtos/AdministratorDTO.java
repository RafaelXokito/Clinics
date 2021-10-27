package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Date;

public class AdministratorDTO {
    private String username;
    private String email;
    private String password;
    private String name;
    private String gender;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;

    public AdministratorDTO(String username, String email, String password, String name, String gender, Date created_at, Date updated_at, Date deleted_at) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public AdministratorDTO(String username, String email, String name, String gender, Date created_at, Date updated_at, Date deleted_at) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public AdministratorDTO() {
        username = "";
        email = "";
        password = "";
        name = "";
        gender = "";
        created_at = new Date();
        updated_at = new Date();
        deleted_at = new Date();
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

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getDeletedAt() {
        return deleted_at;
    }

    public void setDeletedAt(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
}
