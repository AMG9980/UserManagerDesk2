/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.Service;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tn.esprit.Entities.Role;
import tn.esprit.Entities.User;
import tn.esprit.Entities.MyClassIcon;
import tn.esprit.Tools.DbConnect;

public class UserViewController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, Boolean> isActiveColumn;

    @FXML
    private TableColumn<User, List<Role>> rolesColumn;

    @FXML
    private TableColumn<User, User> editDeleteColumn;

    @FXML
    private TableColumn<User, User> deleteColumn;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User user = null;

    ObservableList<User> userList = FXCollections.observableArrayList();
    private String email;
    private Boolean is_active;

    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the columns of the table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        rolesColumn.setCellFactory(column -> new TableCell<User, List<Role>>() {
            @Override
            protected void updateItem(List<Role> roles, boolean empty) {
                super.updateItem(roles, empty);
                if (roles == null || empty) {
                    setText(null);
                } else {
                    setText(roles.stream().map(Role::getNom).collect(Collectors.joining(", ")));
                }
            }
        });

        // Add edit and delete buttons to each row
        editDeleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
editDeleteColumn.setCellFactory(param -> new TableCell<User, User>() {
    MyClassIcon myClass = new MyClassIcon();
    FontAwesomeIconView deleteIcon = myClass.getDeleteIcon();
    FontAwesomeIconView editIcon = myClass.getEditIcon();
    
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        
        if (user == null) {
            setGraphic(null);
            return;
        }
        
        HBox buttons = new HBox(editIcon, deleteIcon);
        setGraphic(buttons);
        
        editIcon.setOnMouseClicked(event -> {
            // Edit button action
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/tn/esprit/GUI/AddUserView.fxml"));
            try {
                Parent root = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

            AddUserViewController addUserController = loader.getController();
            addUserController.setUpdate(true);
            addUserController.setTextField(user.getId(), user.getUsername(), user.getEmail(), /*getPassword(),*/ user.getIsActive());
            /* user.getBirth().toLocalDate(),user.getAdress(), user.getEmail());*/
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            stage.show();
        });
        
        deleteIcon.setOnMouseClicked(event -> {
            // Delete button action
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete User");
            alert.setHeaderText("Are you sure you want to delete this user?");
            alert.setContentText(user.getUsername());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    connection = DbConnect.getConnect();
                    String query = "DELETE FROM user WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, user.getId());
                    int rowsDeleted = preparedStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        userList.remove(user);
                        Alert alertSuccess = new Alert(AlertType.INFORMATION);
                        alertSuccess.setTitle("Delete User");
                        alertSuccess.setHeaderText(null);
                        alertSuccess.setContentText("User has been deleted successfully!");
                        alertSuccess.showAndWait();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Alert alertError = new Alert(AlertType.ERROR);
                    alertError.setTitle("Delete User");
                    alertError.setHeaderText(null);
                    alertError.setContentText("An error occurred while deleting the user. Please try again later.");
                    alertError.showAndWait();
                }
            }
        });
    }
});


        // Populate the table with data
        try {
            ObservableList<User> userList = FXCollections.observableArrayList(UserViewController.getUsers());
            userTableView.setItems(userList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static List<User> getUsers() throws SQLException {
        List<User> userList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnect();
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT u.id, u.username, u.email, u.is_active, r.id as role_id, r.nom as role_name "
                        + "FROM user u "
                        + "JOIN user_role ur ON u.id = ur.user_id "
                        + "JOIN role r ON ur.role_id = r.id")) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("id");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    boolean isActive = rs.getBoolean("is_active");
                    int roleId = rs.getInt("role_id");
                    String roleName = rs.getString("role_name");

                    User user = userList.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
                    if (user == null) {
                        user = new User(userId, username, email, isActive);
                        userList.add(user);
                    }

                    Role role = new Role(roleId, roleName);
                    user.addRole(role);
                }
            }
        }

        return userList;
    }

    @FXML
    public void searchByName(MouseEvent event) throws NoSuchAlgorithmException {
        String username = searchField.getText();
        String email = searchField.getText();
        boolean is_active = Boolean.parseBoolean(searchField.getText());
        try {
            userList.clear();
            String query = "SELECT * FROM user WHERE username LIKE ? OR email LIKE ? ";
            Connection connection = DbConnect.getConnect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + username + "%");
            preparedStatement.setString(2, "%" + email + "%");
            // preparedStatement.setBoolean(3, is_active);
            resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        //resultSet.getString("password"),
                        resultSet.getBoolean("is_active")
                ));

                //userTableView.setItems(userList);
            }
            userTableView.setItems(userList);

        } catch (SQLException ex) {
            Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void refreshTable(MouseEvent event) {
        try {
            ObservableList<User> userList = FXCollections.observableArrayList(UserViewController.getUsers());
            userTableView.setItems(userList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void getAddView(MouseEvent event) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/tn/esprit/GUI/AddUserView.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}