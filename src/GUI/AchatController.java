/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;    
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class AchatController implements Initializable {

    @FXML
    private Button Achat;
    @FXML
    private Button retour;
    @FXML
    private Button Home;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField num;
    @FXML
    private TextField adr;
    @FXML
    private TextField code;
    @FXML
    private TextField numc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void OnAcheterClicked(ActionEvent event) throws IOException {
        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || num.getText().isEmpty()
|| adr.getText().isEmpty() || code.getText().isEmpty() || numc.getText().isEmpty()) {
// Afficher un message d'erreur si un champ est vide
       JOptionPane.showMessageDialog(null, "veuillez remplire tous les champs !");

} else {
if (!num.getText().matches("\\d{10}")) {
       JOptionPane.showMessageDialog(null, "Le numéro de téléphone doit contenir 10 chiffres ! !");
return;
}

    // Vérifier si le numéro de carte bancaire est valide
    if (!numc.getText().matches("\\d{16}")) {
        JOptionPane.showMessageDialog(null, "Le numéro de carte bancaire doit contenir 16 chiffres !");
        return;
    }
    
    // Si tous les champs sont valides, effectuer l'achat
    JOptionPane.showMessageDialog(null,"Achat effectué !");
}
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Wheel.fxml"));
        Parent root = loader.load();
        Achat.getScene().setRoot(root);
    }

    @FXML
    private void OnretourClicked(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PackDetails.fxml"));
            Parent root = loader.load();
            retour.getScene().setRoot(root);
    }

    @FXML
    private void OnHomeClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BackHome.fxml"));
            Parent root = loader.load();
            Home.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(AddPackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    

