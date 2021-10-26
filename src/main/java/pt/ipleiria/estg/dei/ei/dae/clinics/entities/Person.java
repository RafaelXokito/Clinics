package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "Persons",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllPersons",
                query = "SELECT p FROM Person p WHERE p.deleted_at != NULL ORDER BY p.username"
        )
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {
    @Id
    private String username;
    @Email
    @Nullable
    private String email;
    @NotNull
    private String password; //This should be hashed before input, in or out of the server
    @NotNull
    private String name;
    @NotNull
    private String gender;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleted_at;
    @NotNull
    @OneToMany(mappedBy = "created_by", cascade = CascadeType.PERSIST)
    private List<BiometricData> biometricDatasCreated;

    public Person(String username, String email, String password, String name, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }

    public Person() {
    }


    public BiometricData addBiometricData(BiometricData biometricData){
        if (biometricData != null && !this.biometricDatasCreated.contains(biometricData)) {
            biometricDatasCreated.add(biometricData);
            return biometricData;
        }
        return null;
    }

    public BiometricData removeBiometricData(BiometricData biometricData){
        return biometricData != null && biometricDatasCreated.remove(biometricData) ? biometricData : null;
    }

    public List<BiometricData> getBiometricDataCreated() {
        return biometricDatasCreated;
    }

    public void setBiometricDataCreated(List<BiometricData> biometricDataCreated) {
        this.biometricDatasCreated = biometricDataCreated;
    }

    public String getUsername() {
        return username;
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

    public Date getUpdated_at() {
        return updated_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    @PrePersist
    protected void onCreate() {
        this.created_at = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_at = new Date();
        this.deleted_at = new Date();
    }

    public void remove(){
        this.deleted_at = new Date();
    }
}
