package edu.rice.starvote;

import java.io.IOException;

/**
 * Interface for a controller module that diverts a ballot to one of two paths. On reference hardware, diverting up
 * rejects the ballot and diverting down accepts it.
 *
 * @author luejerry
 */
public interface IDiverter {

    /**
     * Move diverter to the up position.
     * @throws IOException If an error occurs when communicating to the device driver.
     */
    void up() throws IOException;

    /**
     * Move diverter to the down position.
     * @throws IOException If an error occurs when communicating to the device driver.
     */
    void down() throws IOException;
}
