package mainWin;


import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import javafx.util.Duration;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class saveLocationPrompt implements Initializable {
    private static double xOffset = 0;
    private static double yOffset = 0;

    @FXML
    private AnchorPane createAP, createAP1;
    @FXML
    private RadioButton create, open;
    @FXML
    private Button locateButton, createButton, createButton1;
    @FXML
    private Label locationText, autoText, autoText1, notValid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        create.setSelected(true);
        FadeTransition ft = new FadeTransition();
        ft.setDuration(new Duration(300));
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.setNode(createAP);

        notValid.setVisible(false);

        FadeTransition ft1 = new FadeTransition();
        ft1.setDuration(new Duration(300));
        ft1.setCycleCount(1);
        ft1.setAutoReverse(false);
        ft1.setNode(createAP1);

        String year = (Calendar.getInstance().get(Calendar.YEAR) - 2000) + "";
        int month = Calendar.getInstance().get(Calendar.MONTH);
        // System.getProperty("user.home") + "/Desktop/BUCC.xls

        if (month >= 0 && month <= 3)
            year = "Spring" + year;
        else if (month <= 7)
            year = "Summer" + year;
        else if (month <= 11)
            year = "Fall" + year;

        String fileName = "BUCC_" + year + ".xls";

        File fileT = new File(System.getProperty("user.home") + "/Desktop/" + fileName);
        createAP1.mouseTransparentProperty().set(true);

        if (fileT.exists()) {
            autoText1.setText("Automatically opens the file named\n" + fileName + " from your desktop folder.");
        }

        createButton1.setOnMouseClicked(event -> {
            boolean valid = true;
            try {
                FileInputStream fin = new FileInputStream(fileT.toString());
                HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(fin));


                if (workbook.getNumberOfSheets() != 0) {
                    HSSFSheet worksheet = workbook.getSheet("Registered");

                    HSSFCell ce0 = worksheet.getRow(0).getCell(0);
                    HSSFCell ce1 = worksheet.getRow(0).getCell(1);
                    HSSFCell ce2 = worksheet.getRow(0).getCell(2);
                    HSSFCell ce3 = worksheet.getRow(0).getCell(3);
                    HSSFCell ce4 = worksheet.getRow(0).getCell(4);
                    HSSFCell ce10 = worksheet.getRow(0).getCell(10);

                    if (!ce0.getStringCellValue().equalsIgnoreCase("name") || !ce1.getStringCellValue().equalsIgnoreCase("id") ||
                            !ce2.getStringCellValue().equalsIgnoreCase("Department") || !ce3.getStringCellValue().equalsIgnoreCase("Email") ||
                            !ce4.getStringCellValue().equalsIgnoreCase("Phone") || !ce10.getStringCellValue().equalsIgnoreCase("Count")) {
                        valid = false;
                        notValid.setVisible(true);
                    }
                } else {
                    valid = false;
                    notValid.setVisible(true);
                }

            } catch (Exception e) {
                valid = false;
                notValid.setVisible(true);
            }

            if (valid) {
                XLSWriter.file = fileT;
                ((Stage) createButton.getScene().getWindow()).close();
            }
        });

        create.setOnMouseClicked(event -> {
            open.setSelected(false);
            locateButton.setText("Create File");
            ft.stop();
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

            if (fileT.exists()) {
                ft1.stop();
                createAP1.mouseTransparentProperty().set(true);
                ft1.setFromValue(1);
                ft1.setToValue(0);
                ft1.play();
            }
        });

        open.setOnMouseClicked(event -> {
            create.setSelected(false);
            locateButton.setText("Select File");
            ft.stop();
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();

            if (fileT.exists()) {
                ft1.stop();
                createAP1.mouseTransparentProperty().set(false);
                ft1.setFromValue(0);
                ft1.setToValue(1);
                ft1.play();
            }
        });


        autoText.setText("Automatically creates a file named\n" + fileName + " in your desktop folder.");

        createButton.setOnMouseClicked(event -> {
            if(fileT.exists() && !open.isSelected())
                XLSWriter.append = false;

            XLSWriter.file = fileT;
            ((Stage) createButton.getScene().getWindow()).close();
        });

        locateButton.setOnMouseClicked(event -> {
            FileChooser file = new FileChooser();
            Window a = (Stage) ((Button) event.getSource()).getScene().getWindow();
            file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls"));
            File file1;

            if (open.isSelected())
                file1 = file.showOpenDialog(a);
            else
                file1 = file.showSaveDialog(a);

            XLSWriter.file = file1;
            locationText.setText(file1.toString());

            if(file1.exists() && !open.isSelected())
                XLSWriter.append = false;

            boolean valid = true;

            if (open.isSelected()) {
                try {
                    FileInputStream fin = new FileInputStream(file1.toString());
                    HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(fin));


                    if (workbook.getNumberOfSheets() != 0) {
                        HSSFSheet worksheet = workbook.getSheet("Registered");

                        HSSFCell ce0 = worksheet.getRow(0).getCell(0);
                        HSSFCell ce1 = worksheet.getRow(0).getCell(1);
                        HSSFCell ce2 = worksheet.getRow(0).getCell(2);
                        HSSFCell ce3 = worksheet.getRow(0).getCell(3);
                        HSSFCell ce4 = worksheet.getRow(0).getCell(4);
                        HSSFCell ce10 = worksheet.getRow(0).getCell(10);

                        if (!ce0.getStringCellValue().equalsIgnoreCase("name") || !ce1.getStringCellValue().equalsIgnoreCase("id") ||
                                !ce2.getStringCellValue().equalsIgnoreCase("Department") || !ce3.getStringCellValue().equalsIgnoreCase("Email") ||
                                !ce4.getStringCellValue().equalsIgnoreCase("Phone") || !ce10.getStringCellValue().equalsIgnoreCase("Count")) {
                            valid = false;
                            notValid.setVisible(true);
                        }
                    } else {
                        valid = false;
                        notValid.setVisible(true);
                    }

                } catch (Exception e) {
                    valid = false;
                    notValid.setVisible(true);
                }
            }

            if (valid) {
                ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
            }
        });
    }

    public void display() {
        Stage primaryStage = new Stage();
        AnchorPane root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("saveLocationPrompt.fxml"));
        } catch (IOException e) {
        }
        primaryStage.setTitle("File Location");
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().add(new Image("images/settingsIcon.png"));
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        root.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });

        Scene mainWindowsScene = new Scene(root, 874, 420);
        mainWindowsScene.setFill(null);
        primaryStage.setResizable(false);
        primaryStage.setScene(mainWindowsScene);
        primaryStage.show();
    }
}
