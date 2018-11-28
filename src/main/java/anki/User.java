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


    public User(int position, WebSocket client, Label speed, Label racePosition, Label vehicle, Label username, Label laps){
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

    public boolean setRacePosition(String x){
        try{
            int test = Integer.valueOf(x);
            this.racePosition.setText(x);
        }
        catch (Exception e){
            return false;
        }
        finally {
            return false;
        }
    }
    public boolean setVehicle(String x){
        if(x != null && !x.equals("")){
            this.vehicle.setText(x);
            return true;
        }
        return false;

    }
    public boolean setUsername(String x){
        if(x != null && !x.equals("")){
            this.username.setText(x);
            return true;
        }
        return false;
    }
    public boolean setSpeed(String x){
        try{
            int test = Integer.valueOf(x);
            this.speed.setText(x);
        }
        catch (Exception e){
            return false;
        }
        finally {
            return false;
        }
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
