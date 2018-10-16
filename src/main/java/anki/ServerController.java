package anki;

import anki.User;
import de.adesso.anki.Vehicle;
import de.adesso.anki.messages.LocalizationPositionUpdateMessage;
import de.adesso.anki.messages.LocalizationTransitionUpdateMessage;
import de.adesso.anki.roadmap.Roadmap;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ServerController {
    User users[];


    JSONObject object;
    JSONObject data;

    int playerCount;

    Roadmap map;
    List<Roadpiece> listMap;
    InetSocketAddress address;
    WebSocketServer server;
    String currentUser;

    public ServerController() throws UnknownHostException {
        address = new InetSocketAddress(5020);
        users = new User[4];
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
                            String vehicle = data.getString("vehicle");
                            System.out.println("PLAYER: " + user + " CONNECTED ON VEHICLE " + vehicle);
                            if (playerCount != 4) {
                                for (int i = 0; i < 4; i++) {
                                    if (users[i] == null) {
                                        users[i] = new User(user, vehicle, -1, conn, null);
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
                                listMap = map.toList();
                            }
                            break;
                        case "locationUpdate":
                            currentUser = data.getString("username");
                            System.out.println(data.toString());
                            for (User u : users) {
                                if (u.getName().equals(currentUser)) {
                                    //LocalizationPositionUpdateMessage positionMessage = (LocalizationPositionUpdateMessage) data.get("message");
                                    //u.setPosition(positionMessage.getRoadPieceId());*/
                                    System.out.println("PLAYER: " + object.get("username") + "   AT POSITION: " + data.get("locationId"));
                                }
                            }
                            break;
                        case "transitionUpdate":
                            //LocalizationTransitionUpdateMessage transitionUpdateMessage = (LocalizationTransitionUpdateMessage) data.get("message");
                            currentUser = object.getString("username");
                            for(User u: users){
                                if(u != null && u.getName().equals(currentUser)){
                                    u.position++;
                                    if(u.position == listMap.size()){
                                        u.position = 0;
                                    }
                                    u.setMapPosition(listMap.get(u.position));
                                    break;
                                }
                            }
                            break;
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
                users[i] = null;
            }
        }
    }
}
