package edu.rice.starvote.ballotbox.swingui;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Plays WAV and AU audio files. This class will attempt to select an external USB sound device for playback from the
 * RPi, falling back to JVM default if none is found. Sound files to play must be located in the resource path.
 *
 * #### Example
 * ~~~java
 * VoicePlayer player = new VoicePlayer();
 * player.play("ding.wav");
 * player.waitUntilFinished();
 * ~~~
 *
 * @author luejerry
 */
public class VoicePlayer {

    private final Semaphore lock = new Semaphore(1);

    private static Mixer mixer = AudioSystem.getMixer(null); // Default audio device

    /* JVM does not default to the correct USB audio device on the Pi; try to get the right one. */
    static {
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            if (mixerInfo.getName().contains("[plughw:1,0]")) {
                mixer = AudioSystem.getMixer(mixerInfo);
                break;
            }
        }
    }

    /**
     * Plays the specified WAV audio file. This method returns immediately after playback begins. If this player is
     * currently playing another file, playback of the new file will not begin until the previous file is finished
     * playing.
     *
     * If the caller desires to block until playback is complete, use `waitUntilFinished()` after invoking this method.
     *
     * @param path Path to audio file. Must be on the resource path.
     * @throws IOException                   If an I/O error occurs, or file could not be found.
     * @throws UnsupportedAudioFileException If the audio file is in an unsupported format.
     */
    public void play(String path) throws IOException, UnsupportedAudioFileException {
        final InputStream fileStream = getClass().getClassLoader().getResourceAsStream(path);
        if (fileStream == null) throw new FileNotFoundException("File " + path + " could not be found.");
        final BufferedInputStream bufferedStream = new BufferedInputStream(fileStream);
        final AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);
        System.out.println("Playing sound with audio device: " + mixer.getMixerInfo().getName());
        playStream(audioStream);
    }

    /**
     * Queues a given AudioInputStream for playback. Ensures that only one stream is played at a time.
     * @param audioStream Audio stream to play.
     * @throws IOException If I/O exception occurs when playing the stream.
     */
    private void playStream(AudioInputStream audioStream) throws IOException {
        final DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
        try {
            final Clip clip = (Clip) mixer.getLine(info);
            clip.addLineListener((e) -> {
                if (e.getType().equals(LineEvent.Type.STOP)) {
                    lock.release();
                    System.out.println("stopped " + lock.availablePermits());
                    clip.close();
                } else if (e.getType().equals(LineEvent.Type.CLOSE)) {
                    try {
                        audioStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            assert lock.availablePermits() == 1 || lock.availablePermits() == 0 : "Synchronization error; multiple semaphores generated";
            lock.acquire();
            clip.open(audioStream);
            clip.start();
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method blocks until the current audio file is finished playing. If no audio is playing, returns
     * immediately.
     */
    public void waitUntilFinished() {
        try {
            lock.acquire();
            lock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
