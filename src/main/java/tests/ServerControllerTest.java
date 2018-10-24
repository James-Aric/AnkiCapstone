package tests;

import anki.ServerController;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerControllerTest {
    ServerController controller = new ServerController();

    @Test
    public void initialize() {
    }

    @Test
    public void launchServer() {
    }

    @Test
    public void removePlayer() {
    }

    @Test
    public void newUserData() {
    }

    @Test
    public void setUserData() {
    }

    @Test
    public void renderMap() {
    }

    @Test
    public void renderCarLocation() {
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
}