package com.carte.gui.front.transaction;

import com.carte.entities.Transaction;
import com.carte.gui.front.MainWindowController;
import com.carte.services.TransactionService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Transaction currentTransaction;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public TextField searchTF;

    List<Transaction> listTransaction;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listTransaction = TransactionService.getInstance().getAll();

        displayData("");
    }

    void displayData(String searchText) {
        mainVBox.getChildren().clear();

        Collections.reverse(listTransaction);

        if (!listTransaction.isEmpty()) {
            for (Transaction transaction : listTransaction) {
                if (String.valueOf(transaction.getAmount()).toLowerCase().startsWith(searchText.toLowerCase())) {
                    mainVBox.getChildren().add(makeTransactionModel(transaction));
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

    public Parent makeTransactionModel(
            Transaction transaction
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_TRANSACTION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#sourceAccountText")).setText("Account : " + transaction.getSourceAccount());
            ((Text) innerContainer.lookup("#destinationAccountText")).setText("Account : " + transaction.getDestinationAccount());
            ((Text) innerContainer.lookup("#amountText")).setText("Amount : " + transaction.getAmount());
            ((Text) innerContainer.lookup("#createdAtText")).setText("CreatedAt : " + transaction.getCreatedAt());

            createQR(((ImageView) innerContainer.lookup("#qrImageView")), transaction);

            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerTransaction(transaction));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterTransaction(ActionEvent event) {
        currentTransaction = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_TRANSACTION);
    }

    private void supprimerTransaction(Transaction transaction) {
        currentTransaction = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer transaction ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            if (TransactionService.getInstance().delete(transaction.getId())) {
                MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_TRANSACTION);
            } else {
                AlertUtils.makeError("Could not delete transaction");
            }
        }
    }


    public void createQR(ImageView qrIV, Transaction transaction) {
        // GENERATE QR CODE
        ByteArrayOutputStream out = QRCode.from(
                "Source account " + transaction.getSourceAccount().toString() +
                        "Destination account " + transaction.getDestinationAccount().toString() +
                        "Amount " + transaction.getAmount()
        ).to(ImageType.PNG).withSize(200, 200).stream();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        qrIV.setImage(new Image(in));

        // SHOW QR CODE
        BorderPane root = new BorderPane();
        Image image = new Image(in);
        ImageView view = new ImageView(image);
        view.setStyle("-fx-stroke-width: 2; -fx-stroke: blue");
        root.setCenter(view);
    }

    @FXML
    private void search(KeyEvent event) {
        displayData(searchTF.getText());
    }
}
