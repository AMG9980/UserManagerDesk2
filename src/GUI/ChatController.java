/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.Mail;


/**
 * FXML Controller class
 *
 * @author HP
 */
public class ChatController implements Initializable {

    @FXML
    private TextArea textchat;
    @FXML
    private TextField ASK;
    
        private Map<String, String> responses;
    @FXML
    private Button Home;
    
private int alertCount = 0;
private static final int MAX_ALERTS = 3;
private static final int BAN_DURATION_MINS = 5;
private static final String[] FORBIDDEN_WORDS = {"fuck", "shutup", "damn"};


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        responses = new HashMap<>();
        responses.put("bonjour", "comment puis je vous aidez");
        responses.put("quelle est votre banque", "BANEKTI");
        responses.put("systeme des packs", "lorsque vous acheter un pack vous allez obtenir plusieurs bonus ! essayer et vous ne regretter pas ");
        responses.put("exemple de bonus", "-15% ***** ti barcha l hassilou ");
        responses.put("nom du admin", "Hammoudi Ayoub");
        responses.put("salut", "salut ");
        responses.put("who are you ?", "je suis un bot créer par Hammoudi Ayoub ");
        responses.put("Banekti?", "Banque Tunisienne fondée par Hammoudi Ayoub");
        responses.put("pack silver ?", "vous allez profitez de plusieurs bonus en achetant le pack silver :------------ ");
        responses.put("pack gold ?", "vous allez profitez de plusieurs bonus en achetant le pack gold : -----------");
        responses.put("pack platinium ?", "vous allez profitez de plusieurs bonus en achetant le pack platinium : -----------");
  
    }    

    @FXML
    private void UserA(ActionEvent event) throws Exception {
        String input = ASK.getText();
        String response = responses.getOrDefault(input, "désolé j'ai pas la réponse ");
        for (String forbiddenWord : FORBIDDEN_WORDS) {
        if (input.contains(forbiddenWord)) {
            alertCount++;
            if (alertCount == MAX_ALERTS) {
                // Ban the user for 5 minutes
                ASK.setDisable(true);
                 Alert alert = new Alert(AlertType.ERROR, "Vous avez été banni ");
                alert.showAndWait();
                Mail.sendMail("ayoub.hammoudi@esprit.tn", "https://buy.stripe.com/test_aEU166gaWcCj1S86op");
                Platform.exit();
            } else {
                Alert alert = new Alert(AlertType.WARNING, "Le message contient un gros mot. Attention!");
                alert.showAndWait();            }
                ASK.clear();
            return;
        }
    }
        textchat.appendText("User: " + input + "\n");
        textchat.appendText("Chatbot: " + response + "\n\n");
        ASK.clear();}

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
    
    


