package edu.rice.starvote;

/**
 * Created by luej on 7/27/16.
 */
public class Controller {

    private final PaperListener listener;
    private final PaperListener halfwaySensor;
    private final ISpooler spooler;
    private final IScanner scanner;
    private final IMotor motor;
    private final IDiverter diverter;
    private final IStatusUpdate updater;
    private final IValidator validator;
    private final Monitor monitor;

    public Controller() {
        listener = new PaperListener(24);
        halfwaySensor = new PaperListener(23);
        motor = new PrinterMotor(22, 27, new PWMBlaster(17, 50, 0));
        diverter = new DiverterPython();
        scanner = new Scan();
        validator = code -> true;
        updater = status -> System.out.println("Ballot status sent to server: " + status.toString());
        spooler = new PaperSpooler(updater, diverter, motor, halfwaySensor, scanner, validator);
        monitor = new Monitor(listener, spooler);
    }

    public void run() {
        monitor.run();
    }

    public static void main (String[] args) {
        final Controller controller = new Controller();
        controller.run();
    }
}
