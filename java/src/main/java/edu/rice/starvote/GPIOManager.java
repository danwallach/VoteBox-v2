package edu.rice.starvote;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

/**
 * Created by luej on 7/21/16.
 */
public class GPIOManager {

    private static GpioController gpio;

    public static GpioController controller() {
        if (gpio == null) {
            gpio = GpioFactory.getInstance();
        }
        return gpio;
    }

    public static void stop() {
        if (gpio != null) {
            gpio.shutdown();
            gpio = null;
        }
    }


}
