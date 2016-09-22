package edu.rice.starvote.ballotbox.drivers;

import edu.rice.starvote.ballotbox.util.ExternalProcess;
import edu.rice.starvote.ballotbox.util.JarResource;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Diverter servo control using an external Python script using the RPi.GPIO library. *This solution is not ideal and
 * has been included for compatibility reasons. pi-blaster should be used instead if possible.*
 *
 * @see DiverterPWM
 * @see PWMBlaster
 * @author luejerry
 */
@Deprecated
public class DiverterPython implements IDiverter {

    private static Path diverterPath = JarResource.getResource("diverter.py");

    static {
        JarResource.getResource("diverter_config.py");
        JarResource.getResource("rpi_servodriver.py");
    }

    @Override
    public void up() throws IOException {
        ExternalProcess.runInDir(diverterPath.getParent().toFile(), "python", "-c", "import diverter; diverter.up()");
    }

    @Override
    public void down() throws IOException {
        ExternalProcess.runInDir(diverterPath.getParent().toFile(), "python", "-c", "import diverter; diverter.down()");
    }
}
