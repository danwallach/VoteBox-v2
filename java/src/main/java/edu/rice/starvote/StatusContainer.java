package edu.rice.starvote;

/**
 * Created by luej on 8/2/16.
 */
public class StatusContainer {

    private volatile BallotStatus status;

    public StatusContainer() {
        status = BallotStatus.OFFLINE;
    }

    public BallotStatus getStatus() {
        return status;
    }

    public void writeStatus(BallotStatus status) {
        this.status = status;
    }
}
