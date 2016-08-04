package edu.rice.starvote;

/**
 * Created by luej on 7/26/16.
 */
public class ServoDriver {

    final static double PWMFREQ = 50;
    final static double MINPULSE = 0.4;
    final static double MAXPULSE = 2.2;

    private final IPWMDriver driver;


    public ServoDriver(IPWMDriver driver) {
        this.driver = driver;
    }

    public void move(double angle) {
        final double pulseWidth = (MAXPULSE - MINPULSE) * angle + MINPULSE;
        final double dc = pulseWidth / (10.0 / PWMFREQ);
        driver.setFrequency(PWMFREQ);
        driver.setDutyCycle(dc);
    }
}
