package tracking.tracking;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.temporal.ChronoUnit;

import io.ciera.runtime.api.domain.Domain;
import io.ciera.runtime.api.domain.Message;
import io.ciera.runtime.api.domain.Port;
import io.ciera.runtime.api.time.Timer;
import io.ciera.runtime.api.types.Duration;
import io.ciera.runtime.domain.AbstractPort;

public abstract class SocketPort extends AbstractPort implements Port {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 2003;
    private static final Duration LISTEN_PERIOD = new Duration(250, ChronoUnit.MILLIS);

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Timer listenTimer;

    private boolean connected;

    public SocketPort(String name, Domain domain) {
        super(name, domain);
        socket = null;
        out = null;
        in = null;
        listenTimer = null;
        connected = false;
        try {
            connect();
            listenTimer = getContext().scheduleRecurringAction(Duration.ZERO, LISTEN_PERIOD, this::listen);
        } catch (IOException e) {
            getApplication().getLogger().error("Failed to connect to %s:%d", HOSTNAME, PORT);
            tearDown();
        }
    }

    @Override
    public boolean satisfied() {
        return connected;
    }

    @Override
    public void send(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            getApplication().getLogger().error("Failed to send message", e);
            e.printStackTrace();
        }
    }

    private void listen() {
        try {
            Message data = poll();
            if (data != null) {
                deliver(data);
            }
        } catch (IOException e) {
            getApplication().getLogger().warn("Connection closed");
            tearDown();
        }
    }

    private Message poll() throws IOException {
        if (connected) {
            try {
                try {
                    // read and deliver data
                    return (Message) in.readObject();
                } catch (InvalidClassException | StreamCorruptedException | OptionalDataException
                        | ClassNotFoundException e) {
                    getApplication().getLogger().error("Failed to deserialize message.");
                    return null;
                }
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException) {
                    // do nothing
                } else {
                    throw e;
                }
            }
        }
        return null;
    }

    private void connect() throws IOException {
        socket = new Socket(HOSTNAME, PORT);
        getApplication().getLogger().info("Connected to localhost on port 2003");
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        socket.setSoTimeout(10); // set read timeout for 10 milliseconds
        connected = true;
    }

    private void tearDown() {
        try {
            if (listenTimer != null) {
                listenTimer.cancel();
                listenTimer = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // do nothing
        }
        connected = false;
    }

}
