package edu.java.lab2.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class ConfirmDialogController {

    @FXML
    private Label lbPath;

    private boolean checkOnSave;
    private boolean checkOnClose;

    public static final Logger log = Logger.getLogger(MainController.class);

    public boolean getCheckOnSave() {
        return checkOnSave;
    }

    public boolean getCheckOnClose() {
        return checkOnClose;
    }

    public void setLbPathText(String path) {

        this.lbPath.setText(path);
    }

    public void actionSave(ActionEvent actionEvent){

        checkOnSave = true;
        checkOnClose = true;
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        log.info("Property Dialog closed");
    }

    public void actionNotSave(ActionEvent actionEvent){

        checkOnSave = false;
        checkOnClose = true;
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        log.info("Property Dialog closed");
    }

    public void actionClose(ActionEvent actionEvent) {

        checkOnSave = false;
        checkOnClose = false;
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        log.info("Property Dialog closed");
    }

}
