package edu.rice.starvote;

/**
 * Created by luej on 7/20/16.
 */
public interface ISpooler {

    DeviceStatus getStatus();

    void takeIn();

    void eject();
}
