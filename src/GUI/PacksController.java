/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.ServicePack;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class PacksController implements Initializable {

    @FXML
    private TableColumn<Pack, String> ColNom;
    @FXML
    private TableColumn<Pack, Integer> ColPrix;
    @FXML
    private TableColumn<Pack, String> ColDesc;
    @FXML
    private TableColumn<Pack, String> ColImage;
    
    public ObservableList<Pack> observablePacks = FXCollections.observableArrayList();
    @FXML
    private TableView<Pack> TablePacks;
    @FXML
    private Button Add;
    @FXML
    private Button home;
    @FXML
    private TextField recherche;
    
    private FilteredList<Pack> filteredPacks = new FilteredList<>(observablePacks, p -> true);
    @FXML
    private Button AfficherB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
        AfficherPacks();
        FilteredList<Pack> filteredPacks = new FilteredList<>(observablePacks, p -> true);
        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPacks.setPredicate(pack -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (pack.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        TablePacks.setItems(filteredPacks);
    } catch (SQLException ex) {
        Logger.getLogger(PacksController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(PacksController.class.getName()).log(Level.SEVERE, null, ex);
    }
}






    
    
    
private void AfficherPacks() throws SQLException, IOException {
    ServicePack ps = new ServicePack();
    List<Pack> lp = ps.getAll();
    
    ColNom.setCellValueFactory(new PropertyValueFactory<Pack, String>("nom"));
    ColPrix.setCellValueFactory(new PropertyValueFactory<Pack, Integer>("prix"));
    ColDesc.setCellValueFactory(new PropertyValueFactory<Pack, String>("Desc"));

    // Modifier la cell factory de la colonne ColImage
    ColImage.setCellFactory(column -> {
        return new TableCell<Pack, String>() {
            private final ImageView imageView = new ImageView();
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    // Charger l'image depuis son chemin et l'afficher dans l'ImageView
                    Image image = new Image(item);
                    imageView.setImage(image);
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    setGraphic(imageView);
                }
            }
        };
    });

    observablePacks = FXCollections.observableArrayList(lp);
    TablePacks.setItems(observablePacks);
}
    

    @FXML
    private void OnHomeClicked(ActionEvent event) {
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BackHome.fxml"));
            Parent root = loader.load();
            home.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(AddPackController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void handleTableRowSelection(MouseEvent event) {
         Pack selectedPack = TablePacks.getSelectionModel().getSelectedItem();
    if (selectedPack != null) {
        // Call a method to load the detail view with the selected fournisseur
        loadPackDetailView(selectedPack);
        
    }
    }
    private void loadPackDetailView(Pack p) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PackDetails.fxml"));
        Parent root = loader.load();
        PackDetailsController controller = loader.getController();
        controller.setPack(p);
        Scene scene = new Scene(root);
        Stage stage = (Stage) TablePacks.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void onAddClicked(ActionEvent event) {
        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("AddPack.fxml"));
            Parent root= loader.load();
            Add.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(PacksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void OnRechercheClicked(ActionEvent event) {
        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
    filteredPacks.setPredicate(pack -> {
        if (newValue == null || newValue.isEmpty()) {
            return true;
        }
        String lowerCaseFilter = newValue.toLowerCase();
        if (pack.getNom().toLowerCase().contains(lowerCaseFilter)) {
            return true;
        }
        return false;
    });
});
    }

    @FXML
    private void OnAfficherBClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Bonuss.fxml"));
        Parent root = loader.load();
        AfficherB.getScene().setRoot(root);
    }
    
}
