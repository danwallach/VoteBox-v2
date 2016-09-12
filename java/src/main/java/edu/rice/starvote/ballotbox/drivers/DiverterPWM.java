package edu.rice.starvote.ballotbox.drivers;

import java.io.IOException;

/**
 * Diverter servo control using a generic IPWMDriver. The up and down positions are calibrated to a HB-6001HD servo.
 *
 * ##### Example #####
 * Using the pi-blaster based PWM driver (recommended):
 *
 * ```java
 * IPWMDriver pwm = new PWMBlaster(PIN_NUMBER, 50);
 * IDiverter diverter = new DiverterPWM(pwm);
 * diverter.up();
 * ```
 *
 * @author luejerry
 */
public class DiverterPWM implements IDiverter {

    /**
     * Duty cycle in the up (reject) position, in %.
     */
    final double DC_UP = 5.6;
    /**
     * Duty cycle in the down (accept) position, in %.
     */
    final double DC_DOWN = 11.0;

    private final IPWMDriver pwm;

    /**
     * Constructor. Does not change the servo state.
     * @param pwm PWM driver to use (e.g. `PWMBlaster`).
     * @see PWMBlaster
     */
    public DiverterPWM(IPWMDriver pwm) {
        this.pwm = pwm;
    }

    @Override
    public void up() throws IOException {
        pwm.setDutyCycle(DC_UP);
    }

    @Override
    public void down() throws IOException {
        pwm.setDutyCycle(DC_DOWN);
    }
}
