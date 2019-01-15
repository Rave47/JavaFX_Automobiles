package edu.java.lab2.controllers;

import edu.java.lab2.objects.Operation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class OperationsDialogController {

    @FXML
    private TableView operationsTable;
    @FXML
    private TableColumn<Operation, String> columnUser;
    @FXML
    private TableColumn<Operation, String> columnFile;
    @FXML
    private TableColumn<Operation, String> columnDate;
    @FXML
    private TableColumn<Operation, String> columnInfo;
    @FXML
    private TextField txtSearch;



    private ObservableList<Operation> list = FXCollections.observableArrayList();

    public void initialize(){

        File log = new File("./src/edu/java/lab2/access/userLog.txt");

        if (log != null) {
            try {
                Scanner scan = new Scanner(log);

                while (scan.hasNext()) {
                    Operation crt = new Operation();

                    crt.setUser(scan.nextLine());
                    crt.setFile(scan.nextLine());
                    crt.setDate(scan.nextLine());
                    crt.setInfo(scan.nextLine());
                    scan.nextLine();
                    list.add(crt);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        columnUser.setCellValueFactory(new PropertyValueFactory<Operation, String>("user"));
        columnFile.setCellValueFactory(new PropertyValueFactory<Operation, String>("file"));
        columnDate.setCellValueFactory(new PropertyValueFactory<Operation, String>("date"));
        columnInfo.setCellValueFactory(new PropertyValueFactory<Operation, String>("info"));

        operationsTable.setItems(list);


        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter();
        });
    }

    private void filter() {
        if (txtSearch.getText().isEmpty()) {
            operationsTable.setItems(list);
        }

        List<Operation> filtered = list.stream()
                .filter(p -> p.getUser().contains(txtSearch.getText()) || p.getFile().contains(txtSearch.getText())).collect(Collectors.toList());

        ObservableList<Operation> result = FXCollections.observableArrayList(filtered);
        operationsTable.setItems(result);
    }

}
