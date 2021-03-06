package edu.rice.starvote.ballotbox.statusserver;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

/**
 * WebSocket handler class for status server. Manages the global WebSocket sessions list.
 *
 * @author luejerry
 */
@WebSocket
public class StatusWebSocket {

    @OnWebSocketConnect
    public void connected(Session session) throws IOException {
        StatusServer.sessions.add(session);
        System.out.println(session.getRemoteAddress().getHostString() + " connected");

        // Send the box status immediately on connect. Note that other parts of the program MUST be updating (writing)
        // the static container in order for this to be accurate.
        session.getRemote().sendString(StaticContainer.statusContainer.getStatus().toString());
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        StatusServer.sessions.remove(session);
        System.out.println(session.getRemoteAddress().getHostString() + " disconnected with code " + statusCode + ": " + reason);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        System.out.println(session.getRemoteAddress().getHostString() + " messaged: " + message);
    }
}
