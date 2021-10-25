package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Biometric_Data;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Biometric_Data_Type;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Patient;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class BiometricDataBean {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(long biometric_data_type, double value, String notes, String username){
        Biometric_Data_Type biometricDataType = entityManager.find(Biometric_Data_Type.class, biometric_data_type);
        if (biometricDataType != null) {
            Patient patient = entityManager.find(Patient.class,username);
            if (patient != null){
                if (value < biometricDataType.getMin() || value > biometricDataType.getMax())
                    return -3; //value out of bouds for limits in Biometric_Data_Type

                Biometric_Data biometricData = new Biometric_Data(biometricDataType,value,notes,patient);
                entityManager.persist(biometricData);
                entityManager.flush();
                return biometricData.getId();
            }
            return -2; //Not found Patient with this username
        }
        return -1; //Not found Biometric_Data_Type with this id
    }

}
