package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricData;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataIssue;
import pt.ipleiria.estg.dei.ei.dae.clinics.entities.BiometricDataType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    @EJB
    ObservationBean observationBean;

    @EJB
    private PersonBean personBean;

    // Pay attention to the correct import: import java.util.logging.Logger;
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        try {
            System.out.println("Config Bean - Here we are setting things up to test");

            System.out.println("Creating Some Administrators");
            long aRafaelId = administratorBean.create("2191266@my.ipleiria.pt", "1234", "Rafael Mendes Pererira","Male", getDate(1990, 5, 10));

            long aGasparId = administratorBean.create("2191267@my.ipleiria.pt", "1234", "Gaspar Mendes Pererira","Male", getDate(1982, 2, 14));
            long aLeitaoId = administratorBean.create("2191268@my.ipleiria.pt", "1234", "Leitão Mendes Pererira","Male", getDate(1995, 9, 22));

            System.out.println("Creating Some HealthcareProfessionals");
            long hBrunaId = healthcareProfessionalBean.create("2191182@my.ipleiria.pt", "1234",
                    "Bruna Alexandra Marques Leitão", "Female", "Cardiologist", aRafaelId, getDate(1991, 1, 11));

            long hJoseId = healthcareProfessionalBean.create("jose@mail.pt", "1234",
                    "Jose Artur Bento", "Male", "Cardiologist", aRafaelId, getDate(1988, 3, 2));

            System.out.println("Creating Some Patients");
            long pDanielId = patientBean.create("219XXXX@my.ipleiria.pt", "1234", "Daniel Carreira", "Male", 123456789,
                    hBrunaId, getDate(2000, 6, 12));
            long pLeonelId = patientBean.create("219XXX1@my.ipleiria.pt", "1234", "Leonél Brás", "Male", 123456798,
                    hJoseId, getDate(2001, 7, 2));
            long pAndreiaId = patientBean.create("219XXX2@my.ipleiria.pt", "1234", "Andreia Brás", "Female", 123456799,
                    hBrunaId, getDate(2002, 12, 7));
            long pSilviaId = patientBean.create("219XXX3@my.ipleiria.pt", "1234", "Silvia Brás", "Female", 123456788,
                    hBrunaId, getDate(2004, 11, 19));

            System.out.println("Creating some Biometric Data Types");
            BiometricDataType temperaturaCorporal = biometricDataTypeBean.create("Temperatura Corporal", 30, 45, "ºC",
                    "Graus Celsius");
            BiometricDataType altura = biometricDataTypeBean.create("Altura", 0, 3, "m", "Metros");

            System.out.println("Creating some Biometric Data Issues");
            BiometricDataIssue febre = biometricDataIssueBean.create("Febre", 38, 45, temperaturaCorporal.getId());

            BiometricDataIssue hipotermia = biometricDataIssueBean.create("Hipotermia", 30, 35,
                    temperaturaCorporal.getId());
            BiometricDataIssue issue1 = biometricDataIssueBean.create("Muito Pequeno", 0, 1,
                    altura.getId());
            BiometricDataIssue issue2 = biometricDataIssueBean.create("Pequeno", 1, 1.6,
                    altura.getId());
            BiometricDataIssue issue3 = biometricDataIssueBean.create("Normal", 1.6, 1.75,
                    altura.getId());
            BiometricDataIssue issue4 = biometricDataIssueBean.create("Alto", 1.75, 1.85,
                    altura.getId());
            BiometricDataIssue issue5 = biometricDataIssueBean.create("Muito Alto", 1.85, 3,
                    altura.getId());

            List<BiometricDataIssue> issues = new ArrayList<BiometricDataIssue>();
            issues.add(febre);
            System.out.println("Creating some Prescriptions");
            prescriptionBean.create(hBrunaId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "2022-02-01 23:30",
                    "Para todos os doentes com febre, repousem e tomam ben-u-ron", issues);
            prescriptionBean.create(hBrunaId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "2022-03-10 23:30",
                    "Repousem e tomam ben-u-ron", issues);
            prescriptionBean.create(hBrunaId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "2022-02-02 23:30",
                    "Para todos os doentes com febre", issues);
            prescriptionBean.create(hBrunaId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "2022-02-03 23:30",
                    "Prescrição 1", issues);

            System.out.println("Associate Patients to Healthcare Professionals");
            healthcareProfessionalBean.associatePatient(hBrunaId,pDanielId);
            healthcareProfessionalBean.associatePatient(hBrunaId,pAndreiaId);
            healthcareProfessionalBean.associatePatient(hBrunaId,pSilviaId);
            healthcareProfessionalBean.associatePatient(hJoseId,pLeonelId);

            System.out.println("Creating some Biometric Data");
            biometricDataBean.create(temperaturaCorporal.getId(), 39.5,
                    "Paciente com dores no peito.", pDanielId, hBrunaId, "Exam", new Date());
            biometricDataBean.create(temperaturaCorporal.getId(), 40.5,
                    "Paciente com dores no peito.", pDanielId, hBrunaId, "Exam", new Date());
            biometricDataBean.create(temperaturaCorporal.getId(), 36.3,
                    "Paciente com dores no peito.", pDanielId, hBrunaId, "Exam", new Date());
            biometricDataBean.create(temperaturaCorporal.getId(), 35.9,
                    "Paciente com dores no peito.", pDanielId, hBrunaId, "Exam", new Date());
            biometricDataBean.create(altura.getId(), 1.75, "Paciente pálido e alto.",
                    pDanielId, pDanielId, "Sensor", new Date());
            biometricDataBean.create(altura.getId(), 1.75, "Paciente pálido e alto.",
                    pLeonelId, hJoseId, "Sensor", new Date());

            System.out.println("Creating some Observations");
            observationBean.create(hBrunaId, pAndreiaId, "yesyesyes", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "2022-01-29 11:30", "more notes");
            observationBean.create(hBrunaId, pSilviaId, "yesyesyes", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "2022-01-29 11:30", "more notes");
            observationBean.create(hBrunaId, pDanielId, "yesyesyes", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "2022-01-29 11:30", "more notes1234");
            observationBean.create(hBrunaId, pDanielId, "yesyesyes", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "2022-01-29 11:30", "more notes");
            observationBean.create(hJoseId, pLeonelId, "nice one", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "2022-01-29 11:30", "more notes");

            administratorBean.delete(aGasparId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private Date getDate(int year, int month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
