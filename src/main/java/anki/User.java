package anki;

import de.adesso.anki.Vehicle;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
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
    Label vehicle, username, speed, racePosition;
    boolean ready;
    long bestLapTime;


    public User(int position, WebSocket client, Label speed, Label racePosition, Label vehicle, Label username){
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
    }

    public int getLapNum(){
        return lapNum;
    }
    public void setLapNum(int laps){
        this.lapNum = laps;
    }

    public void setRacePosition(String x){
        this.racePosition.setText(x);
    }
    public void setVehicle(String x){
        this.vehicle.setText(x);
    }
    public void setUsername(String x){
        this.username.setText(x);
    }
    public void setSpeed(String x){
        this.speed.setText(x);
    }

    public String getName(){
        return username.getText();
    }

    public String getVehicle(){
        return vehicle.getText();
    }

    public void setMapPosition(){
        position++;
    }

    public void removeThisPlayer(){
        this.vehicle.setText("PLACEHOLDER");
        this.username.setText("PLACEHOLDER");
        this.speed.setText("PLACEHOLDER");
        this.racePosition.setText("PLACEHOLDER");
    }

    public void readyUp(){
        this.ready = true;
    }
}
