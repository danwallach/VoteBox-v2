package edu.rice.starvote.ballotbox;

import java.util.Map;
import java.util.Optional;

/**
 * Created by cyricc on 10/10/2016.
 */
public class PageValidator implements IValidator {

    private BallotDatabase ballotDatabase;

    public PageValidator(BallotDatabase ballotDatabase) {
        this.ballotDatabase = ballotDatabase;
    }

    @Override
    public boolean validate(String code) {
        final Optional<BallotProgress> oResult = ballotDatabase.scanPage(code);
        return oResult.isPresent();
    }

    public Optional<BallotProgress> validatePage(String code) {
        return ballotDatabase.scanPage(code);
    }
}
