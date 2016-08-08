package edu.rice.starvote;

/**
 * Interface for a pulse width modulation (PWM) controller.
 *
 * @author luejerry
 */
public interface IPWMDriver {

//    void init(int pin, double frequency, double dutyCycle);

    /**
     * Set the PWM base frequency.
     * @param frequency Frequency in hertz.
     */
    void setFrequency(double frequency);

    /**
     * Set the PWM duty cycle.
     * @param dutyCycle Value in range [0.0, 100.0], where 0 is full-low and 100 is full-high.
     */
    void setDutyCycle(double dutyCycle);

}
