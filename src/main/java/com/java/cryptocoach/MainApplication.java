package com.java.cryptocoach;

import com.java.cryptocoach.utility.DBUtility;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApplication extends Application {
    public static Scene chartScene;
    public static Scene tableScene;
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        // load, and store, the chart scene.
        FXMLLoader chartLoader = new FXMLLoader(MainApplication.class.getResource("chart-view.fxml"));
        chartScene = new Scene(chartLoader.load(), 1000, 1000);

        // load, and store, the table scene.
        FXMLLoader tableLoader = new FXMLLoader(MainApplication.class.getResource("table-view.fxml"));
        tableScene = new Scene(tableLoader.load(), 1000, 1000);

        // store the stage, so that we can swap scenes.
        mainStage = stage;

        // set icon
        stage.getIcons().add(new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Bitcoin.svg/640px-Bitcoin.svg.png"));

        // initialize, and render, the stage.
        stage.setTitle("CryptoCoach");
        stage.setScene(chartScene);
        stage.show();
    }

    public static void main(String[] args) {
        // before we continue, do we want to append a JSON file?
        DBUtility.get().appendFromJson();

        launch();

        // after we close the interface, let's close the connection.
        DBUtility.get().close();
    }
}