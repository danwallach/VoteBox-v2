package edu.rice.starvote;

/**
 * Interface for a code scanner controller.
 *
 * @author luejerry
 */
public interface IScanner {

    /**
     * Attempt to scan a code.
     *
     * @param timeout Timeout in seconds.
     * @return Scanned code, or empty string if timeout elapsed.
     */
    String scan(int timeout);
}
