package edu.rice.starvote.ballotbox;

import com.pi4j.io.gpio.PinEdge;
import edu.rice.starvote.ballotbox.util.GPIOListener;

import java.util.concurrent.TimeUnit;

/**
 * Main program loop that detects when paper has been inserted into the feed tray and starts the paper feeder.
 *
 * @author luejerry
 */
public class Monitor {

    private final GPIOListener listener;
    private final ISpooler spooler;

    /**
     * Constructor.
     * @param listener GPIO listener for the paper feed sensor. The pin should read low when paper is detected.
     * @param spooler Instantiated paper feeder.
     */
    public Monitor(GPIOListener listener, ISpooler spooler) {
        this.listener = listener;
        this.spooler = spooler;
    }

    /**
     * Detect when paper is placed into the tray and run the paper feeder. All available pages are fed and processed.
     * **This method never returns.**
     */
    public void run() {
        while (true) {
            // Blocks until a GPIO interrupt is triggered by the paper sensor.
            listener.waitForEvent(PinEdge.RISING);
            System.out.print("Paper detected... ");
            if (spooler.getStatus() != DeviceStatus.READY) {
                System.out.println("Device " + spooler.getStatus());
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                    if (listener.getState().isLow()) {
                        System.out.println("false positive");
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Process all available pages.
                while (listener.getState().isHigh()) {
                    System.out.println("Spooling in page");
                    spooler.takeIn();
                }
                System.out.println("All pages spooled");
            }
        }
    }
}
