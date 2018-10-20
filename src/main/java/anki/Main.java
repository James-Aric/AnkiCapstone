package anki;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("SKULL");
        URL url = new File("/home/jamearic/Documents/Github/AnkiCapstone/src/main/resources/client.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root, 752, 456);
        scene.setFill(Paint.valueOf("#353b48"));
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}