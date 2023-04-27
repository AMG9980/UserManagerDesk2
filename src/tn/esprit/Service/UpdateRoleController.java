/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.Service;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.Entities.FXMLUtils;
import tn.esprit.Entities.Role;
import tn.esprit.Tools.DbConnect;

/**
 * FXML Controller class
 *
 * @author win 10
 */
public class UpdateRoleController implements Initializable {

    @FXML
    private Button save;
    @FXML
    private Button cancel;
    @FXML
    private TextField nomFld;

    
     
    String query = null;
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
    Role role = null;
    private boolean update;
    int roleId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void save(MouseEvent event) throws IOException, Exception {
        connection = DbConnect.getConnect();
        String nom = nomFld.getText();

        if (nom.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();

        } else {
            query = "UPDATE role SET nom = ? WHERE id = '" + roleId + "'";
            try {
                insert();
            } catch (SQLException ex) {
                Logger.getLogger(UpdateRoleController.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Error executing query. Please try again later.");
                alert.showAndWait();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

       /* FXMLUtils fxmlUtils = new FXMLUtils();
        fxmlUtils.loadFXML("/tn/esprit/GUI/AddRole.fxml", "tn/esprit/Service/UpdateRoleController");
*/
       
    }

    @FXML
    private void cancel(MouseEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        
    }

    

    private void insert() throws SQLException {
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nomFld.getText());
        preparedStatement.executeUpdate();
    }

    void setTextField(int id, String nom) {
        roleId = id;
        nomFld.setText(nom);
    }

    void setUpdate(boolean b) {
        this.update = b;
    }
   
    
}
