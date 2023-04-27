/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author win 10
 */
public class SmbController implements Initializable {

    @FXML
    private AnchorPane contentPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void UsersManagement(ActionEvent event) throws IOException {
        // Load the "UserView.fxml" file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/GUI/UserView.fxml"));
        UserViewController controller = loader.getController();
        Parent root = loader.load();
        contentPane.getChildren().setAll(root);
    }

    public void RolesManagement(ActionEvent event) throws IOException {
        // Load the "UserView.fxml" file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/GUI/RoleView.fxml"));
        UserViewController controller = loader.getController();
        Parent root = loader.load();
        contentPane.getChildren().setAll(root);

    }

    @FXML
    private void logOut(MouseEvent event) throws Exception {
        // Charger la vue de connexion
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/GUI/Login.fxml"));

        // Récupérer le contrôleur de la vue de connexion
        LoginController loginController = loader.getController();
        Parent root = loader.load();
        // Afficher la vue de connexion
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        // Déconnecter l'utilisateur en réinitialisant les champs de connexion
        //loginController.emailField.setText("");
        //loginController.passwordField.setText("");
    }

    /*
   @FXML
    private void logOut(MouseEvent event) throws Exception {

        FXMLUtils fxmlUtils = new FXMLUtils();
        fxmlUtils.loadFXML("/tn/esprit/GUI/Login.fxml", "tn/esprit/Service/LoginController");

    }*/
}
