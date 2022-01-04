package pt.ipleiria.estg.dei.ei.dae.clinics.ejbs;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "EmailEJB")
public class EmailBean {

    @Resource(name = "java:/jboss/mail/fakeSMTP")
    private Session mailSession;

    private static final Logger logger = Logger.getLogger("EmailBean.logger");

    /***
     * Send email
     * @param to who will receive email
     * @param subject subject of email
     * @param text body of email
     * @throws MessagingException error sending email
     */
    public void send(String to, String subject, String text) throws MessagingException {
        Thread emailJob = new Thread(() -> {
            Message message = new MimeMessage(mailSession);
            Date timestamp = new Date();
            try {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to, false));
                message.setSubject(subject);
                message.setText(text);
                message.setSentDate(timestamp);
                Transport.send(message);
            } catch (MessagingException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
        emailJob.start();
    }
}
