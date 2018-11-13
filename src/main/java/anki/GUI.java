package anki;

import de.adesso.anki.MessageListener;
import de.adesso.anki.Model;
import de.adesso.anki.Vehicle;
import de.adesso.anki.messages.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.util.List;


public class GUI implements KeyListener {

    boolean playable = false;

    public Button start, stop;
    //public TextField input;
    Connector connectCars = new Connector();
    private float offsetFromCenter;
    private int currentSpeed;
    private Scene scene = null;
    //private int lights;
    private boolean lights;

    private String user;

    private double startTime, bestLapTime, previousLap;

    @FXML
    private TextField input, ipInput;

    @FXML
    Button connect, skull, groundShock, nuke;

    @FXML
    VBox foundCarsBox;

    Stage stage;

    MessageListener<LocalizationPositionUpdateMessage> positionListener;
    MessageListener<LocalizationTransitionUpdateMessage> transitionListener;

    private String vehicleName;


    @FXML
    Label speedTest, pieceID, offset, drivingDirection, bestLap, previousLapLabel, split;

    @FXML
    Label playerLabel, countdownLabel, ipPrompt;

    LightsPatternMessage.LightConfig lc;
    LightsPatternMessage lpm;
    CustomScanner scanner;


    SocketController controller;
    //Roadmap currentMap;

    Node source;
    public GUI(){
    }

