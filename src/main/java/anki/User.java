package anki;

import de.adesso.anki.Vehicle;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;

public class User {
    String name;
    //String vehicle;
    int position;
    WebSocket conn;
    //static int racePosition;
    CustomRoadpiece roadpiece;
    private int lapNum;
    Label vehicle, username, speed, racePosition, laps;
    boolean ready;
    long bestLapTime;
    String user;


    public User(int position, WebSocket client, Label speed, Label racePosition, Label vehicle, Label username, Label laps, String user){
        this.vehicle = vehicle;
        this.position = position;
        this.conn = client;
        this.racePosition = racePosition;
        this.roadpiece = roadpiece;
        this.speed = speed;
        this.lapNum = 0;
        this.username = username;
        this.ready = false;
        bestLapTime = -1;
        this.laps = laps;
        this.user = user;
    }

    public int getLapNum(){
        return lapNum;
    }
    public void setLapNum(){
        this.lapNum++;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                laps.setText(""+lapNum);
            }
        });
    }

    public void resetLaps(){
        this.lapNum = 0;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                laps.setText(""+lapNum);
            }
        });
    }
    boolean valid = false;
    public boolean setRacePosition(String x){
        valid = false;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try{
                    int test = Integer.valueOf(x);
                    racePosition.setText(x);
                    valid = true;
                }
                catch (Exception e){
                    e.printStackTrace();
                    valid = false;
                }
            }
        });
        return valid;
    }
    public boolean setVehicle(String x){
        valid = false;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(x != null && !x.equals("")){
                    vehicle.setText(x);
                    valid = true;
                }
                valid = false;
            }
        });
        return valid;

    }
    public boolean setUsername(String x){
        valid = false;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(x != null && !x.equals("")){
                    username.setText(x);
                    valid = true;
                }
                valid = false;
            }
        });
        return valid;
    }
    public boolean setSpeed(String x){
        valid = false;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try{
                    int test = Integer.valueOf(x);
                    speed.setText(x);
                    valid = true;
                }
                catch (Exception e){
                    valid = false;
                }
            }
        });
        return valid;
    }

    public String getName(){
        return username.getText();
    }
    public String getUser(){
        return user;
    }

    public String getVehicle(){
        return vehicle.getText();
    }

    public void setMapPosition(){
        position++;
    }

    public void removeThisPlayer(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vehicle.setText("");
                username.setText("");
                speed.setText("");
                racePosition.setText("");
            }
        });

    }

    public void readyUp(){
        this.ready = true;
    }

    public void setLabels(Label[] labels){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String uName, veh, spee, racePos, lap;
                uName = username.getText();
                veh = vehicle.getText();
                spee = speed.getText();
                racePos = racePosition.getText();
                lap = laps.getText();

                username = labels[0];
                vehicle = labels[1];
                speed = labels[2];
                racePosition = labels[3];
                laps = labels[4];

                username.setText(uName);
                vehicle.setText(veh);
                speed.setText(spee);
                racePosition.setText(racePos);
                laps.setText(lap);
            }
        });

    }
}
