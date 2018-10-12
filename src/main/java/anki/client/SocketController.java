package anki.client;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class SocketController {
    public static WebSocketClient client;

    String username, vehicle;

    public SocketController(String username, String vehicle){
        this.username = username;
        this.vehicle = vehicle;
    }

    JSONObject object;
    JSONObject data;

    public static WebSocketClient getClient(){
        return client;
    }

    public void connectSocket() throws URISyntaxException {
        try{
            object = new JSONObject();
            client = new WebSocketClient(new URI("ws://localhost:3000")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                    client.send("Client: " + username);
                    client.send("Vehicle: " + vehicle);
                }

                @Override
                public void onMessage(String message) {
                    try{
                    object = new JSONObject(message);
                    data = null;
                    data = object.getJSONObject("data");
                    switch (object.getString("event")){
                        case "position":
                            break;

                    }
                    } catch (JSONException ignored) {

                    }

                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }

                @Override
                public void onError(Exception e) {

                }
            };
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }
}
