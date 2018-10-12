package anki;

import de.adesso.anki.Vehicle;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;

public class User {
    String name;
    Vehicle vehicle;
    Roadpiece position;
    WebSocket conn;

    public User(String name, Vehicle vehicle, Roadpiece position, WebSocket client){
        this.name = name;
        this.vehicle = vehicle;
        this.position = position;
        this.conn = client;
    }

    public void setPosition(Roadpiece position){
        this.position = position;
    }
}
