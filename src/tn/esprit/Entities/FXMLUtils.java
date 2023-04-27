/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.Entities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import tn.esprit.Service.SmbController;

public class FXMLUtils {
/*
    private void getSmbView(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/GUI/smb.fxml"));
        Parent root = loader.load();
        SmbController controller = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }*/

    /* public static void loadFXML(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(FXMLUtils.class.getResource(fxmlPath));
                                
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
     */

    public void loadFXML(String fxmlFilePath, String controllerFilePath) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        loader.setControllerFactory(t -> {
            try {
                return t.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        loader.setLocation(getClass().getResource(fxmlFilePath));
        loader.setController(getClass().getResource(controllerFilePath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
public void closeParentWindow(Parent parent) {
    Stage stage = (Stage) parent.getScene().getWindow();
    stage.close();
}

}
