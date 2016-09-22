package edu.rice.starvote.ballotbox.drivers;

import java.io.IOException;

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
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    String scan(int timeout) throws IOException;
}
