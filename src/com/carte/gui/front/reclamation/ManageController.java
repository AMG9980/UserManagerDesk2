package com.carte.gui.front.reclamation;

import com.carte.MainApp;
import com.carte.entities.Reclamation;
import com.carte.gui.front.MainWindowController;
import com.carte.services.ReclamationService;
import com.carte.utils.AlertUtils;
import com.carte.utils.Constants;
import com.carte.utils.RelationObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.Properties;
import java.util.ResourceBundle;

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