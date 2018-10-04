package anki;

import de.adesso.anki.MessageListener;
import de.adesso.anki.RoadmapScanner;
import de.adesso.anki.messages.*;
import de.adesso.anki.roadmap.Roadmap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class GUI implements KeyListener {
    JTextField typingArea = new JTextField(20);

    Main main = new Main();


    public Button start, stop;
    //public TextField input;
    Connector connectCars = new Connector();
    private float offsetFromCenter;
    private int currentSpeed;
    private Scene scene = null;
    //private int lights;
    private boolean lights;

    LightsPatternMessage.LightConfig lc;
    LightsPatternMessage lpm;



    Roadmap currentMap;

    public GUI() throws IOException{
    }

    public void connect(ActionEvent event) throws IOException, InterruptedException{
        Node source = (Node) event.getSource();
        scene = source.getScene();
        System.out.println("SCENE SET");
        System.out.println("CONNECTING INITIATED--------------------------");
        try {
            connectCars.connect();
            CustomScanner scanner = new CustomScanner(connectCars.gs);
            scanner.startScanning();
            while (!scanner.isComplete()) {
                Thread.sleep(500);
            }
            scanner.stopScanning();
            currentMap = scanner.getRoadmap();
            System.out.println(currentMap.toList().toString());
            scanner.reset();
            lights = false;
            //lights = 0;
            //connectCars.gs.sendMessage(new SetOffsetFromRoadCenterMessage(0));
            //offsetFromCenter = connectCars.gs.getOffsetFromCenter();
            //MessageListener<LocalizationTransitionUpdateMessage> transListener = (message) -> getReverse(message);
            //connectCars.gs.addMessageListener(LocalizationTransitionUpdateMessage.class, transListener);
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
                            break;
                        case CONTROL:
                            //connectCars.gs.sendMessage(new SetSpeedMessage(currentSpeed, 2000));
                            //lights++;
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
                            
                            /*switch (lights) {
                                case 0:
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.TAIL, LightsPatternMessage.LightEffect.STEADY, 0, 0, 0);
                                    lpm = new LightsPatternMessage();
                                    lights++;
                                    break;
                                case 1:
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.ENGINE_BLUE, LightsPatternMessage.LightEffect.STROBE, 0, 1, 0);
                                    lpm = new LightsPatternMessage();
                                    lights++;
                                    break;
                                case 2:
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.ENGINE_GREEN, LightsPatternMessage.LightEffect.STROBE, 0, 0, 0);
                                    lpm = new LightsPatternMessage();
                                    lights++;
                                    break;
                                case 3:
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.ENGINE_RED, LightsPatternMessage.LightEffect.STROBE, 0, 0, 0);
                                    lpm = new LightsPatternMessage();
                                    lights++;
                                    break;
                                case 4:
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.FRONT_GREEN, LightsPatternMessage.LightEffect.STROBE, 0, 0, 0);
                                    lpm = new LightsPatternMessage();
                                    lights++;
                                    break;
                                case 5:
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.FRONT_RED, LightsPatternMessage.LightEffect.STROBE, 0, 0, 0);
                                    lpm = new LightsPatternMessage();
                                    lights++;
                                    break;
                                default:
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.TAIL, LightsPatternMessage.LightEffect.STEADY, 0, 0, 0);
                                    lpm = new LightsPatternMessage();
                                    lpm.add(lc);
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.ENGINE_RED, LightsPatternMessage.LightEffect.FADE, 0, 0, 0);
                                    lpm.add(lc);
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.ENGINE_GREEN, LightsPatternMessage.LightEffect.FADE, 0, 0, 0);
                                    lpm.add(lc);
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.ENGINE_BLUE, LightsPatternMessage.LightEffect.FADE, 0, 0, 0);
                                    lpm.add(lc);
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.FRONT_RED, LightsPatternMessage.LightEffect.FADE, 0, 0, 0);
                                    lpm.add(lc);
                                    lc = new LightsPatternMessage.LightConfig(LightsPatternMessage.LightChannel.FRONT_GREEN, LightsPatternMessage.LightEffect.FADE, 0, 0, 0);
                                    lights = 0;
                                    break;

                            }*/
                            //lpm.add(lc);
                            //connectCars.gs.sendMessage(lpm);
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
        } catch(Exception exception){

            exception.printStackTrace();
            connectCars.gs.disconnect();
        }
    }
    public void forward(ActionEvent event){
        System.out.println("GO GO GO--------------------------------------");
        //String textInput = input.getText();
        /*if(textInput != null){
            try{
                currentSpeed = Integer.parseInt(textInput);

                connectCars.gs.sendMessage(new SetSpeedMessage(currentSpeed, 1000));
                connectCars.gs.sendMessage(new SetOffsetFromRoadCenterMessage());

            }
            catch (Exception e){
                System.out.println(e);
            }
        }*/
    }

    public void brakes(ActionEvent event){
        System.out.println("BRAKES!-------------------------------------");
        connectCars.gs.sendMessage(new SetSpeedMessage(0, 2000));
        connectCars.gs.sendMessage(new TurnMessage(180, 1));

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e){

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void right(ActionEvent event){
        System.out.println("SWITCHING LANES!-----------------   --------------------");
        //connectCars.gs.sendMessage(new TurnMessage());
        //connectCars.gs.sendMessage(new ChangeLaneMessage(offsetFromCenter++, currentSpeed, 2000));
        connectCars.gs.sendMessage(new SetOffsetFromRoadCenterMessage(0));
        connectCars.gs.sendMessage(new ChangeLaneMessage(23, currentSpeed, 2000));

    }
    public void left(ActionEvent event){
        System.out.println("SWITCHING LANES!-------------------------------------");
        connectCars.gs.sendMessage(new SetOffsetFromRoadCenterMessage(0));
        connectCars.gs.sendMessage(new ChangeLaneMessage(-23, currentSpeed, 2000));
    }


    public void disconnect(ActionEvent event){
        System.out.println("Disconnecting!-------------------------------------");
        connectCars.disconnect();
    }

    public Scene getScene(ActionEvent event){

        return scene;
    }

    /*public void getReverse(LocalizationTransitionUpdateMessage message){
        message.getDrivingDirection();
        connectCars.gs.sendMessage(new TurnMessage(message.getDrivingDirection(),message.getDrivingDirection()));
    }*/

}
