package edu.rice.starvote.statusserver;

import edu.rice.starvote.BallotStatus;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static spark.Spark.*;

/**
 * Web server that supplies the current status of the ballot box. There are two available routes, supporting HTTP and
 * WebSocket methods of communication:
 *
 *  - `/status`: returns the status of the box in plaintext.
 *  - `/pushstatus`: WebSocket that sends the status of the box in plaintext whenever it is updated. Messages from the
 *  client are ignored.
 *
 *  Clients are recommended to use WebSockets, as it allows updates to be pushed directly by the server as they occur
 *  and minimizes unnecessary network usage. However, HTTP polling is also fully supported.
 *
 *  The server is not started on instantiation. To begin listening, call `start()`.
 *
 *  @author luejerry
 */
public class StatusServer {

    private int port;
    private StatusContainer statusProvider;

    /**
     * All connected WebSocket sessions. Unfortunately must be static due to limitation of the Spark/Jetty server.
     */
    static Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    /**
     * Constructor. Does not start the server.
     * @param port Listening port.
     * @param statusProvider Ballot status container to read status from.
     */
    public StatusServer(int port, StatusContainer statusProvider) {
        this.port = port;
        this.statusProvider = statusProvider;
    }

    /**
     * Set HTTP routes (`/status`).
     */
    private void setRoutes() {
        get("/status", (request, response) -> {
            final BallotStatus status = this.statusProvider.getStatus();
            response.status(200);
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-type", "text/html");
            return status.toString();
        });
    }

    /**
     * Set WebSocket route (`/pushstatus`).
     */
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

    /**
     * Start the server.
     */
    public void start() {
        port(port);
        setWebSocket();
        setRoutes();
    }
}
