package edu.rice.starvote;

import edu.rice.starvote.ballotbox.swingui.SwingDisplay;
import org.junit.Test;

import java.awt.*;

/**
 * Created by cyricc on 9/20/2016.
 */
public class SwingUITest {
    @Test
    public void test() throws Exception {
        SwingDisplay display = new SwingDisplay();
        display.start();
        Thread.sleep(10000);
        display.dispose();
    }
}
