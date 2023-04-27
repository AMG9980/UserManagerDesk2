package tn.esprit.Service;

import tn.esprit.Entities.Role;
import tn.esprit.Tools.DbConnect;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.Entities.FXMLUtils;

public class AddRoleController implements Initializable {

    @FXML
    private TextField nomFld;

    @FXML
    private Label text1;
    
    
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
        //text1.setText("Add");
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
            getQuery();
            try {
                insert();
            } catch (SQLException ex) {
                Logger.getLogger(AddRoleController.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Error executing query. Please try again later.");
                alert.showAndWait();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        FXMLUtils fxmlUtils = new FXMLUtils();
        fxmlUtils.loadFXML("/tn/esprit/GUI/AddRole.fxml", "tn/esprit/Service/AddRoleController");
        
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/GUI/RoleView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
         */
    }

    @FXML
    private void cancel(MouseEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        /*nomFld.setText(null);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/GUI/RoleView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(s*/
    }

    private void getQuery() {
        if (update == false) {
            query = "INSERT INTO role (nom) VALUES (?)";
        } else {
            query = "UPDATE role SET nom = ? WHERE id = '" + roleId + "'";
        }
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
