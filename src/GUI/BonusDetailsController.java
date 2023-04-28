/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import entities.Bonus;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import services.ServiceBonus;
import services.ServicePack;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class BonusDetailsController implements Initializable {

    @FXML
    private Button modifier;
    @FXML
    private Button supprimer;
    @FXML
    private Button retour;
    @FXML
    private Button home;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfB1;
    @FXML
    private TextField tfB2;
    @FXML
    private TextField tfB3;
    @FXML
    private TextField tfB4;
    @FXML
    private TextField tfID_Pack;
    public static int id_update;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void setBonus(Bonus b) {
        tfID.setText(String.valueOf(b.getId()));
        tfB1.setText(b.getBonus1());
        tfB2.setText(b.getBonus2());
        tfB3.setText(b.getBonus3());
        tfB4.setText(b.getBonus4());
        tfID_Pack.setText(String.valueOf(b.getP()));
    }

    @FXML
    private void OnModifierClicked(ActionEvent event) {
         id_update = Integer.parseInt(tfID.getText());

        // Load the "ModifFournisseur.fxml" file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateBonus.fxml"));
        try {
            Parent root = loader.load();
            UpdatePackController controller = loader.getController();
            // Show the "ModifFournisseur.fxml" file in a new stage
            Scene scene = modifier.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OnSupprimerClicked(ActionEvent event) {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supprimer Bonus");
        alert.setHeaderText("Confirmer suppression Bonus ?");
        alert.setContentText("Cette action est requise.");

        // Show the confirmation dialog and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // If the user clicks "OK", delete the fournisseur
        if (result.get() == ButtonType.OK) {
            try {
                ServiceBonus sb = new ServiceBonus();
                sb.supprimer(Integer.parseInt(tfID.getText()));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Bonuss.fxml"));
                Parent root = loader.load();
                supprimer.getScene().setRoot(root);
            } catch (IOException ex) {
                Logger.getLogger(PackDetailsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // Close the dialog and do nothing
            alert.close();
        }
    }

    @FXML
    private void OnReturnClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BackHome.fxml"));
            Parent root = loader.load();
            retour.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(AddPackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void OnHomeClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BackHome.fxml"));
            Parent root = loader.load();
            home.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(AddPackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
