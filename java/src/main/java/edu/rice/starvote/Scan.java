package edu.rice.starvote;

import java.io.File;
import java.nio.file.Path;

/**
 * Implementation of scanner controller that invokes an external C scan program.
 *
 * @author luejerry
 */
public class Scan implements IScanner {

    private final static Path scanPath = JarResource.getResource("scan");

    public String scan(int timeout) {
        return ExternalProcess.runInDirAndCapture(scanPath.getParent().toFile(), "./scan", String.valueOf(timeout));
    }
}
