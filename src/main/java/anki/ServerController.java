package anki;

import anki.User;

import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.activation.ActivationGroup_Stub;
import java.util.*;


public class ServerController {

    private int lapWins;

    /*

        CHANGE THIS TO RUN ON YOUR CURRENT IP


     */

    private InetSocketAddress address;// = new InetSocketAddress("129.3.171.208", 5023);

    private static int playerCount = 0;

    private User users[];
    private CustomRoadpiece customMap[];
    private String mapCoords[];
    private int mapIDs[];
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
    private Label user1laps;


    @FXML
    private Label user2;
    @FXML
    private Label user2vehicle;
    @FXML
    private Label user2speed;
    @FXML
    private Label user2direction;
    @FXML
    private Label user2laps;

    @FXML
    private Label user3;
    @FXML
    private Label user3vehicle;
    @FXML
    private Label user3speed;
    @FXML
    private Label user3direction;
    @FXML
    private Label user3laps;

    @FXML
    private Label user4;
    @FXML
    private Label user4vehicle;
    @FXML
    private Label user4speed;
    @FXML
    private Label user4direction;
    @FXML
    private Label user4laps;

    @FXML
    private GridPane grid;

    @FXML
    private Label lapTime, userBestLap, lapsWinLabel;

    @FXML
    private TextField ipName, lapTotal;

    private ImageView views[];


    private JSONObject object;
    private JSONObject data;


    private JSONObject map;
    private WebSocketServer server;
    private String currentUser;
    private String currentVehicle;

    @FXML
    public void initialize(){
        lapTime.setText(""+ Long.MAX_VALUE);
        lapTime.setVisible(false);

    }

    public ServerController() {
        users = new User[4];
        //setLabelArrays();
        //launchServer();

    }
    public void setIP(ActionEvent event) {
        this.address = new InetSocketAddress(ipName.getText(), 5023);
        //this.address = new InetSocketAddress("pi.cs.oswego.edu", 5023);
        ((Button)event.getSource()).setVisible(false);
        ipName.setVisible(false);
        launchServer();
    }

