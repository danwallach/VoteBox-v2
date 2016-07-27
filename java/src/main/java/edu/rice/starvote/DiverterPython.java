package edu.rice.starvote;

import com.sun.tools.doclets.internal.toolkit.util.Extern;

import java.io.File;

/**
 * Created by luej on 7/26/16.
 */
public class DiverterPython implements IDiverter {

    private static String pyPath = DiverterPython.class.getClassLoader().getResource("pwm.py").getPath();
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
