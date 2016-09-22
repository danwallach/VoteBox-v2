package edu.rice.starvote.ballotbox.drivers;

import edu.rice.starvote.ballotbox.util.ExternalProcess;
import edu.rice.starvote.ballotbox.util.JarResource;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Implementation of PWM controller via external Python scripts, which use the RPi.GPIO library. *This solution is
 * not ideal and has been included for compatibility reasons. pi-blaster should be used instead if possible.*
 *
 * @see PWMBlaster
 * @author luejerry
 */
@Deprecated
public class PWMPython implements IPWMDriver {

    private int pin;
    private double frequency;
    private double dutyCycle;
    private static Path pyFile = JarResource.getResource("pwm.py");

    public PWMPython(int pin, double frequency, double dutyCycle) {
        this.pin = pin;
        this.frequency = frequency;
        this.dutyCycle = dutyCycle;
    }

    public void setFrequency(double frequency) throws IOException {
        this.frequency = frequency;
        ExternalProcess.runInDir(pyFile.getParent().toFile(),
                "python", "-c", "import pwm; " +
                        "pwm.start(" + pin + ", " + frequency + "," + dutyCycle + ")");
    }

    public void setDutyCycle(double dutyCycle) throws IOException {
        this.dutyCycle = dutyCycle;
        ExternalProcess.runInDir(pyFile.getParent().toFile(),
                "python", "-c", "import pwm; " +
                        "pwm.start(" + pin + ", " + frequency + ", " + dutyCycle + ")");
    }
}
