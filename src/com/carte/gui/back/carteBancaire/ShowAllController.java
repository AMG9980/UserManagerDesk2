package com.carte.gui.back.carteBancaire;

import com.carte.entities.CarteBancaire;
import com.carte.gui.back.MainWindowController;
import com.carte.services.CarteBancaireService;
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

    public static CarteBancaire currentCarteBancaire;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public ComboBox<String> sortCB;

    List<CarteBancaire> listCarteBancaire;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listCarteBancaire = CarteBancaireService.getInstance().getAll();
        sortCB.getItems().addAll("Type", "Compte", "Nom", "NumCarte", "Cvv", "Email", "Date", "DateExp", "Etat");
        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listCarteBancaire);

        if (!listCarteBancaire.isEmpty()) {
            for (CarteBancaire carteBancaire : listCarteBancaire) {

                mainVBox.getChildren().add(makeCarteBancaireModel(carteBancaire));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeCarteBancaireModel(
            CarteBancaire carteBancaire
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_CARTEBANCAIRE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#typeText")).setText("Type : " + carteBancaire.getType());
            ((Text) innerContainer.lookup("#compteText")).setText("Type : " + carteBancaire.getCompte());
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + carteBancaire.getNom());
            ((Text) innerContainer.lookup("#numCarteText")).setText("NumCarte : " + carteBancaire.getNumCarte());
            ((Text) innerContainer.lookup("#cvvText")).setText("Cvv : " + carteBancaire.getCvv());
            ((Text) innerContainer.lookup("#emailText")).setText("Email : " + carteBancaire.getEmail());
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + carteBancaire.getDate());
            ((Text) innerContainer.lookup("#dateExpText")).setText("DateExp : " + carteBancaire.getDateExp());
            ((Text) innerContainer.lookup("#etatText")).setText("Etat : " + carteBancaire.getEtat());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierCarteBancaire(carteBancaire));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerCarteBancaire(carteBancaire));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterCarteBancaire(ActionEvent event) {
        currentCarteBancaire = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_CARTEBANCAIRE);
    }

    private void modifierCarteBancaire(CarteBancaire carteBancaire) {
        currentCarteBancaire = carteBancaire;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_CARTEBANCAIRE);
    }

    private void supprimerCarteBancaire(CarteBancaire carteBancaire) {
        currentCarteBancaire = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer cette carte bancaire ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            if (CarteBancaireService.getInstance().delete(carteBancaire.getId())) {
                MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_CARTEBANCAIRE);
            } else {
                AlertUtils.makeError("Could not delete carte bancaire");
            }
        }
    }


    @FXML
    public void sort(ActionEvent actionEvent) {
        Constants.compareVar = sortCB.getValue();
        Collections.sort(listCarteBancaire);
        displayData();
    }

    private void specialAction(CarteBancaire carteBancaire) {

    }
}
