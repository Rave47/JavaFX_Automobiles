package edu.java.lab2.controllers;

import edu.java.lab2.interfaces.impls.UserList;
import edu.java.lab2.objects.Automobile;

import edu.java.lab2.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;


public class PropertyDialogController {

    private final ObservableList<String> optionsBirthplace = FXCollections.observableArrayList(
            "Russia", "USA", "China", "Germany", "Japan", "South Korea", "USSR",
            "India", "Spain", "Canada", "France", "Brazil", "United Kingdom", "Italy", "Slovakia",
            "Poland", "Austria", "Portugal", "Finland"
    );


    // Labels
    @FXML
    private Label lbName;

    // Textfields
    @FXML
    private TextField txtProducer;

    @FXML
    private TextField txtModel;

    //ComboBoxes
    @FXML
    private ComboBox comboxRelease;
    @FXML
    private ComboBox comboxBirthplace;
    @FXML
    private ComboBox comboxOwner;



    private Automobile automobile;
    private boolean check;


    public static final Logger log = Logger.getLogger(PropertyDialogController.class);


    private static UserList userList;
    public static void setUserList(UserList userList) {
        PropertyDialogController.userList = userList;
    }


    @FXML
    private void initialize(){

        for(int i = 2017; i > 1948; --i){
            comboxRelease.getItems().add(String.valueOf(i));
        }

        comboxBirthplace.getItems().addAll(optionsBirthplace.sorted());

        comboxOwner.getItems().add(" ");
        for(User crt : userList.getUserList()){
            comboxOwner.getItems().add(crt.getLogin());
        }


        if(!userList.getCurrentUser().getAccessRight().contains("Admin")){
           comboxOwner.setDisable(true);
        } else {
            comboxOwner.setDisable(false);
        }
    }

    public boolean getCheck(){
        return check;
    }
    public Automobile getAutomobile(){
        return automobile;
    }


    public void setLbNameText(String name) {
        this.lbName.setText(name);
    }
    public void setFields(Automobile automobile){
        check = false;
        if(automobile == null){
            return;
        }
        this.automobile = automobile;
        if(automobile.getProducer() == null){ txtProducer.setText("");}
        else { txtProducer.setText(automobile.getProducer());}
        if(automobile.getModel() == null){ txtModel.setText("");}
        else {txtModel.setText(automobile.getModel());}
        if(automobile.getRelease() == null){
            comboxRelease.setValue("");}
        else {comboxRelease.setValue(automobile.getRelease());}
        if(automobile.getBirthplace() == null){ comboxBirthplace.setValue("");}
        else {comboxBirthplace.setValue(automobile.getBirthplace());}
        if(automobile.getOwner() == null){
            comboxOwner.setValue(" ");
        } else {
            if (automobile.getOwner().equals(" ")){
                comboxOwner.setValue(" ");
            } else {
                for (User crt : userList.getUserList()) {
                    String tmp = getLoginfromString(automobile.getOwner());
                    if (crt.getLogin().equals(tmp)) {
                        comboxOwner.setValue(crt.getLogin());
                    }
                }
            }
        }
    }
    public void setDefaultStyles(){
        txtProducer.setStyle("");
        txtModel.setStyle("");
        comboxRelease.setStyle("");
        comboxBirthplace.setStyle("");
    }
    private boolean textCheck() {
        setDefaultStyles();
        boolean ch = true;
        String regEx = "[A-Z]{1}[a-z]{2,11}||[A-Z]{1}[a-z]{2,11}[ ][A-Z]{1}[a-z]{2,11}||[A-Z]{3,5}";
        if(txtProducer.getText().isEmpty() || !txtProducer.getText().matches(regEx)){

            txtProducer.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
            ch = false;
        }

        regEx = "[A-Z]{1}[a-z]{0,11}||[A-Z]{0,3}[1-9][0-9]{0,2}||[A-Z]{3}";
        if(txtModel.getText().isEmpty() || !txtModel.getText().matches(regEx)){

            txtModel.setStyle("-fx-text-box-border: red ; -fx-focus-color: red");
            ch = false;
        }

        if(comboxRelease.getValue().equals("")){

            comboxRelease.setStyle("-fx-border-color: red; -fx-focus-color: red");
            ch = false;
        }

        if(comboxBirthplace.getValue().equals("")){

            comboxBirthplace.setStyle("-fx-border-color: red; -fx-focus-color: red");
            ch = false;
        }

        return ch;
    }
    private String getLoginfromString(String str){
        int i;
        for(i = 0; i < str.length(); ++i){
            if((str.charAt(i)) == '('){
                break;
            }
        }
        return str.substring(i + 1, str.length() - 1);
    }
    private String getStringfromLogin(String str){
        for(User crt : userList.getUserList()){
            if(crt.getLogin().equals(str)){
                return (crt.getName() + " (" + str + ")");
            }
        }
        return null;
    }


    public void actionConfirm(ActionEvent actionEvent){

        if (textCheck()) {

            automobile.setProducer(txtProducer.getText());
            automobile.setModel(txtModel.getText());
            automobile.setRelease((String) comboxRelease.getValue());
            automobile.setBirthplace((String) comboxBirthplace.getValue());
            if(comboxOwner.getValue().equals(" ")){
                automobile.setOwner(" ");
            } else {
                automobile.setOwner(getStringfromLogin((String) comboxOwner.getValue()));
            }
            check = true;

            actionClose(actionEvent);
        }
    }
    public void actionClose(ActionEvent actionEvent) {
        setDefaultStyles();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        log.info("Property Dialog closed");
    }
}
