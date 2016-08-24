package edu.rice.starvote;

/**
 * Main entry point of program. Instantiates all components of the ballot box software and links them together.
 *
 * @author luejerry
 */
public class Controller {

    private final GPIOListener listener;
    private final GPIOListener halfwaySensor;
    private final ISpooler spooler;
    private final IScanner scanner;
    private final IMotor motor;
    private final IDiverter diverter;
    private final IStatusUpdate updater;
    private final IValidator validator;
    private final Monitor monitor;
    private final StatusContainer statusProvider;
    private final StatusServer statusServer;

    /**
     * Instantiates all modules, performing the necessary linking. Does not start the paper listener or status server.
     */
    public Controller() {
        listener = new GPIOListener(24);
        halfwaySensor = new GPIOListener(23);
        motor = new PrinterMotor(22, 27, new PWMBlaster(17, 50, 0));
        diverter = new DiverterBlaster(new PWMBlaster(18, 50));
        scanner = new Scan();
        validator = code -> true;
        statusProvider = new StatusContainer();
        updater = status -> {
            System.out.println("Ballot status sent to server: " + status.toString());
            statusProvider.writeStatus(status);
        };
        spooler = new PaperSpooler(updater, diverter, motor, halfwaySensor, scanner, validator);
        monitor = new Monitor(listener, spooler);
        statusServer = new StatusServer(7654, statusProvider);
    }

    /**
     * Start the status server and paper listener. **This method does not return.**
     */
    public void run() {
        final Thread serverThread = new Thread(statusServer::start);
        serverThread.run();
        updater.pushStatus(BallotStatus.WAITING);
        monitor.run();
    }

    /**
     * Program main entry point. Starts up the ballot box.
     * @param args Ignored.
     */
    public static void main (String[] args) {
        final Controller controller = new Controller();
        controller.run();
    }
}
