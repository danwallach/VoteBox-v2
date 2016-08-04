package edu.rice.starvote;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by luej on 8/2/16.
 */
public class StatusContainer {

    private volatile BallotStatus status;
    private final List<Consumer<BallotStatus>> listeners;

    public StatusContainer() {
        status = BallotStatus.OFFLINE;
        listeners = new LinkedList<>();
    }

    public BallotStatus getStatus() {
        return status;
    }

    public void writeStatus(BallotStatus status) {
        this.status = status;
        listeners.forEach((listener) -> listener.accept(status));
    }

    public void addListener(Consumer<BallotStatus> task) {
        listeners.add(task);
    }

    public void removeListeners() {
        listeners.clear();
    }
}
