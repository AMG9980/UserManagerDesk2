/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import entities.Pack;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import services.ServicePack;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class UpdatePackController implements Initializable {

    @FXML
    private TextField tfid;
    @FXML
    private TextField tfnom;
    @FXML
    private TextField tfPrix;
    @FXML
    private TextArea tfDesc;
    @FXML
    private ImageView imagePreview;
    @FXML
    private Button btnImage;
    @FXML
    private Label imagePath;
    @FXML
    private Button btnModifi;
    @FXML
    private Button btnRetour;
    private ServicePack sp = new ServicePack();
    private Pack p;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tfid.setText(String.valueOf(PackDetailsController.id_update));
        p = sp.getOneById(PackDetailsController.id_update);
        tfDesc.setText(p.getDesc());
        tfnom.setText(p.getNom());
        tfPrix.setText(String.valueOf(p.getPrix()));
        imagePath.setText(p.getImage());
        imagePreview.setImage(new Image(new File(p.getImage().replace('/', '\\')).toURI().toString()));
    }

    @FXML
    private void onImageClicked(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File SelectedFile = fc.showOpenDialog(null);
        if (SelectedFile != null) {
            p.setImage(SelectedFile.getAbsolutePath());
            imagePath.setText(SelectedFile.getAbsolutePath());
            imagePreview.setImage(new Image(new File(SelectedFile.getAbsolutePath().replace('/', '\\')).toURI().toString()));
        } else {
            Alert A = new Alert(Alert.AlertType.ERROR);
            A.setContentText("Selectionnez une image !  ");
            A.showAndWait();
        }
    }

    private boolean verif() {
        if (tfnom.getText().isEmpty() || !tfnom.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Le champ nom est obligatoire et doit contenir uniquement des lettres de l'alphabet.");
            return false;
        }
        if ( tfPrix.getText().isEmpty() || Integer.valueOf(tfPrix.getText()) <= 0 ) {
            JOptionPane.showMessageDialog(null, "Le champ prix est obligatoire et et doit être positif!");
            return false;
        }
        if (tfDesc.getText().isEmpty() || tfDesc.getText().length() < 10 ) {
            JOptionPane.showMessageDialog(null, "Le champ description est obligatoire et doit contenir au moins 10 lettres de l'alphabet.");
            return false;
        }
        
        

        return true;
    }

    @FXML
    private void onModifClicked(ActionEvent event) {
        if (verif()) {
            p.setPrix(Integer.parseInt(tfPrix.getText()));
            p.setDesc(tfDesc.getText());
            p.setNom(tfnom.getText());
            p.setImage(p.getImage().replace('/', '\\'));
            sp.modifier(p);
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PackDetails.fxml"));
                Parent root = loader.load();
                PackDetailsController controller = loader.getController();
                controller.setPack(p);
                Scene scene = new Scene(root);
                Stage stage = (Stage) btnModifi.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void OnRetourClicked(ActionEvent event) {
        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PackDetails.fxml"));
                Parent root = loader.load();
                PackDetailsController controller = loader.getController();
                controller.setPack(p);
                Scene scene = new Scene(root);
                Stage stage = (Stage) btnModifi.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
