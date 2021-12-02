package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricData;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.Prescription;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    AdministratorBean administratorBean;

    @EJB
    HealthcareProfessionalBean healthcareProfessionalBean;

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

            System.out.println("Creating Some HealthcareProfessionals");
            healthcareProfessionalBean.create("bruna.leitao", "2191182@my.ipleiria.pt","248", "Bruna Alexandra Marques Leitão", "Female", "Cardiologist", "rafael.pereira");

            System.out.println("Creating Some Patients");
            patientBean.create("daniel.carreira", "219XXXX@my.ipleiria.pt", "1234", "Daniel Carreira", "Male", 123456789, "bruna.leitao");

            System.out.println("Creating some Biometric Data Types");
            BiometricDataType temperaturaCorporal = biometricDataTypeBean.create("Temperatura Corporal", 30, 45, "ºC", "(Graus Celsius)");
            BiometricDataType altura = biometricDataTypeBean.create("Altura", 0, 3, "m", "(Metros)");

            System.out.println("Creating some Biometric Data Issues");
            BiometricDataIssue febre = biometricDataIssueBean.create("Febre", 38, 45, temperaturaCorporal.getId());

            System.out.println("Creating some Biometric Data");
            BiometricData biometricData1 = biometricDataBean.create(temperaturaCorporal.getId(),39.5,"Paciente com dores no peito.","daniel.carreira", "bruna.leitao");
            BiometricData biometricData2 = biometricDataBean.create(altura.getId(),1.75,"Paciente pálido e alto.","daniel.carreira", "daniel.carreira");

            BiometricDataIssue hipotermia = biometricDataIssueBean.create("Hipotermia", 30, 35, temperaturaCorporal.getId());
            biometricDataIssueBean.delete(hipotermia.getId());

            System.out.println(febre);
            List<BiometricDataIssue> issues = new ArrayList<BiometricDataIssue>();
            issues.add(febre);
            System.out.println("Creating some Prescriptions");
            Prescription prescription1 = prescriptionBean.create("bruna.leitao","2021-12-25 11:30", "2022-01-01 23:30","Para todos os doentes com febre, repousem e tomam ben-u-ron", issues);

            System.out.println("Updating some Administrators");
            administratorBean.update("rafael.pereira", "2191266@my.ipleiria.pt", "1234", "Rafael Mendes Pererira","Male");
        }catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
