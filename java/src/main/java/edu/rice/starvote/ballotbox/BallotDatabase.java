package edu.rice.starvote.ballotbox;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cyricc on 10/10/2016.
 */
public class BallotDatabase {
    private Map<String, BallotProgress> ballots = new ConcurrentHashMap<>();

    public Optional<BallotProgress> scanPage(String code) {
        if (!ballots.containsKey(code)) {
            return Optional.empty();
        }
        final BallotProgress oldProgress = ballots.get(code);
        final BallotProgress newProgress = new BallotProgress(code, oldProgress.pagesScanned + 1, oldProgress.pagesTotal);
        if (newProgress.completed()) {
            ballots.remove(code);
        }
        return Optional.of(newProgress);
    }

    public void addBallot(String code, int pages) {
        ballots.put(code, new BallotProgress(code, 0, pages));
    }

    public void removeBallot(String code) {
        ballots.remove(code);
    }
}
