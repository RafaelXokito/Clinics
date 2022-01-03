package pt.ipleiria.estg.dei.ei.dae.clinics.entities;

import io.smallrye.common.constraint.NotNull;
import org.eclipse.persistence.annotations.AdditionalCriteria;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "Persons",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllPersons",
                query = "SELECT p FROM Person p ORDER BY p.id"
        ),
        @NamedQuery(
                name = "getPersonByEmail",
                query = "SELECT p FROM Person p WHERE p.deleted_at != NULL and p.email = :email"
        )
})
@Inheritance(strategy = InheritanceType.JOINED)
@AdditionalCriteria("this.deleted_at is null")
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    private String email;

    @NotNull
    private String password; //This should be hashed before input, in or out of the server

    @NotNull
    private String name;

    @NotNull
    private String gender;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birth_date")
    private Date birthDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleted_at;

    @NotNull
    @OneToMany(mappedBy = "created_by", cascade = CascadeType.PERSIST)
    private List<BiometricData> biometricDatasCreated;

    public Person(String email, String password, String name, String gender, Date birthDate) {
        this.email = email;
        try {
            this.password = generateStrongPasswordHash(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public Person() {
    }

    public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public static boolean validatePassword(String originalPassword, String storedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public BiometricData addBiometricData(BiometricData biometricData) {
        if (biometricData != null && !this.biometricDatasCreated.contains(biometricData)) {
            biometricDatasCreated.add(biometricData);
            return biometricData;
        }
        return null;
    }

    public BiometricData removeBiometricData(BiometricData biometricData) {
        return biometricData != null && biometricDatasCreated.remove(biometricData) ? biometricData : null;
    }

    public List<BiometricData> getBiometricDataCreated() {
        return biometricDatasCreated;
    }

    public void setBiometricDataCreated(List<BiometricData> biometricDataCreated) {
        this.biometricDatasCreated = biometricDataCreated;
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
        try {
            this.password = generateStrongPasswordHash(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
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

    public void setDeleted_at() {
        this.deleted_at = new Date();
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @PrePersist
    protected void onCreate() {
        this.created_at = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_at = new Date();
    }

    @PreRemove
    protected void onRemove() {
        this.deleted_at = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return this.id == person.id && this.email.equals(person.email) && this.password.equals(person.password) && this.name.equals(person.name) && this.gender.equals(person.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, gender);
    }
}
