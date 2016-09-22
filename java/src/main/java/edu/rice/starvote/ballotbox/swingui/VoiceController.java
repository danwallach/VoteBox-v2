package edu.rice.starvote.ballotbox.swingui;

import edu.rice.starvote.ballotbox.BallotStatus;
import edu.rice.starvote.ballotbox.IStatusUpdate;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Created by cyricc on 9/21/2016.
 */
public class VoiceController implements IStatusUpdate {

    private final VoicePlayer player = new VoicePlayer();

    @Override
    public void pushStatus(BallotStatus status) {
        try {
            switch (status) {
                case ACCEPT:
                    player.play("success.wav");
                    break;
                case REJECT:
                    player.play("reject.wav");
                    break;
            }
            player.waitUntilFinished();
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

    }
}
