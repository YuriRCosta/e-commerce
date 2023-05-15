package br.com.ecommerce.ecommerce.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class ServiceSendEmail {

    private String userName = "n0xfps1@gmail.com";
    private String senha = "qbcpvhihwvrpgtov";

    @Async
    public void enviarEmailHtml(String assunto, String mensagem, String destinatario) throws UnsupportedEncodingException, MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.auth", "true"); //Autorização
        properties.put("mail.smtp.starttls", "true"); //Autenticação
        properties.put("mail.smtp.host", "smtp.gmail.com"); //Servidor Gmail Google
        properties.put("mail.smtp.port", "465"); //Porta do servidor
        properties.put("mail.smtp.socketFactory.port", "465"); //Expecifica a porta a ser conectada pelo socket
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //Classe socket de conexão ao SMTP

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, senha);
            }
        });

        session.setDebug(true);

        Address[] toUser = InternetAddress.parse(destinatario);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userName, "Yuri - Java", "UTF-8")); //Quem está enviando
        message.setRecipients(Message.RecipientType.TO, toUser); //Email de destino
        message.setSubject(assunto); //Assunto do Email
        message.setContent(mensagem, "text/html; charset=utf-8");

        Transport.send(message);

    }

}
