package anki;

import de.adesso.anki.Vehicle;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;

public class User {
    String name;
    String vehicle;
    int position;
    WebSocket conn;
    static int mapPosition;


    public User(String name, String vehicle, int position, WebSocket client){
        this.name = name;
        this.vehicle = vehicle;
        this.position = position;
        this.conn = client;
        this.mapPosition = 0;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public String getName(){
        return name;
    }

    public String getVehicle(){
        return vehicle;
    }

    public void setMapPosition(){
        mapPosition++;
    }
}
