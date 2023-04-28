package com.carte.gui.front;

import com.carte.MainApp;
import com.carte.utils.Animations;
import com.carte.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class TopBarController implements Initializable {

    private final Color COLOR_GRAY = new Color(0.9, 0.9, 0.9, 1);
    private final Color COLOR_PRIMARY = Color.web("#000000");
    private final Color COLOR_DARK = new Color(1, 1, 1, 0.65);
    private Button[] liens;

    @FXML
    private Button btnComptes;

    @FXML
    private Button btnTransactions;

    @FXML
    private Button btnCarteBancaires;

    @FXML
    private AnchorPane mainComponent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        liens = new Button[]{
                btnComptes,
                btnTransactions,
                btnCarteBancaires,
        };

        mainComponent.setBackground(new Background(new BackgroundFill(COLOR_PRIMARY, CornerRadii.EMPTY, Insets.EMPTY)));

        for (Button lien : liens) {
            lien.setTextFill(COLOR_DARK);
            lien.setBackground(new Background(new BackgroundFill(COLOR_PRIMARY, CornerRadii.EMPTY, Insets.EMPTY)));
            Animations.animateButton(lien, COLOR_GRAY, Color.WHITE, COLOR_PRIMARY, 0, false);
        }

        btnComptes.setTextFill(COLOR_DARK);

        btnTransactions.setTextFill(COLOR_DARK);

        btnCarteBancaires.setTextFill(COLOR_DARK);

    }

    @FXML
    private void afficherComptes(ActionEvent event) {
        goToLink(Constants.FXML_FRONT_DISPLAY_ALL_COMPTE);

        btnComptes.setTextFill(COLOR_PRIMARY);
        Animations.animateButton(btnComptes, COLOR_GRAY, Color.WHITE, COLOR_PRIMARY, 0, false);
    }

    @FXML
    private void afficherTransactions(ActionEvent event) {
        goToLink(Constants.FXML_FRONT_DISPLAY_ALL_TRANSACTION);

        btnTransactions.setTextFill(COLOR_PRIMARY);
        Animations.animateButton(btnTransactions, COLOR_GRAY, Color.WHITE, COLOR_PRIMARY, 0, false);
    }

    @FXML
    private void afficherCarteBancaires(ActionEvent event) {
        goToLink(Constants.FXML_FRONT_DISPLAY_ALL_CARTEBANCAIRE);

        btnCarteBancaires.setTextFill(COLOR_PRIMARY);
        Animations.animateButton(btnCarteBancaires, COLOR_GRAY, Color.WHITE, COLOR_PRIMARY, 0, false);
    }

    private void goToLink(String link) {
        for (Button lien : liens) {
            lien.setTextFill(COLOR_DARK);
            Animations.animateButton(lien, COLOR_GRAY, COLOR_DARK, COLOR_PRIMARY, 0, false);
        }
        MainWindowController.getInstance().loadInterface(link);
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        MainApp.getInstance().logout();
    }
}
