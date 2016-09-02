package edu.rice.starvote.drivers;

import edu.rice.starvote.util.ExternalProcess;
import edu.rice.starvote.util.JarResource;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Implementation of scanner controller that invokes an external C scan program.
 *
 * @author luejerry
 */
public class Scan implements IScanner {

    private final static Path scanPath = JarResource.getResource("scan");

    public String scan(int timeout) throws IOException {
        return ExternalProcess.runInDirAndCapture(scanPath.getParent().toFile(), "./scan", String.valueOf(timeout));
    }
}
