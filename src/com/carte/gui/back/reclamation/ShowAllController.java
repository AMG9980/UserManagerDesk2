package com.carte.gui.back.reclamation;

import com.carte.entities.Reclamation;
import com.carte.services.ReclamationService;
import com.carte.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAllController implements Initializable {
    
    public static Reclamation currentReclamation;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;
    @FXML
    public TextField searchTF;

    List<Reclamation> listReclamation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listReclamation = ReclamationService.getInstance().getAll();
        
        displayData("");
    }

    void displayData(String searchText) {
        mainVBox.getChildren().clear();
        
        Collections.reverse(listReclamation);

        if (!listReclamation.isEmpty()) {
            for (Reclamation reclamation : listReclamation) {
                  if (reclamation.getTitre().toLowerCase().startsWith(searchText.toLowerCase())) {
                    mainVBox.getChildren().add(makeReclamationModel(reclamation));
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

    public Parent makeReclamationModel(
            Reclamation reclamation
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_RECLAMATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#userText")).setText("User : " + reclamation.getUser());
            ((Text) innerContainer.lookup("#titreText")).setText("Titre : " + reclamation.getTitre());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + reclamation.getDescription());
            
            Path selectedImagePath = FileSystems.getDefault().getPath(reclamation.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }
    @FXML
    private void search(KeyEvent event) {
        displayData(searchTF.getText());
    }
    
    private void specialAction(Reclamation reclamation) {
        
    }
}
