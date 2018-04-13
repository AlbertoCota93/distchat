package discovery;

import java.io.Serializable;
import java.net.InetAddress;

public class DiscoveryMessage implements Serializable {
    static final long serialVersionUID = 42L;

    private byte Sender_ID;
    private char message;

    public DiscoveryMessage(byte sender_id, char message) {
        Sender_ID = sender_id;
        this.message = message;
    }

    public DiscoveryMessage() {
        Sender_ID = -1;
        this.message = 'Z';
    }

    public byte getSender_ID() {
        return Sender_ID;
    }

    public void setSender_ID(byte sender_ID) {
        Sender_ID = sender_ID;
    }

    public char getMessage() {
        return message;
    }

    public void setMessage(char message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "sender: "+Sender_ID+" message: "+message;
    }
}
