package edu.rice.starvote.statusserver;

/**
 * A global static instance of a Status Container. This is a dirty hack to allow the websocket servlet
 * (`StatusWebSocket`) to access the box status, since it can only be scoped to the global namespace. The status
 * updater (`IStatusUpdate`) must write to this global status container in order for websocket-based status updates
 * to be accurate.
 *
 * @see StatusWebSocket
 * @author luejerry
 */
public class StaticContainer {
    /**
     * The global `StatusContainer`.
     */
    public static StatusContainer statusContainer = new StatusContainer();
}
