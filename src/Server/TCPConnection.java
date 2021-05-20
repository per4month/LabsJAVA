package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class TCPConnection {
    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 9001;
    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException {
        this(eventListener, new Socket(ipAddr, port));
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);
                    while (!rxThread.isInterrupted()) {
                        eventListener.onReceiveObject(TCPConnection.this, in.readObject());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    rxThread.interrupt();
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public synchronized void sendObject(Object object) {
        try {
            out.writeObject(object);
            out.reset();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection " + socket.getInetAddress() + ": " + socket.getPort();
    }

    public static class BoxUsers implements Serializable {
        final public ArrayList<String> users;
        public BoxUsers(ArrayList<String> arr) {
            this.users = arr;
        }

    }
}
