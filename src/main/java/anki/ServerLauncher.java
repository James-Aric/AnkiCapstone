package anki;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

public class ServerLauncher extends Application {
    public static void main(String[]args) throws Exception{

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World");
        URL url = new File("/home/jamearic/Documents/Github/AnkiCapstone/src/main/resources/server.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        scene.setFill(Paint.valueOf("#353b48"));
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
