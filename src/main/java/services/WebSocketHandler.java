package services;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import main.Server;

@WebSocket
public class WebSocketHandler {

    private String sender;
    private String message;


    @OnWebSocketConnect
    public void onConnect(Session user)  throws Exception {
        System.out.println("Conectando SensorMessage: " + user.getLocalAddress().getAddress().toString());
        Server.sensorsConnected.add(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("Desconectando SensorMessage: " + user.getLocalAddress().getAddress().toString());
        Server.sensorsConnected.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println("Message sent by " + user.getLocalAddress().getAddress().toString() + ": " + message);
    }
}
