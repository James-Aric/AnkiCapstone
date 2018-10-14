package anki;

import anki.User;
import de.adesso.anki.Vehicle;
import de.adesso.anki.roadmap.Roadmap;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class ServerController {
    User users[];


    JSONObject object;
    JSONObject data;

    int playerCount;

    Roadmap map;
    InetSocketAddress address;
    WebSocketServer server;

    public ServerController() throws UnknownHostException {
        address = new InetSocketAddress(5020);
        server = new WebSocketServer(address) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                /*sers = new User[4];*/
                System.out.println("NEW CONNECTION");
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {

            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                try {
                    object = new JSONObject(message);
                    data = null;
                    data = object.getJSONObject("data");
                    System.out.println(data.toString());
                    switch (object.getString("event")) {
                        case "connect":
                            String user = data.getString("username");
                            Vehicle vehicle = (Vehicle) data.get("vehicle");
                            System.out.println("PLAYER: " + user + " CONNECTED ON VEHICLE " + vehicle.getAddress());
                            if (playerCount != 4) {
                                for (int i = 0; i < 4; i++) {
                                    if (users[i] == null) {
                                        users[i] = new User(user, vehicle, null, conn);
                                    }
                                }
                            } else {
                                this.broadcast("full");
                            }
                            break;
                        case "disconnect":
                            user = data.getString("username");
                            removePlayer(user);
                            break;
                        case "roadmap":
                            if (map == null) {
                                map = (Roadmap) data.get("roadmap");
                            }
                            break;
                        case "locationUpdate":
                            String userPosition = data.getString("username");
                            System.out.println(data.get("data").toString());
                            for (User u : users) {
                                if (u.getName().equals(userPosition)) {
                                    u.setPosition((Roadpiece) data.get("position"));
                                }
                            }
                            break;
                        case "":
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                System.out.println(ex.getLocalizedMessage());
                ex.printStackTrace();
            }

            @Override
            public void onStart() {

            }
        };
    }


            //new InetSocketAddress("ws://localhost",3000);




    public void removePlayer(String name){
        for(int i = 0; i < 4; i++){
            if(users[i].getName().equals(name)){
                users[i].getVehicle().disconnect();
                users[i] = null;
            }
        }
    }
}
