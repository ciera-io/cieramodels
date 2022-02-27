package gps_watch;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import gps_watch.ui.UIUI;
import io.ciera.runtime.api.domain.Message;
import io.ciera.runtime.domain.AbstractDomain;
import io.ciera.runtime.domain.PortMessage;
import tracking.shared.Indicator;
import tracking.shared.Unit;
import ui.shared.IUI;

public class UI extends AbstractDomain {

    private static GuiConnection requester = null;
    private static final int SOCKET_ERROR = -1;
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 2003;

    // ports
    private IUI UIUI;

    public UI(String name) {
        super(name);
        UIUI = new UIUI(this);
    }

    // domain functions
    public void setIndicator(final Indicator p_indicator) {
        if (requester != null) {
            try {
                requester.sendMessage(new IUI.SetIndicator(p_indicator));
            } catch (IOException e) {
                e.printStackTrace();
                getApplication().getLogger().warn("Connection lost.1");
                requester.tearDown();
                requester = null;
            }
        }
    }

    public void setTime(final int p_time) {
        if (requester != null) {
            try {
                requester.sendMessage(new IUI.SetTime(p_time));
            } catch (IOException e) {
                e.printStackTrace();
                getApplication().getLogger().warn("Connection lost.2");
                requester.tearDown();
                requester = null;
            }
        }
    }

    public void setData(final double p_value, final Unit p_unit) {
        getApplication().getLogger().trace("value: %.2f, unit: %s", p_value, p_unit.name());
        if (requester != null) {
            try {
                requester.sendMessage(new IUI.SetData(p_value, p_unit));
            } catch (IOException e) {
                e.printStackTrace();
                getApplication().getLogger().warn("Connection lost.3");
                requester.tearDown();
                requester = null;
            }
        }
    }

    // port accessors
    public IUI UIUI() {
        return UIUI;
    }

    @Override
    public void initialize() {
        if (connect() != SOCKET_ERROR) {
            getContext().execute(() -> listen());
        } else {
            getContext().halt();
        }
    }

    @Override
    public UI getDomain() {
        return (UI) super.getDomain();
    }

    private void listen() {
        Message msg = poll();
        if (msg != null) {
            if (msg.getId() != SOCKET_ERROR) {
                if (UIUI().satisfied()) {
                    UIUI().send(msg);
                } else {
                    getApplication().getLogger().warn("UI port is not satisfied");
                }
            } else {
                getApplication().getLogger().info("Socket listener shuting down.");
                getContext().halt();
                return;
            }
        }
        // listen again
        getContext().execute(() -> listen());
    }

    private int connect() {
        if (requester == null) {
            requester = new GuiConnection();
        }
        try {
            requester.connect();
        } catch (UnknownHostException unknownHost) {
            getApplication().getLogger().warn("You are trying to connect to an unknown host.");
            requester.tearDown();
            requester = null;
            return SOCKET_ERROR;
        } catch (IOException e) {
                e.printStackTrace();
            getApplication().getLogger().warn("Connection lost.4");
            requester.tearDown();
            requester = null;
            return SOCKET_ERROR;
        }
        return 1;
    }

    private Message poll() {
        if (requester != null) {
            try {
                return requester.poll();
            } catch (IOException e) {
                e.printStackTrace();
                getApplication().getLogger().warn("Connection lost.5");
                requester.tearDown();
                requester = null;
                return new PortMessage(SOCKET_ERROR);
            }
        } else {
            return new PortMessage(SOCKET_ERROR);
        }
    }

    private class GuiConnection {

        private Socket requestSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        private void connect() throws IOException {
            requestSocket = new Socket(HOSTNAME, PORT);
            getApplication().getLogger().info("Connected to localhost on port 2003");
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            requestSocket.setSoTimeout(10); // set read timeout for 10 milliseconds
        }

        private Message poll() throws IOException {
            try {
                try {
                    return (Message) in.readObject();
                } catch (InvalidClassException | StreamCorruptedException | OptionalDataException
                        | ClassNotFoundException e) {
                    getApplication().getLogger().error("Failed to deserialize message.");
                    return new PortMessage(SOCKET_ERROR);
                }
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException) {
                    // do nothing
                } else {
                    throw e;
                }
            }
            return null;
        }

        private void sendMessage(Message msg) throws IOException {
            out.writeObject(msg);
            out.flush();
        }

        private void tearDown() {
            try {
                if (requestSocket != null)
                    requestSocket.close();
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                // do nothing
            }
        }

    }

}
