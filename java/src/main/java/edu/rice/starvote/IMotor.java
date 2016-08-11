package edu.rice.starvote;

/**
 * Interface for a bidirectional variable speed motor controller. This controls the main printer motor on reference
 * hardware.
 *
 * @author luejerry
 */
public interface IMotor {

    /**
     * Stop the motor.
     */
    void stop();

    /**
     * Move motor forward at full speed.
     */
    void forward();

    /**
     * Move motor forward at slow speed. The exact speed is undefined but should be less than half full. For
     * precise speed control, use the method `forward(double speed)`.
     */
    void forwardSlow();

    /**
     * Move motor forward at a specified speed.
     * @param speed Value in range [0.0, 100.0].
     */
    void forward(double speed);

    /**
     * Move motor reverse at full speed.
     */
    void reverse();

    /**
     * Move motor reverse at slow speed. The exact speed is undefined but should be less than half full. For
     * Precise speed control, use the method `reverse(double speed)`.
     */
    void reverseSlow();

    /**
     * Move motor reverse at a specified speed.
     * @param speed Value in range [0.0, 100.0].
     */
    void reverse(double speed);

}
