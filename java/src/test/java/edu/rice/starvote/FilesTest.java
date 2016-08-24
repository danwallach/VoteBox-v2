package edu.rice.starvote;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by luej on 8/23/16.
 */
public class FilesTest {
    @Test
    public void test() throws Exception {
        final Path resource = JarResource.getResource("pwm.py");
        System.out.println(resource.toAbsolutePath().toString());
    }
}
