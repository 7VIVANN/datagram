import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;

public class ServerThread extends Thread{

    protected DatagramSocket socket =null;
    protected BufferedReader in = null;
    protected PrintWriter out = null;
    protected boolean moreQuotes = true;

    public ServerThread() throws IOException{
        this("SERVER THREAD");
    }

    public ServerThread(String name) throws IOException{
        super(name);
        socket = new DatagramSocket(8080);
        try{
            in = new BufferedReader(new FileReader("one-liners.txt"));
            out = new PrintWriter(new FileWriter("log.txt"));
        }
        catch (FileNotFoundException e)
        {
            out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void run()
    {
        while (moreQuotes)
        {
            try{
                byte[] buf = new byte[256];

                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String dString = null;

                if(in == null)
                    dString = new Date().toString();
                else
                    dString = getNextQuote();

                InetAddress address = packet.getAddress();
                System.out.println("INET: "+address);
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);

                socket.send(packet);
            }
            catch(IOException e)
            {
                moreQuotes=false;
                out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        socket.close();
    }

    private String getNextQuote() {
        String returnValue = null;

        try{
            if((returnValue = in.readLine()) == null)
            {
                in.close();
                moreQuotes = false;
                returnValue = "NO MORE QUOTES.";
            }
        }
        catch (IOException e)
        {
            out.println(Arrays.toString(e.getStackTrace()));
        }

        return returnValue;
    }
}
