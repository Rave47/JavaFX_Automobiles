package edu.java.lab2.controllers;

import edu.java.lab2.objects.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserEditController {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtLogin;
    @FXML
    private TextField txtPassword;
    @FXML
    private ComboBox cbAccess;
    @FXML
    private Label lbError;


    private static User crtUser;

    public static void setCrtUser(User crtUser) {

        UserEditController.crtUser = crtUser;
    }



    public void initialize(){

        txtName.setText(crtUser.getName());
        txtLogin.setText(crtUser.getLogin());
        txtPassword.setText(crtUser.getPassword());
        cbAccess.getItems().addAll("Admin", "User", "Banned");
        cbAccess.setValue(crtUser.getAccessRight());
    }


    private boolean textCheck() {

        txtName.setStyle("");
        txtLogin.setStyle("");
        txtPassword.setStyle("");
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

        return true;

    }

    public void actionConfirm(ActionEvent actionEvent){

        if (textCheck()) {

            crtUser.setName(txtName.getText());
            crtUser.setLogin(txtLogin.getText());
            crtUser.setPassword(txtPassword.getText());
            crtUser.setAccessRight((String)cbAccess.getValue());

            actionClose(actionEvent);
        }
    }
    public void actionClose(ActionEvent actionEvent) {

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
