package com.carte.gui.back.transaction;


import com.carte.entities.Compte;
import com.carte.entities.Transaction;
import com.carte.gui.back.MainWindowController;
import com.carte.services.CompteService;
import com.carte.services.TransactionService;
import com.carte.utils.AlertUtils;
import com.carte.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public ComboBox<Compte> sourceAccountCB;
    @FXML
    public ComboBox<Compte> destinationAccountCB;
    @FXML
    public TextField amountTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (Compte compte : CompteService.getInstance().getAll()) {
            sourceAccountCB.getItems().add(compte);
            destinationAccountCB.getItems().add(compte);
        }

        topText.setText("Ajouter transaction");
        btnAjout.setText("Ajouter");
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Transaction transaction = new Transaction(
                    sourceAccountCB.getValue(),
                    destinationAccountCB.getValue(),
                    Float.parseFloat(amountTF.getText()),
                    LocalDate.now()
            );

            if (TransactionService.getInstance().add(transaction)) {
                Compte sourceAccount = sourceAccountCB.getValue();
                Compte destinationAccount = destinationAccountCB.getValue();

                sourceAccount.setBalance(sourceAccount.getBalance() - Float.parseFloat(amountTF.getText()));
                destinationAccount.setBalance(destinationAccount.getBalance() + Float.parseFloat(amountTF.getText()));

                if (CompteService.getInstance().edit(sourceAccount)) {
                    if (CompteService.getInstance().edit(destinationAccount)) {
                        if (TransactionService.getInstance().add(transaction)) {
                            AlertUtils.makeInformation("Transaction effectué avec succés");
                            MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_TRANSACTION);
                        }
                    }
                }
            } else {
                AlertUtils.makeError("Error");
            }
        }
    }


    private boolean controleDeSaisie() {


        if (sourceAccountCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir compte");
            return false;
        }


        if (destinationAccountCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir compte");
            return false;
        }

        if (sourceAccountCB.getValue().getId() == destinationAccountCB.getValue().getId()) {
            AlertUtils.makeInformation("Source et destination doivent etre different");
            return false;
        }

        if (amountTF.getText().isEmpty()) {
            AlertUtils.makeInformation("amount ne doit pas etre vide");
            return false;
        }


        return true;
    }
}