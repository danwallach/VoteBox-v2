package edu.rice.starvote.ballotbox;

import edu.rice.starvote.ballotbox.drivers.*;
import edu.rice.starvote.ballotbox.swingui.DisplayController;
import edu.rice.starvote.ballotbox.swingui.SwingDisplay;
import edu.rice.starvote.ballotbox.swingui.VoiceController;
import edu.rice.starvote.ballotbox.util.GPIOListener;

import java.util.Optional;

/**
 * Main entry point of program. Instantiates all components of the ballot box software and links them together.
 *
 * @author luejerry
 */
public class PageController {

    private final GPIOListener listener;
    private final GPIOListener halfwaySensor;
    private final ISpooler spooler;
    private final IScanner scanner;
    private final IMotor motor;
    private final IDiverter diverter;
    private final IStatusUpdate updater;
    private final IValidator validator;
    private final Monitor monitor;
    private final DisplayController display;
    private final BallotDatabase ballotDb;

    /**
     * Instantiates all modules, performing the necessary linking. Does not start the paper listener or status server.
     */
    public PageController() {
        listener = new GPIOListener(24);
        halfwaySensor = new GPIOListener(23);
        motor = new PrinterMotor(22, 27, new PWMBlaster(17, 50));
        diverter = new DiverterPWM(new PWMBlaster(18, 50));
        scanner = new Scan();
        ballotDb = new BallotDatabase();
        display = new DisplayController(new SwingDisplay());
        final IStatusUpdate voiceController = new VoiceController();
        updater = status -> {
            display.pushStatus(status);
            voiceController.pushStatus(status);
        };
        validator = code -> {
            final Optional<BallotProgress> oProgress = ballotDb.scanPage(code);
            return oProgress.map((progress) -> {
                if (progress.completed()) {
                    System.out.println("Ballot code " + code + " completed");
                    updater.pushStatus(BallotStatus.ACCEPT);
                } else {
                    System.out.println("Ballot code " + code + " scanned " + progress.pagesScanned + " of " + progress.pagesTotal);
                    display.pushProgress(progress);
                }
                return true;
            }).orElseGet(() -> {
                System.out.println("Ballot code " + code + " invalid");
                updater.pushStatus(BallotStatus.REJECT);
                return false;
            });
        };
        spooler = new PaperSpooler(updater, diverter, motor, halfwaySensor, scanner, validator);
        monitor = new Monitor(listener, spooler);
    }

    /**
     * Start the status server and paper listener. **This method does not return.**
     */
    public void run() {
        display.start();
        updater.pushStatus(BallotStatus.WAITING);
        monitor.run();
    }

    public void addTestBallots() {
        ballotDb.addBallot("Acc3ptB@LL07", 3);
    }

    /**
     * Program main entry point. Starts up the ballot box.
     * @param args Ignored.
     */
    public static void main (String[] args) {
        final PageController controller = new PageController();
        controller.run();
    }
}
