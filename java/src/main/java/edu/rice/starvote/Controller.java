package edu.rice.starvote;

/**
 * Created by luej on 7/27/16.
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

    public Controller() {
        listener = new GPIOListener(24);
        halfwaySensor = new GPIOListener(23);
        motor = new PrinterMotor(22, 27, new PWMBlaster(17, 50, 0));
        diverter = new DiverterPython();
        scanner = new Scan();
        validator = code -> true;
        statusProvider = new StatusContainer();
        updater = status -> {
            System.out.println("Ballot status sent to server: " + status.toString());
            statusProvider.writeStatus(status);
        };
        spooler = new PaperSpooler(updater, diverter, motor, halfwaySensor, scanner, validator);
        monitor = new Monitor(listener, spooler);
        statusServer = new StatusServer(80, statusProvider);
    }

    public void run() {
        final Thread serverThread = new Thread(statusServer::start);
        serverThread.run();
        updater.pushStatus(BallotStatus.WAITING);
        monitor.run();
    }

    public static void main (String[] args) {
        final Controller controller = new Controller();
        controller.run();
    }
}
