import java.net.Socket;

public class Client {

    private String host;
    private int port;
    private Socket socket;
    Connection connection=null;

    public boolean start() {
        try {
            socket = new Socket(host, port);
        } catch (Exception ex) {
            return false;
        }

        connection = new Connection(socket);
        connection.start();

        return true;
    }


    public void stop() {
        if (connection != null)
            connection.close();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isAlive() {
        return (connection != null && connection.isAlive());
    }

    public Events receiveMessage() {
        if (connection.messagesQueue.isEmpty()) {
            return null;
        } else {
            Events ge = new Events((String) connection.messagesQueue
                    .getFirst());
            connection.messagesQueue.removeFirst();
            return ge;
        }
    }
    public void sendMessage(Events events) {
        connection.sendMessage(events.toSend());
    }

    public void setMessage(int m) {
        connection.sendMessage(m);
    }
}
