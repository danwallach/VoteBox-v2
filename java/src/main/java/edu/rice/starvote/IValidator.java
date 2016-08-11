package edu.rice.starvote;

/**
 * Interface for code validator. Success of validation determines whether a ballot is accepted or rejected.
 *
 * @author luejerry
 */
public interface IValidator {

    /**
     * Check a code string for validity.
     *
     * @param code Scanned code string.
     * @return True if code is valid, false otherwise.
     */
    boolean validate(String code);
}
