/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import entities.Pack;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;
import services.ServicePack;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class PackDetailsController implements Initializable {

    @FXML
    private Label labelID;
    @FXML
    private Label labelPrix;
    @FXML
    private Label labelNom;
    @FXML
    private TextArea labelDesc;
    private Label labelImage;
    @FXML
    private Button btnModif;
    @FXML
    private Button Btnsupp;
    @FXML
    private Button gohome;
    @FXML
    private Button btnretour;

    public static int id_update;
    @FXML
    private ImageView imagePreview;
    @FXML
    private Button AcheterP;
    @FXML
    private Button AcheterS;
    public static String choix;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setPack(Pack p) {
        labelID.setText(String.valueOf(p.getId()));
        labelNom.setText(p.getNom());
        labelPrix.setText(String.valueOf(p.getPrix()));
        labelDesc.setText(p.getDesc());
        imagePreview.setImage(new Image(new File(p.getImage().replace('/', '\\')).toURI().toString()));
        choix=labelNom.getText();
    }

    @FXML
    private void OnModifClicked(ActionEvent event) {
        // Get the values from the labels in the current scene
        id_update = Integer.parseInt(labelID.getText());

        // Load the "ModifFournisseur.fxml" file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdatePack.fxml"));
        try {
            Parent root = loader.load();
            UpdatePackController controller = loader.getController();
            // Show the "ModifFournisseur.fxml" file in a new stage
            Scene scene = btnModif.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onsuppclicked(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Supprimer Pack");
        alert.setHeaderText("Confirmer suppression pack ?");
        alert.setContentText("Cette action est requise.");

        // Show the confirmation dialog and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // If the user clicks "OK", delete the fournisseur
        if (result.get() == ButtonType.OK) {
            try {
                Notifications.create()
        .title("Succès")
        .text("Pack supprimé !")
        .showInformation(); 
                ServicePack sp = new ServicePack();
                sp.supprimer(Integer.parseInt(labelID.getText()));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Packs.fxml"));
                Parent root = loader.load();
                Btnsupp.getScene().setRoot(root);
            } catch (IOException ex) {
                Logger.getLogger(PackDetailsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // Close the dialog and do nothing
            alert.close();
        }
    }

    @FXML
    private void onHomeclicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BackHome.fxml"));
            Parent root = loader.load();
            gohome.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(AddPackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onretourclicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Packs.fxml"));
            Parent root = loader.load();
            btnretour.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(AddPackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void OnAcheterClicked(ActionEvent event) throws IOException {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("Achat.fxml"));
            Parent root = loader.load();
            AcheterP.getScene().setRoot(root);
    }

    @FXML
    private void OnStripeClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Stripe.fxml"));
            Parent root = loader.load();
            AcheterP.getScene().setRoot(root);
    }

}
