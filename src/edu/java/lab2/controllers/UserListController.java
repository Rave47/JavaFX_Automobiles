package edu.java.lab2.controllers;

import edu.java.lab2.interfaces.impls.UserList;
import edu.java.lab2.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class UserListController {

    @FXML
    private TableView userTable;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnLogin;
    @FXML
    private TableColumn columnPassword;
    @FXML
    private TableColumn columnAccess;
    @FXML
    private TableColumn columnDate;
    @FXML
    private Label lbStatus;
    @FXML
    private Button butEdit;
    @FXML
    private Button butDelete;
    @FXML
    private TextField txtSearch;


    private Parent root;

    private Stage regStage;
    private Stage ueStage;
    private RegDialogController regDialogController;
    private UserEditController userEditController;


    private static UserList userList;

    public static void setUserList(UserList newList) {
        userList = newList;
    }


    public void initialize(){

        // Set table configuration
        columnName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        columnLogin.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        columnPassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        columnAccess.setCellValueFactory(new PropertyValueFactory<User, String>("accessRight"));
        columnDate.setCellValueFactory(new PropertyValueFactory<User, String>("regDate"));

        userTable.setItems(userList.getUserList());

        lbStatus.setText("Всего объектов: " + userList.getUserList().size());
        if(userList.getUserList().size() > 1){
            txtSearch.setDisable(false);
        }

        listeners();
    }
    private void listeners(){

        userList.getUserList().addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends User> c) {
                lbStatus.setText("Всего объектов: " + userList.getUserList().size());
                userTable.setItems(userList.getUserList());
                txtSearch.setDisable(userList.getUserList().size() < 2);

            }
        });

        userTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                butEdit.setDisable(userTable.getSelectionModel().getSelectedItem() == null);
                butDelete.setDisable(userTable.getSelectionModel().getSelectedItem() == null);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter();
        });

        userTable.setRowFactory( tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    editUser();
                }
            });
            return row ;
        });

    }
    private void filter() {
        if (txtSearch.getText().isEmpty()) {
            userTable.setItems(userList.getUserList());
        }

        List<User> filtered = userList.getUserList().stream()
                .filter(p -> p.getName().contains(txtSearch.getText()) || p.getLogin().contains(txtSearch.getText())
                || p.getPassword().contains(txtSearch.getText()) || p.getAccessRight().contains(txtSearch.getText())
                || p.getRegDate().contains(txtSearch.getText())).collect(Collectors.toList());

        ObservableList<User> result = FXCollections.observableArrayList(filtered);
        userTable.setItems(result);
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
        regStage.initModality(Modality.APPLICATION_MODAL);

        regStage.showAndWait();
    }
    private void showUserEditDialog(){

        userEditController.setCrtUser((User)userTable.getSelectionModel().getSelectedItem());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/UserEditDialog.fxml"));
            root = fxmlLoader.load();
            userEditController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ueStage = new Stage();
        ueStage.setTitle("");
        ueStage.getIcons().add(new Image("edu/java/lab2/img/car.png"));
        ueStage.setResizable(false);
        ueStage.setScene(new Scene(root, 460, 340));
        ueStage.initModality(Modality.APPLICATION_MODAL);

        ueStage.showAndWait();
    }

    public void addUser(){
        regDialogController.setUserList(userList);
        showRegDialog();
    }
    public void editUser(){

        showUserEditDialog();
        userTable.refresh();
        userList.save();
    }
    public void removeUser(){

        if(userList.getCurrentUser() == userTable.getSelectionModel().getSelectedItem()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Невозможно удалить учетную запись, от имени которой запущено приложение!");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait().ifPresent(rs -> { });

        } else {

            if(((User)userTable.getSelectionModel().getSelectedItem()).getAccessRight().contains("Main")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("Невозможно удалить учетную запись главного администратора!");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait().ifPresent(rs -> { });
            } else {
                userList.delete((User) userTable.getSelectionModel().getSelectedItem());
                userList.save();
            }
        }
    }

}
