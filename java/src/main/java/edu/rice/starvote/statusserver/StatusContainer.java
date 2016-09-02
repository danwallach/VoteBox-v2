package edu.rice.starvote.statusserver;

import edu.rice.starvote.BallotStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Holds a ballot status value. Event handlers can be registered to objects of this class, to be executed whenever
 * a change is made to the status. Modifications to the status are thread-safe.
 *
 * @author luejerry
 */
public class StatusContainer {

    private volatile BallotStatus status;
    private final List<Consumer<BallotStatus>> listeners;

    /**
     * Constructs a new status container with an initial status of OFFLINE.
     */
    public StatusContainer() {
        status = BallotStatus.OFFLINE;
        listeners = new LinkedList<>();
    }

    /**
     * Get the currently stored status.
     * @return Current status.
     */
    public BallotStatus getStatus() {
        return status;
    }

    /**
     * Modify the status. Runs the event handlers of all registered listeners, passing to them the new status.
     * @param status Updated status.
     */
    public synchronized void writeStatus(BallotStatus status) {
        this.status = status;
        listeners.forEach((listener) -> listener.accept(status));
    }

    /**
     * Register a new event listener, which fires whenever the status is updated via `writeStatus()`.
     * @param task Function to be executed when the status is changed. The function takes one parameter, the updated
     *             status value.
     */
    public synchronized void addListener(Consumer<BallotStatus> task) {
        listeners.add(task);
    }

    /**
     * Clear all registered event listeners.
     */
    public synchronized void removeListeners() {
        listeners.clear();
    }
}
