package edu.rice.starvote;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static spark.Spark.*;
/**
 * Created by luej on 8/2/16.
 */
public class StatusServer {

    private int port;
    private StatusContainer statusProvider;
    static Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    public StatusServer(int port, StatusContainer statusProvider) {
        this.port = port;
        this.statusProvider = statusProvider;
    }

    private void setRoutes() {
        get("/status", (request, response) -> {
            final BallotStatus status = this.statusProvider.getStatus();
            response.status(200);
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-type", "text/html");
            return status.toString();
        });
    }

    private void setWebSocket() {
        webSocket("/pushstatus", StatusWebSocket.class);
        statusProvider.addListener((status) -> sessions.parallelStream().forEach((session) -> {
            System.out.println("Send status update: " + status.toString() + " to " + session.getRemoteAddress().getHostString());
            try {
                session.getRemote().sendString(status.toString());
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }));
    }

    public void start() {
        port(port);
        setWebSocket();
        setRoutes();
    }
}
