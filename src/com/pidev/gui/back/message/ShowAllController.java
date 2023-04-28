package com.pidev.gui.back.message;

import com.pidev.gui.back.MainWindowController;
import com.pidev.entities.Message;
import com.pidev.services.MessageService;
import com.pidev.utils.AlertUtils;
import com.pidev.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class ShowAllController implements Initializable {
    
    public static Message currentMessage;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public ComboBox<String> sortCB;

    List<Message> listMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listMessage = MessageService.getInstance().getAll();
        sortCB.getItems().addAll("User","Description");
        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();
        
        Collections.reverse(listMessage);

        if (!listMessage.isEmpty()) {
            for (Message message : listMessage) {
                
                mainVBox.getChildren().add(makeMessageModel(message));
                
            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeMessageModel(
            Message message
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_MESSAGE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#userText")).setText("User : " + message.getUser());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + message.getDescription());
            
            
            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierMessage(message));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerMessage(message));
            

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }
    
    @FXML
    private void ajouterMessage(ActionEvent event) {
        currentMessage = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_MESSAGE);
    }

    private void modifierMessage(Message message) {
        currentMessage = message;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_MESSAGE);
    }

    private void supprimerMessage(Message message) {
        currentMessage = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer message ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            if (MessageService.getInstance().delete(message.getId())) {
                MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_MESSAGE);
            } else {
                AlertUtils.makeError("Could not delete message");
            }
        }
    }
    
    
    @FXML
    public void sort(ActionEvent actionEvent) {
        Constants.compareVar = sortCB.getValue();
        Collections.sort(listMessage);
        displayData();
    }
    
    private void specialAction(Message message) {
        
    }
}
