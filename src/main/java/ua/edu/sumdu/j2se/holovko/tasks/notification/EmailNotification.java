package ua.edu.sumdu.j2se.holovko.tasks.notification;

import com.sun.mail.smtp.SMTPTransport;
import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailNotification implements Notification {
    final static Logger logger = Logger.getLogger(EmailNotification.class);
    private static final String SMTP_SERVER = "";
    private static final String SMTP_PORT = "25";
    private static final String EMAIL_FROM = "";
    private static final String EMAIL_TO = "";
    private static final String EMAIL_SUBJECT = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    @Override
    public void sendNotification(String message) {
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport

        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", SMTP_PORT); // default port 25

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(EMAIL_FROM));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));
            msg.setSubject(EMAIL_SUBJECT);
            msg.setText(message);
            msg.setSentDate(new Date());
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
            t.sendMessage(msg, msg.getAllRecipients());
            logger.info("Отчет об отправке уведомений: " + t.getLastServerResponse());
            t.close();
        } catch (MessagingException e) {
            logger.error("Уведомление не отправлено!");
            e.printStackTrace();
        }
    }
}
