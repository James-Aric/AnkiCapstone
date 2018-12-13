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

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/client.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root, 752, 456);
        scene.setFill(Paint.valueOf("#353b48"));
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}