package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BiometricDataTypeBean {

    @PersistenceContext
    private EntityManager entityManager;

    /***
     * Execute BiometricDataType types query getting all BiometricDataIssue
     * @return a list of All BiometricDataType as Object Array
     */
    public List<Object[]> getAllBiometricDataTypes() {
        Query query = entityManager.createQuery("SELECT b.id, b.name, b.unit, b.unit_name, b.min, b.max FROM BiometricDataType b ORDER BY b.id DESC");
        List<Object[]> biometricDataTypeList = query.getResultList();
        return biometricDataTypeList;
    }

    /***
     * Execute BiometricDataType query getAllBiometricDataTypes getting all BiometricDataType Class
     * @return a list of All BiometricDataType
     */
    public List<BiometricDataType> getAllBiometricDataTypeClass() {
        return entityManager.createNamedQuery("getAllBiometricDataTypes", BiometricDataType.class).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    /***
     * Execute BiometricDataType query getAllBiometricDataTypesWithTrashed getting all BiometricDataType Class
     * @return a list of All BiometricDataType
     */
    public List<BiometricDataType> getAllBiometricDataTypeClassWithTrashed() {
        return entityManager.createNamedQuery("getAllBiometricDataTypesWithTrashed", BiometricDataType.class).setLockMode(LockModeType.OPTIMISTIC).getResultList();
    }

    /***
     * Find BiometricDataType by given @Id:id
     * @param id @Id to find BiometricDataType
     * @return founded BiometricDataType or Null if dont
     */
    public BiometricDataType findBiometricDataType(long id) throws MyEntityNotFoundException {
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, id);
        if (biometricDataType == null || biometricDataType.getDeleted_at() != null)
            throw new MyEntityNotFoundException("BiometricDataType \"" + id + "\" does not exist");

        return biometricDataType;
    }

    /***
     * Creating Biometric Data Type
     * @param name of the Biometric Data Type ("Temperatura Corporal")
     * @param min value acepted in input (30)
     * @param max value acepted in input (45)
     * @param unit (ºC)
     * @param unit_name (Graus Celsius)
     * @return BiometricDataType created
     */
    public BiometricDataType create(String name, double min, double max, String unit, String unit_name) throws MyIllegalArgumentException {
        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");
        if (unit == null || unit.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"unit\" is required");
        if (unit_name == null || unit_name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"unit_name\" is required");
        if (min > max)
            throw new MyIllegalArgumentException("Field \"max\" must be greater or equal to the field \"min\"");

        BiometricDataType newBiometricDataType = new BiometricDataType(name.trim(), min, max, unit.trim(), unit_name.trim());

        entityManager.persist(newBiometricDataType);
        entityManager.flush();

        return newBiometricDataType;
    }

    /***
     * Delete a Biometric Data Type by given @Id:id
     * @param id @Id to find the proposal delete Biometric Data Type
     * @return Biometric Data Type deleted or null if dont find the Biometric Data Type with @Id:id given
     */
    public boolean delete(long id) throws MyEntityNotFoundException {
        BiometricDataType biometricDataType = findBiometricDataType(id);
        for (BiometricDataIssue b:biometricDataType.getIssues()) {
            b.setDeleted_at();
        }
        biometricDataType.setDeleted_at();
        return true;
        //entityManager.remove(biometricDataType);
        //return entityManager.find(BiometricDataType.class, id) == null;
    }

    /***
     * Update a Biometric Data Issue by given @Id:id
     * @param id @Id to find the proposal update Biometric Data Issue
     * @param name to update Biometric Data Issue
     * @param min to update Biometric Data Issue
     * @param max to update Biometric Data Issue
     * @param unit to update Biometric Data Issue
     * @param unit_name to update Biometric Data Issue
     * @return Biometric Data
     */
    public BiometricDataType update(long id, String name, double min, double max, String unit, String unit_name) throws MyEntityNotFoundException, MyIllegalArgumentException {
        BiometricDataType biometricDataType = findBiometricDataType(id);
        entityManager.lock(biometricDataType, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        if (name == null || name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"name\" is required");
        if (unit == null || unit.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"unit\" is required");
        if (unit_name == null || unit_name.trim().isEmpty())
            throw new MyIllegalArgumentException("Field \"unit_name\" is required");
        if (min > max)
            throw new MyIllegalArgumentException("Field \"max\" must be greater or equal to the field \"min\"");

        biometricDataType.setName(name.trim());
        biometricDataType.setMin(min);
        biometricDataType.setMax(max);
        biometricDataType.setUnit(unit.trim());
        biometricDataType.setUnit_name(unit_name.trim());

        return biometricDataType;
    }

    /***
     * Restore a Biometric Data Type by given @Id:id - Change deleted_at field to null date
     * @param id @Id to find the proposal restore Biometric Data Type
     */
    public boolean restore(long id) throws MyEntityNotFoundException, MyEntityExistsException {
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, id);
        if (biometricDataType == null)
            throw new MyEntityNotFoundException("Biometric Data Type \"" + id + "\" does not exist");
        if (biometricDataType.getDeleted_at() == null)
            throw new MyEntityExistsException("Biometric Data Type \"" + id + "\" already exist");
        biometricDataType.setDeleted_at(null);

        //Para prevenir que todos os issues sejam restaurados, não damos restore em nenhum
        //Um biometric data issue apagado deve ser mantido apagado
        //for (BiometricDataIssue b:biometricDataType.getIssues()) {
        //    b.setDeleted_at(null);
        //}
        return true;
    }
}
