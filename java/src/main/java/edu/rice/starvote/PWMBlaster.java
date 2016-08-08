package edu.rice.starvote;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implementation of PWM controller using [pi-blaster](https://github.com/sarfata/pi-blaster). Note that PWM frequency
 * cannot be changed in this implementation due to a limitation with pi-blaster.
 *
 * @author luejerry
 */
public class PWMBlaster implements IPWMDriver {

    private static Path pwmpath = Paths.get("/dev/pi-blaster");

    private int pin;

    /**
     * Helper method to send commands to pi-blaster.
     * @param data Command string to write to pi-blaster.
     */
    private static void writePWM(String data) {
        try {
            Files.write(pwmpath, (data + "\n").getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Constructor. Immediately starts PWM on the specified pin at the specified duty cycle.
     * @param pin PWM output pin (BCM numbering).
     * @param frequency Ignored.
     * @param dutyCycle PWM duty cycle in range [0.0, 100.0].
     */
    public PWMBlaster(int pin, double frequency, double dutyCycle) {
        this.pin = pin;
        writePWM(pin + "=" + dutyCycle / 100.0);
    }

    /**
     * pi-blaster does not support changing the frequency. Invoking this method only prints a warning message.
     * @param frequency Ignored.
     */
    @Override
    public void setFrequency(double frequency) {
        System.out.println("Note: changing PWM frequency is not supported by PiBlaster");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDutyCycle(double dutyCycle) {
        writePWM(pin + "=" + dutyCycle / 100.0);
    }
}
