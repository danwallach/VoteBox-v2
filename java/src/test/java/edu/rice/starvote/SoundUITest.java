package edu.rice.starvote;

import edu.rice.starvote.ballotbox.BallotStatus;
import edu.rice.starvote.ballotbox.IStatusUpdate;
import edu.rice.starvote.ballotbox.swingui.DisplayController;
import edu.rice.starvote.ballotbox.swingui.SwingDisplay;
import edu.rice.starvote.ballotbox.swingui.VoiceController;
import org.junit.Test;

/**
 * Created by cyricc on 9/21/2016.
 */
public class SoundUITest {
    @Test
    public void test() throws Exception {
        final DisplayController displayController = new DisplayController(new SwingDisplay());
        final IStatusUpdate voiceController = new VoiceController();
        final IStatusUpdate updater = status -> {
            displayController.pushStatus(status);
            voiceController.pushStatus(status);
        };
        displayController.start();
        updater.pushStatus(BallotStatus.ACCEPT);
        updater.pushStatus(BallotStatus.REJECT);
        updater.pushStatus(BallotStatus.ACCEPT);

    }
}
