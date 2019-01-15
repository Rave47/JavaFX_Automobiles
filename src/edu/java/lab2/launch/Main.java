package edu.java.lab2.launch;

import edu.java.lab2.controllers.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

public class Main extends Application {


    @Override
    public void start(Stage loginStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../fxml/LogInWindow.fxml"));
        Parent fxmlMain = fxmlLoader.load();
        LogInController loginController = fxmlLoader.getController();
        loginController.setStage(loginStage);

        loginStage.setTitle("");
        loginStage.getIcons().add(new Image("edu/java/lab2/img/car.png"));
        loginStage.setResizable(false);
        loginStage.setScene(new Scene(fxmlMain, 320, 230));
        loginStage.show();
    }

    public static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Start Application");
        launch(args);
        log.info("Finish Application");
    }
}