    public void setLapWins(ActionEvent event){
        try{
            lapWins = Integer.valueOf(lapTotal.getText());
            lapTotal.setVisible(false);
            ((Button)event.getSource()).setVisible(false);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lapsWinLabel.setText(""+lapWins);
                }
            });
        }catch (Exception e){
            System.out.println("invalid input");
        }
    }

    public void resetGame(ActionEvent event){
        userBestLap.setText("");
        lapTime.setText("" + Double.MAX_VALUE);
        lapTime.setVisible(false);
        for(int i = 0; i < 4; i++){
            if(users[i] != null){
                users[i].position = 0;
                users[i].resetLaps();
                renderCarLocation(userCars[i], users[i]);
            }
        }
    }

    public void gameOver(String winner){
        JSONObject object = new JSONObject();
        object.put("event", "gameOver");
        object.put("winner", winner);
        server.broadcast(object.toString());
    }

    public void launchServer(){
        //user1.setText("test");
        //InetSocketAddress address2 = new InetSocketAddress("localhost", 5023);
        server = new WebSocketServer(address) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                /*Users = new User[4];*/
                System.out.println("NEW CONNECTION");

            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                for(User u: users){
                    if(u.conn == conn){
                        removePlayer(u.getName());
                    }
                }
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                try {
                    object = new JSONObject(message);
                    data = null;
                    try{
                        data = object.getJSONObject("data");
                    }catch (Exception e){
                        System.out.println("received ready/start message");
                    }
                    //System.out.println(data.toString());
                    switch (object.getString("event")) {
                        case "connect":
                            String user = data.getString("username");
                            String vehicle = data.getString("vehicle");
                            System.out.println("PLAYER: " + user + " CONNECTED ON VEHICLE " + vehicle);
                            if (playerCount != 4) {
                                for (int i = 0; i < 4; i++) {
                                    if (users[i] == null) {
                                        Label[] temp = returnUserData(i);
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                temp[0].setText(user);
                                                temp[1].setText(vehicle);
                                            }
                                        });
                                        users[i] = new User(0, conn, temp[2], temp[3],temp[1],temp[0], temp[4]);
                                        setVehicle(data.getString("vehicle"), i);
                                        if(map == null){
                                            object = new JSONObject();
                                            object.put("event", "scan");
                                            conn.send(object.toString());
                                        }
                                        else{
                                            renderCarLocation(userCars[i], users[i]);
                                        }
                                        //newUserData(i, data);


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
                                System.out.println("map received------------------------------------------------");
                                //map = (Roadmap) data.get("map");
                                //listMap = map.toList();
                                map = data.getJSONObject("map");
                                mapLength = data.getInt("length");
                                constructIDList(data.getJSONObject("ids"));
                                System.out.println("RECIEVED MAP: " + data.get("map"));
                                renderMap(object);
                                for(int i = 0; i < 4; i++){
                                    if(users[i] != null){
                                        renderCarLocation(userCars[i], users[i]);
                                    }
                                }
                            }
                            /*for(int i = 0; i < 4; i++){
                                if(users[i].getName().equals(data.getString("username"))){
                                    renderCarLocation(userCars[i], users[i]);
                                    break;
                                }
                            }*/
                            break;
                        case "locationUpdate":
                            currentUser = data.getString("username");
                            System.out.println(data.toString());
                            for (int i = 0; i < 4; i++) {
                                if (users[i] != null && users[i].getName().equals(currentUser)) {
                                    //LocalizationPositionUpdateMessage positionMessage = (LocalizationPositionUpdateMessage) data.get("message");
                                    //u.setPosition(positionMessage.getRoadPieceId());*/
                                    System.out.println("PLAYER: " + data.getString("username") + "   AT POSITION: " + data.get("position"));
                                    setUserData(users[i], data);
                                    break;
                                }
                            }
                            break;
                        case "transitionUpdate":
                            if(map != null) {
                                for (int i = 0; i < 4; i++) {
                                    if (users[i] != null && data.getString("username").equals(users[i].getName())) {
                                        System.out.println("CAR RENDER IN PROGRESS");
                                        //users[i].setMapPosition();
                                        users[i].position++;
                                        renderCarLocation(userCars[i], users[i]);
                                        setRacePositions();
                                        if (users[i].position % mapLength == 0) {
                                            object = new JSONObject();
                                            object.put("event", "lapEnd");
                                            System.out.println("SENDING LAP COMPLETE -----------------------------------------------------------------------");
                                            users[i].conn.send(object.toString());
                                            users[i].setLapNum();

                                            if(users[i].getLapNum() == lapWins){
                                                gameOver(users[i].getName());
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            break;
                        case "readyUp":
                            currentUser = object.getString("username");
                            currentVehicle = object.getString("vehicle");
                            for(User u: users){
                                if(u.username.equals(currentUser) && u.getVehicle().equals(currentVehicle)){
                                    u.readyUp();
                                    playerCount++;
                                    JSONObject ready = new JSONObject();
                                    ready.put("event", "playerCount");
                                    ready.put("players", playerCount);
                                    server.broadcast(ready.toString());
                                }
                            }
                            break;
                        case "startRace":
                            sendStartMessage();
                            break;
                        case "bestLap":
                            System.out.println("RECIEVED NEW LAPTIME: " + object.getLong("time") + "    _-------------------------------------------------------------------------------------------------------------------------");
                            if(object.getLong("time") < Double.valueOf(lapTime.getText())){
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        lapTime.setText(""+object.getDouble("time"));
                                        lapTime.setVisible(true);
                                        userBestLap.setText(object.getString("user"));
                                    }
                                });
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
        server.setReuseAddr(true);
        server.start();
        System.out.println("Server started on " + server.getAddress());
        //user1.setText("test");
    }


    public void removePlayer(String name){
        for(int i = 0; i < 4; i++){
            if(users[i].getName().equals(name)){
                users[i].removeThisPlayer();
                users[i] = null;
                break;
            }
        }
    }


    /*public void newUserData(int location, JSONObject message){
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
    }*/

    public void setUserData(User u, JSONObject data){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                u.setSpeed(""+data.getInt("speed"));
            }
        });
    }
    public Label[] returnUserData(int location){
        Label[] labels = new Label[5];
        switch(location){
            case 0:
                labels[0] = user1;
                labels[1] = user1vehicle;
                labels[2] = user1speed;
                labels[3] = user1direction;
                labels[4] = user1laps;
                break;
            case 1:
                labels[0] = user2;
                labels[1] = user2vehicle;
                labels[2] = user2speed;
                labels[3] = user2direction;
                labels[4] = user2laps;
            break;
            case 2:
                labels[0] = user3;
                labels[1] = user3vehicle;
                labels[2] = user3speed;
                labels[3] = user3direction;
                labels[4] = user3laps;
                break;
            case 3:
                labels[0] = user4;
                labels[1] = user4vehicle;
                labels[2] = user4speed;
                labels[3] = user4direction;
                labels[4] = user4laps;
                break;
        }
        return labels;
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
                customMap = new CustomRoadpiece[length];
                //currentEnd helps determine where the end of the track piece is (up, down, left, right)
                //0 = right
                //1 = up
                //2 = left
                //3 = down;
                int currentEnd = 0;

                String[] mapPieces = new String[length];
                JSONObject mapNames = data.getJSONObject("map");
                JSONObject reversed = data.getJSONObject("reversed");
                JSONObject locationIDs = data.getJSONObject("ids");
                for(int i = 0; i < length; i++){
                    mapPieces[i] = mapNames.getString(String.valueOf(i));
                }


                currentEnd = 1;

                views = new ImageView[length];
                grid.setHgap(0);
                grid.setVgap(0);
                int startOrEnd = -1;

                for(int i = 0; i < length; i++){
                    System.out.println(mapPieces[i]);
                    System.out.println("LENGTH: " + mapPieces.length);
                    switch (mapPieces[i]){
                        case "CurvedRoadpiece":
                            if(reversed.getBoolean(String.valueOf(i))){
                                views[i] = new ImageView();
                                views[i].setImage(new Image("curve.png"));
                                currentEnd -= 2;
                                views[i].setRotate(90 + (90 * Math.abs(currentEnd%4)));
                            }
                            else{
                                //place the curved in
                                views[i] = new ImageView();
                                views[i].setImage(new Image("curve.png"));
                                currentEnd++;
                                views[i].setRotate(90 + (90 * Math.abs(currentEnd%4)));
                            }


                            break;
                        case "StraightRoadpiece":
                            views[i] = new ImageView();
                            views[i].setImage(new Image("straight.png"));
                            views[i].setRotate(90*(Math.abs(currentEnd%2) + 1));
                            //place straight
                            break;
                        case "FinishRoadpiece":
                            if(startOrEnd == -1){
                                startOrEnd = i;
                            }
                            views[i] = new ImageView();
                            views[i].setImage(new Image("start.png"));
                            views[i].setRotate(90*(Math.abs(currentEnd%2) + 1));
                            break;
                        case "StartRoadpiece":
                            if(startOrEnd == -1){
                                startOrEnd = i;
                            }
                            views[i] = new ImageView();
                            views[i].setImage(new Image("start.png"));
                            views[i].setRotate(90*(Math.abs(currentEnd%2) + 1));
                            break;

                    }
                    customMap[i] = new CustomRoadpiece(x, y, mapPieces[i], i);
                    views[i].setY(100);
                    views[i].setX(100);
                    views[i].setFitHeight(200);
                    views[i].setFitWidth(200);
                    grid.add(views[i], x, y);
                    mapCoords[i] = new String(x + ":" + y);
                    if(mapPieces[i].equals("FinishRoadpiece")){
                        mapCoords[i] = x+":"+y;
                    }
                    else{
                        rawCoords = getCoordinates(x, y, currentEnd);
                        x = Integer.valueOf(rawCoords.split(":")[0]);
                        y = Integer.valueOf(rawCoords.split(":")[1]);
                    }

                }
                for(int i = 0; i < mapLength; i++){
                    System.out.println(mapCoords[i]);
                }

            }
        });
    }

    public void renderCarLocation(ImageView vehicle, User user){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println( user.position + "        "  + Math.abs(user.position)%mapLength + "=============================================================================" + mapLength + "     " + mapCoords.length);
                //System.out.println(user.position + "    " + mapCoords[user.position]);
                int x = Integer.valueOf(mapCoords[Math.abs(user.position)%mapLength].split(":")[0]);
                int y = Integer.valueOf(mapCoords[Math.abs(user.position)%mapLength].split(":")[1]);
                grid.getChildren().remove(vehicle);
                grid.add(vehicle, x, y);
                System.out.println("CAR LOCATION NOW AT X: " + x + "  Y: " +y + "      INDEX: " +user.position%mapLength);
                GridPane.setHalignment(vehicle, HPos.CENTER);
            }
        });
    }

    public Integer[][] calculateRacePosition(User users[]){
        Integer indexPlaces[][] = new Integer[4][2];
        for(int i = 0; i < 4; i++){
            /*if(users[i] != null && users[firstIndex] != null){
                if(users[i].position > users[firstIndex].position){
                    firstIndex = i;
                }
            }*/
            if(users[i] != null) {
                indexPlaces[i][0] = users[i].position;
                indexPlaces[i][1] = i;
            }
            else{
                indexPlaces[i][0] = -100;
                indexPlaces[i][1] = -1;
            }
        }
        Arrays.sort(indexPlaces, java.util.Comparator.comparingInt(a -> a[0]));
        for(int i = 0; i < 4; i++){
            if(indexPlaces[i] != null){
                System.out.println(indexPlaces[i][0] + "    " + indexPlaces[i][1]);
            }
        }
        return  indexPlaces;
    }

    public void setRacePositions(){
        Integer[][] racePos = calculateRacePosition(users);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int pos = -1;
                for(int i = 0; i < 4; i++){
                    if(users[i] != null){
                        for(int j = 0; j < 4; j++){
                            if(racePos[j][1] == i){
                                //users[i].setRacePosition(""+j);
                                switch (j){
                                    case 0:
                                        pos = 4;
                                        break;
                                    case 1:
                                        pos = 3;
                                        break;
                                    case 2:
                                        pos = 2;
                                        break;
                                    case 3:
                                        pos = 1;
                                        break;
                                }
                                users[i].setRacePosition(""+pos);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    public void sendStartMessage(){
        Timer timer = new Timer();
        timer.schedule(new Countdown(5), 0, 1000);
    }

    public void constructIDList(JSONObject idlist){
        mapIDs = new int[mapLength];
        for(int i = 0; i < mapLength; i++){
            mapIDs[i] = idlist.getInt(i+"");
        }
    }

    public String getCoordinates(int x, int y, int currentEnd){
        //separated by :
        String coords = "";
        System.out.println(x + " " + y);

        switch(currentEnd%4){
            case 0:
                y--;
                break;
            case 1:
                x++;
                break;
            case 2:
                y++;
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

    public void setVehicle(String vehicle, int i){
        switch(vehicle){
            case "GROUNDSHOCK":
                userCars[i] = new ImageView();
                userCars[i].setImage(new Image("gs.jpg"));
                userCars[i].setFitWidth(50);
                userCars[i].setFitHeight(50);
                break;
            case "SKULL":
                userCars[i] = new ImageView();
                userCars[i].setImage(new Image("skull.jpg"));
                userCars[i].setFitWidth(50);
                userCars[i].setFitHeight(50);
                break;
            case "NUKE":
                userCars[i] = new ImageView();
                userCars[i].setImage(new Image("nuke.jpg"));
                userCars[i].setFitWidth(50);
                userCars[i].setFitHeight(50);
                break;
            //add more if i can remember to.
        }
    }

    class Countdown extends TimerTask{
        int time;
        public Countdown(int time){
            this.time = time;
        }

        @Override
        public void run() {
            object = new JSONObject();
            object.put("event", "countdown");
            object.put("time", time);
            time--;
            server.broadcast(object.toString());
            if(time == 0){
                object = new JSONObject();
                object.put("event", "countdown");
                object.put("time", time);
                server.broadcast(object.toString());
                this.cancel();
            }
        }
    }

    public ServerController instance(){
        return this;
    }
}


