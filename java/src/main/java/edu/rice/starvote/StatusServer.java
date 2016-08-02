package edu.rice.starvote;

import static spark.Spark.*;
/**
 * Created by luej on 8/2/16.
 */
public class StatusServer {

    private int port;
    private StatusContainer statusProvider;

    public StatusServer(int port, StatusContainer statusProvider) {
        this.port = port;
        this.statusProvider = statusProvider;
    }

    private void setRoutes() {
        get("/", (request, response) -> {
            final BallotStatus status = this.statusProvider.getStatus();
            response.status(200);
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-type", "text/html");
            response.body(status.toString());
            return response;
        });
    }

    public void start() {
        port(port);
        setRoutes();
    }
}
