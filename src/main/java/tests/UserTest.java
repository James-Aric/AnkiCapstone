package tests;

import anki.User;
import javafx.scene.control.Label;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class UserTest {
    User test;
    @Before
    public void setup() throws URISyntaxException {
        test = new User(1, new WebSocketClient(new URI("boop")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

            }

            @Override
            public void onMessage(String message) {

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {

            }
        }, new Label(), new Label(), new Label(), new Label(), new Label());
    }

    @Test
    public void setRacePositionTest() {
        Assert.assertTrue(test.setRacePosition("1"));
        Assert.assertTrue(test.setRacePosition("2"));
        Assert.assertTrue(test.setRacePosition("3"));
        Assert.assertTrue(test.setRacePosition("4"));
        Assert.assertFalse(test.setRacePosition("poop"));
        Assert.assertFalse(test.setRacePosition(" "));
        Assert.assertFalse(test.setRacePosition(null));
        Assert.assertFalse(test.setRacePosition(""));

    }

    @Test
    public void setVehicle() {
        Assert.assertTrue(test.setVehicle("1"));
        Assert.assertTrue(test.setVehicle("2"));
        Assert.assertTrue(test.setVehicle("3"));
        Assert.assertTrue(test.setVehicle("4"));
        Assert.assertTrue(test.setVehicle("poop"));
        Assert.assertFalse(test.setVehicle(" "));
        Assert.assertFalse(test.setVehicle(null));
        Assert.assertFalse(test.setVehicle(""));
    }

    @Test
    public void setUsername() {
        Assert.assertTrue(test.setUsername("1"));
        Assert.assertTrue(test.setUsername("2"));
        Assert.assertTrue(test.setUsername("3"));
        Assert.assertTrue(test.setUsername("4"));
        Assert.assertTrue(test.setUsername("poop"));
        Assert.assertFalse(test.setUsername(" "));
        Assert.assertFalse(test.setUsername(null));
        Assert.assertFalse(test.setUsername(""));
    }

    @Test
    public void setSpeed() {
        Assert.assertTrue(test.setSpeed("1"));
        Assert.assertTrue(test.setSpeed("2"));
        Assert.assertTrue(test.setSpeed("3"));
        Assert.assertTrue(test.setSpeed("4"));
        Assert.assertTrue(test.setSpeed("1001230"));
        Assert.assertFalse(test.setSpeed("poop"));
        Assert.assertFalse(test.setSpeed(" "));
        Assert.assertFalse(test.setSpeed(null));
        Assert.assertFalse(test.setSpeed(""));
    }
}