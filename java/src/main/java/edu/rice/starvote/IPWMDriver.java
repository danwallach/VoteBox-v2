package edu.rice.starvote;

import java.io.IOException;

/**
 * Interface for a pulse width modulation (PWM) controller.
 *
 * @author luejerry
 */
public interface IPWMDriver {

    /**
     * Set the PWM base frequency.
     * @param frequency Frequency in hertz.
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    void setFrequency(double frequency) throws IOException;

    /**
     * Set the PWM duty cycle.
     * @param dutyCycle Value in range [0.0, 100.0], where 0 is full-low and 100 is full-high.
     * @throws IOException If an error occurs when communicating with the device driver.
     */
    void setDutyCycle(double dutyCycle) throws IOException;

}
