/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;


/**
 *
 * @author ChaosInfinityViral
 */
public class clientserver extends Thread{
    private DatagramSocket socket;
    private InetAddress address;
    protected boolean running;
    private int expectedServerCount;
    private byte[] buf;
    private Thread servertask;
    private List<String> listtext = new ArrayList<String>();

    public clientserver() throws IOException {
        socket = new DatagramSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(4445));
    }
    public clientserver(int port) throws SocketException{
        socket = new DatagramSocket(port);
        socket.setBroadcast(true);
    }

    public static void main(String[] args) {

    /*    int port = 17;
        String Filetoload = args[0];
        try {
            clientserver server = new clientserver(port);

            server.loadtext(Filetoload);
            server.service();
        } catch (SocketException e) {
            System.out.println("Socket Error");
        } catch (IOException e){
            System.out.println("I/O error");
        }*/
    }
    private void service()throws IOException{
        while(true){
            DatagramPacket request = new DatagramPacket(new byte[1], 1);
            socket.receive(request);

            String textline = loadlastword();
            byte[] buffer = textline.getBytes();

            InetAddress clientAddress = request.getAddress();
            int clientPort = request.getPort();

            DatagramPacket response = new DatagramPacket(buffer,buffer.length, clientAddress,clientPort);
            socket.send(response);
        }
    }

    private void loadtext(String text) throws IOException{
        BufferedReader reader= new BufferedReader(new FileReader(text));
        String textinthetext;
        String[] word ;
        while ((textinthetext = reader.readLine())!=null){
            word = reader.readLine().split("\\W+");
            for (int i = 0; i < word.length; i++) {
                textinthetext=word[i];
                listtext.add(text);
            }
        }
        reader.close();

    }
    private String loadlastword(){
        int lastword = listtext.size();
        String texttoreturn = listtext.get(lastword);
        return texttoreturn;
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
    public void run() {
		running = true;
		System.out.println("Server started on port " );
	}
    public void server(){
         servertask = new Thread(){
            public void run() {
                  while(running){
                    try {
                        byte[]buf = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        socket.receive(packet);
                        InetAddress address = packet.getAddress();
                        int port = packet.getPort();
                        packet = new DatagramPacket(buf, buf.length, address, port);
                        String received = new String(packet.getData(), 0, packet.getLength());
                        if (received.equals("end")) {
                            running = false;
                            continue;
                        }
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                        running = false;
                    }
               }
                socket.close();
            }
        };
        servertask.start();
    }

}
