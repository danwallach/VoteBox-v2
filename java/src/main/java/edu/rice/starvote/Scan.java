package edu.rice.starvote;

import java.io.File;

/**
 * Created by luej on 7/26/16.
 */
public class Scan implements IScanner {

    final static File wd = new File(Scan.class.getClassLoader().getResource("scan").getPath()).getParentFile();

    public String scan(int timeout) {
        return ExternalProcess.runInDirAndCapture(wd, "./scan", String.valueOf(timeout));
    }
}
