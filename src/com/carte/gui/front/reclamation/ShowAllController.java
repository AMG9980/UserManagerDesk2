package com.carte.gui.front.reclamation;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.carte.entities.Reclamation;
import com.carte.gui.front.MainWindowController;
import com.carte.services.ReclamationService;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class ShowAllController implements Initializable {
    
    public static Reclamation currentReclamation;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public TextField searchTF;
    @FXML
    public Button excelButton;

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
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_RECLAMATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#userText")).setText("User : " + reclamation.getUser());
            ((Text) innerContainer.lookup("#titreText")).setText("Titre : " + reclamation.getTitre());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + reclamation.getDescription());
            
            Path selectedImagePath = FileSystems.getDefault().getPath(reclamation.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }
            
            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierReclamation(reclamation));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerReclamation(reclamation));
            ((Button) innerContainer.lookup("#pdfButton")).setOnAction((event) -> genererPDF(reclamation));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }
    
    @FXML
    private void ajouterReclamation(ActionEvent event) {
        currentReclamation = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_RECLAMATION);
    }

    private void modifierReclamation(Reclamation reclamation) {
        currentReclamation = reclamation;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_RECLAMATION);
    }

    private void supprimerReclamation(Reclamation reclamation) {
        currentReclamation = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer reclamation ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            if (ReclamationService.getInstance().delete(reclamation.getId())) {
                MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RECLAMATION);
            } else {
                AlertUtils.makeError("Could not delete reclamation");
            }
        }
    }
    
    
    @FXML
    private void search(KeyEvent event) {
        displayData(searchTF.getText());
    }
    
    private void genererPDF(Reclamation reclamation) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("reclamation.pdf")));
            document.open();

            com.itextpdf.text.Font font = new com.itextpdf.text.Font();
            font.setSize(20);

            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(reclamation.getImage());
            image.scaleAbsoluteWidth(300);
            image.scaleAbsoluteHeight(300);
            image.isScaleToFitHeight();

            document.add(new Paragraph("- Reclamation -"));
            document.add(image);
            document.add(new Paragraph("Titre : " + reclamation.getTitre()));
            document.add(new Paragraph("Description : " + reclamation.getDescription()));
            document.add(new Paragraph("Utilisateur : " + reclamation.getUser().getName()));
            document.newPage();
            document.close();

            writer.close();

            Desktop.getDesktop().open(new File("reclamation.pdf"));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

    public void genererExcel(ActionEvent actionEvent) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            FileChooser chooser = new FileChooser();
            // Set extension filter
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel Files(.xls)", ".xls");
            chooser.getExtensionFilters().add(filter);

            HSSFSheet workSheet = workbook.createSheet("sheet 0");
            workSheet.setColumnWidth(1, 25);

            HSSFFont fontBold = workbook.createFont();
            fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFCellStyle styleBold = workbook.createCellStyle();
            styleBold.setFont(fontBold);

            Row row1 = workSheet.createRow((short) 0);
            workSheet.autoSizeColumn(7);
            row1.createCell(0).setCellValue("Id");
            row1.createCell(1).setCellValue("Titre");
            row1.createCell(2).setCellValue("Description");
            row1.createCell(3).setCellValue("Utilisateur");

            int i = 0;
            for (Reclamation reclamation : listReclamation) {
                i++;
                Row row2 = workSheet.createRow((short) i);
                row2.createCell(0).setCellValue(reclamation.getId());
                row2.createCell(1).setCellValue(reclamation.getTitre());
                row2.createCell(2).setCellValue(reclamation.getDescription());
                row2.createCell(3).setCellValue(reclamation.getUser().getName());

            }

            workbook.write(Files.newOutputStream(Paths.get("reclamation.xls")));
            Desktop.getDesktop().open(new File("reclamation.xls"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
