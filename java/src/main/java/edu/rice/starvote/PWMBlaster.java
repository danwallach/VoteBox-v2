package edu.rice.starvote;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implementation of PWM controller using [pi-blaster](https://github.com/sarfata/pi-blaster). Pi-blaster uses a more
 * efficient hardware-based PWM timing mechanism than the Python RPi.GPIO library. *Note that PWM frequency cannot be
 * changed in this implementation due to a limitation with pi-blaster.*
 *
 * @author luejerry
 */
public class PWMBlaster implements IPWMDriver {

    private static Path pwmpath = Paths.get("/dev/pi-blaster");

    private int pin;

    /**
     * Helper method to send commands to pi-blaster.
     * @param data Command string to write to pi-blaster.
     * @throws IOException If an I/O error occurs writing to the driver.
     */
    private static void writePWM(String data) throws IOException {
        Files.write(pwmpath, (data + "\n").getBytes(Charset.forName("UTF-8")));
    }

    /**
     * Constructor. Does not start PWM (use `setDutyCycle()` to start).
     * @param pin PWM output pin (BCM numbering).
     * @param frequency Ignored.
     */
    public PWMBlaster(int pin, double frequency) {
        this.pin = pin;
    }

    /**
     * **pi-blaster does not support changing the frequency.** Invoking this method only prints a warning message.
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
    public void setDutyCycle(double dutyCycle) throws IOException {
        writePWM(pin + "=" + dutyCycle / 100.0);
    }
}
