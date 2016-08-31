package edu.rice.starvote.util;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.wiringpi.GpioUtil;

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
            deprivilege();
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

    private static void deprivilege() {
        try {
            GpioUtil.enableNonPrivilegedAccess();
        } catch (RuntimeException e) {
            System.err.println("Pi4J failed to start in non-privileged mode: " + e.getMessage());
            System.err.println("This application must be started with superuser privileges.");
        }
    }
}
