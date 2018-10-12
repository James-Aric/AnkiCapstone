package anki;

import de.adesso.anki.MessageListener;
import de.adesso.anki.RoadmapScanner;
import de.adesso.anki.messages.*;
import de.adesso.anki.roadmap.Position;
import de.adesso.anki.roadmap.Roadmap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Scanner;

public class GUI implements KeyListener {
    JTextField typingArea = new JTextField(20);

    //Main main = new Main();


    public Button start, stop;
    //public TextField input;
    Connector connectCars = new Connector();
    private float offsetFromCenter;
    private int currentSpeed;
    private Scene scene = null;
    //private int lights;
    private boolean lights;

    @FXML
    private GridPane grid;

    @FXML
    Button connect;

    Stage stage;


    ImageView vehicleView;

    MessageListener<LocalizationPositionUpdateMessage> positionListener;
    MessageListener<LocalizationTransitionUpdateMessage> transitionListener;


    ImageView views[];


    @FXML
    Label speedTest, pieceID, offset, drivingDirection;

    LightsPatternMessage.LightConfig lc;
    LightsPatternMessage lpm;



    Roadmap currentMap;

    public GUI() throws IOException{
    }

    public void connect(ActionEvent event) throws IOException, InterruptedException{
        speedTest.setText("500");
        Node source = (Node) event.getSource();
        scene = source.getScene();
        stage = (Stage) scene.getWindow();
        //setMap();
        //updateMap();
        System.out.println("SCENE SET");
        System.out.println("CONNECTING INITIATED--------------------------");
        positionListener = (message) -> positionUpdate(message);
        transitionListener = (message) -> transitionUpdate(message);
        try {
            connectCars.connect();
            CustomScanner scanner = new CustomScanner(connectCars.gs);

            scanner.startScanning();
            while (!scanner.isComplete()) {
                Thread.sleep(500);
            }
            scanner.stopScanning();



            //currentMap = scanner.getRoadmap();
            System.out.println("TEST1");
            //System.out.println(scanner.getIdList());
            System.out.println(scanner.getIdList());
            scanner.test();
            System.out.println("TEST2");
            scanner.reset();
            connectCars.gs.addMessageListener(LocalizationPositionUpdateMessage.class, positionListener);
            connectCars.gs.addMessageListener(LocalizationTransitionUpdateMessage.class, transitionListener);

            lights = false;
            scene.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
                @Override
                public void handle(javafx.scene.input.KeyEvent e) {
                    System.out.println(e.getCode());
                    switch (e.getCode()) {
                        //up
                        case UP:
                            currentSpeed += 50;
                            connectCars.gs.sendMessage(new SetSpeedMessage(currentSpeed, 2000));
                            System.out.println("Increasing speed to: " + currentSpeed + "!!!!!!!!");
                            break;
                        //down
                        case DOWN:
                            if (currentSpeed >= 50) {
                                currentSpeed -= 50;
                            } else {
                                currentSpeed = 0;
                            }
                            connectCars.gs.sendMessage(new SetSpeedMessage(currentSpeed, 2000));
                            speedTest.setText("SPEED: "+ currentSpeed);
                            break;
                        case CONTROL:
                            if(lights){
                                lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.TAIL, LightsPatternMessage.LightEffect.THROB, 0, 0, 0);
                                lpm = new LightsPatternMessage();
                                lights = false;
                            }
                            else{
                                lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.TAIL, LightsPatternMessage.LightEffect.STROBE, 0, 0, 0);
                                lpm = new LightsPatternMessage();
                                lights = true;
                            }
                            break;
                        //right
                        case RIGHT:
                            connectCars.gs.sendMessage(new SetOffsetFromRoadCenterMessage(0));
                            connectCars.gs.sendMessage(new ChangeLaneMessage(23, currentSpeed, 2000));
                            break;

                        //right
                        case LEFT:
                            connectCars.gs.sendMessage(new SetOffsetFromRoadCenterMessage(0));
                            connectCars.gs.sendMessage(new ChangeLaneMessage(-23, currentSpeed, 2000));
                            break;
                        case D:
                            connectCars.disconnect();
                            break;

                        case M:
                            connectCars.gs.sendMessage(new TurnMessage(180, 1));
                            break;
                    }
                }
            });
        } catch(NullPointerException exception){
            System.out.println("No Cars");
        } catch(ConnectException exception){
            System.out.println("Connection Failed");
        } catch(Exception exception){
            connectCars.gs.disconnect();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e){

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void positionUpdate(LocalizationPositionUpdateMessage message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                speedTest.setText("SPEED: " + message.getSpeed());
                pieceID.setText("ROAD ID: " + message.getRoadPieceId());
                offset.setText("OFFSET: " + message.getOffsetFromRoadCenter());
            }
        });
    }

    public void transitionUpdate(LocalizationTransitionUpdateMessage message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                drivingDirection.setText("DIRECTION: " + message.getDrivingDirection());
            }
        });
    }

    public void setMap(){
        views = new ImageView[4];
        for(int i = 0; i < 4; i++){
            views[i] = new ImageView();
            views[i].setImage(new Image("curve.png"));

            if(i <= 1) {
                views[i].setRotate(90-(i*90));
                grid.add(views[i], 1 + i, 10);
            }
            else{
                views[i].setRotate(90*i);
                grid.add(views[i], 1 + i%2, 9);
            }
        }
    }

    public void updateMap(){
        if(vehicleView == null){
            vehicleView = new ImageView(new Image("gs.jpg"));
            vehicleView.setFitHeight(50);
            vehicleView.setFitWidth(50);
        }
        grid.add(vehicleView, 2, 10);
    }

}
