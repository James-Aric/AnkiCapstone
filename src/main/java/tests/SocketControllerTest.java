package tests;

import anki.GUI;
import anki.SocketController;
import de.adesso.anki.AnkiConnector;
import de.adesso.anki.Vehicle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class SocketControllerTest {
    SocketController controllerTest;

    @Before
    public void setup() throws Exception{
        controllerTest = new SocketController("James", new Vehicle(new AnkiConnector(InetAddress.getByName(null).toString(), 1000), "boop", "boop", "boop"), new GUI(), "address");
    }

    @Test
    public void onMessageTest(){
        Assert.assertTrue(true);
    }
}