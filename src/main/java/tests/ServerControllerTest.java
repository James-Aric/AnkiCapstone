package tests;

import anki.ServerController;
import anki.ServerLauncher;
import anki.User;
import com.google.common.annotations.Beta;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.*;

public class ServerControllerTest{
    static ServerController controller;
    WebSocket conn;
    User u;
    static ServerLauncher launcher;

    @BeforeClass
    public static void setup() throws Exception{
        launcher = new ServerLauncher();
        //controller = new ServerController();
        //controller.initialize();
        Thread thread = new Thread(){
            public void run(){
                try{
                    Application.launch(ServerLauncher.class);
                }catch (Exception e){

                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(3000);  // Wait for 3 seconds before interrupting JavaFX application
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        thread.interrupt();
        try {
            thread.join(1); // Wait 1 second for our wrapper thread to finish.
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        controller = launcher.controller;
    }

    @Test
    public void initialize() {
    }

    @Test
    public void launchServer() {
    }

    @Test
    public void addPlayer(){
        JSONObject data = new JSONObject();
        data.put("username", "test");
        data.put("vehicle", "test");
        Assert.assertTrue(controller.addPlayer(data, controller.connTest));
        data = new JSONObject();
        data.put("username", "");
        data.put("vehicle", "");
        Assert.assertTrue(controller.addPlayer(data, controller.connTest));
        data = new JSONObject();
        data.put("username", (String)null);
        data.put("vehicle", (String)null);
        Assert.assertFalse(controller.addPlayer(data, controller.connTest));
    }

    @Test
    public void removePlayer() {
        JSONObject data = new JSONObject();
        data.put("username", "test");
        data.put("vehicle", "test");
        Assert.assertTrue(controller.addPlayer(data, controller.connTest));
        for(int i = 0; i < 4; i++){
            if(controller.getUser(i) != null){
                System.out.println(controller.getUser(i).getName());
            }
        }
        Assert.assertTrue(controller.removePlayer("test"));
        Assert.assertFalse(controller.removePlayer("bob"));
        Assert.assertFalse(controller.removePlayer(null));
    }


    @Test
    public void setUserData() {
        JSONObject data = new JSONObject();
        data.put("speed", 100);
    }

    @Test
    public void getCoordinates() {
        int x = 10;
        int y = 10;
        int currentEnd = 0;

        assertEquals("10:9", controller.getCoordinates(x, y, currentEnd));
        currentEnd++;
        assertEquals("11:10", controller.getCoordinates(x, y, currentEnd));
        currentEnd++;
        assertEquals("10:11", controller.getCoordinates(x, y, currentEnd));
        currentEnd++;
        assertEquals("9:10", controller.getCoordinates(x, y, currentEnd));
    }
    

    @Test
    public void setRacePositions() {
    }

    @Test
    public void constructIDList() {
    }

    @Test
    public void setVehicle() {
    }
}