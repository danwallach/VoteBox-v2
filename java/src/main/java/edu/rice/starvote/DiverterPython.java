package edu.rice.starvote;

import java.io.File;
import java.nio.file.Path;

/**
 * Diverter servo control using an external Python script using the RPi.GPIO library. This is not an ideal solution
 * but works for our purposes. The native Pi4J library supports only pure software PWM and is unsuitable for driving
 * servos.
 *
 * @author luejerry
 */
public class DiverterPython implements IDiverter {

    private static Path diverterPath = JarResource.getResource("diverter.py");

    static {
        JarResource.getResource("diverter_config.py");
        JarResource.getResource("rpi_servodriver.py");
    }

    @Override
    public void up() {
        ExternalProcess.runInDir(diverterPath.getParent().toFile(), "python", "-c", "import diverter; diverter.up()");
    }

    @Override
    public void down() {
        ExternalProcess.runInDir(diverterPath.getParent().toFile(), "python", "-c", "import diverter; diverter.down()");
    }
}
