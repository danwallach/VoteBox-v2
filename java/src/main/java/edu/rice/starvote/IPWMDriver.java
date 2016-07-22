package edu.rice.starvote;

/**
 * Created by luej on 7/20/16.
 */
public interface IPWMDriver {

//    void init(int pin, double frequency, double dutyCycle);

    void setFrequency(double frequency);

    void setDutyCycle(double dutyCycle);

}
