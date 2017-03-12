package com.peaceful.apm.alert.helper;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * @author WangJun
 * @version 1.0 16/6/18
 */
public class MailHelper {


    private static String username;
    private static String password;
    private static String mailHost;
    private static Properties props = new Properties();
    private static final Logger LOGGER = LoggerFactory.getLogger(MailHelper.class);

    static {
        props.put("mail.smtp.auth", AlertApplication.getConfigContext().getString("mail.smtp.auth"));
        props.put("mail.smtp.host", AlertApplication.getConfigContext().getString("mail.smtp.host"));
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.debug", "false");
        username = AlertApplication.getConfigContext().getString("mail.user");
        password = AlertApplication.getConfigContext().getString("mail.password");
        mailHost = AlertApplication.getConfigContext().getString("mail.smtp.host");
    }

    /**
     * @param receivers   多个user用‘,’分割
     * @param subject
     * @param content
     */
    public static void send(String receivers, String subject, String content) {
        Session session = Session.getDefaultInstance(props);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setSubject(subject);
            mimeMessage.setFrom(username);
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receivers));
            Multipart multipart = new MimeMultipart();
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(content, "text/html;charset=GBK");
            multipart.addBodyPart(bodyPart);
            mimeMessage.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(mailHost, username, password);
            transport.sendMessage(mimeMessage, InternetAddress.parse(receivers));
            transport.close();
        } catch (MessagingException e) {
            LOGGER.error("mail send fail: receiver->{} \ncontent->{}", receivers, content);
            Throwables.propagate(e);
        }
    }

}
