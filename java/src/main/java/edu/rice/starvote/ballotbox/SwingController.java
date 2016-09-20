package edu.rice.starvote.ballotbox;

import edu.rice.starvote.ballotbox.drivers.*;
import edu.rice.starvote.ballotbox.statusserver.StaticContainer;
import edu.rice.starvote.ballotbox.statusserver.StatusContainer;
import edu.rice.starvote.ballotbox.statusserver.StatusServer;
import edu.rice.starvote.ballotbox.swingui.DisplayController;
import edu.rice.starvote.ballotbox.swingui.SwingDisplay;
import edu.rice.starvote.ballotbox.util.GPIOListener;

/**
 * Main entry point of program. Instantiates all components of the ballot box software and links them together.
 *
 * @author luejerry
 */
public class SwingController {

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

    /**
     * Instantiates all modules, performing the necessary linking. Does not start the paper listener or status server.
     */
    public SwingController() {
        listener = new GPIOListener(24);
        halfwaySensor = new GPIOListener(23);
        motor = new PrinterMotor(22, 27, new PWMBlaster(17, 50));
        diverter = new DiverterPWM(new PWMBlaster(18, 50));
        scanner = new Scan();
        validator = code -> true;
        display = new DisplayController(new SwingDisplay());
        updater = display;
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

    /**
     * Program main entry point. Starts up the ballot box.
     * @param args Ignored.
     */
    public static void main (String[] args) {
        final SwingController controller = new SwingController();
        controller.run();
    }
}
