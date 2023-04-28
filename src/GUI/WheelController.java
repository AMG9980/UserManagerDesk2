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
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class WheelController implements Initializable {

    @FXML
    private ImageView Wheel;
    @FXML
    private Button Start;
    @FXML
    private ImageView Marker;
    @FXML
    private Button Home;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void OnStartClicked(ActionEvent event) { 
    RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), Wheel); // Crée une transition de rotation de 4 secondes sur l'ImageView Wheel
    rotateTransition.setByAngle(360 * Math.random()*360); // Tourne la roulette de 4 tours complets (360 degrés par tour)
    rotateTransition.play(); // Démarre la transition
    
    // Ajouter un événement de fin de transition pour mettre à jour la couleur du marqueur
    rotateTransition.setOnFinished(e -> {
        // Mettre à jour la couleur du marqueur en fonction de l'angle de la roulette
        double angle = Wheel.getRotate() % 360;
        // Calculer la couleur correspondante à l'angle et mettre à jour l'image du marqueur
        // ...
           if(angle>=0 && angle<10){
             System.out.println("20dt");}
           if(angle>=10 && angle<20){
             System.out.println("30dt");}
            if(angle>=20 && angle<30){
             System.out.println("20dt");}
            if(angle>=30 && angle<39){
             System.out.println("30Dt");}
            if(angle>=39 && angle<49){
             System.out.println("20dt");}
            if(angle>=49 && angle<59){
             System.out.println("30Dt");}
            if(angle>=59 && angle<69){
             System.out.println("20dt");}
            if(angle>=69 && angle<79){
             System.out.println("30Dt");}
            if(angle>=79 && angle<89){
             System.out.println("20dt");}
            if(angle>=89 && angle<99){
             System.out.println("30Dt");}
             if(angle>=99 && angle<109){
             System.out.println("20dt");}
             if(angle>=109 && angle<119){
             System.out.println("30Dt");}
             if(angle>=119 && angle<129){
             System.out.println("20dt");}
             if(angle>=129 && angle<139){
             System.out.println("30Dt");}
             if(angle>=139 && angle<149){
             System.out.println("20dt");}
             if(angle>=149 && angle<159){
             System.out.println("30Dt");}
             if(angle>=159 && angle<169){
             System.out.println("20dt");}
             if(angle>=169 && angle<179){
             System.out.println("30Dt");}
             if(angle>=179 && angle<189){
             System.out.println("20dt");}
             if(angle>=189 && angle<199){
             System.out.println("30Dt");}
             if(angle>=199 && angle<209){
             System.out.println("20dt");}
             if(angle>=209 && angle<219){
             System.out.println("30Dt");}
             if(angle>=219 && angle<229){
             System.out.println("20dt");}
             if(angle>=229 && angle<239){
             System.out.println("30Dt");}
             
             if(angle>=239 && angle<249){
             System.out.println("20dt");}
             if(angle>=249 && angle<259){
             System.out.println("30Dt");}
             if(angle>=259 && angle<269){
             System.out.println("20dt");}
             if(angle>=269 && angle<279){
             System.out.println("30Dt");}
             
             if(angle>=279 && angle<289){
             System.out.println("20dt");}
             if(angle>=289 && angle<299){
             System.out.println("30Dt");}
             if(angle>=299 && angle<309){
             System.out.println("20dt");}
             if(angle>=309 && angle<319){
             System.out.println("30Dt");}
             if(angle>=319 && angle<329){
             System.out.println("20dt");}
             if(angle>=329 && angle<339){
             System.out.println("30Dt");}
             if(angle>=339 && angle<349){
             System.out.println("20dt");}
             if(angle>=349 && angle<359){
             System.out.println("30Dt");}
             if(angle>=359 && angle<369){
             System.out.println("20dt");}
             
             
            
    });
    
    Notifications.create()
        .title("Succès")
        .text("FELICITATION !PROFITEZ DE VOTRE BONUS")
        .showInformation(); 
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
