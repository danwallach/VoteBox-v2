package edu.rice.starvote;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by luej on 7/20/16.
 */
public class PWMBlaster implements IPWMDriver {

    private static Path pwmpath = Paths.get("/dev/pi-blaster");

    private int pin;

    private static void writePWM(String data) {
        try {
            Files.write(pwmpath, data.getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public static IPWMDriver init(int pin, double frequency, double dutyCycle) {
        return new PWMBlaster(pin, frequency, dutyCycle);
    }

    public PWMBlaster(int pin, double frequency, double dutyCycle) {
        this.pin = pin;
        writePWM(pin + "=" + dutyCycle / 100.0);
    }

    @Override
    public void setFrequency(double frequency) {
        System.out.println("Note: changing PWM frequency is not supported by PiBlaster");
    }

    @Override
    public void setDutyCycle(double dutyCycle) {
        writePWM(pin + "=" + dutyCycle / 100.0);
    }
}
