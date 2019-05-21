package lab.server.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * MessageSender class is used for sending messages using created gmail account.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.7.0
 */
public class MessageSender {

    private Properties properties;

    /**
     * MessageSender class constructor.
     */
    public MessageSender() {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.connectiontimeout", "1000");
        properties.put("mail.smtp.user", "user");
        properties.put("mail.smtp.password", "password");
    }

    /**
     * Send message to specified recipient.
     *
     * @param email email or recipient.
     * @param text  text of message.
     * @throws MessagingException if message can't be send or connection with smtp server can't be created.
     */
    public void sendMessage(String email, String text) throws MessagingException {
        Session session = Session.getInstance(properties, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(properties.getProperty("mail.smtp.user")));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Пароль для работы с сервисом объектов");
        message.setText(text);
        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", properties.getProperty("mail.smtp.user"), properties.getProperty("mail.smtp.password"));
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

}
