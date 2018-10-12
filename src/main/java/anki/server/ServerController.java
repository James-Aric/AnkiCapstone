package anki.server;

import anki.client.User;
import de.adesso.anki.Vehicle;
import de.adesso.anki.roadmap.Roadmap;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

public class ServerController {
    User users[];


    JSONObject object;
    JSONObject data;

    int playerCount;

    Roadmap map;

    WebSocketServer server = new WebSocketServer() {
        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            users = new User[4];
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
                switch (object.getString("event")) {
                    case "connect":
                        String user = data.getString("username");
                        Vehicle vehicle = (Vehicle) data.get("vehicle");
                        if(playerCount != 4){
                            for(int i = 0; i < 4; i++){
                                if(users[i] == null){
                                    users[i] = new User(user, vehicle, null, conn);
                                }
                            }
                        }
                        else{
                            this.broadcast("full");
                        }
                        break;
                    case "disconnect":
                        user = data.getString("username");
                        removePlayer(user);
                        break;
                    case "roadmap":
                        if(map == null){
                            map = (Roadmap) data.get("roadmap");
                        }
                        break;
                    case "locationUpdate":
                        String userPosition = data.getString("username");
                        for(User u: users){
                            if(u.getName().equals(userPosition)){
                                u.setPosition((Roadpiece)data.get("position"));
                            }
                        }
                        break;
                    case "":
                }
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage());
            }
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {

        }

        @Override
        public void onStart() {

        }
    };

    public void removePlayer(String name){
        for(int i = 0; i < 4; i++){
            if(users[i].getName().equals(name)){
                users[i].getVehicle().disconnect();
                users[i] = null;
            }
        }
    }
}
