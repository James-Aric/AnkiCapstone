package anki.launchers;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Hello World");
        Parent root = FXMLLoader.load(getClass().getResource("../../resources/client.fxml"));
        Scene scene = new Scene(root, 250, 200);
        scene.setFill(Paint.valueOf("#353b48"));
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}