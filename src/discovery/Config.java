package mx.com.ITESO.discovery;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Config {
    public static final char REQ = 'R';
    public static final char ACK = 'A';

    public static final int MAX_CICLES = 5000000;


    public static boolean stop_program = false;

    public static byte My_ID = 0;

    public static int Discover_port = 9090;
    public static int My_port = 9191;

    public static InetAddress My_address;
    public static InetAddress Discover_address;



    static {
        try {
            My_address = InetAddress.getLocalHost();
            Discover_address = InetAddress.getByName("230.0.0.0");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
