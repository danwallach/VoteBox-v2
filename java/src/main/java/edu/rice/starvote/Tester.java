package edu.rice.starvote;

/**
 * Created by luej on 7/20/16.
 */
public class Tester {

    public static void main(String[] args) {
        final String cat = ExternalProcess.runAndCapture("cat", "/dev/null");
        System.out.println(cat.length());
    }
}
