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
                    AlertUtils.makeSuccessNotification("CarteBancaire ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CARTEBANCAIRE);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                carteBancaire.setId(currentCarteBancaire.getId());
                if (CarteBancaireService.getInstance().edit(carteBancaire)) {
                    AlertUtils.makeSuccessNotification("CarteBancaire modifié avec succés");
                    ShowAllController.currentCarteBancaire = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CARTEBANCAIRE);
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