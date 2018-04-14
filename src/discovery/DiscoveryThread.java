package discovery;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DiscoveryThread implements Runnable{

    @Override
    public void run() {
        System.out.println("Sending REQ");
        DiscoveryMessage msg = new DiscoveryMessage();
        InetSocketAddress is_msg = null;
        ByteBuffer buffer = ByteBuffer.allocate(2048);

        // send request
        try (MulticastSocket sendingSocket = new MulticastSocket(Config.Discover_port)){

            // get destination data
            InetAddress IPAddress = Config.Broadcast_address;
            int port = Config.Discover_port;

            // join group
            sendingSocket.joinGroup(IPAddress);

            // Build new message
            msg.setMessage(Config.REQ);
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
            System.out.println("Sent REQ to IP "+ IPAddress.getHostAddress());
            sendingSocket.leaveGroup(IPAddress);
        } catch (Exception e){
            e.printStackTrace();
        }

        // receive answers
        try(DatagramChannel channel = DatagramChannel.open()){
            channel.bind(new InetSocketAddress(Config.My_address,Config.My_port));
            channel.configureBlocking(false);

            // flavor text
            System.out.println("anyone out there?");

            // retry MAX_CICLES times to receive answers
            for(int i = 0; i<Config.MAX_CICLES; i++){

                System.out.println("checking");

                // receive from channel
                is_msg = (InetSocketAddress) channel.receive(buffer);
                buffer.flip();
                if(is_msg == null) continue;

                // flavor text
                System.out.println("Got a message from "+is_msg.getAddress().toString());

                // rebuild message
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
                msg = (DiscoveryMessage) ois.readObject();

                // if ACK update My_ID
                if(msg.getMessage() == Config.ACK && (++Config.My_ID) < msg.getSender_ID()){
                    Config.My_ID = (byte) (msg.getSender_ID()+1);
                }
                // if it's another request update them based on what I know
                else if(msg.getMessage() == Config.REQ){

                    // rewrite message
                    msg.setSender_ID(Config.My_ID);
                    msg.setMessage(Config.ACK);

                    // prepare to send ACK
                    try(DatagramSocket sendingSocket = new DatagramSocket()){
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(msg);
                        oos.flush();
                        byte[] Buf= baos.toByteArray();

                        // send packet
                        DatagramPacket packet = new DatagramPacket(Buf, Buf.length, is_msg.getAddress(), Config.My_port);
                        sendingSocket.send(packet);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("My id is" + Config.My_ID + " :D");
        (new Thread(new AnswerThread())).start();
    }

    /*public static void main(String[] args) {
        (new Thread(new DiscoveryThread())).start();
    }*/
}
