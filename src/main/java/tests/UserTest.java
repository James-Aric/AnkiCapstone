package tests;

import anki.ServerController;
import anki.ServerLauncher;
import anki.User;
import javafx.application.Application;
import javafx.scene.control.Label;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class UserTest {
    User test;
    ServerController controller = new ServerController();
    ServerLauncher launcher;
    @Before
    public void setup() throws URISyntaxException {
        launcher = new ServerLauncher();
        Label[] temp = controller.returnUserData(0);
        test = new User(1, null, temp[2], temp[3],temp[1],temp[0], temp[4], "controller.getUser(0)");
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
        JSONObject data = new JSONObject();
        data.put("username", "test");
        data.put("vehicle", "test");
        controller.addPlayer(data, null);
    }

    @Test
    public void setRacePositionTest() {
        Assert.assertTrue(controller.getUser(0).setRacePosition("1"));
        Assert.assertTrue(controller.getUser(0).setRacePosition("2"));
        Assert.assertTrue(controller.getUser(0).setRacePosition("3"));
        Assert.assertTrue(controller.getUser(0).setRacePosition("4"));
        Assert.assertFalse(controller.getUser(0).setRacePosition("poop"));
        Assert.assertFalse(controller.getUser(0).setRacePosition(" "));
        Assert.assertFalse(controller.getUser(0).setRacePosition(null));
        Assert.assertFalse(controller.getUser(0).setRacePosition(""));

    }

    @Test
    public void setVehicle() {
        Assert.assertTrue(controller.getUser(0).setVehicle("1"));
        Assert.assertTrue(controller.getUser(0).setVehicle("2"));
        Assert.assertTrue(controller.getUser(0).setVehicle("3"));
        Assert.assertTrue(controller.getUser(0).setVehicle("4"));
        Assert.assertTrue(controller.getUser(0).setVehicle("poop"));
        Assert.assertFalse(controller.getUser(0).setVehicle(" "));
        Assert.assertFalse(controller.getUser(0).setVehicle(null));
        Assert.assertFalse(controller.getUser(0).setVehicle(""));
    }

    @Test
    public void setUsername() {
        Assert.assertTrue(controller.getUser(0).setUsername("1"));
        Assert.assertTrue(controller.getUser(0).setUsername("2"));
        Assert.assertTrue(controller.getUser(0).setUsername("3"));
        Assert.assertTrue(controller.getUser(0).setUsername("4"));
        Assert.assertTrue(controller.getUser(0).setUsername("poop"));
        Assert.assertFalse(controller.getUser(0).setUsername(" "));
        Assert.assertFalse(controller.getUser(0).setUsername(null));
        Assert.assertFalse(controller.getUser(0).setUsername(""));
    }

    @Test
    public void setSpeed() {
        Assert.assertTrue(controller.getUser(0).setSpeed("1"));
        Assert.assertTrue(controller.getUser(0).setSpeed("2"));
        Assert.assertTrue(controller.getUser(0).setSpeed("3"));
        Assert.assertTrue(controller.getUser(0).setSpeed("4"));
        Assert.assertTrue(controller.getUser(0).setSpeed("1001230"));
        Assert.assertFalse(controller.getUser(0).setSpeed("poop"));
        Assert.assertFalse(controller.getUser(0).setSpeed(" "));
        Assert.assertFalse(controller.getUser(0).setSpeed(null));
        Assert.assertFalse(controller.getUser(0).setSpeed(""));
    }
}