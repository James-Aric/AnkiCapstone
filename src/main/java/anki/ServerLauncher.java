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
    public static ServerController controller;
    public static void main(String[]args) throws Exception{

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World");
        //ClassLoader loader = getClass().getClassLoader();
        //File url = new File(loader.getResource("server.fxml").getFile());
        //Parent root = FXMLLoader.load(url.toURL());
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/server.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Paint.valueOf("#353b48"));
        primaryStage.setScene(scene);

        primaryStage.show();
        controller = loader.getController();
    }
}
