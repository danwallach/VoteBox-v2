package edu.rice.starvote;

/**
 * Created by luej on 7/20/16.
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
