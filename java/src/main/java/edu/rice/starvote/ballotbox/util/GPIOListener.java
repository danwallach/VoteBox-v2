package edu.rice.starvote.ballotbox.util;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Handles hardware interrupts for a GPIO input pin. Makes Pi4J GPIO interrupts easier to use. Allows efficient
 * event-driven control of software by GPIO sensors, rather than polling.
 *
 * @author luejerry
 */
public class GPIOListener {

    private final Pin wpiPin;
    private final GpioPinDigitalInput sensor;

    /**
     * Constructor. Initializes the given pin for GPIO digital input.
     * @param pin Pin to listen for hardware interrupts (BCM numbering).
     */
    public GPIOListener(int pin) {
        wpiPin = PinMap.mapPin(pin).get();
        sensor = GPIOManager.controller().provisionDigitalInputPin(wpiPin);
    }

    /**
     * Register an event handler to execute once the next time the pin state changes in the desired direction. This
     * method blocks indefinitely until the event occurs, or it is interrupted. The listener does not persist after
     * first execution; subsequent events will not trigger it.
     *
     * Note that since this method only blocks until the event occurs, the event handler task itself may run
     * concurrently with the parent thread. Be careful to implement additional synchronization if concurrency is not
     * desired.
     *
     * @param edge Direction of state change to listen for: `RISING`, `FALLING`, or `BOTH`.
     * @return True if event occured, false if interrupted while waiting.
     */
    public boolean waitForEvent(PinEdge edge) {
        boolean didTask;
        final CountDownLatch latch = new CountDownLatch(1);
        final GpioPinListenerDigital listener = generateListener(edge, () -> {}, latch);
        sensor.addListener(listener);
        try {
            latch.await();
            didTask = true;
        } catch (InterruptedException e) {
            System.err.println(e.toString());
            didTask = false;
        } finally {
            sensor.removeAllListeners();
        }
        sensor.removeAllListeners();
        return didTask;
    }

    /**
     * Register an event handler to execute once the next time the pin state changes in the desired direction. This
     * method blocks until the event occurs, the timeout expires, or it is interrupted. The listener does not persist
     * after first execution; subsequent events will not trigger it.
     *
     * Note that since this method only blocks until the event occurs, the event handler task itself may run
     * concurrently with the parent thread. Be careful to implement additional synchronization if concurrency is not
     * desired.
     *
     * @param edge Direction of state change to listen for: `RISING`, `FALLING`, or `BOTH`.
     * @param timeout Time to wait in milliseconds.
     * @return True if event occured, false if timeout expired or interrupted.
     */
    public boolean waitForEvent(PinEdge edge, long timeout) {
        boolean didTask;
        final CountDownLatch latch = new CountDownLatch(1);
        final GpioPinListenerDigital listener = generateListener(edge, () -> {}, latch);
        sensor.addListener(listener);
        try {
            didTask = latch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.err.println(e.toString());
            didTask = false;
        } finally {
            sensor.removeAllListeners();
        }
        return didTask;
    }

    /**
     * Get current state of pin.
     * @return Pin state. `LOW` or `HIGH`.
     */
    public PinState getState() {
        return sensor.getState();
    }

    /**
     * Helper method to generate the event listener passed to Pi4J; factoring out common code.
     * @param edge Direction of state change to listen for.
     * @param task Task to execute when event occurs.
     * @param latch Latch that notifies parent thread when event is detected.
     * @return Pi4J GPIO event listener.
     */
    private GpioPinListenerDigital generateListener(PinEdge edge, Runnable task, CountDownLatch latch) {
        return event -> {
                switch (edge) {
                    case BOTH:
                        latch.countDown();
                        task.run();
                        return;
                    case RISING:
                        if (event.getState() == PinState.HIGH) {
                            latch.countDown();
                            task.run();
                        }
                        return;
                    case FALLING:
                        if (event.getState() == PinState.LOW) {
                            latch.countDown();
                            task.run();
                        }
                        return;
                    default:
                }
            };
    }
}
