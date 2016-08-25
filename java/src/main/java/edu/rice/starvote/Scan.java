package edu.rice.starvote;

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
