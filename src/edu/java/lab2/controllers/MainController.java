package edu.java.lab2.controllers;

import edu.java.lab2.interfaces.impls.AutoData;
import edu.java.lab2.interfaces.impls.UserList;
import edu.java.lab2.objects.Automobile;
import edu.java.lab2.thread.MultiThreadSearch;
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

import java.io.FileWriter;
import java.io.IOException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;


public class MainController {

    // Labels
    @FXML
    private Label lbStatus;
    @FXML
    private Label lbLastAction;
    @FXML
    private Label lbCrtUser;

    // MenuItems
    @FXML
    private MenuItem miSave;
    @FXML
    private MenuItem miSaveAs;
    @FXML
    private MenuItem miAdd;
    @FXML
    private MenuItem miEdit;
    @FXML
    private MenuItem miDelete;
    @FXML
    private MenuItem miDeleteAll;
    @FXML
    private MenuItem miReport;
    @FXML
    private Menu menuAdmin;


    // Buttons
    @FXML
    private Button butSave;
    @FXML
    private Button butAdd;
    @FXML
    public Button butEdit;
    @FXML
    private Button butDelete;
    @FXML
    private Button butOperations;
    @FXML
    private Button butUsers;
    @FXML
    private Button butReserve;

    // Textfields
    @FXML
    private TextField txtSearch;

    // Table
    @FXML
    private TitledPane titledPane;
    @FXML
    private TableView<Automobile> tableData;
    @FXML
    private TableColumn<Automobile, String> columnProducer;
    @FXML
    private TableColumn<Automobile, String> columnModel;
    @FXML
    private TableColumn<Automobile, String> columnRelease;
    @FXML
    private TableColumn<Automobile, String> columnBirthplace;
    @FXML
    private TableColumn<Automobile, String> columnOwner;



    private Parent root1;
    private Parent root2;

    private Stage mainStage;
    private Stage pdStage;
    private Stage cdStage;
    private Stage ulStage;
    private Stage odStage;

    private PropertyDialogController pdController;
    private ConfirmDialogController cdController;
    private UserListController ulController;
    private OperationsDialogController odController;


    private AutoData data = new AutoData();
    private static UserList userList;


    public static final Logger log = Logger.getLogger(MainController.class);


    public static void setUserList(UserList newList) {
        userList = newList;
    }
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }


    @FXML
    private void initialize(){

        log.info("Main Window opened");

        if(userList.getCurrentUser().getAccessRight().contains("Admin")){
            menuAdmin.setVisible(true);
            butOperations.setVisible(true);
            butUsers.setVisible(true);
        }
        lbCrtUser.setText("Пользователь - " + userList.getCurrentUser().getName() + " (" + userList.getCurrentUser().getLogin() + ")");

        // Set table configuration
        columnProducer.setCellValueFactory(new PropertyValueFactory<Automobile, String>("producer"));
        columnModel.setCellValueFactory(new PropertyValueFactory<Automobile, String>("model"));
        columnRelease.setCellValueFactory(new PropertyValueFactory<Automobile, String>("release"));
        columnBirthplace.setCellValueFactory(new PropertyValueFactory<Automobile, String>("birthplace"));
        columnOwner.setCellValueFactory(new PropertyValueFactory<Automobile, String>("owner"));

        // Launching listeners
        listeners();

        // Initializing stage for PropertyDialog window
        pdController.setUserList(userList);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/PropertyDialog.fxml"));
            root1 = fxmlLoader.load();
            pdController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error " + e.toString());
        }


    }
    private void listeners(){

        // Buttons Access
        data.getDataList().addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends Automobile> c) {
                updateStatus();
                tableData.setItems(data.getDataList());
                txtSearch.setDisable(data.getDataList().size() < 2);
                butReserve.setDisable(data.getDataList().size() < 1 && tableData.getSelectionModel().getSelectedItem() == null);
                miDeleteAll.setDisable(data.getDataList().size() == 0);
                miReport.setDisable(data.getDataList().size() == 0);
                miSave.setDisable(false);
                butSave.setDisable(false);
            }
        });

        // Buttons Access
        tableData.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends Automobile> c) {


                miSaveAs.setDisable(tableData.isDisable());

                miAdd.setDisable(tableData.isDisable());
                miEdit.setDisable(tableData.getSelectionModel().getSelectedItem() == null);
                miDelete.setDisable(tableData.getSelectionModel().getSelectedItem() == null);

                butAdd.setDisable(tableData.isDisable());
                butEdit.setDisable(tableData.getSelectionModel().getSelectedItem() == null);
                butDelete.setDisable(tableData.getSelectionModel().getSelectedItem() == null);

                if(tableData.getSelectionModel().getSelectedItem() != null) {
                    butReserve.setDisable(!tableData.getSelectionModel().getSelectedItem().getOwner().equals(" ") &&
                    !tableData.getSelectionModel().getSelectedItem().getOwner().equals(userList.getCurrentUser().getName()
                    + " (" + userList.getCurrentUser().getLogin() + ")"));
                }

                lbLastAction.setText("");

            }
        });

        // Search field
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            log.info("search method entered");

            if (txtSearch.getText().isEmpty()) {
                tableData.setItems(data.getDataList());
            }
            ObservableList<Automobile> result = FXCollections.observableArrayList();
