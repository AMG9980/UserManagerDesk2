package com.carte.gui.front.compte;


import com.carte.entities.Compte;
import com.carte.gui.front.MainWindowController;
import com.carte.services.CompteService;
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
    public TextField nameTF;
    @FXML
    public TextField balanceTF;
    @FXML
    public TextField etatTF;
    @FXML
    public TextField numeroCompteTF;
    @FXML
    public TextField ribTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Compte currentCompte;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentCompte = ShowAllController.currentCompte;

        if (currentCompte != null) {
            topText.setText("Modifier compte");
            btnAjout.setText("Modifier");

            try {
                nameTF.setText(currentCompte.getName());
                balanceTF.setText(String.valueOf(currentCompte.getBalance()));
                etatTF.setText(currentCompte.getEtat());
                numeroCompteTF.setText(String.valueOf(currentCompte.getNumeroCompte()));
                ribTF.setText(String.valueOf(currentCompte.getRib()));

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter compte");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent event) {

        if (controleDeSaisie()) {

            Compte compte = new Compte(
                    nameTF.getText(),
                    Float.parseFloat(balanceTF.getText()),
                    etatTF.getText(),
                    Long.parseLong(numeroCompteTF.getText()),
                    Long.parseLong(ribTF.getText())
            );

            if (currentCompte == null) {
                if (CompteService.getInstance().add(compte)) {
                    AlertUtils.makeInformation("Compte ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_COMPTE);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                compte.setId(currentCompte.getId());
                if (CompteService.getInstance().edit(compte)) {
                    AlertUtils.makeInformation("Compte modifié avec succés");
                    ShowAllController.currentCompte = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_COMPTE);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (nameTF.getText().isEmpty()) {
            AlertUtils.makeInformation("name ne doit pas etre vide");
            return false;
        }


        if (balanceTF.getText().isEmpty()) {
            AlertUtils.makeInformation("balance ne doit pas etre vide");
            return false;
        }


        try {
            Float.parseFloat(balanceTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("balance doit etre un réel");
            return false;
        }
        if (etatTF.getText().isEmpty()) {
            AlertUtils.makeInformation("etat ne doit pas etre vide");
            return false;
        }


        if (numeroCompteTF.getText().isEmpty()) {
            AlertUtils.makeInformation("numeroCompte ne doit pas etre vide");
            return false;
        }

        if (numeroCompteTF.getText().length() != 13) {
            AlertUtils.makeInformation("numeroCompte doit avoir 13 chiffres");
            return false;
        }

        try {
            Long.parseLong(numeroCompteTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("numeroCompte doit etre un nombre");
            return false;
        }

        if (ribTF.getText().isEmpty()) {
            AlertUtils.makeInformation("rib ne doit pas etre vide");
            return false;
        }

        if (ribTF.getText().length() != 16) {
            AlertUtils.makeInformation("rib doit avoir 16 chiffres");
            return false;
        }

        try {
            Long.parseLong(ribTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("rib doit etre un nombre");
            return false;
        }

        return true;
    }
}