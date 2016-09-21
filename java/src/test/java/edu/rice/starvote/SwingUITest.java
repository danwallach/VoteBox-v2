package edu.rice.starvote;

import edu.rice.starvote.ballotbox.BallotStatus;
import edu.rice.starvote.ballotbox.SwingController;
import edu.rice.starvote.ballotbox.swingui.DisplayController;
import edu.rice.starvote.ballotbox.swingui.SwingDisplay;
import org.junit.Test;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by cyricc on 9/20/2016.
 */
public class SwingUITest {
    @Test
    public void test() throws Exception {
        final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        final DisplayController display = new DisplayController(new SwingDisplay());
        display.start();
        timer.scheduleAtFixedRate(() -> display.pushStatus(BallotStatus.REJECT), 2000, 4000, TimeUnit.MILLISECONDS);
        timer.scheduleAtFixedRate(() -> display.pushStatus(BallotStatus.ACCEPT), 4000, 4000, TimeUnit.MILLISECONDS);
        Thread.sleep(10000);
    }
}
