package tests;

import anki.ServerController;
import anki.User;
import javafx.scene.control.Label;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.util.Collection;

import static org.junit.Assert.*;

public class ServerControllerTest {
    ServerController controller;
    WebSocket conn;
    User u;

    public ServerControllerTest(ServerController controller){
        this.controller = controller;
    }

    @Before
    public void setup(){

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
        int currentEnd = 2;

        assertEquals("10:9", controller.getCoordinates(x, y, currentEnd));
        currentEnd++;
        assertEquals("9:10", controller.getCoordinates(x, y, currentEnd));
        currentEnd = 0;
        assertEquals("10:11", controller.getCoordinates(x, y, currentEnd));
        currentEnd++;
        assertEquals("11:10", controller.getCoordinates(x, y, currentEnd));
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