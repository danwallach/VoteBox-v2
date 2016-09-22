package edu.rice.starvote;

import edu.rice.starvote.ballotbox.swingui.VoicePlayer;
import org.junit.Test;

/**
 * Created by cyricc on 9/21/2016.
 */
public class SoundTest {
    @Test
    public void test() throws Exception {
        VoicePlayer player = new VoicePlayer();
        player.play("reject.wav");
        player.waitUntilFinished();
        player.play("success.wav");
        player.waitUntilFinished();

    }
}
