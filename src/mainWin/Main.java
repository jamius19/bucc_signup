package mainWin;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {
    private static double xOffset = 0;
    private static double yOffset = 0;

    FadeTransition[] ft = new FadeTransition[5];

    private boolean submitOk, nameOk, idOk, deptOk, emailOk, phoneOk;

    private String nameI, idI, deptI, emailI, phoneI;

    @FXML
    private ImageView buttonS, minimize, back2, buttonGray, close, settings, about, done, over;
    @FXML
    private TextField name, id, dept, email, phone;
    @FXML
    private ProgressIndicator loader1, loader2, loader3, loader4, loader5;
    @FXML
    private Button subButton;
    @FXML
    private ImageView tick1, tick2, tick3, tick4, tick5;
    @FXML
    private ProgressIndicator loaderMain;
    @FXML
    private Label submitLabel, textAbout;

    private Stage ap1;

    private boolean emailAuto = false;
    public static boolean writingInProcess;

    public static Main main;

    private boolean[] on = new boolean[5];

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ******* Submit Button **********
        Image onButtonMouseOver = new Image("/images/confimButtonHover.png");
        Image onButtonMouseExit = new Image("/images/confimButton.png");
        Image onDisable = new Image("/images/confimGrayed.png");

        subButton.setOnMouseEntered(event -> {
            buttonS.setImage(onButtonMouseOver);
        });

        subButton.setOnMouseExited(event -> {
            buttonS.setImage(onButtonMouseExit);
        });

        settings.setOnMouseClicked(event -> {
            if (event.isStillSincePress())
                new saveLocationPrompt().display();
        });


        FadeTransition aboutT = new FadeTransition(new Duration(500), textAbout);
        aboutT.setFromValue(0);
        aboutT.setToValue(1);
        aboutT.setCycleCount(1);
        aboutT.setAutoReverse(false);

        about.setOnMouseClicked(event -> {
            if (event.isStillSincePress()) {
                aboutT.stop();

                if (textAbout.getOpacity() == 0) {
                    aboutT.setFromValue(0);
                    aboutT.setToValue(1);
                } else if (textAbout.getOpacity() == 1) {
                    aboutT.setFromValue(1);
                    aboutT.setToValue(0);
                }

                aboutT.play();
            }
        });


        close.setOnMouseClicked(event -> {
            if (event.isStillSincePress())
                new Thread(() -> {
                    while (writingInProcess) ;
                    ((Stage) close.getScene().getWindow()).close();
                }).run();
        });

        buttonS.setOnMouseClicked(event -> {
            nameI = name.getText();
            idI = id.getText();
            deptI = dept.getText();
            emailI = email.getText();
            phoneI = phone.getText();
            submitClick();
        });

        FadeTransition ft1 = new FadeTransition();
        ft1.setFromValue(1);
        ft1.setToValue(0.62);
        ft1.setCycleCount(10000);
        ft1.setAutoReverse(true);
        ft1.setNode(back2);
        ft1.setDuration(new Duration(2000));
        ft1.play();

        FadeTransition ftS = new FadeTransition();
        ftS.setCycleCount(1);
        ftS.setAutoReverse(false);
        ftS.setNode(buttonGray);
        ftS.setDuration(new Duration(500));


        FadeTransition ftD = new FadeTransition();
        ftD.setCycleCount(2);
        ftD.setFromValue(0);
        ftD.setToValue(1);
        ftD.setAutoReverse(true);
        ftD.setNode(done);
        ftD.setDuration(new Duration(1500));
        // ********************************
        subButton.setOnMouseClicked(event -> {
            if (submitOk) {
                writingInProcess = true;
                XLSWriter write = new XLSWriter(name.getText(), id.getText(), dept.getText(), email.getText(), phone.getText());
                name.setText("");
                id.setText("");
                dept.setText("");
                email.setText("");
                phone.setText("");

                submitOk = nameOk = idOk = deptOk = emailOk = emailAuto = phoneOk = false;

                for (int i = 0; i < on.length; i++) {
                    on[i] = false;
                }

                for (int i = 0; i < 5; i++) {
                    FTHandler(i, 1, 0);
                }

                ftS.stop();
                ftS.setFromValue(0);
                ftS.setToValue(1);
                ftS.play();

                Thread a = new Thread(write);
                a.start();
                ftD.stop();
                ftD.play();
            }
        });

        loader1.setOpacity(0);
        loader2.setOpacity(0);
        loader3.setOpacity(0);
        loader4.setOpacity(0);
        loader5.setOpacity(0);

        tick1.setVisible(false);
        tick2.setVisible(false);
        tick3.setVisible(false);
        tick4.setVisible(false);
        tick5.setVisible(false);

        for (int i = 0; i < ft.length; i++) {
            ft[i] = new FadeTransition();
            ft[i].setDuration(new Duration(200));
            ft[i].setCycleCount(1);
        }


        ft[0].setNode(loader1);
        ft[1].setNode(loader2);
        ft[2].setNode(loader3);
        ft[3].setNode(loader4);
        ft[4].setNode(loader5);

        minimize.setOnMouseClicked(event -> {
            if (event.isStillSincePress())
                ((Stage) ((ImageView) event.getSource()).getScene().getWindow()).setIconified(true);
        });


        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\D*")) {
                name.setText(newValue.replaceAll("[0-9]", ""));
            }

            nameOk = nameOk(name.getText());
            on[0] = nameOk;
            loader1.setVisible(!nameOk);
            tick1.setVisible(nameOk);

            if (newValue.length() != 0) {
                String set = "";
                String current = name.getText();

                set += Character.toUpperCase(current.charAt(0));

                for (int i = 1; i < current.length(); i++) {
                    if (current.charAt(i - 1) == ' ') {
                        set += Character.toUpperCase(current.charAt(i));
                    } else {
                        set += current.charAt(i);
                    }

                }
                name.setText(set);
            }

            loaderMain.setProgress(getProgess());
            if (getProgess() == 1f) {
                if (buttonGray.getOpacity() != 0) {
                    ftS.setFromValue(1);
                    ftS.setToValue(0);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#FFFFFF"));
            } else {
                if (buttonGray.getOpacity() != 1) {
                    ftS.setFromValue(1);
                    ftS.setToValue(0);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#a4a4a4"));
            }

            name.setText(name.getText().substring(0, Math.min(32, name.getText().length())));
        });

        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                name.setText(name.getText().trim());
            }
        });

        id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                id.setText(newValue.replaceAll("[^\\d]", ""));
            }

            idOk = idOk(id.getText());
            on[1] = idOk;
            loader2.setVisible(!idOk);
            tick2.setVisible(idOk);

            if (idOk) {
                int number = Integer.parseInt(newValue.substring(3, 5));
                switch (number) {
                    case 1:
                        dept.setText("CSE");
                        break;
                    case 21:
                        dept.setText("EEE");
                        break;
                    case 9:
                        dept.setText("LAW");
                        break;
                    case 10:
                        dept.setText("EEE");
                        break;
                    case 36:
                        dept.setText("MNS");
                        break;
                    case 3:
                        dept.setText("ENH");
                        break;
                    case 41:
                        dept.setText("CSE");
                        break;
                }
            } else {
                dept.setText("");
            }
            loaderMain.setProgress(getProgess());
            if (getProgess() == 1f) {
                if (buttonGray.getOpacity() != 0) {
                    ftS.setFromValue(1);
                    ftS.setToValue(0);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#FFFFFF"));
            } else {
                if (buttonGray.getOpacity() != 1) {
                    ftS.setFromValue(0);
                    ftS.setToValue(1);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#a4a4a4"));
            }

            id.setText(id.getText().substring(0, Math.min(8, id.getText().length())));
        });

        dept.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\W*")) {
                dept.setText(newValue.replaceAll("[0-9]", ""));
            }

            deptOk = deptOk(dept.getText());
            on[2] = deptOk;
            loader3.setVisible(!deptOk);
            tick3.setVisible(deptOk);

            dept.setText(dept.getText().toUpperCase());
            loaderMain.setProgress(getProgess());
            if (getProgess() == 1f) {
                if (buttonGray.getOpacity() != 0) {
                    ftS.setFromValue(1);
                    ftS.setToValue(0);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#FFFFFF"));
            } else {
                if (buttonGray.getOpacity() != 1) {
                    ftS.setFromValue(0);
                    ftS.setToValue(1);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#a4a4a4"));
            }

            dept.setText(dept.getText().substring(0, Math.min(4, dept.getText().length())));
        });


        email.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                emailAuto = false;
            }
        });

        email.textProperty().addListener((observable, oldValue, newValue) -> {
            emailOk = emailOk(email.getText());
            on[3] = emailOk;
            loader4.setVisible(!emailOk);
            tick4.setVisible(emailOk);

            if (newValue.length() >= 3) {
                String current = email.getText();
                int pos = current.length();
                email.requestFocus();

                if (current.charAt(current.length() - 2) == '@' && !emailAuto) {
                    if (current.charAt(current.length() - 1) == 'g')
                        email.setText(current + "mail.com");
                    else if (current.charAt(current.length() - 1) == 'y')
                        email.setText(current + "ahoo.com");
                    else if (current.charAt(current.length() - 1) == 'h')
                        email.setText(current + "otmail.com");
                    else if (current.charAt(current.length() - 1) == 'l')
                        email.setText(current + "ive.com");
                    else if (current.charAt(current.length() - 1) == 'o')
                        email.setText(current + "utlook.com");
                    else if (current.charAt(current.length() - 1) == 'm')
                        email.setText(current + "ail.com");
                    else if (current.charAt(current.length() - 1) == 'r')
                        email.setText(current + "ocketmail.com");

                    Platform.runLater(() -> {
                                email.selectRange(pos, email.getText().length());
                            }
                    );

                    emailAuto = true;
                }
                email.setText(email.getText().toLowerCase());
            }
            loaderMain.setProgress(getProgess());
            if (getProgess() == 1f) {
                if (buttonGray.getOpacity() != 0) {
                    ftS.setFromValue(1);
                    ftS.setToValue(0);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#FFFFFF"));
            } else {
                if (buttonGray.getOpacity() != 1) {
                    ftS.setFromValue(0);
                    ftS.setToValue(1);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#a4a4a4"));
            }
        });

        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phone.setText(newValue.replaceAll("[^\\d]", ""));
            }

            phoneOk = phoneOk(phone.getText());
            on[4] = phoneOk;
            loader5.setVisible(!phoneOk);
            tick5.setVisible(phoneOk);
            loaderMain.setProgress(getProgess());
            if (getProgess() == 1f) {
                if (buttonGray.getOpacity() != 0) {
                    ftS.setFromValue(1);
                    ftS.setToValue(0);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#FFFFFF"));
            } else {
                if (buttonGray.getOpacity() != 1) {
                    ftS.setFromValue(0);
                    ftS.setToValue(1);
                    ftS.play();
                }
                submitLabel.setTextFill(Color.web("#a4a4a4"));
            }

            phone.setText(phone.getText().substring(0, Math.min(13, phone.getText().length())));
        });


        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if (!newValue.equals(""))
                    name.selectAll();
            });

            if (newValue) {
                FTHandler(0, 0, 1);
                if (loader2.getOpacity() != 0 && !on[1])
                    FTHandler(1, 1, 0);
                if (loader3.getOpacity() != 0 && !on[2])
                    FTHandler(2, 1, 0);
                if (loader4.getOpacity() != 0 && !on[3])
                    FTHandler(3, 1, 0);
                if (loader5.getOpacity() != 0 && !on[4])
                    FTHandler(4, 1, 0);
            }
        });


        id.focusedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if (!newValue.equals(""))
                    id.selectAll();
            });
            if (newValue) {
                if (loader1.getOpacity() != 0 && !on[0])
                    FTHandler(0, 1, 0);
                FTHandler(1, 0, 1);
                if (loader3.getOpacity() != 0 && !on[2])
                    FTHandler(2, 1, 0);
                if (loader4.getOpacity() != 0 && !on[3])
                    FTHandler(3, 1, 0);
                if (loader5.getOpacity() != 0 && !on[4])
                    FTHandler(4, 1, 0);
            }
        });


        dept.focusedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if (!newValue.equals(""))
                    dept.selectAll();
            });
            if (newValue) {
                if (loader1.getOpacity() != 0 && !on[0])
                    FTHandler(0, 1, 0);
                if (loader2.getOpacity() != 0 && !on[1])
                    FTHandler(1, 1, 0);
                FTHandler(2, 0, 1);
                if (loader4.getOpacity() != 0 && !on[3])
                    FTHandler(3, 1, 0);
                if (loader5.getOpacity() != 0 && !on[4])
                    FTHandler(4, 1, 0);
            }
        });


        email.focusedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if (!newValue.equals(""))
                    email.selectAll();
            });
            if (newValue) {
                if (loader1.getOpacity() != 0 && !on[0])
                    FTHandler(0, 1, 0);
                if (loader2.getOpacity() != 0 && !on[1])
                    FTHandler(1, 1, 0);
                if (loader3.getOpacity() != 0 && !on[2])
                    FTHandler(2, 1, 0);
                FTHandler(3, 0, 1);
                if (loader5.getOpacity() != 0 && !on[4])
                    FTHandler(4, 1, 0);
            }
        });


        phone.focusedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if (!newValue.equals(""))
                    phone.selectAll();
            });
            if (newValue) {
                if (loader1.getOpacity() != 0 && !on[0])
                    FTHandler(0, 1, 0);
                if (loader2.getOpacity() != 0 && !on[1])
                    FTHandler(1, 1, 0);
                if (loader3.getOpacity() != 0 && !on[2])
                    FTHandler(2, 1, 0);
                if (loader4.getOpacity() != 0 && !on[3])
                    FTHandler(3, 1, 0);
                FTHandler(4, 0, 1);
            }
        });
    }

    private void FTHandler(int index, double... value) {
        ft[index].setFromValue(value[0]);
        ft[index].setToValue(value[1]);
        ft[index].play();
    }


    public boolean nameOk(String s) {
        boolean spaceF = false;
        for (int i = 0; i < s.length(); i++) {
            if ((s.charAt(i) == ' ' && i < s.length() - 1 && s.charAt(i + 1) != ' ')) {
                spaceF = true;
                break;
            }
        }

        if (spaceF) {
            boolean noNum = true;

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) >= 48 && s.charAt(i) <= 57) {
                    noNum = false;
                }
            }

            return noNum;
        }
        return false;
    }

    public boolean idOk(String s) {
        boolean allNum = true;

        if (s.length() >= 2) {
            int ck = Integer.parseInt(s.substring(0, 2));
            int year = Calendar.getInstance().get(Calendar.YEAR) - 2000;

            if (ck < 1 || ck > year)
                return false;
        }

        if (s.length() != 8)
            return false;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < 48 || s.charAt(i) > 57 || s.charAt(i) == ' ')
                allNum = false;
        }

        return allNum;
    }

    public boolean deptOk(String s) {
        if (s.length() == 3 || s.length() == 4)
            return true;

        return false;
    }

    public boolean emailOk(String s) {

        boolean atFound = false, afDot = false;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '@') {
                atFound = true;
                continue;
            }

            if (atFound && s.charAt(i) == '.' && i < s.length() - 1 && s.charAt(i + 1) >= 97 && s.charAt(i + 1) <= 122)
                return true;
        }

        return false;
    }

    public boolean phoneOk(String s) {

        boolean isNorm = false;

        if (s.length() >= 3 && s.charAt(0) == '0' && s.charAt(1) == '1' && (s.charAt(2) == '7' || s.charAt(2) == '8' || s.charAt(2) == '9' || s.charAt(2) == '5' ||
                s.charAt(2) == '6' || s.charAt(2) == '1')) {
            isNorm = true;
        }
        ;

        if (s.length() >= 3) {
            if (isNorm && s.length() == 11)
                return true;
            else if (!isNorm && s.length() <= 13)
                return true;
        }

        return false;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("BUCC Freshers Signup");
        Scene mainWindowsScene = new Scene(root, 1110, 661);
        primaryStage.setScene(mainWindowsScene);
        mainWindowsScene.setFill(null);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

        primaryStage.getIcons().add(new Image("/images/signup-icon.png"));

        primaryStage.initStyle(StageStyle.TRANSPARENT);

        ImageView a1 = (ImageView) mainWindowsScene.lookup("#over");

        FadeTransition ft = new FadeTransition(new Duration(1200), a1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);


        root.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });


        primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            ft.stop();

            if (newValue) {
                a1.mouseTransparentProperty().set(true);
                ft.setFromValue(a1.getOpacity());
                ft.setToValue(0);
            } else {
                a1.mouseTransparentProperty().set(false);
                ft.setFromValue(a1.getOpacity());
                ft.setToValue(1);
            }

            ft.play();
        });

        a1.setOpacity(0);
        primaryStage.show();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                new saveLocationPrompt().display();
            }
        });

    }

    float getProgess() {
        float count = 0f;

        count = nameOk ? count + 1f : count;
        count = idOk ? count + 1f : count;
        count = deptOk ? count + 1f : count;
        count = emailOk ? count + 1f : count;
        count = phoneOk ? count + 1f : count;

        submitOk = count == 5f ? true : false;

        count = count / 5f;
        return count == 0f ? -1f : count;
    }

    private boolean submitClick() {
        return false;
    }


}
