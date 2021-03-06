package pt.ipleiria.estg.dei.ei.dae.clinics.dtos;

import java.util.Date;

public class PersonDTO {
    private long id;
    private String email;
    private String password;
    private String name;
    private String gender;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
    private String scope;
    private int healthNo;
    private String specialty;

    public PersonDTO(long id,String email, String name, String gender, Date created_at, Date updated_at, Date deleted_at, String role, String specialty) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.scope = role;
        this.specialty = specialty;
    }

    public PersonDTO(long id,String email, String name, String gender, Date created_at, Date updated_at, Date deleted_at, String role, int healthNo) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.scope = role;
        this.healthNo = healthNo;
    }

    public PersonDTO(long id, String email, String password, String name, String gender, Date created_at, Date updated_at, Date deleted_at, String scope) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.scope = scope;
    }

    public PersonDTO(long id, String email, String password, String name, String gender, Date created_at, Date updated_at, Date deleted_at) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public PersonDTO(long id,String email, String name, String gender, Date created_at, Date updated_at, Date deleted_at, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.scope = role;
    }

    public PersonDTO() {
        email = "";
        password = "";
        name = "";
        gender = "";
        scope = "";
        created_at = new Date();
        updated_at = new Date();
        deleted_at = new Date();
    }

    public PersonDTO(String email, String name, String gender) {
        this.email = email;
        this.name = name;
        this.gender = gender;
    }

    public int getHealthNo() {
        return healthNo;
    }

    public void setHealthNo(int healthNo) {
        this.healthNo = healthNo;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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
}
