package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;
import pt.ipleiria.estg.dei.ei.dae.clinics.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BiometricDataTypeBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getAllBiometricDataTypes() {
        Query query = entityManager.createQuery("SELECT b.id, b.name, b.unit, b.unit_name, b.min, b.max FROM BiometricDataType b ORDER BY b.id DESC");
        List<Object[]> biometricDataTypeList = query.getResultList();
        return biometricDataTypeList;
    }

    public BiometricDataType findBiometricDataType(long id) throws MyEntityNotFoundException {
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, id);
        if (biometricDataType == null)
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
    public BiometricDataType create(String name, double min, double max, String unit, String unit_name){
        BiometricDataType newBiometricDataType = new BiometricDataType(name, min, max, unit, unit_name);
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
        entityManager.remove(biometricDataType);
        return entityManager.find(BiometricDataType.class, id) == null;
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
    public BiometricDataType update(long id, String name, double min, double max, String unit, String unit_name) throws MyEntityNotFoundException {
        BiometricDataType biometricDataType = findBiometricDataType(id);

        biometricDataType.setName(name);
        biometricDataType.setMin(min);
        biometricDataType.setMax(max);
        biometricDataType.setUnit(unit);
        biometricDataType.setUnit(unit_name);

        return biometricDataType;
    }
}
