import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) throws IOException {
        if(args.length!=1) {
            System.err.println("Usage : <HOSTNAME>");
            return;
        }

        DatagramSocket socket = new DatagramSocket();

        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName(args[0]);

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8080);

        socket.receive(packet);

        String received = new String(packet.getData(),0, packet.getLength());

        System.out.print("QUOTE OF THE MOMENT: "+received);

        socket.close();
    }
}
