package edu.rice.starvote;

import edu.rice.starvote.drivers.IScanner;
import edu.rice.starvote.drivers.Scan;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by luej on 8/2/16.
 */
public class ScanTest {
    @Test
    public void test() throws Exception {
        IScanner scanner = new Scan();
        String code = scanner.scan(10);
        System.out.println(code);
        assertFalse("Code was not scanned", code.isEmpty());
        assertEquals("Code was not valid", "Acc3ptB@LL07", code);
    }

}
