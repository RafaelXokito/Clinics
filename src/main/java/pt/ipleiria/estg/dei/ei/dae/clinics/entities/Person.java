package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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

@NamedNativeQuery(
        name="getAllPersons2",
        query = "SELECT p.username,p.email, p.name, p.gender FROM Person",
        resultClass = Person.class
)
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
        try {
            this.password = generateStorngPasswordHash(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        this.name = name;
        this.gender = gender;
    }

    public Person() {
    }

    public Person(String username) {
        this.name = username;
    }

    public static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
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
    }

    public void remove(){
        this.deleted_at = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return username.equals(person.username) && email.equals(person.email) && password.equals(person.password) && name.equals(person.name) && gender.equals(person.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password, name, gender);
    }
}
