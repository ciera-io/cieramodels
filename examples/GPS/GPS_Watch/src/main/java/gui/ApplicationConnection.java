package gui;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.Socket;

import io.ciera.runtime.api.domain.Message;
import tracking.shared.Indicator;
import tracking.shared.Unit;
import ui.shared.IUI;

/**
 * The <code>ApplicationConnection</code> is the connection to the underlying
 * application that takes input from the GUI and displays data via the GUI.
 * Messages are from the application are received on this thread. Message
 * sending occurs from the UI thread.
 */
public class ApplicationConnection extends Thread {

    Gui app = null;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message = "";

    public ApplicationConnection(Gui app, Socket connection) {
        this.app = app;
        app.setApplicationConnection(this);
        this.connection = connection;
    }

    public void sendSignal(Message data) {
        try {
            out.writeObject(data);
            out.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            app.getLogger().info("Connection received from " + connection.getInetAddress().getHostName());
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());

            // main loop
            while (true) {
                try {
                    // read a message
                    Message data = null;
                    try {
                        data = (Message) in.readObject();
                    } catch (InvalidClassException | StreamCorruptedException | OptionalDataException
                            | ClassNotFoundException e) {
                        app.getLogger().error("Failed to deserialize message.");
                        continue;
                    }

                    // handle the message
                    switch (data.getId()) {
                    case IUI.SETDATA:
                        app.getGuiDisplay().setData((double) data.get("p_value"), (Unit) data.get("p_unit"));
                        break;
                    case IUI.SETTIME:
                        app.getGuiDisplay().setTime((int) data.get("p_time"));
                        break;
                    case IUI.SETINDICATOR:
                        app.getGuiDisplay().setIndicator((Indicator) data.get("p_indicator"));
                        break;
                    default:
                        break;
                    }
                    Thread.sleep(10);
                } catch (IOException ioe) {
                    app.getLogger().warn("Connection closed by client.");
                    break;
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                connection.close();
                app.getGuiDisplay().setTime(0);
                app.getGuiDisplay().setData(0f, Unit.KM);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
