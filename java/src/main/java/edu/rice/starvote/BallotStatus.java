package edu.rice.starvote;

/**
 * Describes the state of the ballot box. Each message displayed on the ballot box screen correlates with one status.
 * The `name` of each status is the string pushed to the status display server.
 *
 * @author luejerry
 */
public enum BallotStatus {

    OFFLINE("offline"),
    WAITING("waiting"),
    SPOOLING("pending"),
    ACCEPT("accept"),
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
