package edu.rice.starvote;

/**
 * Created by luej on 8/24/16.
 */
public class DiverterBlaster implements IDiverter {

    final double DC_UP = 5.6;
    final double DC_DOWN = 11.0;

    private final IPWMDriver pwm;

    public DiverterBlaster(IPWMDriver pwm) {
        this.pwm = pwm;
    }

    @Override
    public void up() {
        pwm.setDutyCycle(DC_UP);
    }

    @Override
    public void down() {
        pwm.setDutyCycle(DC_DOWN);
    }
}
