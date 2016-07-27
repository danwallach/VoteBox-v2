package edu.rice.starvote;

/**
 * Created by luej on 7/26/16.
 */
public class Diverter6001HD implements IDiverter {

    final static double UP = 0.4;
    final static double DOWN = 1.0;

    private final IPWMDriver driver;

    public Diverter6001HD(IPWMDriver driver) {
        this.driver = driver;
    }

    @Override
    public void up() {

    }

    @Override
    public void down() {

    }
}
