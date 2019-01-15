package edu.java.lab2.controllers;

import edu.java.lab2.interfaces.impls.UserList;
import edu.java.lab2.objects.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegDialogController {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtPasswordCheck;

    @FXML
    private Label lbError;


    private static UserList userList;

    public static void setUserList(UserList userList) {
        RegDialogController.userList = userList;
    }


    public void actionConfirm(ActionEvent actionEvent){

        User user = new User();

        if (textCheck()) {
            user.setName(txtName.getText());
            user.setLogin(txtLogin.getText());
            user.setPassword(txtPassword.getText());
            user.setRegDate(new SimpleDateFormat().format(new Date()));
            user.setAccessRight("User");

            userList.add(user);

            FileWriter fr = null;
            try {
                fr = new FileWriter("./src/edu/java/lab2/access/userList.txt", true);
                fr.append(user.getName() + System.lineSeparator());
                fr.append(user.getLogin() + System.lineSeparator());
                fr.append(user.getPassword() + System.lineSeparator());
                fr.append(user.getRegDate() + System.lineSeparator());
                fr.append(user.getAccessRight() + System.lineSeparator());
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

            actionClose(actionEvent);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Пользователь зарегистрирован!");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait().ifPresent(rs -> {});
        }
    }
    public void actionClose(ActionEvent actionEvent) {

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    private boolean textCheck() {

        txtName.setStyle("");
        txtLogin.setStyle("");
        txtPassword.setStyle("");
        txtPasswordCheck.setStyle("");
        lbError.setText("");

        // Name
        String regEx = "[A-Z]{1}[a-zA-Z]{1,14}";
        if (txtName.getText().isEmpty() || !txtName.getText().matches(regEx)) {

            txtName.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
            lbError.setText("Имя пользователя должно начинаться с заглавной буквы\n" +
                    "и иметь длину от 2 до 15 символов,\n" +
                    "может содержать только буквы английского алфавита!");
            return false;
        }

        // Login
        regEx = "^[a-zA-Z][a-zA-Z0-9-_\\.]{4,20}$";
        if (txtLogin.getText().isEmpty() || !txtLogin.getText().matches(regEx)) {

            txtLogin.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
            lbError.setText("Логин должен начинаться с буквы\n" +
                    "и иметь длину от 4 до 20 символов,\n" +
                    "может содержать только цифры и буквы английского алфавита!");
            return false;
        }

        // Password
        regEx = "[a-zA-Z0-9]{4,16}";
        if (txtPassword.getText().isEmpty() || !txtPassword.getText().matches(regEx)) {

            txtPassword.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
            lbError.setText("Пароль должен иметь длину от 4 до 16 символов,\n" +
                    "может содержать только цифры и буквы английского алфавита!");
            return false;
        }

        if(!txtPassword.getText().equals(txtPasswordCheck.getText())){
            txtPassword.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
            txtPasswordCheck.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
            lbError.setText("Пароли не совпадают!");
            return false;
        }

        return true;

    }
}
