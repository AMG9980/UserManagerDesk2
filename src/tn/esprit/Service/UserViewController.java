/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
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
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import tn.esprit.Entities.Role;
import tn.esprit.Entities.User;
import tn.esprit.Entities.FXMLUtils;
import tn.esprit.Entities.MyClassIcon;
import tn.esprit.Tools.DbConnect;

import java.util.Properties;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;

import javax.mail.Session;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UserViewController implements Initializable {

    @FXML
    private Button exitBtn;
    @FXML
    private Pagination pagination;
    @FXML
    private Button add;
    @FXML
    private AnchorPane contentPane;
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
    private TableColumn<User, User> actionsColumn;

    @FXML
    private TableColumn<User, User> deleteColumn;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User user = null;
    int pageSize = 5;
    ObservableList<User> userList = FXCollections.observableArrayList();
    private String username;
    private String email;
    private String password;
    private Boolean is_active;

    public void initialize(URL url, ResourceBundle rb) {
        dynRefresh();
        configurePagination();
        // Initialize the columns of the table
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        isActiveColumn.setCellFactory(column -> new TableCell<User, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "Actif" : "Inactif");
                    setStyle(item ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                }
            }
        });

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
        actionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        actionsColumn.setCellFactory(param -> new TableCell<User, User>() {
            MyClassIcon myClass = new MyClassIcon();
            FontAwesomeIconView deleteIcon = myClass.getDeleteIcon();
            FontAwesomeIconView editIcon = myClass.getEditIcon();
            FontAwesomeIconView mailIcon = myClass.getMailIcon();
            FontAwesomeIconView userIcon = myClass.getBanIcon();

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (user == null) {
                    setGraphic(null);
                    return;
                }

                HBox buttons = new HBox(5, userIcon, new Label(" "), mailIcon, new Label(" "), editIcon, new Label(" "), deleteIcon);

                setGraphic(buttons);

                editIcon.setOnMouseClicked(event -> {
                    // Edit button action
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/tn/esprit/GUI/UpdateUser.fxml"));
                    try {
                        Parent root = loader.load();
                    } catch (IOException ex) {
                        Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    UpdateUserController updateUserController = loader.getController();
                    updateUserController.setUpdate(true);
                    updateUserController.setTextField(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getIsActive());

                    Parent parent = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.initStyle(StageStyle.UTILITY);

                    /* Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();*/
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
                   dynRefresh();
                });
// block unser
                // Set the initial color of the icon based on the isActive property
                userIcon.setFill(user.getIsActive() ? Color.GREEN : Color.RED);

                userIcon.setOnMouseClicked(event -> {
                    try {
                        String query = "UPDATE user SET is_active = !user.is_active WHERE id = ?";
                        connection = DbConnect.getConnect();
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, user.getId());
                        int rowsDeleted = preparedStatement.executeUpdate();

                        // toggle the color of the icon
                        if (userIcon.getFill().equals(Color.RED)) {
                            userIcon.setFill(Color.GREEN);
                        } else {
                            userIcon.setFill(Color.RED);
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        Alert alertError = new Alert(AlertType.ERROR);
                        alertError.setTitle("Update User");
                        alertError.setHeaderText(null);
                        alertError.setContentText("An error occurred while updating the user. Please try again later.");
                        alertError.showAndWait();
                    }
                });

                //sending Mail
                mailIcon.setOnMouseClicked(event -> {

                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Send Email");
                    alert.setHeaderText("Are you sure you want to send an email to this user?");
                    alert.setContentText(user.getUsername());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        String recipient = user.getEmail();

                        // Show dialog to get subject text
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setContentText("Please enter the email subject:");
                        TextField textField = dialog.getEditor();
                        System.out.println(textField);
// Définir la largeur du champ de saisie
                        textField.setPrefWidth(400);

// Définir la hauteur du champ de saisie
                        textField.setPrefHeight(200);

                        Optional<String> subjectResult = dialog.showAndWait();

                        // Check if subject text was entered
                        if (subjectResult.isPresent()) {
                            String subject = subjectResult.get();
                            String body = "Username: " + user.getUsername() + "\nEmail: " + user.getEmail();
                            sendEmail(recipient, subject, body);
                        }
                    }
                }
                );

                // fin sending mail 
            }

        }
        );

        // Populate the table with data
        try {
            List<User> users = UserViewController.getUsers();
            userList = FXCollections.observableArrayList(users);
            userTableView.setItems(userList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
dynRefresh();
    }

    private void sendEmail(String recipient, String subject, String body) {
        // final String username = "gmohsen6@gmail.com";
        //final String password = "ydljogkacxatuszj";
        /**
         * <<<<<<< HEAD String from = "gmohsen6@gmail.com"; String password =
         * "gzegzcumhcgazugf"; // replace with your email password*
         */
        String from = "amine.gharbi.1@esprit.tn";
        String password = "colhhjhazqitkyep"; // replace with your email password

        // Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(body);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private void configurePagination() {
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, userList.size());
            List<User> subList = userList.subList(fromIndex, toIndex);
            ObservableList<User> observableSubList = FXCollections.observableArrayList(subList);
            userTableView.setItems(observableSubList);
            updatePageCount();
            return userTableView;
        });
        pagination.setMaxPageIndicatorCount(5);
        updatePageCount();
    }

    private void updatePageCount() {
        int pageCount = (int) Math.ceil((double) userList.size() / pageSize);
        pagination.setPageCount(pageCount);
    }

    public static List<User> getUsers() throws SQLException {
        List<User> userList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnect();
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT u.id, u.username, u.email, u.password, u.is_active, r.id as role_id, r.nom as role_name "
                        + "FROM user u "
                        + "JOIN user_role ur ON u.id = ur.user_id "
                        + "JOIN role r ON ur.role_id = r.id")) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("id");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    boolean isActive = rs.getBoolean("is_active");
                    int roleId = rs.getInt("role_id");
                    String roleName = rs.getString("role_name");

                    User user = userList.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
                    if (user == null) {
                        user = new User(userId, username, email, password, isActive);
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
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_active")
                ));

                //userTableView.setItems(userList);
            }
            userTableView.setItems(userList);

        } catch (SQLException ex) {
            Logger.getLogger(UserViewController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void refresh(MouseEvent event) {
        try {
            ObservableList<User> userList = FXCollections.observableArrayList(UserViewController.getUsers());
            userTableView.setItems(userList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void dynRefresh() {
        try {
            ObservableList<User> userList = FXCollections.observableArrayList(UserViewController.getUsers());
            userTableView.setItems(userList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void getAddView(MouseEvent event) throws Exception {

        FXMLUtils fxmlUtils = new FXMLUtils();
        fxmlUtils.loadFXML("/tn/esprit/GUI/AddUserView.fxml", "tn/esprit/Service/AddUserViewController");

    }

    @FXML
    private void print(MouseEvent event) {

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        TableView tableView = new TableView();
        // Add columns and data to your table view
        // ...
        tableView.getColumns().addAll();
        tableView.setItems(userList);
        tableView.autosize();
        tableView.setMaxWidth(Double.MAX_VALUE);
        tableView.setMaxHeight(Double.MAX_VALUE);
        // Print the table view
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = job.printPage(pageLayout, tableView);
            if (success) {
                job.endJob();
            }
        }
    }

    // Handle the logout event
}