package edu.rice.starvote;

import edu.rice.starvote.ballotbox.drivers.DiverterPython;
import edu.rice.starvote.ballotbox.drivers.IDiverter;
import org.junit.Test;

/**
 * Created by luej on 8/2/16.
 */
public class DiverterTest {
    @Test
    public void test() throws Exception {
        IDiverter diverter = new DiverterPython();
        diverter.up();
        Thread.sleep(1000);
        diverter.down();
        Thread.sleep(1000);
        diverter.up();
        Thread.sleep(1000);
    }
}
