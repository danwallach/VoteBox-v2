package edu.rice.starvote;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

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
    public void stop() {
        driver.setDutyCycle(0);
    }

    @Override
    public void forward() {
        forwardPin.high();
        reversePin.low();
        driver.setDutyCycle(100);
    }

    @Override
    public void forwardSlow() {
        forwardPin.high();
        reversePin.low();
        driver.setDutyCycle(25);
    }

    @Override
    public void forward(double speed) {
        forwardPin.high();
        reversePin.low();
        driver.setDutyCycle(speed);
    }

    @Override
    public void reverse() {
        reversePin.high();
        forwardPin.low();
        driver.setDutyCycle(100);
    }

    @Override
    public void reverseSlow() {
        reversePin.high();
        forwardPin.low();
        driver.setDutyCycle(25);
    }

    @Override
    public void reverse(double speed) {
        reversePin.high();
        forwardPin.low();
        driver.setDutyCycle(speed);
    }
}
