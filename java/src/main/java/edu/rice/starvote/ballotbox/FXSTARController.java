package edu.rice.starvote.ballotbox;

import auditorium.NetworkException;
import edu.rice.starvote.ballotbox.drivers.*;
import edu.rice.starvote.ballotbox.statusfx.StatusUIController;
import edu.rice.starvote.ballotbox.util.GPIOListener;
import votebox.AuditoriumParams;

import java.util.Scanner;

/**
 * Main entry point of program. Instantiates all components of the ballot box software and links them together.
 *
 * @author luejerry
 */
public class FXSTARController {

    private final GPIOListener listener;
    private final GPIOListener halfwaySensor;
    private final ISpooler spooler;
    private final IScanner scanner;
    private final IMotor motor;
    private final IDiverter diverter;
    private final IStatusUpdate updater;
    private final IValidator validator;
    private final Monitor monitor;


    /**
     * Instantiates all modules, performing the necessary linking. Does not start the paper listener or status server.
     *
     * @param serial Unique machine identifier number. Must correspond to a valid key in `/keys`.
     * @param launchCode Election day launch code, unique to each poll location.
     */
    public FXSTARController(int serial, String launchCode, IStatusUpdate updater) {
        listener = new GPIOListener(24);
        halfwaySensor = new GPIOListener(23);
        motor = new PrinterMotor(22, 27, new PWMBlaster(17, 50));
        diverter = new DiverterPWM(new PWMBlaster(18, 50));
        scanner = new Scan();

        STARConnector starConnector;
        IValidator tempValidator;
        try {
            starConnector = new STARConnector(serial, launchCode);
            final STARConnector star = starConnector;
            tempValidator = code -> {
                final BallotResult result = star.validate(code, 5000);
                return (result == BallotResult.ACCEPT);
            };
            System.out.println("Connected to the Auditorium network.");
        } catch (NetworkException e) {
            System.err.println("ERROR: Could not connect to the Auditorium network! Running in test mode only");
            e.printStackTrace();
            tempValidator = code -> true;
        }
        validator = tempValidator;

        this.updater = updater;
        spooler = new PaperSpooler(updater, diverter, motor, halfwaySensor, scanner, validator);
        monitor = new Monitor(listener, spooler);
    }

    /**
     * Start the status server and paper listener. **This method does not return.**
     */
    public void run() {
        updater.pushStatus(BallotStatus.WAITING);
        monitor.run();
    }

    /**
     * Program main entry point. Starts up the ballot box.
     * @param args Ignored.
     */
//    public static void main (String[] args) {
//        final AuditoriumParams _constants = new AuditoriumParams("bs.conf");
//        final int serial;
//        if (args.length == 1) {
//            serial = Integer.parseInt(args[0]);
//        } else {
//            serial = _constants.getDefaultSerialNumber();
//        }
//        if (serial == -1) {
//            throw new RuntimeException("No machineID provided");
//        }
//        final Scanner keyboard = new Scanner(System.in);
//        System.out.print("Enter launch code: ");
//        final String launchCode = keyboard.next();
//        keyboard.close();
//        final FXSTARController controller = new FXSTARController(serial, launchCode);
//        controller.run();
//    }
}
