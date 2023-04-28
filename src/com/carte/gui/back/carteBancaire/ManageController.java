package com.carte.gui.back.carteBancaire;


import com.carte.entities.CarteBancaire;
import com.carte.entities.Compte;
import com.carte.entities.Type;
import com.carte.gui.back.MainWindowController;
import com.carte.services.CarteBancaireService;
import com.carte.services.CompteService;
import com.carte.services.TypeService;
import com.carte.utils.AlertUtils;
import com.carte.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ManageController implements Initializable {

    @FXML
    public ComboBox<Type> typeCB;
    @FXML
    public ComboBox<Compte> compteCB;
    @FXML
    public TextField nomTF;
    @FXML
    public TextField numCarteTF;
    @FXML
    public TextField cvvTF;
    @FXML
    public TextField emailTF;
    @FXML
    public DatePicker dateDP;
    @FXML
    public DatePicker dateExpDP;
    @FXML
    public TextField etatTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    CarteBancaire currentCarteBancaire;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (Type type : TypeService.getInstance().getAll()) {
            typeCB.getItems().add(type);
        }
        for (Compte compte : CompteService.getInstance().getAll()) {
            compteCB.getItems().add(compte);
        }


        currentCarteBancaire = ShowAllController.currentCarteBancaire;

        if (currentCarteBancaire != null) {
            topText.setText("Modifier carte bancaire");
            btnAjout.setText("Modifier");

            try {
                typeCB.setValue(currentCarteBancaire.getType());
                compteCB.setValue(currentCarteBancaire.getCompte());
                nomTF.setText(currentCarteBancaire.getNom());
                numCarteTF.setText(String.valueOf(currentCarteBancaire.getNumCarte()));
                cvvTF.setText(String.valueOf(currentCarteBancaire.getCvv()));
                emailTF.setText(currentCarteBancaire.getEmail());
                dateDP.setValue(currentCarteBancaire.getDate());
                dateExpDP.setValue(currentCarteBancaire.getDateExp());
                etatTF.setText(currentCarteBancaire.getEtat());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter carte bancaire");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent event) {

        if (controleDeSaisie()) {

            CarteBancaire carteBancaire = new CarteBancaire(
                    typeCB.getValue(),
                    compteCB.getValue(),
                    nomTF.getText(),
                    Long.parseLong(numCarteTF.getText()),
                    Integer.parseInt(cvvTF.getText()),
                    emailTF.getText(),
                    dateDP.getValue(),
                    dateExpDP.getValue(),
                    etatTF.getText()
            );

            if (currentCarteBancaire == null) {
                if (CarteBancaireService.getInstance().add(carteBancaire)) {
                       try {
                        sendMail(emailTF.getText());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    AlertUtils.makeSuccessNotification("CarteBancaire ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CARTEBANCAIRE);
                } else {
                    AlertUtils.makeError("Num carte existe deja");
                }
            } else {
                carteBancaire.setId(currentCarteBancaire.getId());
                if (CarteBancaireService.getInstance().edit(carteBancaire)) {
                    AlertUtils.makeSuccessNotification("CarteBancaire modifié avec succés");
                    ShowAllController.currentCarteBancaire = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CARTEBANCAIRE);
                } else {
                    AlertUtils.makeError("Num carte existe deja");
                }
            }

        }
    }

   
   
      public static void sendMail(String recepient) throws Exception {
        System.out.println("Preparing to send email");
        Properties properties = new Properties();

        //Enable authentication
        properties.put("mail.smtp.auth", "true");
        //Set TLS encryption enabled
        properties.put("mail.smtp.starttls.enable", "true");
        //Set SMTP host
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //Set smtp port
        properties.put("mail.smtp.port", "587");

        //Your gmail address
        String myAccountEmail = "pidev.app.esprit@gmail.com";
        //Your gmail password
        String password = "jkemsuddgeptmcsb";

        //Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        //Prepare email message
        Message message = prepareMessage(session, myAccountEmail, recepient);

        //Send mail
        Transport.send(message);
        System.out.println("Message sent successfully");
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Notification");
            String htmlCode = "<h1>Notification</h1> <br/> <h2><b>Nouvelle carte ajouté</b></h2>";
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (MessagingException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private boolean controleDeSaisie() {


        if (typeCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir type");
            return false;
        }


        if (compteCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir compte");
            return false;
        }


        if (nomTF.getText().isEmpty()) {
            AlertUtils.makeInformation("nom ne doit pas etre vide");
            return false;
        }


        if (numCarteTF.getText().isEmpty()) {
            AlertUtils.makeInformation("numCarte ne doit pas etre vide");
            return false;
        }

        if (numCarteTF.getText().length() != 16) {
            AlertUtils.makeInformation("numCarte doit avoir 16 chiffres");
            return false;
        }

        try {
            Long.parseLong(numCarteTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("numCarte doit etre un nombre");
            return false;
        }

        if (cvvTF.getText().isEmpty()) {
            AlertUtils.makeInformation("cvv ne doit pas etre vide");
            return false;
        }

        if (cvvTF.getText().length() != 3) {
            AlertUtils.makeInformation("cvv doit avoir 3 chiffres");
            return false;
        }

        try {
            Integer.parseInt(cvvTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("cvv doit etre un nombre");
            return false;
        }

        if (emailTF.getText().isEmpty()) {
            AlertUtils.makeInformation("email ne doit pas etre vide");
            return false;
        }
        if (!Pattern.compile("^(.+)@(.+)$").matcher(emailTF.getText()).matches()) {
            AlertUtils.makeInformation("Email invalide");
            return false;
        }


        if (dateDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour date");
            return false;
        }


        if (dateExpDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour dateExp");
            return false;
        }


        if (etatTF.getText().isEmpty()) {
            AlertUtils.makeInformation("etat ne doit pas etre vide");
            return false;
        }


        return true;
    }
}