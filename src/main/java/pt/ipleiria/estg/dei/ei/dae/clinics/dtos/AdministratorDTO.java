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

    public AdministratorDTO(String username, String email, String name, String gender) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.gender = gender;
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
}
