package edu.rice.starvote.ballotbox.drivers;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import edu.rice.starvote.ballotbox.util.GPIOManager;
import edu.rice.starvote.ballotbox.util.PinMap;

import java.io.IOException;

/**
 * Reference implementation of the feed motor controller, driving the main motor in an HP 1010 printer.
 *
 * @author luejerry
 */
public class PrinterMotor implements IMotor {

    private final GpioPinDigitalOutput forwardPin; // motor forward (pin 22)
    private final GpioPinDigitalOutput reversePin; // motor reverse (pin 27)
    private final IPWMDriver driver; // motor enable (pin 17)

    /**
     * Constructor.
     * @param forwardPin Pin that moves motor forward when HIGH (BCM numbering).
     * @param reversePin Pin that moves motor reverse when HIGH (BCM numbering).
     * @param driver PWM driver module.
     */
    public PrinterMotor(int forwardPin, int reversePin, IPWMDriver driver) {
        this.forwardPin = GPIOManager.controller().provisionDigitalOutputPin(PinMap.mapPin(forwardPin).get(), PinState.LOW);
        this.reversePin = GPIOManager.controller().provisionDigitalOutputPin(PinMap.mapPin(reversePin).get(), PinState.LOW);
        this.driver = driver;
    }

    /**
     * Constructor.
     * @param forwardPin Pin that moves motor forward when HIGH (Pi4J pin).
     * @param reversePin Pin that moves motor reverse when HIGH (Pi4J pin).
     * @param driver PWM driver module.
     */
    public PrinterMotor(Pin forwardPin, Pin reversePin, IPWMDriver driver) {
        this.forwardPin = GPIOManager.controller().provisionDigitalOutputPin(forwardPin, PinState.LOW);
        this.reversePin = GPIOManager.controller().provisionDigitalOutputPin(reversePin, PinState.LOW);
        this.driver = driver;
    }

    @Override
    public void stop() throws IOException {
        driver.setDutyCycle(0);
    }

    @Override
    public void forward() throws IOException {
        forwardPin.high();
        reversePin.low();
        driver.setDutyCycle(100);
    }

    @Override
    public void forwardSlow() throws IOException {
        forwardPin.high();
        reversePin.low();
        driver.setDutyCycle(25);
    }

    @Override
    public void forward(double speed) throws IOException {
        forwardPin.high();
        reversePin.low();
        driver.setDutyCycle(speed);
    }

    @Override
    public void reverse() throws IOException {
        reversePin.high();
        forwardPin.low();
        driver.setDutyCycle(100);
    }

    @Override
    public void reverseSlow() throws IOException {
        reversePin.high();
        forwardPin.low();
        driver.setDutyCycle(25);
    }

    @Override
    public void reverse(double speed) throws IOException {
        reversePin.high();
        forwardPin.low();
        driver.setDutyCycle(speed);
    }
}
