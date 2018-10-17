package anki;
import de.adesso.anki.Vehicle;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

public class SocketController {

    static String username;
    static Vehicle vehicle;
    static WebSocketClient client;

    public SocketController(String username, Vehicle vehicle) throws URISyntaxException{
        this.username = username;
        this.vehicle = vehicle;
        client = new WebSocketClient(new URI("ws://localhost:5020")) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                object = new JSONObject();
                data = new JSONObject();
                data.put("username", username);
                data.put("vehicle", vehicle.getAdvertisement().getModel().toString());
                object.put("event", "connect");
                object.put("data", data);
                client.send(object.toString());
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
                this.close();
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }

    static JSONObject object;
    static JSONObject data;
    static InetSocketAddress address = new InetSocketAddress("localhost", 3000);





    public static WebSocketClient getClient(){
        return client;
    }

    public void connectSocket() throws URISyntaxException {
        try{
            client.connect();
            System.out.println("Connecting client on: " + client.getURI());
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }
}
