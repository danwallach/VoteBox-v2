package edu.rice.starvote;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by luej on 7/20/16.
 */
public class GPIOListener {

    // Paper sensor is on bcm gpio24, or wiringpi 5
    private final Pin wpiPin;
    private final GpioPinDigitalInput sensor;

    public GPIOListener(int pin) {
        wpiPin = PinMap.mapPin(pin).get();
        sensor = GPIOManager.controller().provisionDigitalInputPin(wpiPin);
    }

    public boolean waitForEvent(PinEdge edge, Runnable task) {
        boolean didTask;
        final CountDownLatch latch = new CountDownLatch(1);
        final GpioPinListenerDigital listener = generateListener(edge, task, latch);
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

    public boolean waitForEvent(PinEdge edge, Runnable task, long timeout) {
        boolean didTask;
        final CountDownLatch latch = new CountDownLatch(1);
        final GpioPinListenerDigital listener = generateListener(edge, task, latch);
        sensor.addListener(listener);
        try {
            didTask = latch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.err.println(e.toString());
            didTask = false;
        } finally {
            sensor.removeAllListeners();
        }
        sensor.removeAllListeners();
        return didTask;
    }

    public PinState getState() {
        return sensor.getState();
    }
}
