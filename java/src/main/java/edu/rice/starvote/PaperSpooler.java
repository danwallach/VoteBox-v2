package edu.rice.starvote;

import com.pi4j.io.gpio.PinEdge;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by luej on 7/26/16.
 */
public class PaperSpooler implements ISpooler {

    final static int SCANTIME = 4;

    private DeviceStatus status = DeviceStatus.READY;
    private final IStatusUpdate statusUpdater;
    private final IDiverter diverter;
    private final IMotor motor;
    private final GPIOListener halfwaySensor;
    private final IScanner scanner;
    private final IValidator validator;

    public PaperSpooler(IStatusUpdate statusUpdater, IDiverter diverter, IMotor motor, GPIOListener halfwaySensor, IScanner scanner,
                        IValidator validator) {
        this.statusUpdater = statusUpdater;
        this.diverter = diverter;
        this.motor = motor;
        this.halfwaySensor = halfwaySensor;
        this.scanner = scanner;
        this.validator = validator;
    }

    @Override
    public DeviceStatus getStatus() {
        return status;
    }

    @Override
    public synchronized void takeIn() {
        final CountDownLatch signalDone = new CountDownLatch(1);

        statusUpdater.pushStatus(BallotStatus.SPOOLING);
        status = DeviceStatus.BUSY;
        diverter.up();

        motor.forward();

        // Register the event listener
        if (!halfwaySensor.waitForEvent(PinEdge.FALLING, () -> {
            System.out.println("Paper taken in, beginning scan");
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.err.println(e.toString());
            }
            motor.reverseSlow();
            String code = scanner.scan(SCANTIME);
            motor.stop();
            // TODO: add delays for accept/reject messages
            if (code.isEmpty()) {
                System.out.println("Ballot not scanned");
                statusUpdater.pushStatus(BallotStatus.REJECT);
                diverter.up();
            } else {
                if (validator.validate(code)) {
                    System.out.println("Ballot code valid");
                    statusUpdater.pushStatus(BallotStatus.ACCEPT);
                    diverter.down();
                } else {
                    System.out.println("Ballot code invalid");
                    statusUpdater.pushStatus(BallotStatus.REJECT);
                    diverter.up();
                }
            }
            try {
                // Time to allow diverter to fully actuate
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e.toString());
            }
            motor.reverse();
            if (!halfwaySensor.waitForEvent(PinEdge.RISING, () -> {
                System.out.println("Spooler cleared");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException ignored) {}
                motor.stop();
                status = DeviceStatus.READY;
                statusUpdater.pushStatus(BallotStatus.WAITING);
                signalDone.countDown();
            }, 2000)) {
                if (halfwaySensor.getState().isLow()) {
                    // Paper still in spooler, abort with error
                    System.out.println("Spooler jammed");
//                motor.reverse();
//                try {
//                    TimeUnit.SECONDS.sleep(2);
//                } catch (InterruptedException e) {
//                    System.err.println(e.toString());
//                }
                    motor.stop();
                    status = DeviceStatus.ERROR;
                    statusUpdater.pushStatus(BallotStatus.OFFLINE);
                } else {
                    // Spooler is clear, continue
                    System.out.println("Spooler checked clear");
                    motor.stop();
                    status = DeviceStatus.READY;
                    statusUpdater.pushStatus(BallotStatus.WAITING);
                }
                signalDone.countDown();
            }
        }, 2000)) {
            // Tray was empty
            System.out.println("Paper tray empty");

            // Reset the feeder
            motor.reverse();
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                System.err.println(e.toString());
            }
            motor.stop();
            
            status = DeviceStatus.READY;
            statusUpdater.pushStatus(BallotStatus.WAITING);
            signalDone.countDown();
        }
        try {
            final boolean completed = signalDone.await(15000, TimeUnit.MILLISECONDS);
            System.out.println(completed ? "Paper spool complete" : "Paper spool timed out");
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        }
    }

    @Override
    public void eject() {

    }
}
