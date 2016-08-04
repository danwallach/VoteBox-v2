package edu.rice.starvote;

import com.pi4j.io.gpio.PinEdge;

/**
 * Created by luej on 7/21/16.
 */
public class Monitor {

    private final GPIOListener listener;
    private final ISpooler spooler;

    public Monitor(GPIOListener listener, ISpooler spooler) {
        this.listener = listener;
        this.spooler = spooler;
    }

    public void run() {
        while (true) {
            listener.waitForEvent(PinEdge.FALLING, () -> {
                System.out.println("Paper detected");
                if (spooler.getStatus() != DeviceStatus.READY) {
                    System.out.println("Device " + spooler.getStatus());
                } else {
                    while (listener.getState().isLow()) {
                        System.out.println("Spooling in page");
                        spooler.takeIn();
                    }
                    System.out.println("All pages spooled");
                }
            });
        }
    }
}