//            for(int i = 0; i < data.getDataList().size(); ++i){
//                Automobile crt = data.getDataList().get(i);
//                if(crt.getProducer().contains(txtSearch.getText())){
//                    result.add(crt);
//                    continue;
//                }
//                if(crt.getModel().contains(txtSearch.getText())){
//                    result.add(crt);
//                    continue;
//                }
//                if(crt.getRelease().contains(txtSearch.getText())){
//                    result.add(crt);
//                    continue;
//                }
//                if(crt.getBirthplace().contains(txtSearch.getText())){
//                    result.add(crt);
//                    continue;
//                }
//                if(crt.getOwner().contains(txtSearch.getText())){
//                    result.add(crt);
//                    continue;
//                }
//            }

            MultiThreadSearch first_thread  = new MultiThreadSearch(0, data.getDataList().size() / 3, txtSearch.getText(), data.getDataList(), result);
            first_thread.start();

            MultiThreadSearch second_thread = new MultiThreadSearch(data.getDataList().size() / 3, data.getDataList().size() / 3 * 2, txtSearch.getText(), data.getDataList(), result);
            second_thread.start();

            MultiThreadSearch third_tread = new MultiThreadSearch(data.getDataList().size() / 3 * 2, data.getDataList().size(), txtSearch.getText(), data.getDataList(), result);
            third_tread.start();


            try {
                first_thread.join();
                second_thread.join();
                third_tread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tableData.setItems(result);
        });

        // DoubleClick for editing
        tableData.setRowFactory( tv -> {
            TableRow<Automobile> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    editObject();
                }
            });
            return row ;
        });

    }
    private void updateStatus(){

        lbStatus.setText("Всего объектов: " + data.getDataList().size());
    }

    private void showPropertyDialog(){

        pdController.setUserList(userList);

        if(pdStage == null) {
            pdStage = new Stage();
            pdStage.setTitle("Параметры");
            pdStage.getIcons().add(new Image("edu/java/lab2/img/car.png"));
            pdStage.setOnCloseRequest(e -> {
                pdController.setDefaultStyles();
                e.consume();
                pdStage.close();
            });
            pdStage.setResizable(false);
            pdStage.setScene(new Scene(root1, 520, 300));
            pdStage.initModality(Modality.WINDOW_MODAL);
            pdStage.initOwner(mainStage);
        }
        log.info("Property Dialog opened");
        pdStage.showAndWait();
    }
    private void showConfirmDialog(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/ConfirmDialog.fxml"));
            root2 = fxmlLoader.load();
            cdController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error " + e.toString());
        }
        cdStage = new Stage();
        cdStage.setTitle("");
        cdStage.getIcons().add(new Image("edu/java/lab2/img/car.png"));
        cdStage.setResizable(false);
        cdStage.setScene(new Scene(root2, 420, 180));
        cdStage.initModality(Modality.WINDOW_MODAL);
        cdStage.initOwner(mainStage);

        if(data.getFile() != null) {
            cdController.setLbPathText(data.getFile().getName());
        } else { cdController.setLbPathText(""); }
        log.info("Confirm Dialog opened");
        cdStage.showAndWait();
    }
    public void showUserListWindow(){

        ulController.setUserList(userList);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/UserListWindow.fxml"));
            root2 = fxmlLoader.load();
            ulController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error " + e.toString());
        }
        ulStage = new Stage();
        ulStage.setTitle("Список пользователей");
        ulStage.getIcons().add(new Image("edu/java/lab2/img/car.png"));
        ulStage.setMinWidth(800);
        ulStage.setMinHeight(400);
        ulStage.setScene(new Scene(root2, 800, 400));
        ulStage.initModality(Modality.WINDOW_MODAL);
        ulStage.initOwner(mainStage);

