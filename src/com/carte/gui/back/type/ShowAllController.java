package com.carte.gui.back.type;

import com.carte.entities.Type;
import com.carte.gui.back.MainWindowController;
import com.carte.services.TypeService;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Type currentType;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public TextField searchTF;

    List<Type> listType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listType = TypeService.getInstance().getAll();

        displayData("");
    }

    void displayData(String searchText) {
        mainVBox.getChildren().clear();

        Collections.reverse(listType);

        if (!listType.isEmpty()) {
            for (Type type : listType) {
                if (type.getNom().toLowerCase().startsWith(searchText.toLowerCase())) {
                    mainVBox.getChildren().add(makeTypeModel(type));
                }

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeTypeModel(
            Type type
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_TYPE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomText")).setText("Nom : " + type.getNom());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + type.getDescription());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierType(type));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerType(type));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterType(ActionEvent event) {
        currentType = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_TYPE);
    }

    private void modifierType(Type type) {
        currentType = type;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_TYPE);
    }

    private void supprimerType(Type type) {
        currentType = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer type ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            if (TypeService.getInstance().delete(type.getId())) {
                MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_TYPE);
            } else {
                AlertUtils.makeError("Could not delete type");
            }
        }
    }


    @FXML
    private void search(KeyEvent event) {
        displayData(searchTF.getText());
    }

    private void specialAction(Type type) {

    }
}
