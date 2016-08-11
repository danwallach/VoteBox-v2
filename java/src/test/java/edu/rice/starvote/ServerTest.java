package edu.rice.starvote;

import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by luej on 8/3/16.
 */
public class ServerTest {
//    @Test
    public static void test() throws Exception {
        StatusContainer provider = new StatusContainer();
        StatusServer server = new StatusServer(4444, provider);
        provider.writeStatus(BallotStatus.ACCEPT);
        new Thread(() -> server.start()).start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                provider.writeStatus(BallotStatus.REJECT);
            }
        }, 2000, 4000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                provider.writeStatus(BallotStatus.ACCEPT);
            }
        }, 4000, 4000);
    }

    public static void main (String[] args) throws Exception {
        test();
    }
}