    public void searchForCars(ActionEvent event) throws IOException {
        source = (Node) event.getSource();
        scene = source.getScene();
        bestLapTime = Double.MAX_VALUE;
        stage = (Stage) scene.getWindow();
        List<Vehicle> foundCars = connectCars.returnVehicles();
        //countdownLabel.setText("COUNTDOWN HERE");
        for(Vehicle v: foundCars){
            switch(v.getAdvertisement().getModel()){
                case GROUNDSHOCK:
                    groundShock.setVisible(true);
                    groundShock.setOnAction(e -> {
                        try {
                            connect(Model.GROUNDSHOCK);
                            vehicleName = "groundshock";
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    });
                    break;
                case NUKE:
                    nuke.setVisible(true);
                    nuke.setOnAction(e -> {
                        try {
                            connect(Model.NUKE);
                            vehicleName = "nuke";
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    });
                    break;
                case SKULL:
                    skull.setVisible(true);
                    skull.setOnAction(e -> {
                        try {
                            connect(Model.SKULL);
                            vehicleName = "skull";
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    });
                    break;
                case GUARDIAN:
                    break;
            }
        }
    }

    public void connect(Model model) throws URISyntaxException {
        try {
            //speedTest.setText("500");
            /*Node source = (Node) event.getSource();
            scene = source.getScene();
            stage = (Stage) scene.getWindow();*/
            //setMap();
            //updateMap();
            System.out.println("SCENE SET");
            System.out.println("CONNECTING INITIATED--------------------------");
            positionListener = (message) -> positionUpdate(message);
            transitionListener = (message) -> transitionUpdate(message);
            user = input.getText();
            controller.address = ipInput.getText();
            ipInput.setVisible(false);
            ipPrompt.setVisible(false);
        }
        catch (NullPointerException ex){

        }
        try {

            connectCars.connect(model);
            controller = new SocketController(user, connectCars.gs, this);
            scanner = new CustomScanner(connectCars.gs, controller);

            controller.connectSocket();

            input.setVisible(false);
            if(groundShock.isVisible()){
                groundShock.setVisible(false);
            }

            if(skull.isVisible()){
                skull.setVisible(false);
            }

            if(nuke.isVisible()){
                nuke.setVisible(false);
            }


            //currentMap = scanner.getRoadmap();
            System.out.println("TEST1");
            //System.out.println(scanner.getIdList());
            //System.out.println(scanner.getIdList());
            //scanner.test();
            System.out.println("TEST2");
            //scanner.reset();

            lights = false;
            Thread.sleep(100);
            //scanner.sendMap();

            scene.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
                @Override
                public void handle(javafx.scene.input.KeyEvent e) {
                    System.out.println(e.getCode());
                    JSONObject object = new JSONObject();
                    if(playable){
                        switch(e.getCode()){
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
                                    currentSpeed -= 50;
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
                                //connectCars.gs.sendMessage(new TurnMessage(1, 0));
                                break;

                            //right
                            case LEFT:
                                connectCars.gs.sendMessage(new SetOffsetFromRoadCenterMessage(0));
                                connectCars.gs.sendMessage(new ChangeLaneMessage(-23, currentSpeed, 2000));
                                //connectCars.gs.sendMessage(new TurnMessage(2, 0));
                                break;
                            case D:
                                connectCars.disconnect();
                                break;

                            case SHIFT:
                                connectCars.gs.sendMessage(new TurnMessage(3, 0));
                                break;
                        }
                    }
                    else{
                        switch (e.getCode()) {
                            //up

                            case R:
                                object.put("event", "ready");
                                object.put("username", user);
                                object.put("vehicle", vehicleName);
                                controller.getClient().send(object.toString());
                                break;
                            case S:
                                object = new JSONObject();
                                object.put("event", "startRace");
                                controller.getClient().send(object.toString());
                                connectCars.gs.addMessageListener(LocalizationPositionUpdateMessage.class, positionListener);
                                connectCars.gs.addMessageListener(LocalizationTransitionUpdateMessage.class, transitionListener);
                                break;
                        }
                    }
                }
            });
        } catch(NullPointerException exception){
            System.out.println("No Cars");
        } catch(ConnectException exception){
            System.out.println("Connection Failed");
        } catch(Exception exception){
            System.out.println(exception.getLocalizedMessage());
            exception.printStackTrace();
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
                JSONObject object = new JSONObject();
                JSONObject data = new JSONObject();
                object.put("event", "locationUpdate");
                data.put("username", user);
                data.put("vehicle", vehicleName);
                //data.put("locationId", message.getLocationId());
                data.put("position", message.getRoadPieceId());
                data.put("offset", message.getOffsetFromRoadCenter());
                data.put("speed", message.getSpeed());
                object.put("data", data);
                System.out.println(object.toString());
                controller.getClient().send(object.toString());
            }
        });
    }

    public void transitionUpdate(LocalizationTransitionUpdateMessage message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //drivingDirection.setText("DIRECTION: " + message.getDrivingDirection());
                JSONObject object = new JSONObject();
                JSONObject data = new JSONObject();
                object.put("event", "transitionUpdate");
                data.put("username", user);
                data.put("vehicle", vehicleName);
                data.put("message", message.toString());
                object.put("data", data);
                controller.getClient().send(object.toString());
                System.out.println("SENT TRANSITION UPDATE ================================================================");
            }
        });
    }

    public void updatePlayerCount(int playerCount){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                playerLabel.setText("Players Ready: " + playerCount);
                System.out.println("Player count updated");
            }
        });
    }

    public void updateCountdown(int countdown){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(countdown != 0) {
                    countdownLabel.setText("" + countdown);
                }
                else {
                    countdownLabel.setText("GO!!!");
                    playable = true;
                    startTime = System.currentTimeMillis();
                }
            }
        });
    }

    public void startScanning() throws InterruptedException{
        scanner.startScanning();
        while (!scanner.isComplete()) {
            Thread.sleep(500);
        }
        while(!scanner.isComplete()){

        }
        previousLap = 0;
    }

    public void calculateLapTime(double newTime){
        System.out.println(startTime + "     " + newTime + "    ---------------------------------");
        System.out.println((newTime - startTime)/1000);
        double currentLap = newTime-startTime;

        if((currentLap) < bestLapTime){
            bestLapTime = newTime-startTime;
        }
        JSONObject object = new JSONObject();
        object.put("event", "bestLap");
        object.put("time", (bestLapTime/1000));
        object.put("user", user);
        controller.getClient().send(object.toString());
        double temp = currentLap/1000;
        double splitTime =  temp - previousLap;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                bestLap.setText(String.valueOf(bestLapTime/1000));
                previousLapLabel.setText(String.valueOf(previousLap));
                split.setText(String.valueOf(splitTime));
            }
        });
        previousLap = currentLap/1000;
        startTime = (double)System.currentTimeMillis();
    }

    public void endGame(String winner){
        connectCars.gs.sendMessage(new SetSpeedMessage(0, 5000));
        playable = false;
        countdownLabel.setText("WINNER:\n"+winner);
    }
}
