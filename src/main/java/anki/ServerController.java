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
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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

    /*

        CHANGE THIS TO RUN ON YOUR CURRENT IP


     */

    private InetSocketAddress address = new InetSocketAddress("129.3.169.221", 5021);



    private User users[];
    private String mapCoords[];
    private ImageView userCars[] = new ImageView[4];
    private static int mapLength;

    @FXML
    private AnchorPane anchor;

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
    private Label user2speed;
    @FXML
    private Label user2direction;
    @FXML
    private Label user2position;

    @FXML
    private Label user3;
    @FXML
    private Label user3vehicle;
    @FXML
    private Label user3speed;
    @FXML
    private Label user3direction;
    @FXML
    private Label user3position;

    @FXML
    private Label user4;
    @FXML
    private Label user4vehicle;
    @FXML
    private Label user4speed;
    @FXML
    private Label user4direction;
    @FXML
    private Label user4position;

    @FXML
    private VBox user1box;

    @FXML
    private GridPane grid;


    private ImageView views[];


    private JSONObject object;
    private JSONObject data;

    private int playerCount;

    private JSONObject map;
    private List<Roadpiece> listMap;
    private WebSocketServer server;
    private String currentUser;

    @FXML
    public void initialize(){

    }

    public ServerController() {
        users = new User[4];
        //setLabelArrays();
        launchServer();

    }


    public void launchServer(){
        //user1.setText("test");
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
                                        users[i] = new User(user, vehicle, -1, conn);
                                        newUserData(i, data);
                                        switch(vehicle){
                                            case "GROUNDSHOCK":
                                                userCars[i] = new ImageView();
                                                userCars[i].setImage(new Image("gs.jpg"));
                                                userCars[i].setFitWidth(75);
                                                userCars[i].setFitHeight(75);
                                                break;
                                            case "SKULL":
                                                break;
                                            case "NUKE":
                                                break;
                                        }
                                        break;
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
                                //map = (Roadmap) data.get("map");
                                //listMap = map.toList();
                                map = data.getJSONObject("map");
                                mapLength = data.getInt("length");
                                System.out.println("RECIEVED MAP: " + data.get("map"));
                                renderMap(object);
                            }
                            break;
                        case "locationUpdate":
                            currentUser = data.getString("username");
                            System.out.println(data.toString());
                            for (int i = 0; i < 4; i++) {
                                if (users[i] != null && users[i].getName().equals(currentUser)) {
                                    //LocalizationPositionUpdateMessage positionMessage = (LocalizationPositionUpdateMessage) data.get("message");
                                    //u.setPosition(positionMessage.getRoadPieceId());*/
                                    System.out.println("PLAYER: " + data.getString("username") + "   AT POSITION: " + data.get("position"));
                                    setUserData(i, data);
                                }
                            }
                            break;
                        case "transitionUpdate":
                            for(int i = 0; i < 4; i++){
                                if(users[i] != null && data.getString("username").equals(users[i].getName())){
                                    System.out.println("CAR RENDER IN PROGRESS");
                                    renderCarLocation(userCars[i], users[i]);
                                }
                            }
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
                switch(i){
                    case 0:
                        user1.setText("NULL");
                        user1vehicle.setText("NULL");
                        user1speed.setText("NULL");
                        user1position.setText("NULL");
                        user1direction.setText("NULL");
                        break;
                    case 1:
                        user2.setText("NULL");
                        user2vehicle.setText("NULL");
                        user2speed.setText("NULL");
                        user2position.setText("NULL");
                        user2direction.setText("NULL");
                        break;
                    case 2:
                        user3.setText("NULL");
                        user3vehicle.setText("NULL");
                        user3speed.setText("NULL");
                        user3position.setText("NULL");
                        user3direction.setText("NULL");
                        break;
                    case 3:
                        user4.setText("NULL");
                        user4vehicle.setText("NULL");
                        user4speed.setText("NULL");
                        user4position.setText("NULL");
                        user4direction.setText("NULL");
                        break;
                }
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
                        user1.setText(message.get("username").toString());
                        user1vehicle.setText(message.get("vehicle").toString());
                        break;
                    case 1:
                        user2.setText(message.get("username").toString());
                        user2vehicle.setText(message.get("vehicle").toString());
                        break;
                    case 2:
                        user3.setText(message.get("username").toString());
                        user3vehicle.setText(message.get("vehicle").toString());
                        break;
                    case 3:
                        user4.setText(message.get("username").toString());
                        user4vehicle.setText(message.get("vehicle").toString());
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

    public void renderMap(JSONObject map){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                int x = 5;
                int y = 5;
                String rawCoords;
                grid.setAlignment(Pos.CENTER);
                JSONObject data = map.getJSONObject("data");
                int length = data.getInt("length");
                mapCoords = new String[length];

                //currentEnd helps determine where the end of the track piece is (up, down, left, right)
                //0 = right
                //1 = up
                //2 = left
                //3 = down;
                int currentEnd = 0;

                String[] mapPieces = new String[length];
                JSONObject mapNames = data.getJSONObject("map");

                int curveNum = 0;
                for(int i = 0; i < length; i++){
                    mapPieces[i] = mapNames.getString(String.valueOf(i));
                    if(mapPieces[i].equals("CurvedRoadpiece")){
                        curveNum++;
                    }
                }

                if(curveNum != 4){
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");
                    System.out.println("Uh oh");

                }


                if(mapPieces[0].equals("straight")){
                    currentEnd = 1;
                }
                else{
                    currentEnd = 0;
                }

                views = new ImageView[length];
                grid.setHgap(0);
                grid.setVgap(0);

                for(int i = 0; i < length; i++){
                    System.out.println(mapPieces[i]);
                    switch (mapPieces[i]){
                        case "CurvedRoadpiece":
                            //place the curved in
                            views[i] = new ImageView();
                            views[i].setImage(new Image("curve.png"));
                            views[i].setRotate(90 - (90 * Math.abs(currentEnd%4)));
                            currentEnd++;
                            break;
                        case "StraightRoadpiece":
                            views[i] = new ImageView();
                            views[i].setImage(new Image("straight.png"));
                            views[i].setRotate(90*(Math.abs(currentEnd%4)));
                            //place straight
                            break;
                        case "StartRoadpiece":
                            views[i] = new ImageView();
                            views[i].setImage(new Image("straight.png"));
                            views[i].setRotate(90*(Math.abs(currentEnd%4)));
                            break;
                    }
                    views[i].setY(100);
                    views[i].setX(100);
                    views[i].setFitHeight(350);
                    views[i].setFitWidth(350);
                    grid.add(views[i], x, y);
                    mapCoords[i] = new String(x + ":" + y);
                    rawCoords = getCoordinates(x, y, currentEnd);
                    x = Integer.valueOf(rawCoords.split(":")[0]);
                    y = Integer.valueOf(rawCoords.split(":")[1]);
                }

            }
        });

    }

    public void renderCarLocation(ImageView vehicle, User user){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int x = Integer.valueOf(mapCoords[Math.abs(user.position%mapLength)].split(":")[0]);
                int y = Integer.valueOf(mapCoords[Math.abs(user.position%mapLength)].split(":")[1]);
                grid.getChildren().remove(vehicle);
                user.position++;
                grid.add(vehicle, x, y);

                GridPane.setHalignment(vehicle, HPos.CENTER);
            }
        });

    }

    public String getCoordinates(int x, int y, int currentEnd){
        //seperated by :
        String coords = "";
        System.out.println(x + " " + y);

        switch(currentEnd){
            case 0:
                y++;
                break;
            case 1:
                x++;
                break;
            case 2:
                y--;
                break;
            case 3:
                x--;
                break;
        }

        coords = x + ":" + y;
        System.out.println(currentEnd);
        System.out.println("---------------------"+coords+"---------------------");
        return coords;
    }


    /*public void setMap(){
        views = new ImageView[4];
        for(int i = 0; i < 4; i++){
            views[i] = new ImageView();
            views[i].setImage(new Image("curve.png"));

            if(i <= 1) {
                views[i].setRotate(90-(i*90));
                grid.add(views[i], 1 + i, 10);
            }
            else{
                views[i].setRotate(90*i);
                grid.add(views[i], 1 + i%2, 9);
            }
        }
    }

    public void updateMap(){
        if(vehicleView == null){
            vehicleView = new ImageView(new Image("gs.jpg"));
            vehicleView.setFitHeight(50);
            vehicleView.setFitWidth(50);
        }
        grid.add(vehicleView, 2, 10);
    }*/
}


