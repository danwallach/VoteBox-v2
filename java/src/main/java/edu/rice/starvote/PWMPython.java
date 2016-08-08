package edu.rice.starvote;

import java.io.File;

/**
 * Implementation of PWM controller via external Python scripts, which use the RPi.GPIO library. This solution is
 * not ideal and pi-blaster should be used instead if possible.
 *
 * @see PWMBlaster
 * @author luejerry
 */
public class PWMPython implements IPWMDriver {

    private int pin;
    private double frequency;
    private double dutyCycle;
    private static String pyPath = PWMPython.class.getClassLoader().getResource("pwm.py").getPath();

    public PWMPython(int pin, double frequency, double dutyCycle) {
        this.pin = pin;
        this.frequency = frequency;
        this.dutyCycle = dutyCycle;
//        ExternalProcess.runInDir(new File(pyPath).getParentFile(),
//                "python", "-c", "import pwm; " +
//                        "pwm.start(" + pin + ", " + frequency + ", " + dutyCycle + ")");
//        System.out.println(ExternalProcess.runInDirAndCapture(new File(pyPath).getParentFile(),
//                "python", "-c", "import pwm; " +
//                        "pwm.start(" + pin + ", " + frequency + ", " + dutyCycle + ")"));
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
        ExternalProcess.runInDir(new File(pyPath).getParentFile(),
                "python", "-c", "import pwm; " +
                        "pwm.start(" + pin + ", " + frequency + "," + dutyCycle + ")");
//        System.out.println(ExternalProcess.runInDirAndCapture(new File(pyPath).getParentFile(),
//                "python", "-c", "import pwm; " +
//                        "pwm.set_freq(" + pin + ", " + frequency + ")"));
    }

    public void setDutyCycle(double dutyCycle) {
        this.dutyCycle = dutyCycle;
        ExternalProcess.runInDir(new File(pyPath).getParentFile(),
                "python", "-c", "import pwm; " +
                        "pwm.start(" + pin + ", " + frequency + ", " + dutyCycle + ")");
//        System.out.println(ExternalProcess.runInDirAndCapture(new File(pyPath).getParentFile(),
//                "python", "-c", "import pwm; " +
//                        "pwm.set_dc(" + pin + ", " + dutyCycle + ")"));
    }
}
