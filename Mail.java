package edu.devapps.services;

import java.util.Properties;
import javafx.scene.control.TextField;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
  

/**
 *
 * @author user
 */
public class Mail {
    public static void sendMail(String recepient,int code) throws Exception{
                TwilloService.sendSms("+21629583883", "une dechet est ajouter voir le list de dechet");

        System.out.println("preparing to send email");
        Properties properties =new Properties();
        
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        
        String myAccountEmail="medamine.abidi@esprit.tn";
        String password="AmineAmine12";   //twillo password AmineAminetheoldisback12
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password); //To change body of generated methods, choose Tools | Templates.
            }
            
}); 
        
   
            Message message =new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("dechet");
            
             
message.setContent("<body style='box-sizing: border-box;'><div style='position: relative;max-width: 800px;margin: auto 0;'><div><img style='vertical-align:middle;width:100%;' src='https://www.linkpicture.com/q/thumb-1920-555700.jpg'><div style='font-size: 20px; position:relative;text-align: center;background:black; height: 50  px;'><table style='width: 80%;margin-left: 15%;'><tr><td style='width: 70%;color: rgb(255,249,234); text-align: center;'> une dechet est ajouter merci de voir le liste de dechet </td><td style='font-size: 30px;color: rgb(243,184,68);text-align:left;width: 30%;'></td></tr></table></div><img style='vertical-align:middle;width:100%;' src='https://www.linkpicture.com/q/thumb-1920-555700.jpg'></div></body>"
        
        ,"text/html");
           //message.setText(msg);
            
            
        Transport.send(message);
        System.out.println("message sent succesfully");
    
}

    public static void sendMail(TextField emailv3, int code) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
}