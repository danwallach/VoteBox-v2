package edu.rice.starvote.ballotbox;

/**
 * Interface for ballot status updates to be sent from the paper feeder. Status updates are sent to the status server
 * in reference implementation.
 *
 * @author luejerry
 */
public interface IStatusUpdate {

    /**
     * Update the status of the ballot box.
     * @param status Ballot box status.
     */
    void pushStatus(BallotStatus status);
}
