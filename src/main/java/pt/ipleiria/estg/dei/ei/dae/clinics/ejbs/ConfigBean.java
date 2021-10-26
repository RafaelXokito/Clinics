package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    AdministratorBean administratorBean;

    @EJB
    DoctorBean doctorBean;

    @EJB
    PatientBean patientBean;

    @EJB
    BiometricDataTypeBean biometricDataTypeBean;

    @EJB
    BiometricDataIssueBean biometricDataIssueBean;

    @EJB
    BiometricDataBean biometricDataBean;

    @EJB
    PrescriptionBean prescriptionBean;

    // Pay attention to the correct import: import java.util.logging.Logger;
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        try {
            System.out.println("Config Bean - Here we are setting things up to test");

            System.out.println("Creating Some Administrators");
            administratorBean.create("rafael.pereira", "2191266@my.ipleiria.pt", "1234", "Rafael Mendes Pererira","Male");

            System.out.println("Creating Some Doctors");
            doctorBean.create("bruna.leitao", "2191182@my.ipleiria.pt","248", "Bruna Alexandra Marques Leitão", "Female", "Cardiologist", "rafael.pereira");

            System.out.println("Creating Some Patients");
            patientBean.create("daniel.carreira", "219XXXX@my.ipleiria.pt", "1234", "Daniel Carreira", "Male", 123456789, "bruna.leitao");

            System.out.println("Creating some Biometric Data Types");
            long temperaturaCorporalId = biometricDataTypeBean.create("Temperatura Corporal", 30, 45, "ºC (Graus Celsius)");
            long alturaId = biometricDataTypeBean.create("Altura", 0, 3, "m (Metros)");

            System.out.println("Creating some Biometric Data Issues");
            long febreId = biometricDataIssueBean.create("Febre", 38, 45, temperaturaCorporalId);

            long hipotermiaId = biometricDataIssueBean.create("Hipotermia", 30, 35, temperaturaCorporalId);
            biometricDataIssueBean.delete(hipotermiaId);

            System.out.println("Creating some Biometric Data");
            long biometricData1 = biometricDataBean.create(temperaturaCorporalId,39.5,"Paciente com dores no peito.","daniel.carreira", "bruna.leitao");
            long biometricData2 = biometricDataBean.create(alturaId,1.75,"Paciente pálido e alto.","daniel.carreira", "daniel.carreira");

            System.out.println("Creating some Prescriptions");
            long prescription1Id = prescriptionBean.create("bruna.leitao","25/12/2021", "01/01/2022","Para todos os doentes com febre, repousem e tomam ben-u-ron",febreId);


            System.out.println("Updating some Administrators");
        }catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
