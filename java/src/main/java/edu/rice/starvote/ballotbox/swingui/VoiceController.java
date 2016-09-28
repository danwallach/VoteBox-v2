package edu.rice.starvote.ballotbox.swingui;

import edu.rice.starvote.ballotbox.BallotStatus;
import edu.rice.starvote.ballotbox.IStatusUpdate;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Plays audible feedback messages for applicable conditions of the ballot box. Currently plays a "ding" sound
 * when a valid ballot is accepted, and a spoken error message when a ballot is invalid or fails to scan.
 */
public class VoiceController implements IStatusUpdate {

    private final VoicePlayer player = new VoicePlayer();

    /**
     * Plays an audible feedback message associated with the `BallotStatus` (if one is available).
     * @param status Ballot box status.
     */
    @Override
    public void pushStatus(BallotStatus status) {
        try {
            switch (status) {
                case ACCEPT:
                    player.play("success.wav");
                    break;
                case REJECT:
                    player.play("reject.wav");
                    player.waitUntilFinished();
                    break;
            }
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

    }
}
