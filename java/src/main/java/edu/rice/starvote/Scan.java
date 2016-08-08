package edu.rice.starvote;

import java.io.File;

/**
 * Implementation of scanner controller that invokes an external C scan program.
 *
 * @author luejerry
 */
public class Scan implements IScanner {

    final static File wd = new File(Scan.class.getClassLoader().getResource("scan").getPath()).getParentFile();

    public String scan(int timeout) {
        return ExternalProcess.runInDirAndCapture(wd, "./scan", String.valueOf(timeout));
    }
}
