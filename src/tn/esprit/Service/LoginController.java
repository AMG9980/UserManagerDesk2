package tn.esprit.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.Entities.FXMLUtils;
import tn.esprit.Entities.Role;
import tn.esprit.Tools.DbConnect;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Role role = null;
    
 @FXML
    private void getAddView(MouseEvent event) throws Exception {

        FXMLUtils fxmlUtils = new FXMLUtils();
        fxmlUtils.loadFXML("/tn/esprit/GUI/Register.fxml", "tn/esprit/Service/RegisterController");
    }
    @FXML
   public void handleLogin(ActionEvent event) {
    String email = emailField.getText();
    String password = passwordField.getText();

    // Se connecter à la base de données
    connection = DbConnect.getConnect();

    try {
        // Créer la requête SQL pour vérifier l'utilisateur et son rôle
        String sql = "SELECT u.*, r.nom FROM user u " +
             "JOIN user_role ur ON u.id = ur.user_id " +
             "JOIN role r ON ur.role_id = r.id " +
             "WHERE u.email=? AND u.password=? AND u.is_active=1";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, password);

        // Exécuter la requête SQL
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String role = resultSet.getString("nom");

            // Utiliser un switch pour rediriger l'utilisateur en fonction de son rôle
            switch (role) {
                case "Admin":
                    // Rediriger vers la page smb.fxml en cas de succès
                    Parent root = FXMLLoader.load(getClass().getResource("/com/carte/gui/back/MainWindow.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    
                    stage.show();
                    break;
                case "User":
                    // Rediriger vers la page Profil.fxml en cas de succès
                    Parent root2 = FXMLLoader.load(getClass().getResource("/com/carte/gui/front/MainWindow.fxml"));
                    Scene scene2 = new Scene(root2);
                    Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage2.setScene(scene2);
                    stage2.show();
                    break;
                default:
                    // Afficher une alerte d'échec de connexion
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de connexion");
                    alert.setHeaderText("Identifiants incorrects ou utilisateur sans rôle ADMIN ou USER ou Compte Inactif");
                    alert.setContentText("Veuillez vérifier votre email, votre mot de passe et votre rôle.");
                    alert.showAndWait();
                    break;
            }
        } else {
            // Afficher une alerte d'échec de connexion
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText("Identifiants incorrects");
            alert.setContentText("Veuillez vérifier votre email et votre mot de passeou Compte Inactif");
            alert.showAndWait();
        }
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
}



}


