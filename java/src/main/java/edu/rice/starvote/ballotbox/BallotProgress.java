package edu.rice.starvote.ballotbox;

/**
 * Created by cyricc on 10/10/2016.
 */
public class BallotProgress {
    public final String code;
    public final int pagesScanned;
    public final int pagesTotal;

    public BallotProgress(String code, int pagesScanned, int pagesTotal) {
        this.code = code;
        this.pagesScanned = pagesScanned;
        this.pagesTotal = pagesTotal;
    }

    public boolean completed() {
        return pagesScanned >= pagesTotal;
    }
}
