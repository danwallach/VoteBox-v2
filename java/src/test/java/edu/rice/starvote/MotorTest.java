package edu.rice.starvote;

import org.junit.Test;

/**
 * Created by luej on 8/2/16.
 */
public class MotorTest {
    @Test
    public void test() throws Exception {
        IMotor motor = new PrinterMotor(22, 27, new PWMPython(17, 50, 0));
        motor.forward();
        Thread.sleep(1000);
        motor.forwardSlow();
        Thread.sleep(1000);
        motor.stop();
        Thread.sleep(1000);
        motor.reverse();
        Thread.sleep(1000);
        motor.reverseSlow();
        Thread.sleep(1000);
    }
}
