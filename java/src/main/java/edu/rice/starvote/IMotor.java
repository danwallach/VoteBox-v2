package edu.rice.starvote;

/**
 * Created by luej on 7/26/16.
 */
public interface IMotor {

    void stop();
    void forward();
    void forwardSlow();
    void forward(double speed);
    void reverse();
    void reverseSlow();
    void reverse(double speed);

}
