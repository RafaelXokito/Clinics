package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricData;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BiometricDataTypeBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<BiometricDataType> getAllBiometricDataTypes() {
        return (List<BiometricDataType>) entityManager.createNamedQuery("getAllBiometricDataTypes").getResultList();
    }

    public BiometricDataType findBiometricDataType(long id) {
        return entityManager.find(BiometricDataType.class, id);
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
    public BiometricDataType create(String name, int min, int max, String unit, String unit_name){

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
    public BiometricDataType delete(long id) {
        entityManager.remove(entityManager.find(BiometricDataType.class,id));
        return entityManager.find(BiometricDataType.class,id);
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
     * @throw TODO - Acrescentar os throws e a descrição
     */
    public BiometricDataType update(long id, String name, int min, int max, String unit, String unit_name){
        BiometricDataType biometricDataType = entityManager.find(BiometricDataType.class, id);
        if (biometricDataType != null) {

                biometricDataType.setName(name);
                biometricDataType.setMin(min);
                biometricDataType.setMax(max);
                biometricDataType.setUnit(unit);
                biometricDataType.setUnit(unit_name);

                return biometricDataType;
        }
        return null; //Not found BiometricData with the given id
    }
}
