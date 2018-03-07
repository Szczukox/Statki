import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;

public class Connection extends Thread {
    private Socket socket;
    private BufferedReader in;

    private DataOutputStream out;

    public LinkedList<String> messagesQueue;

    public Connection(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
        }
        messagesQueue = new LinkedList<String>();
        this.socket = socket;
    }

    public void sendMessage(int x) {
       try {
           ByteBuffer buff = ByteBuffer.allocate(4);
           byte[] b = buff.order(ByteOrder.LITTLE_ENDIAN).putInt(x).array();
           out.write(b);
       }catch (IOException ex){

       }
    }
    /*public void sendId(String id) {
        try {
            PrintWriter outer = new PrintWriter(socket.getOutputStream(), true);

            outer.println(id);
        }catch (IOException ex){

        }
    }*/

    public void run() {
        String s;
        try {
            while ((s = in.readLine()) != null) {
                messagesQueue.add(s);
                System.out.println(s);
            }
            out.close();
            in.close();
        } catch (IOException ex) {
        }

        try {
            socket.close();
        } catch (Exception ex) {
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception ex) {
        }
    }

    /*public BufferedReader getIn() {
        return in;
    }*/

    /*public void setIn(BufferedReader in) {
        this.in = in;
    }*/



    /*public Socket getSocket() {
        return socket;
    }*/

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /*int ntohl(byte[] x)
    {
        int res = 0;
        for (int i = 0; i < 4; i++)
        {
            res <<= 8;
            res |= (int) x[i];
        }
        return res;
    }*/

    /*byte[] htonl(int x)
    {
        byte[] res = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            res[i] = (new Integer(x >>> 24)).byteValue();
            x <<= 8;
        }
        return res;
    }*/

    /*int htonl1(int value) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            return value;
        }
        return Integer.reverseBytes(value);
    }*/


}
