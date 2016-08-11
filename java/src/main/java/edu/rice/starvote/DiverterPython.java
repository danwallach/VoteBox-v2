package edu.rice.starvote;

import java.io.File;

/**
 * Diverter servo control using an external Python script using the RPi.GPIO library. This is not an ideal solution
 * but works for our purposes. The native Pi4J library supports only pure software PWM and is unsuitable for driving
 * servos.
 *
 * @author luejerry
 */
public class DiverterPython implements IDiverter {

    /**
     * Path of the Python diverter script.
     */
    private static String pyPath = DiverterPython.class.getClassLoader().getResource("pwm.py").getPath();

    /**
     * Working directory containing the script.
     */
    private static File wd = new File(pyPath).getParentFile();

    @Override
    public void up() {
        ExternalProcess.runInDir(wd, "python", "-c", "import diverter; diverter.up()");
    }

    @Override
    public void down() {
        ExternalProcess.runInDir(wd, "python", "-c", "import diverter; diverter.down()");
    }
}
