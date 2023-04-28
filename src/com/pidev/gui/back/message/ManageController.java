package com.pidev.gui.back.message;


import com.pidev.entities.Message;
import com.pidev.gui.back.MainWindowController;
import com.pidev.services.MessageService;
import com.pidev.utils.AlertUtils;
import com.pidev.utils.Constants;
import com.pidev.utils.RelationObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.text.Text;


import java.net.URL;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManageController implements Initializable {

    @FXML
    public ComboBox<RelationObject> userCB;
    @FXML
    public TextField descriptionTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Message currentMessage;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        for (RelationObject user : MessageService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }
        
        currentMessage = ShowAllController.currentMessage;

        if (currentMessage != null) {
            topText.setText("Modifier message");
            btnAjout.setText("Modifier");
            
            try {
                userCB.setValue(currentMessage.getUser());
                descriptionTF.setText(currentMessage.getDescription());
                
            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter message");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent event) {

        if (controleDeSaisie()) {
            
            Message message = new Message(
                userCB.getValue(),
                descriptionTF.getText()
            );

            if (currentMessage == null) {
                if (MessageService.getInstance().add(message)) {
                     AlertUtils.makeSuccessNotification("Message ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_MESSAGE);
                } else {
                    AlertUtils.makeError("Erreur");
                }
            } else {
                message.setId(currentMessage.getId());
                if (MessageService.getInstance().edit(message)) {
                      AlertUtils.makeSuccessNotification("Message modifié avec succés");
                    ShowAllController.currentMessage = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_MESSAGE);
                } else {
                    AlertUtils.makeError("Erreur");
                }
            }
            
        }
    }

    

    private boolean controleDeSaisie() {
        
        
        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir user");
            return false;
        }
        
        
        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("description ne doit pas etre vide");
            return false;
        }
        
        
        
        return true;
    }
}