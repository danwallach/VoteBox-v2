package edu.rice.starvote.ballotbox;

/**
 * Created by cyricc on 10/13/2016.
 */
public class BallotStatusProgress {
    public final BallotStatus status;
    public final BallotProgress progress;

    public BallotStatusProgress(BallotStatus status, BallotProgress progress) {
        this.status = status;
        this.progress = progress;
    }
}
