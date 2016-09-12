package edu.rice.starvote;

import edu.rice.starvote.ballotbox.util.JarResource;
import org.junit.Test;

import java.nio.file.Path;

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
