package anki;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class SocketController {
    WebSocketClient client;

    /*try{}

    public void connectSocket(){
        client = new WebSocketClient(new URI("ws://localhost:3000")) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {

            }

            @Override
            public void onMessage(String s) {

            }

            @Override
            public void onClose(int i, String s, boolean b) {

            }

            @Override
            public void onError(Exception e) {

            }
        }
    }*/
}
