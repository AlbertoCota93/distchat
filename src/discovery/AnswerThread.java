package discovery;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class AnswerThread implements Runnable {
    @Override
    public void run() {
        // flavor text
        System.out.println("checking for new requests");

        // reusable variables
        byte[] incomingData = new byte[2048];
        DiscoveryMessage msg = new DiscoveryMessage((byte) -1,'Z');

        try (MulticastSocket socket = new MulticastSocket(Config.Discover_port)){
            socket.joinGroup(Config.Broadcast_address);
            // receive requests
            while(!Config.stop_program){

                // read message
                DatagramPacket incomingPacket = new DatagramPacket(incomingData,incomingData.length);
                socket.receive(incomingPacket);

                // flavor text
                System.out.println("Mesage recieved");

                // read message cont
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    msg = (DiscoveryMessage) is.readObject();
                    System.out.println(msg);
                }catch (Exception e){
                    msg = new DiscoveryMessage((byte) -1,'Z');
                    e.printStackTrace();
                }

                // confirm msg is a request, if not, skip
                if(msg.getMessage() != Config.REQ || msg.getSender_ID() == Config.My_ID) continue;

                // flavor text
                System.out.println("Sending ACK");

                // send answer
                try (DatagramSocket sendingSocket = new DatagramSocket()){

                    // get destination data
                    InetAddress IPAddress = incomingPacket.getAddress();
                    int port = incomingPacket.getPort();

                    // Build new message
                    msg.setMessage(Config.ACK);
                    msg.setSender_ID(Config.My_ID);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(msg);
                    oos.flush();
                    byte[] Buf= baos.toByteArray();

                    // send packet
                    DatagramPacket packet = new DatagramPacket(Buf, Buf.length, IPAddress, port);
                    sendingSocket.send(packet);

                    // flavor text
                    System.out.println("Sent ACK to IP "+ IPAddress.getHostAddress());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
