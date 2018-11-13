package anki;
import de.adesso.anki.Vehicle;
import de.adesso.anki.messages.SetSpeedMessage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class SocketController {

    /*

        CHANGE THIS TO MATCH SERVER IP

     */
    static String address = "129.3.171.65";


    static String username;
    static Vehicle vehicle;
    static WebSocketClient client;
    static JSONObject object;
    static JSONObject data;


    public SocketController(String username, Vehicle vehicle, GUI gui) throws URISyntaxException{
        this.username = username;
        this.vehicle = vehicle;
        client = new WebSocketClient(new URI("ws://" + address + ":5023")) {
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
                    try{
                        data = object.getJSONObject("data");
                    }catch (Exception e){

                    }
                    switch (object.getString("event")){
                        case "position":
                            break;
                        case "playerCount":
                            gui.updatePlayerCount(object.getInt("playerCount"));
                            System.out.println("UPDATING PLAYER COUNT");
                            break;
                        case "countdown":
                            gui.updateCountdown(object.getInt("time"));
                            break;
                        case "scan":
                            gui.startScanning();
                            break;
                        case "lapEnd":
                            System.out.println("calculating lap time!!!!!!!!!!!!!----------------------------------------------------------------------------");
                            gui.calculateLapTime((double)System.currentTimeMillis());
                            break;
                        case "gameOver":
                            gui.endGame(object.getString("winner"));
                    }
                } catch (Exception ignored) {
                    ignored.printStackTrace();
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





    public WebSocketClient getClient(){
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
