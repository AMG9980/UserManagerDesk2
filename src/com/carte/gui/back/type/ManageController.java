package com.carte.gui.back.type;


import com.carte.entities.Type;
import com.carte.gui.back.MainWindowController;
import com.carte.services.TypeService;
import com.carte.utils.AlertUtils;
import com.carte.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField nomTF;
    @FXML
    public TextField descriptionTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Type currentType;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentType = ShowAllController.currentType;

        if (currentType != null) {
            topText.setText("Modifier type");
            btnAjout.setText("Modifier");

            try {
                nomTF.setText(currentType.getNom());
                descriptionTF.setText(currentType.getDescription());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter type");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent event) {

        if (controleDeSaisie()) {

            Type type = new Type(
                    nomTF.getText(),
                    descriptionTF.getText()
            );

            if (currentType == null) {
                if (TypeService.getInstance().add(type)) {
                    AlertUtils.makeSuccessNotification("Type ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_TYPE);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                type.setId(currentType.getId());
                if (TypeService.getInstance().edit(type)) {
                    AlertUtils.makeSuccessNotification("Type modifié avec succés");
                    ShowAllController.currentType = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_TYPE);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (nomTF.getText().isEmpty()) {
            AlertUtils.makeInformation("nom ne doit pas etre vide");
            return false;
        }


        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("description ne doit pas etre vide");
            return false;
        }


        return true;
    }
}