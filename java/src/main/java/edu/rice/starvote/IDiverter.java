package edu.rice.starvote;

/**
 * Interface for a controller module that diverts a ballot to one of two paths. On reference hardware, diverting up
 * rejects the ballot and diverting down accepts it.
 *
 * @author luejerry
 */
public interface IDiverter {

    /**
     * Move diverter to the up position.
     */
    void up();

    /**
     * Move diverter to the down position.
     */
    void down();
}
