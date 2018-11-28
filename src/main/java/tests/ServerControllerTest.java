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

    @Before
    public void setup(){
        controller = new ServerController();
        conn = new WebSocket() {
            @Override
            public void close(int code, String message) {

            }

            @Override
            public void close(int code) {

            }

            @Override
            public void close() {

            }

            @Override
            public void closeConnection(int code, String message) {

            }

            @Override
            public void send(String text) throws NotYetConnectedException {

            }

            @Override
            public void send(ByteBuffer bytes) throws IllegalArgumentException, NotYetConnectedException {

            }

            @Override
            public void send(byte[] bytes) throws IllegalArgumentException, NotYetConnectedException {

            }

            @Override
            public void sendFrame(Framedata framedata) {

            }

            @Override
            public void sendFrame(Collection<Framedata> frames) {

            }

            @Override
            public void sendPing() throws NotYetConnectedException {

            }

            @Override
            public void sendFragmentedFrame(Framedata.Opcode op, ByteBuffer buffer, boolean fin) {

            }

            @Override
            public boolean hasBufferedData() {
                return false;
            }

            @Override
            public InetSocketAddress getRemoteSocketAddress() {
                return null;
            }

            @Override
            public InetSocketAddress getLocalSocketAddress() {
                return null;
            }

            @Override
            public boolean isConnecting() {
                return false;
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public boolean isClosing() {
                return false;
            }

            @Override
            public boolean isFlushAndClose() {
                return false;
            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public Draft getDraft() {
                return null;
            }

            @Override
            public READYSTATE getReadyState() {
                return null;
            }

            @Override
            public String getResourceDescriptor() {
                return null;
            }

            @Override
            public <T> void setAttachment(T attachment) {

            }

            @Override
            public <T> T getAttachment() {
                return null;
            }
        };
        u = new User(0, conn, new Label(), new Label(), new Label(), new Label(), new Label());
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
        controller.addPlayer(data, conn);
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