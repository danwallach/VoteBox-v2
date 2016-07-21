package edu.rice.starvote;

/**
 * Created by luej on 7/20/16.
 */
public enum BallotStatus {

    OFFLINE("Offline"),
    WAITING("Waiting"),
    SPOOLING("Pending"),
    ACCEPT("Accepted"),
    REJECT("Rejected");

    private String displayName;

    BallotStatus(String name) {
        displayName = name;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
