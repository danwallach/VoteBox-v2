package edu.rice.starvote;

import java.io.File;

/**
 * Created by luej on 7/20/16.
 */
public class PWMPython implements IPWMDriver {

    private int pin;
    private static String pyPath = PWMPython.class.getClassLoader().getResource("pwm.py").getPath();

    public static IPWMDriver init(int pin, double frequency, double dutyCycle) {
        return new PWMPython(pin, frequency, dutyCycle);
    }

    public PWMPython(int pin, double frequency, double dutyCycle) {
        this.pin = pin;
        ExternalProcess.runInDir(new File(pyPath).getParentFile(),
                "python", "-c", "import pwm; " +
                        "pwm.start(" + pin + ", " + frequency + ", " + dutyCycle + ")");
    }

    public void setFrequency(double frequency) {
        ExternalProcess.runInDir(new File(pyPath).getParentFile(),
                "python", "-c", "import pwm; " +
                        "pwm.set_freq(" + pin + ", " + frequency + ")");
    }

    public void setDutyCycle(double dutyCycle) {
        ExternalProcess.runInDir(new File(pyPath).getParentFile(),
                "python", "-c", "import pwm; " +
                        "pwm.set_dc(" + pin + ", " + dutyCycle + ")");
    }
}
