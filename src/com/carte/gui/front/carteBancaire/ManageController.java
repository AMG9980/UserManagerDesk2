package com.carte.gui.front.carteBancaire;


import com.carte.entities.CarteBancaire;
import com.carte.entities.Compte;
import com.carte.entities.Type;
import com.carte.gui.front.MainWindowController;
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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManageController implements Initializable {

    @FXML
    public ComboBox<Type> typeCB;
    @FXML
    public ComboBox<Compte> compteCB;
    @FXML
    public TextField nomTF;
    @FXML
    public TextField emailTF;
    @FXML
    public DatePicker dateDP;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;
    @FXML
    public TextField numCarteTF;

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
                emailTF.setText(currentCarteBancaire.getEmail());
                dateDP.setValue(currentCarteBancaire.getDate());
                numCarteTF.setText(String.valueOf(currentCarteBancaire.getNumCarte()));

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter une carte bancaire");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            CarteBancaire carteBancaire = new CarteBancaire(
                    typeCB.getValue(),
                    compteCB.getValue(),
                    nomTF.getText(),
                    Long.parseLong(numCarteTF.getText()),
                    0,
                    emailTF.getText(),
                    dateDP.getValue(),
                    null,
                    null
            );

            if (currentCarteBancaire == null) {
                if (CarteBancaireService.getInstance().addFromUser(carteBancaire)) {
                  
                    AlertUtils.makeSuccessNotification("CarteBancaire ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_CARTEBANCAIRE);
                } else {
                    AlertUtils.makeError("Num carte existe deja");
                }
            } else {
                carteBancaire.setId(currentCarteBancaire.getId());
                if (CarteBancaireService.getInstance().editForUser(carteBancaire)) {
                    AlertUtils.makeSuccessNotification("CarteBancaire modifié avec succés");
                    ShowAllController.currentCarteBancaire = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_CARTEBANCAIRE);
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

        return true;
    }
}