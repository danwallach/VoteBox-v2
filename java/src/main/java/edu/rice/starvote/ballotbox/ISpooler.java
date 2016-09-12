package edu.rice.starvote.ballotbox;

/**
 * Interface for the ballot box paper feeder controller. The entire ballot feeding and processing sequence is
 * controlled by this module.
 *
 * @author luejerry
 */
public interface ISpooler {

    /**
     * Get the status of the paper feeder. Any status other than READY indicates that the device should not be used.
     * A status of READY is returned if and only if the device is able to operate immediately.
     * @return Status of paper feeder device.
     */
    DeviceStatus getStatus();

    /**
     * Take in, scan, and process a single page from the paper feed tray. This method adheres to the following
     * contract:
     *
     * ##### Preconditions #####
     *  - All necessary hardware and GPIO pins are initialized and ready to take commands
     *  - A validator object has been initialized
     *  - A ballot status receiver has been initialized
     *  - The device status is READY
     *
     * ##### Effects #####
     *  - Exactly one page is scanned. If no pages are available, do nothing and return
     *  - The ballot status is updated and sent to the status updater as appropriate
     *  - The scanned code is sent to `IValidator`. A valid code is accepted into the machine. An invalid code is
     *    ejected
     *  - The device status is always BUSY until this method returns
     *
     * ##### Postconditions #####
     *  - Device status is READY, or ERROR if error occurred
     *  - The last ballot status sent is WAITING, or OFFLINE if error occurred
     */
    void takeIn();

    /**
     * Eject the first page from the paper tray through the rejection pathway without scanning it.
     */
    void eject();
}
