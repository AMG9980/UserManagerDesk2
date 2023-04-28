/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

/**
 *
 * @author HP
 */
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    public static void main(String[] args) {
        // Adresse email de l'expéditeur
        String senderEmail = "votre_email@domaine.com";
        // Mot de passe de l'expéditeur
        String senderPassword = "votre_mot_de_passe";
        // Adresse email du destinataire
        String recipientEmail = "email_destinataire@domaine.com";
        // Objet de l'email
        String emailSubject = "Sujet de l'email";
        // Corps de l'email
        String emailBody = "Contenu de l'email";
        
        // Configuration des propriétés du serveur SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        // Création d'une session d'email avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        
        try {
            // Création d'un objet de message MIME
            Message message = new MimeMessage(session);
            // Définition de l'expéditeur
            message.setFrom(new InternetAddress(senderEmail));
            // Définition du destinataire
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            // Définition du sujet
            message.setSubject(emailSubject);
            // Définition du corps de l'email
            message.setText(emailBody);
            // Envoi de l'email
            Transport.send(message);
            System.out.println("Email envoyé avec succès !");
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }

}
