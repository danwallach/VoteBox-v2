package edu.rice.starvote.ballotbox.swingui;

import edu.rice.starvote.ballotbox.BallotStatus;
import edu.rice.starvote.ballotbox.IStatusUpdate;

/**
 * Created by cyricc on 9/20/2016.
 */
public class DisplayController implements IStatusUpdate {

    private SwingDisplay display;

    public DisplayController(SwingDisplay display) {
        this.display = display;
    }

    public void start() {
        display.start();
    }

    public void pushStatus(BallotStatus status) {
        switch (status) {
            case ACCEPT:
                display.setText(
                        "Your ballot was cast!",
                        "128px",
                        "#FFFFFF",
                        "#388E3C");
                break;
            case REJECT:
                display.setText(
                        "Your ballot was not recognized. Only insert unfolded ballots.",
                        "80px",
                        "#FFFFFF",
                        "#F44336");
                break;
            case SPOOLING:
                display.setText(
                        "",
                        "10px",
                        "#FFFFFF",
                        "#616161"
                );
                break;
            case WAITING:
                display.setText(
                        "Please insert your ballot.",
                        "128px",
                        "#000000",
                        "#FFFFFF"
                );
                break;
            case OFFLINE:
                display.setText(
                        "Use next machine.",
                        "128px",
                        "#FFFFFF",
                        "#F44336");
                break;
            default:
                display.setText(
                        "Error: unknown status.",
                        "128px",
                        "#FFFFFF",
                        "#F44336"
                );
                break;
        }
    }
}
