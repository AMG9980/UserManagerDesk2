package com.carte.gui.back.compte;

import com.carte.entities.Compte;
import com.carte.gui.back.MainWindowController;
import com.carte.services.CompteService;
import com.carte.utils.AlertUtils;
import com.carte.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Compte currentCompte;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public ComboBox<String> sortCB;

    List<Compte> listCompte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listCompte = CompteService.getInstance().getAll();
        sortCB.getItems().addAll("Name", "Balance", "Etat", "NumeroCompte", "Rib");
        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listCompte);

        if (!listCompte.isEmpty()) {
            for (Compte compte : listCompte) {

                mainVBox.getChildren().add(makeCompteModel(compte));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeCompteModel(
            Compte compte
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_COMPTE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nameText")).setText("Name : " + compte.getName());
            ((Text) innerContainer.lookup("#balanceText")).setText("Balance : " + compte.getBalance());
            ((Text) innerContainer.lookup("#etatText")).setText("Etat : " + compte.getEtat());
            ((Text) innerContainer.lookup("#numeroCompteText")).setText("NumeroCompte : " + compte.getNumeroCompte());
            ((Text) innerContainer.lookup("#ribText")).setText("Rib : " + compte.getRib());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierCompte(compte));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerCompte(compte));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterCompte(ActionEvent event) {
        currentCompte = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_COMPTE);
    }

    private void modifierCompte(Compte compte) {
        currentCompte = compte;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_COMPTE);
    }

    private void supprimerCompte(Compte compte) {
        currentCompte = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer compte ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            if (CompteService.getInstance().delete(compte.getId())) {
                MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_COMPTE);
            } else {
                AlertUtils.makeError("Could not delete compte");
            }
        }
    }


    @FXML
    public void sort(ActionEvent actionEvent) {
        Constants.compareVar = sortCB.getValue();
        Collections.sort(listCompte);
        displayData();
    }

    private void specialAction(Compte compte) {

    }
}
