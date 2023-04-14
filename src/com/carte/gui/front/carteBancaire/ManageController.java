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

import java.net.URL;
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
                    null,
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
                    AlertUtils.makeError("Error");
                }
            } else {
                carteBancaire.setId(currentCarteBancaire.getId());
                if (CarteBancaireService.getInstance().editForUser(carteBancaire)) {
                    AlertUtils.makeSuccessNotification("CarteBancaire modifié avec succés");
                    ShowAllController.currentCarteBancaire = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_CARTEBANCAIRE);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
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