//        ulStage.setOnCloseRequest(e -> {
//            e.consume();
//            ulStage.close();
//        });

        ulStage.showAndWait();
    }
    public void showOperationsDialog(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/OperationsDialog.fxml"));
            root2 = fxmlLoader.load();
            odController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error " + e.toString());
        }
        odStage = new Stage();
        odStage.setTitle("История операций");
        odStage.getIcons().add(new Image("edu/java/lab2/img/car.png"));
        odStage.setMinWidth(1000);
        odStage.setMinHeight(500);
        odStage.setScene(new Scene(root2, 1000, 500));
        odStage.initModality(Modality.WINDOW_MODAL);
        odStage.initOwner(mainStage);

//        ulStage.setOnCloseRequest(e -> {
//            e.consume();
//            ulStage.close();
//        });

        odStage.showAndWait();
    }

    public void newFile() {
        if (!butSave.isDisable()) {

            showConfirmDialog();
            if (cdController.getCheckOnClose()) {

                if (cdController.getCheckOnSave()) {
                    saveFile();
                    if (!data.getSaveCheck()){
                        return;
                    }
                }
            } else return;
        }

        data.deleteAll();
        data.resetFile();
        miAdd.setDisable(false);
        miSave.setDisable(false);
        miSaveAs.setDisable(false);
        butSave.setDisable(false);
        butAdd.setDisable(false);
        tableData.setDisable(false);
        tableData.setItems(data.getDataList());
        titledPane.setText("Несохраненный файл");
        lbLastAction.setText("Файл создан");
        writeUserLog("New File Created");

    }
    public void openFile(){

        if(!butSave.isDisable()) {
            showConfirmDialog();
            if (cdController.getCheckOnClose()) {

                if (cdController.getCheckOnSave()) {
                    saveFile();
                    if (!data.getSaveCheck()){
                        return;
                    }
                }
            } else return;
        }

        data.open();

        if(data.getOpenCheck() == false){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Ошибка!");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea("Файл поврежден или содержит некорректные данные!" +
                    "\nДанные могут быть представлены некорректно.")));
            alert.showAndWait();
        }

        if (data.getFile() != null) {
            tableData.setItems(data.getDataList());
            miAdd.setDisable(false);
            miSaveAs.setDisable(false);

            if (data.getDataList().size() != 0) {
                miDeleteAll.setDisable(false);
            }

            butAdd.setDisable(false);
            tableData.setDisable(false);
            miSave.setDisable(true);
            butSave.setDisable(true);
            titledPane.setText(data.getFile().getName());
            lbLastAction.setText("Файл открыт");
            writeUserLog("File opened");

        }

    }
    public void saveFile(){
        if(data.getFile() != null) {
            data.save();
            miSave.setDisable(true);
            butSave.setDisable(true);
            writeUserLog("File saved");
            lbLastAction.setText("Файл сохранен");
        } else {
            saveFileAs();
        }
    }

    public void saveFileAs(){

        data.saveAs();
        if (data.getFile() != null) {
            writeUserLog("File saved as " + data.getFile().getName());
            lbLastAction.setText("Файл сохранен");
            miSave.setDisable(true);
            butSave.setDisable(true);
            titledPane.setText(data.getFile().getName());
        }
    }
    public void addObject() {

        pdController.setUserList(userList);
        pdController.setFields(new Automobile());
        pdController.setLbNameText("Добавление");
        showPropertyDialog();
        if(pdController.getCheck()) {
            writeUserLog("Object deleted {" + pdController.getAutomobile().getProducer() + " "
                    + pdController.getAutomobile().getModel() + " "
                    + pdController.getAutomobile().getRelease() + " "
                    + pdController.getAutomobile().getBirthplace() + " "
                    + pdController.getAutomobile().getOwner() + "}");
            data.add(pdController.getAutomobile());
            //tableData.refresh();
            lbLastAction.setText("Объект добавлен");
        }

    }
    public void editObject(){

        pdController.setUserList(userList);

        pdController.setFields(tableData.getSelectionModel().getSelectedItem());
        writeUserLog("Object editing {" + tableData.getSelectionModel().getSelectedItem().getProducer() + " "
                + tableData.getSelectionModel().getSelectedItem().getModel() + " "
                + tableData.getSelectionModel().getSelectedItem().getRelease() + " "
                + tableData.getSelectionModel().getSelectedItem().getBirthplace() + " "
                + tableData.getSelectionModel().getSelectedItem().getOwner() + "}");
        pdController.setLbNameText("Редактирование");
        showPropertyDialog();
        if(pdController.getCheck()) {
            writeUserLog("Object edit result: {" + tableData.getSelectionModel().getSelectedItem().getProducer() + " "
                    + tableData.getSelectionModel().getSelectedItem().getModel() + " "
                    + tableData.getSelectionModel().getSelectedItem().getRelease() + " "
                    + tableData.getSelectionModel().getSelectedItem().getBirthplace() + " "
                    + tableData.getSelectionModel().getSelectedItem().getOwner() + "}");
            miSave.setDisable(false);
            butSave.setDisable(false);
            tableData.setItems(data.getDataList());
            tableData.refresh();
            lbLastAction.setText("Объект изменен");
        }
    }
    public void removeObject(){
        writeUserLog("Object deleted {" + tableData.getSelectionModel().getSelectedItem().getProducer() + " "
                + tableData.getSelectionModel().getSelectedItem().getModel() + " "
                + tableData.getSelectionModel().getSelectedItem().getRelease() + " "
                + tableData.getSelectionModel().getSelectedItem().getBirthplace() + " "
                + tableData.getSelectionModel().getSelectedItem().getOwner() + "}");
        data.delete(tableData.getSelectionModel().getSelectedItem());
        miSave.setDisable(false);
        butSave.setDisable(false);
        lbLastAction.setText("Объект удален");
    }
    public void removeList(){
        data.deleteAll();

        miSave.setDisable(false);
        butSave.setDisable(false);
       // tableData.refresh();
       // tableData.setItems(data.getDataList());
        lbLastAction.setText("Объекты удалены");
        writeUserLog("All objects deleted");
    }

    public void reserveObject(){
        if(tableData.getSelectionModel().getSelectedItem().getOwner().equals(" ")) {
            tableData.getSelectionModel().getSelectedItem().setOwner(userList.getCurrentUser().getName()
                    + " (" + userList.getCurrentUser().getLogin() + ")");
            writeUserLog("Object reserved {" + tableData.getSelectionModel().getSelectedItem().getProducer() + " "
                    + tableData.getSelectionModel().getSelectedItem().getModel() + " "
                    + tableData.getSelectionModel().getSelectedItem().getRelease() + " "
                    + tableData.getSelectionModel().getSelectedItem().getBirthplace() + "}");;
        } else {
            tableData.getSelectionModel().getSelectedItem().setOwner(" ");
            writeUserLog("Object unreserved {" + tableData.getSelectionModel().getSelectedItem().getProducer() + " "
                    + tableData.getSelectionModel().getSelectedItem().getModel() + " "
                    + tableData.getSelectionModel().getSelectedItem().getRelease() + " "
                    + tableData.getSelectionModel().getSelectedItem().getBirthplace() + "}");;
        }
        tableData.setItems(data.getDataList());
        tableData.refresh();
    }

    @FXML
    private void createReport() {
        log.info("createReport method entered");
        ArrayList<Automobile> tmp = new ArrayList<>();
        for (Automobile crt : data.getDataList()) {
            tmp.add(crt);
        }

        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(tmp);
            JasperReport report = JasperCompileManager.compileReport("C:/Users/Andrei/JaspersoftWorkspace/MyReports/myReport.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

            JasperExportManager.exportReportToPdfFile(print,"C:/AProjects/IDEA/Java 2 FX Thread/Reports/Report.pdf");
            JasperExportManager.exportReportToXmlFile(print, "C:/AProjects/IDEA/Java 2 FX Thread/Reports/Report.xml",false);
            JasperExportManager.exportReportToHtmlFile(print, "C:/AProjects/IDEA/Java 2 FX Thread/Reports/Report.html");
            lbLastAction.setText("Отчет успешно создан");
            writeUserLog("Report Generated");

        } catch (JRException e) {
            log.error("Error " + e.toString());
            e.printStackTrace();
        }
    }

    private void writeUserLog(String message){
        FileWriter fr = null;
        try {
            fr = new FileWriter("./src/edu/java/lab2/access/userlog.txt", true);
            fr.append(userList.getCurrentUser().getName()+ " (" + userList.getCurrentUser().getLogin() + ")" + System.lineSeparator());
            if(data.getFile() == null){
                fr.append(" " + System.lineSeparator());
            } else {
                fr.append(data.getFile().getName() + System.lineSeparator());
            }
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

    public void closeProgram(){
        if(!butSave.isDisable()) {

            showConfirmDialog();
            if (cdController.getCheckOnClose()) {
                if (cdController.getCheckOnSave()) {
                    saveFile();
                    if (!data.getSaveCheck()){
                        return;
                    }
                }
            } else return;
        }
        mainStage.close();
        writeUserLog("Logged out");
        log.info("Main Window closed");
    }

}
