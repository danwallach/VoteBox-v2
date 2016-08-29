package edu.rice.starvote.util;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class provides a convenience method to map BCM pin numbers to Pi4J pins. All invocations of Pi4J GPIO functions
 * **must** use the Pi4J pin mapping.
 *
 * @author luejerry
 */
public class PinMap {

    private final static Map<Integer, Pin> map = new HashMap<>();

    static {
        map.put(17, RaspiPin.GPIO_00);
        map.put(18, RaspiPin.GPIO_01);
        map.put(22, RaspiPin.GPIO_03);
        map.put(23, RaspiPin.GPIO_04);
        map.put(24, RaspiPin.GPIO_05);
        map.put(25, RaspiPin.GPIO_06);
        map.put(27, RaspiPin.GPIO_02);
    }

    /**
     * Get the Pi4J pin corresponding to a given BCM pin.
     * @param bcm BCM pin number.
     * @return Pi4J pin in an Optional. Optional is empty if pin mapping does not exist.
     */
    public static Optional<Pin> mapPin(Integer bcm) {
        return Optional.ofNullable(map.get(bcm));
    }
}
