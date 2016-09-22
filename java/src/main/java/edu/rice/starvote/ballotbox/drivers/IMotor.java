package edu.rice.starvote.ballotbox.drivers;

import java.io.IOException;

/**
 * Interface for a bidirectional variable speed motor controller. This controls the main printer motor on reference
 * hardware.
 *
 * @author luejerry
 */
public interface IMotor {

    /**
     * Stop the motor.
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    void stop() throws IOException;

    /**
     * Move motor forward at full speed.
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    void forward() throws IOException;

    /**
     * Move motor forward at slow speed. The exact speed is undefined but should be less than half full. For
     * precise speed control, use the method `forward(double speed)`.
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    void forwardSlow() throws IOException;

    /**
     * Move motor forward at a specified speed.
     * @param speed Value in range [0.0, 100.0].
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    void forward(double speed) throws IOException;

    /**
     * Move motor reverse at full speed.
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    void reverse() throws IOException;

    /**
     * Move motor reverse at slow speed. The exact speed is undefined but should be less than half full. For
     * Precise speed control, use the method `reverse(double speed)`.
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    void reverseSlow() throws IOException;

    /**
     * Move motor reverse at a specified speed.
     * @param speed Value in range [0.0, 100.0].
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    void reverse(double speed) throws IOException;

}
