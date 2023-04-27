
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import tn.esprit.Entities.FXMLUtils;
import tn.esprit.Entities.Role;
import tn.esprit.Tools.DbConnect;

public class RoleViewController implements Initializable {

    @FXML
    private Pagination pagination;
    @FXML
    private TextField SearchFld;
    @FXML
    private TableView<Role> rolesTable;
    @FXML
    private TableColumn<Role, String> idCol;
    @FXML
    private TableColumn<Role, String> nomCol;
   
    @FXML
    private TableColumn<Role, String> editCol;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Role role = null;

    ObservableList<Role> RoleList = FXCollections.observableArrayList();

    int pageSize = 3;
    @FXML
     private Label text1;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       configurePagination();
        loadDate();
        
    }

    private void configurePagination() {
    pagination.setPageFactory(pageIndex -> {
        int fromIndex = pageIndex * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, RoleList.size());
        rolesTable.setItems(FXCollections.observableArrayList(RoleList.subList(fromIndex, toIndex)));
        updatePageCount(); // Mise à jour du nombre de pages
        return rolesTable;
    });
    pagination.setMaxPageIndicatorCount(5); // Définir le nombre de pages maximum à afficher
    updatePageCount(); // Mise à jour du nombre de pages
}

    private void updatePageCount() {
        int pageCount = (int) Math.ceil((double) RoleList.size() / pageSize);
        pagination.setPageCount(pageCount);
    }
    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    
    private void getAddView(MouseEvent event) throws Exception {
        
        FXMLUtils fxmlUtils = new FXMLUtils();
        fxmlUtils.loadFXML("/tn/esprit/GUI/AddRole.fxml", "tn/esprit/Service/AddRoleController");
        
       

       
        /*try {
            Parent parent = FXMLLoader.load(getClass().getResource("/tn/esprit/GUI/AddRole.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            /*
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(RoleViewController.class.getName()).log(Level.SEVERE, null, ex);
        }*/

    }

    @FXML
    public void refreshTable() {
        try {
            RoleList.clear();

            query = "SELECT * FROM role";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoleList.add(new Role(
                        resultSet.getInt("id"),
                        resultSet.getString("nom")));
                
                rolesTable.setItems(RoleList);

            }

        } catch (SQLException ex) {
            Logger.getLogger(RoleViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void searchByName(MouseEvent event) {
        String nom = SearchFld.getText();
        try {
            RoleList.clear();
            query = "SELECT * FROM role WHERE nom LIKE ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + nom + "%");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoleList.add(new Role(
                        resultSet.getInt("id"),
                        resultSet.getString("nom")
                ));
            }
            rolesTable.setItems(RoleList);
        } catch (SQLException ex) {
            Logger.getLogger(RoleViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void print(MouseEvent event) {
        
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        TableView tableView = new TableView();
        // Add columns and data to your table view
        // ...
        tableView.getColumns().addAll();
        tableView.setItems(RoleList);
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

    private void loadDate() {

        connection = DbConnect.getConnect();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        //add cell of button edit 
        Callback<TableColumn<Role, String>, TableCell<Role, String>> cellFoctory = (TableColumn<Role, String> param) -> {
            // make cell containing buttons
            final TableCell<Role, String> cell = new TableCell<Role, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                role = rolesTable.getSelectionModel().getSelectedItem();
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Delete Confirmation");
                                alert.setHeaderText("Are you sure you want to delete this role?");
                                alert.setContentText("This action cannot be undone.");
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK) {
                                    query = "DELETE FROM role WHERE id =" + role.getId();
                                    connection = DbConnect.getConnect();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(RoleViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {

                            role = rolesTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/tn/esprit/GUI/AddRole.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(RoleViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            AddRoleController addRoleController = loader.getController();
                            addRoleController.setUpdate(true);
                            addRoleController.setTextField(role.getId(), role.getNom());

                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);

                            //Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                           // currentStage.close();

                            stage.show();

                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
        editCol.setCellFactory(cellFoctory);
        rolesTable.setItems(RoleList);

    }

}
