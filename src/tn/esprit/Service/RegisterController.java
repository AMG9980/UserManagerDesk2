/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.Service;

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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.Entities.User;
import tn.esprit.Entities.Role;

import java.sql.Statement;
import java.util.Base64;
import java.util.regex.Pattern;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import tn.esprit.Entities.FXMLUtils;

/**
 * FXML Controller class
 *
 * @author win 10
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField confirmPassFld;

    private UserViewController userViewController;
    @FXML
    private TextField usernameFld;
    @FXML
    private TextField emailFld;
    @FXML
    private TextField passwordFld;
    @FXML
    private TextField roleFld;
    @FXML
    private CheckBox isActiveFld;

    String query = null;
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
    User user = null;
    private boolean update;
    int userId;
    private String isActive;
    private int roleId;
    //private Object roleComboBox;
    //private ComboBox<String> roleComboBox;

    /**
     * Initializes the controller class.
     */
    @FXML
    private ComboBox<String> roleComboBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML

    private void save(MouseEvent event) throws IOException, Exception {
        connection = DbConnect.getConnect();
        String username = usernameFld.getText();
        String email = emailFld.getText();
        String password = passwordFld.getText();
        String confirmPassword = confirmPassFld.getText();
        roleId = 2;
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();
        } else {
            //Controle de saisie : 
            // Valider le nom d'utilisateur (minimum 3 caractères)
            if (username.length() < 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Username must be at least 3 characters long.");
                alert.showAndWait();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Password and Confirm Password do not match.");
                alert.showAndWait();
                return;
            }
            // Valider l'adresse e-mail (format d'email)
            Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
            if (!emailPattern.matcher(email).matches()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid email address.");
                alert.showAndWait();
                return;
            }

            // Encoder le mot de passe
            if (password == null || password.length() < 5) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Password must be at least 5 characters long.");
                alert.showAndWait();
                return;
            }
            // String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

            try {
                // Insérer l'utilisateur dans la table utilisateur
                query = "INSERT INTO user (username, email, password, is_active) VALUES (?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.setBoolean(4, false); // set isActive to 0
                preparedStatement.executeUpdate();

                // Obtenir l'ID du nouvel utilisateur inséré
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to insert user, no ID obtained.");
                }

                // Insérer le rôle utilisateur dans la table user_role
                String userRoleQuery = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
                PreparedStatement userRoleStatement = connection.prepareStatement(userRoleQuery);
                userRoleStatement.setInt(1, userId);
                userRoleStatement.setInt(2, roleId);
                userRoleStatement.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(AddUserViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            FXMLUtils fxmlUtils = new FXMLUtils();
            fxmlUtils.loadFXML("/tn/esprit/GUI/Login.fxml", "tn/esprit/Service/LoginController");
        }
    }

    @FXML
    private void cancel(MouseEvent event) throws IOException {
        //usernameFld.setText(null);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        /*UserViewController userViewController= new UserViewController();
        userViewController.refresh();*/
    }

    private void getQuery() {
        if (update == false) {
            query = "INSERT INTO user (username, email, password, is_active) VALUES (?, ?, ?, ?)";
        } else {
            query = "UPDATE user SET username=?, email=?, password=?, is_active=?, updated_at=NOW() WHERE id=?";
        }
    }

    private void insert() {
        try {
            // Insérer ou mettre à jour l'utilisateur dans la table user
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, usernameFld.getText());
            preparedStatement.setString(2, emailFld.getText());
            preparedStatement.setString(3, passwordFld.getText());
            preparedStatement.setBoolean(4, isActiveFld.isSelected());

            if (update) {
                preparedStatement.setInt(5, userId);
            }
            preparedStatement.executeUpdate();

            // Récupérer l'identifiant de l'utilisateur nouvellement inséré ou mis à jour
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int userId = 0;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }
            // System.out.print(userId);

            // Insérer le rôle pour l'utilisateur dans la table user_role
            String roleQuery = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
            PreparedStatement roleStatement = connection.prepareStatement(roleQuery);
            roleStatement.setInt(1, userId); // l'identifiant de l'utilisateur nouvellement inséré ou mis à jour

            roleStatement.setInt(2, roleId); // l'identifiant du rôle à insérer
            roleStatement.executeUpdate();
        } catch (SQLException ex) {
            // Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void setTextField(int id, String username, String email, /*String password, */ boolean isActive) {
        // Set the values of the text fields in the Add User view
        //idFld.setText(Integer.toString(id));
        usernameFld.setText(username);
        emailFld.setText(email);
        //passwordFld.setText(password);
        isActiveFld.setSelected(isActive);

    }

    void setUpdate(boolean b) {
        this.update = b;
    }

}
