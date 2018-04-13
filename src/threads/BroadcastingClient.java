/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ChaosInfinityViral
 */
public class BroadcastingClient {

    public BroadcastingClient(int expectedServers) {
    }
    private BroadcastingClient client;
    private DatagramSocket socket;
    private InetAddress address;
    protected boolean running;
    private int expectedServerCount;
    private byte[] buf;
    private Thread servertask;
    private List<String> listtext = new ArrayList<String>();

    public void whenBroadcasting_thenDiscoverExpectedServers() throws Exception {
        int expectedServers = 4;
        initializeForExpectedServers(expectedServers);

        int serversDiscovered = client.discoverServers("hello server");
        assertEquals(expectedServers, serversDiscovered);
    }

    private void initializeForExpectedServers(int expectedServers) throws Exception {
        for (int i = 0; i < expectedServers; i++) {
           // new BroadcastingEchoServer().start();
        }

        client = new BroadcastingClient(expectedServers);
    }

    public void tearDown() throws IOException {
        stopEchoServer();
        client.close();
    }

    private void stopEchoServer() throws IOException {
        client.discoverServers("end");
    }

    public int discoverServers(String msg) throws IOException {
        initializeSocketForBroadcasting();
        copyMessageOnBuffer(msg);

        // When we want to broadcast not just to local network, call listAllBroadcastAddresses() and execute broadcastPacket for each value.
        broadcastPacket(address);

        return receivePackets();
    }

    private void initializeSocketForBroadcasting() throws SocketException, UnknownHostException {
        socket = new DatagramSocket(17);
        socket.setBroadcast(true);
        this.address = InetAddress.getByName("255.255.255.255");
    }
    private void copyMessageOnBuffer(String msg) {
        buf = msg.getBytes();
    }

    private void broadcastPacket(InetAddress address) throws IOException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
    }

    private int receivePackets() throws IOException {
        int serversDiscovered = 0;
        while (serversDiscovered != expectedServerCount) {
            receivePacket();
            serversDiscovered++;
        }
        return serversDiscovered;
    }

    private void receivePacket() throws IOException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
    }

    public void close() {
        socket.close();
    }

    private void assertEquals(int expectedServers, int serversDiscovered) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
