package edu.java.lab2.controllers;

import edu.java.lab2.interfaces.impls.UserList;
import edu.java.lab2.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class LogInController {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lbError;


    private Parent root;
    private Stage loginStage;
    private Stage mainStage;
    private Stage regStage;
    private MainController mainController;
    private RegDialogController regDialogController;

    private UserList userList = new UserList();
    private User currentUser = null;

    public void setStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    public void initialize(){
        loadUserList();

    }

    public void login(){

        currentUser = null;
        lbError.setText("");
        txtLogin.setStyle("");
        txtPassword.setStyle("");

        if(!txtLogin.getText().isEmpty()) {

            for (User crt : userList.getUserList()) {
                if (crt.getLogin().equals(txtLogin.getText())) {
                    currentUser = crt;
                    continue;
                }
            }
            if (currentUser != null) {
                if (currentUser.getPassword().equals(txtPassword.getText())) {
                    if (!currentUser.getAccessRight().equals("Banned")) {
                        lbError.setText("");
                        txtLogin.setStyle("");
                        txtPassword.setStyle("");

                        writeUserLog("Logged in");

                        showMainWindow();

                    } else {
                        lbError.setText("Пользователь с этим логином заблокирован!");
                    }
                } else {
                    lbError.setText("Неверный пароль!");
                    txtPassword.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
                }
            } else {
                lbError.setText("Пользователя с таким логином не существует!");
                txtLogin.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
            }
        } else {
            lbError.setText("Введите логин");
            txtLogin.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
        }

    }
    public void registration(){
        showRegDialog();
    }

    private void loadUserList() {

        File users = new File("./src/edu/java/lab2/access/userList.txt");

        if (users != null) {
            try {
                Scanner scan = new Scanner(users);

                while (scan.hasNext()) {

                    User user = new User();
                    user.setName(scan.nextLine());
                    user.setLogin(scan.nextLine());
                    user.setPassword(scan.nextLine());
                    user.setRegDate(scan.nextLine());
                    user.setAccessRight(scan.nextLine());

                    userList.add(user);

                    scan.nextLine();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    private void showMainWindow(){

        userList.setCurrentUser(currentUser);
        mainController.setUserList(userList);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/MainWindow.fxml"));
            root = fxmlLoader.load();
            mainController = fxmlLoader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStage = new Stage();
        mainController.setMainStage(mainStage);
        mainStage.setTitle("Автомобильный реестр 2.0");
        mainStage.getIcons().add(new Image("edu/java/lab2/img/car.png"));
        mainStage.setMinHeight(400);
        mainStage.setMinWidth(505);
        mainStage.setScene(new Scene(root, 920, 600));
        mainStage.initModality(Modality.WINDOW_MODAL);
        mainStage.initOwner(loginStage);
        mainStage.setOnCloseRequest(e -> {
            e.consume();
            mainController.closeProgram();
        });

        loginStage.hide();
        mainStage.showAndWait();

    }
    private void showRegDialog(){

        regDialogController.setUserList(userList);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/RegDialog.fxml"));
            root = fxmlLoader.load();
            regDialogController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        regStage = new Stage();
        regStage.setTitle("");
        regStage.getIcons().add(new Image("edu/java/lab2/img/car.png"));
        regStage.setResizable(false);
        regStage.setScene(new Scene(root, 500, 340));
        regStage.initModality(Modality.WINDOW_MODAL);
        regStage.initOwner(loginStage);

        regStage.showAndWait();
    }


    private void writeUserLog(String message){
        FileWriter fr = null;
        try {
            fr = new FileWriter("./src/edu/java/lab2/access/userlog.txt", true);
            fr.append(currentUser.getName()+ " (" + currentUser.getLogin() + ")" + System.lineSeparator());
            fr.append(" " + System.lineSeparator());
            fr.append(new SimpleDateFormat().format(new Date()) + System.lineSeparator());
            fr.append(message);
            fr.write(System.lineSeparator());
            fr.write(System.lineSeparator());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
