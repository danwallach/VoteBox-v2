package edu.rice.starvote.ballotbox;

/**
 * Describes the state of the ballot box. Each message displayed on the ballot box screen correlates with one status.
 * The `name` of each status is the string pushed to the status display server.
 *
 * @author luejerry
 */
public enum BallotStatus {

    /**
     * Box in error state and cannot be used.
     */
    OFFLINE("offline"),
    /**
     * Box is active and waiting for paper to be placed in tray.
     */
    WAITING("waiting"),
    /**
     * Box is busy processing a ballot.
     */
    SPOOLING("pending"),
    /**
     * A sheet of a multipage ballot was accepted.
     */
    PARTIALACCEPT("partial"),
    /**
     * The ballot was accepted.
     */
    ACCEPT("accept"),
    /**
     * The ballot was rejected.
     */
    REJECT("reject");

    private String displayName;

    BallotStatus(String name) {
        displayName = name;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
