package com.pidev.gui.front.reclamation;

import com.pidev.MainApp;
import com.pidev.entities.Reclamation;
import com.pidev.gui.front.MainWindowController;
import com.pidev.services.ReclamationService;
import com.pidev.utils.AlertUtils;
import com.pidev.utils.Constants;
import com.pidev.utils.RelationObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.scene.text.Text;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManageController implements Initializable {

    @FXML
    public ComboBox<RelationObject> userCB;
    @FXML
    public TextField titreTF;
    @FXML
    public TextField descriptionTF;
    @FXML
    public ImageView imageIV;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Reclamation currentReclamation;
    Path selectedImagePath;
    boolean imageEdited;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        for (RelationObject user : ReclamationService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }
        
        currentReclamation = ShowAllController.currentReclamation;

        if (currentReclamation != null) {
            topText.setText("Modifier reclamation");
            btnAjout.setText("Modifier");
            
            try {
                userCB.setValue(currentReclamation.getUser());
                titreTF.setText(currentReclamation.getTitre());
                descriptionTF.setText(currentReclamation.getDescription());
                selectedImagePath = FileSystems.getDefault().getPath(currentReclamation.getImage());
                if (selectedImagePath.toFile().exists()) {
                    imageIV.setImage(new Image(selectedImagePath.toUri().toString()));
                }
                
            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter reclamation");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent event) {

        if (controleDeSaisie()) {
            
            String imagePath;
            if (imageEdited) {
                imagePath = currentReclamation.getImage();
            } else {
                createImageFile();
                imagePath = selectedImagePath.toString();
            }
            
            Reclamation reclamation = new Reclamation(
                userCB.getValue(),
                titreTF.getText(),
                descriptionTF.getText(),
                imagePath
            );

            if (currentReclamation == null) {
                try {
                    sendMail(reclamation.getUser().getName());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (ReclamationService.getInstance().add(reclamation)) {
                     AlertUtils.makeSuccessNotification("Reclamation ajouté avec succés");

                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.makeError("Erreur");
                }
            } else {
                reclamation.setId(currentReclamation.getId());
                if (ReclamationService.getInstance().edit(reclamation)) {
                      AlertUtils.makeSuccessNotification("Reclamation modifié avec succés");
                    ShowAllController.currentReclamation = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.makeError("Erreur");
                }
            }
            
            if (selectedImagePath != null) {
                createImageFile();
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
        if (message != null){
            Transport.send(message);
            System.out.println("Mail sent successfully");
        } else {
            System.out.println("Error sending the mail");
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Notification");
            String htmlCode = "<h1>Notification</h1> <br/> <h2><b>Réclamation ajouté avec succes</b></h2>";
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @FXML
    public void chooseImage(ActionEvent actionEvent) {

        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(MainApp.mainStage);
        if (file != null) {
            selectedImagePath = Paths.get(file.getPath());
            imageIV.setImage(new Image(file.toURI().toString()));
        }
    }

    public void createImageFile() {
        try {
            Path newPath = FileSystems.getDefault().getPath("src/com/pidev/images/uploads/" + selectedImagePath.getFileName());
            Files.copy(selectedImagePath, newPath, StandardCopyOption.REPLACE_EXISTING);
            selectedImagePath = newPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean controleDeSaisie() {
        
        
        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir user");
            return false;
        }
        
        
        if (titreTF.getText().isEmpty()) {
            AlertUtils.makeInformation("titre ne doit pas etre vide");
            return false;
        }
        
        
        
        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("description ne doit pas etre vide");
            return false;
        }
        
        
        
        if (selectedImagePath == null) {
            AlertUtils.makeInformation("Veuillez choisir une image");
            return false;
        }
        
        
        return true;
    }
}