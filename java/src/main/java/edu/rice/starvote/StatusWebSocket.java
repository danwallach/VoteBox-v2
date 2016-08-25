package edu.rice.starvote;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

import static edu.rice.starvote.StatusServer.sessions;

/**
 * WebSocket handler class for status server. Manages the global WebSocket sessions list.
 *
 * @author luejerry
 */
@WebSocket
public class StatusWebSocket {

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        System.out.println(session.getRemoteAddress().getHostString() + " connected");
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
        System.out.println(session.getRemoteAddress().getHostString() + " disconnected with code " + statusCode + ": " + reason);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        System.out.println(session.getRemoteAddress().getHostString() + " messaged: " + message);
    }
}
