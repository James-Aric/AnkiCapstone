package anki;

import anki.User;
import de.adesso.anki.Vehicle;
import de.adesso.anki.messages.LocalizationPositionUpdateMessage;
import de.adesso.anki.messages.LocalizationTransitionUpdateMessage;
import de.adesso.anki.roadmap.Roadmap;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import javafx.scene.control.Label;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;


public class ServerController {
    User users[];

    @FXML
    AnchorPane anchor;

    //user stat stuff
    @FXML
    private Label user1;
    @FXML
    private Label user1vehicle;
    @FXML
    private Label user1speed;
    @FXML
    private Label user1direction;
    @FXML
    private Label user1position;


    @FXML
    private Label user2;
    @FXML
    private Label user2vehicle;
    @FXML
    Label user2speed;
    @FXML
    Label user2direction;
    @FXML
    Label user2position;

    @FXML
    Label user3;
    @FXML
    Label user3vehicle;
    @FXML
    Label user3speed;
    @FXML
    Label user3direction;
    @FXML
    Label user3position;

    @FXML
    Label user4;
    @FXML
    Label user4vehicle;
    @FXML
    Label user4speed;
    @FXML
    Label user4direction;
    @FXML
    Label user4position;

    @FXML
    VBox user1box;


    JSONObject object;
    JSONObject data;

    int playerCount;

    Roadmap map;
    List<Roadpiece> listMap;
    InetSocketAddress address;
    WebSocketServer server;
    String currentUser;

    private InetSocketAddress test = new InetSocketAddress("129.3.169.200", 5020);

    @FXML
    public void initialize(){

    }

    public ServerController() throws UnknownHostException {
        address = new InetSocketAddress(5020);
        users = new User[4];
        //setLabelArrays();
        launchServer();

    }


    public void launchServer(){
        //user1.setText("test");
        server = new WebSocketServer(test) {
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
                                        newUserData(i, data);
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
                            for (int i = 0; i < 4; i++) {
                                if (users[i].getName().equals(currentUser)) {
                                    //LocalizationPositionUpdateMessage positionMessage = (LocalizationPositionUpdateMessage) data.get("message");
                                    //u.setPosition(positionMessage.getRoadPieceId());*/
                                    System.out.println("PLAYER: " + data.getString("username") + "   AT POSITION: " + data.get("position"));
                                    setUserData(i, data);
                                }
                            }
                            break;
                        case "transitionUpdate":
                            //LocalizationTransitionUpdateMessage transitionUpdateMessage = (LocalizationTransitionUpdateMessage) data.get("message");
                            /*currentUser = object.getString("username");
                            for(User u: users){
                                if(u != null && u.getName().equals(currentUser)){
                                    u.position++;
                                    if(u.position == listMap.size()){
                                        u.position = 0;
                                    }
                                    u.setMapPosition(listMap.get(u.position));
                                    break;
                                }
                            }*/
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
        server.start();
        System.out.println("Server started on " + server.getAddress());
        //user1.setText("test");
    }
            //new InetSocketAddress("ws://localhost",3000);




    public void removePlayer(String name){
        for(int i = 0; i < 4; i++){
            if(users[i].getName().equals(name)){
                users[i] = null;
            }
        }
    }

    /*public void setLabelArrays(){

        user1Labels[0] = user1;
        user1Labels[1] = user1vehicle;
        user1Labels[2] = user1speed;
        user1Labels[3] = user1speed;
        user1Labels[4] = user1direction;

        user2Labels[0] = user2;
        user2Labels[1] = user2vehicle;
        user2Labels[2] = user2speed;
        user2Labels[3] = user2speed;
        user2Labels[4] = user2direction;

        user3Labels[0] = user3;
        user3Labels[1] = user3vehicle;
        user3Labels[2] = user3speed;
        user3Labels[3] = user3speed;
        user3Labels[4] = user3direction;

        user4Labels[0] = user4;
        user4Labels[1] = user4vehicle;
        user4Labels[2] = user4speed;
        user4Labels[3] = user4speed;
        user4Labels[4] = user4direction;

        labels[0] = user1Labels;
        labels[1] = user2Labels;
        labels[2] = user3Labels;
        labels[3] = user4Labels;


        return;
    }*/
    public void newUserData(int location, JSONObject message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switch(location){
                    case 0:
                        user1speed.setText(message.get("username").toString());
                        user1position.setText(message.get("vehicle").toString());
                        break;
                    case 1:
                        user2speed.setText(message.get("username").toString());
                        user2position.setText(message.get("vehicle").toString());
                        break;
                    case 2:
                        user3speed.setText(message.get("username").toString());
                        user3position.setText(message.get("vehicle").toString());
                        break;
                    case 3:
                        user4speed.setText(message.get("username").toString());
                        user4position.setText(message.get("vehicle").toString());
                        break;
                }
            }
        });
    }

    public void setUserData(int location, JSONObject message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switch(location){
                    case 0:
                        user1speed.setText(message.get("speed").toString());
                        user1position.setText(message.get("position").toString());
                        user1direction.setText(message.get("offset").toString());
                        break;
                    case 1:
                        user2speed.setText(message.get("speed").toString());
                        user2position.setText(message.get("position").toString());
                        user2direction.setText(message.get("offset").toString());
                        break;
                    case 2:
                        user3speed.setText(message.get("speed").toString());
                        user3position.setText(message.get("position").toString());
                        user3direction.setText(message.get("offset").toString());
                        break;
                    case 3:
                        user4speed.setText(message.get("speed").toString());
                        user4position.setText(message.get("position").toString());
                        user4direction.setText(message.get("offset").toString());
                        break;
                }

            }
        });
    }
}


