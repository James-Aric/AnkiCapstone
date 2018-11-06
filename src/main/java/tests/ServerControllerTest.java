package tests;

import anki.ServerController;
import anki.User;
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

    @Test
    public void calculateRacePosition() {
        /*User user1 = new User("user1", "GS", 100, null, null);
        User user2 = new User("user2", "GS", 5124, null, null);
        User user3 = new User("user3", "GS", 1021, null, null);
        User user4 = new User("user4", "GS", 10, null, null);
        User testUsers[] = new User[4];
        testUsers[0] = user1;
        testUsers[1] = user2;
        testUsers[2] = user3;
        testUsers[3] = user4;

        Integer testArry[][] = controller.calculateRacePosition(testUsers);
        assertEquals(Integer.valueOf(3), Integer.valueOf(testArry[0][1]));
        assertEquals(Integer.valueOf(0), Integer.valueOf(testArry[1][1]));
        assertEquals(Integer.valueOf(2), Integer.valueOf(testArry[2][1]));
        assertEquals(Integer.valueOf(1), Integer.valueOf(testArry[3][1]));*/
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