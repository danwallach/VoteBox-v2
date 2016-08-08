package edu.rice.starvote;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

/**
 * Utility class to supply a singleton Pi4J GpioController. All classes wishing to control GPIO via Pi4J should use
 * this class instead of creating a GpioController instance.
 *
 * @author luejerry
 */
public class GPIOManager {

    private static GpioController gpio;

    /**
     * Get the master GpioController instance, creating it if it has not been made.
     * @return Singleton Pi4J GpioController instance.
     */
    public static GpioController controller() {
        if (gpio == null) {
            gpio = GpioFactory.getInstance();
        }
        return gpio;
    }

    /**
     * Shut down the master GpioController instance. **Subsequent calls using the old handle are invalidated, even if
     * the controller is started again.**
     */
    public static void stop() {
        if (gpio != null) {
            gpio.shutdown();
            gpio = null;
        }
    }


}
