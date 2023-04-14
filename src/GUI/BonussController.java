/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import entities.Bonus;
import entities.Pack;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceBonus;
import services.ServicePack;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class BonussController implements Initializable {

    @FXML
    private TableColumn<Bonus, Integer> colID;
    @FXML
    private TableColumn<Bonus, String> Colnom_b1;
    @FXML
    private TableColumn<Bonus, String> Colnom_b2;
    @FXML
    private TableColumn<Bonus, String> Colnom_b3;
    @FXML
    private TableColumn<Bonus, String> Colnom_b4;
    @FXML
    private TableColumn<Bonus, Integer> Colid_pack;
    
    public ObservableList<Bonus> observableBonuss = FXCollections.observableArrayList();
    @FXML
    private TableView<Bonus> TableBonuss;
    @FXML
    private Button home;
    @FXML
    private Button ajouter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         try {
            // TODO
            AfficherBonuss();
        } catch (SQLException ex) {
            Logger.getLogger(BonussController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BonussController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
      private void AfficherBonuss() throws SQLException, IOException {
            ServiceBonus bs = new ServiceBonus();
            List<Bonus> lb = bs.getAll();
         
        colID.setCellValueFactory(new PropertyValueFactory<Bonus, Integer>("id"));

        Colnom_b1.setCellValueFactory(new PropertyValueFactory<Bonus, String>("bonus1"));
     
        Colnom_b2.setCellValueFactory(new PropertyValueFactory<Bonus, String>("bonus2"));
        
        Colnom_b3.setCellValueFactory(new PropertyValueFactory<Bonus, String>("bonus2"));

        Colnom_b4.setCellValueFactory(new PropertyValueFactory<Bonus, String>("bonus4"));
        
        //Colid_pack.setCellValueFactory(new PropertyValueFactory<Bonus, Integer>("p"));

        
      
        
         observableBonuss = FXCollections.observableArrayList(lb);
        TableBonuss.setItems(observableBonuss);
       
    }

    @FXML
    private void OnHomeClicked(ActionEvent event) {
    }

    @FXML
    private void onAddClicked(ActionEvent event) {
    }
    
}
