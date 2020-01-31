package com.biocad.utils;

import com.biocad.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailRuSmtpClient {

    private Logger log = LogManager.getLogger(getClass());

    private User sender;
    private Properties sessionProps;

    private boolean isMailSent = false;

    public MailRuSmtpClient(User sender) {
        this.sender = sender;
        setUpSessionProperties();
    }

    public void sendEmail(String toAddress, String topic, String content) throws MessagingException {
        Session session = Session.getInstance(this.sessionProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender.getEmail(), sender.getPassword());
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender.getEmail()));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(toAddress));
        message.setSubject(topic);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(content, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
        log.debug("Message was sent to {}", toAddress);
        isMailSent = true;
    }

    public boolean isMailSent() {
        return isMailSent;
    }

    private void setUpSessionProperties() {
        sessionProps = new Properties();
        sessionProps.put("mail.smtp.auth", true);
        sessionProps.put("mail.smtp.host", "smtp.mail.ru");
        sessionProps.put("mail.smtp.port", "465");
        sessionProps.put("mail.smtp.sender", sender.getEmail());
        sessionProps.put("mail.smtp.password", sender.getPassword());
        sessionProps.put("mail.smtp.starttls.enable", "true");
        sessionProps.put("mail.smtp.socketFactory.port", "465");
        sessionProps.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        sessionProps.put("mail.smtp.socketFactory.fallback", "false");
        sessionProps.setProperty("mail.smtp.quitwait", "false");
    }
}
