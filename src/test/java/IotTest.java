import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;

public class IotTest {
    @Test
    @Ignore
    public void test() throws IOException {
        InetAddress inet;

        inet = InetAddress.getByAddress(new byte[] { (byte)192, (byte)168, 0, 103 });
        System.out.println("Sending Ping Request to " + inet);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

        //103-wojtek
        //105-aga
    }
}
