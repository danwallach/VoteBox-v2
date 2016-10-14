package edu.rice.starvote.ballotbox.swingui;

import edu.rice.starvote.ballotbox.BallotProgress;
import edu.rice.starvote.ballotbox.BallotStatus;
import edu.rice.starvote.ballotbox.IStatusUpdate;

/**
 * Controls the box status Swing display. A `BallotStatus` is translated to a formatted, human-readable
 * message shown on the display.
 *
 * The `start()` method must be called to show the display before it is to be used.
 *
 * @author luejerry
 */
public class DisplayController implements IStatusUpdate {

    private SwingDisplay display;

    public DisplayController(SwingDisplay display) {
        this.display = display;
    }

    /**
     * Start the status display UI, making it visible. This must be invoked before any other method.
     */
    public void start() {
        display.start();
    }

    /**
     * Display a human-readable formatted ballot status.
     * @param status Ballot box status.
     */
    public void pushStatus(BallotStatus status) {
        switch (status) {
            case ACCEPT:
                display.setText(
                        "Your ballot was cast!",
                        "96px",
                        "#FFFFFF",
                        "#388E3C");
                break;
            case REJECT:
                display.setText(
                        "Your ballot was not recognized. Only insert unfolded ballots.",
                        "64px",
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
                        "96px",
                        "#000000",
                        "#FFFFFF"
                );
                break;
            case OFFLINE:
                display.setText(
                        "Use next machine.",
                        "96px",
                        "#FFFFFF",
                        "#F44336");
                break;
            default:
                display.setText(
                        "Error: unknown status.",
                        "96px",
                        "#FFFFFF",
                        "#F44336"
                );
                break;
        }
    }

    public void pushProgress(BallotProgress progress) {
        if (progress.completed()) {
            pushStatus(BallotStatus.ACCEPT);
        } else {
            display.setText(
                    "Page " + progress.pagesScanned + " of " + progress.pagesTotal + " accepted.",
                    "96px",
                    "#FFFFFF",
                    "#388E3C");
        }
    }
}
