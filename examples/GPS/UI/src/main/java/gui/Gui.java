package gui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.googlecode.jsonrpc4j.ProxyUtil;
import com.googlecode.jsonrpc4j.StreamServer;

import ui.shared.IUI;

public class Gui {

  private static final int APP_PORT = 1420;
  private static final int SERVER_PORT = 1421;

  private static final Logger logger = LoggerFactory.getLogger(Gui.class);

  public static void main(String[] args) {

    // create RPC client to proxy UI interface
    IUI uiProxy = null;
    while (uiProxy == null) {
      try {
        logger.debug("MAIN: Connecting to the application...");
        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), APP_PORT), 1000);
        uiProxy =
            ProxyUtil.createClientProxy(
                Gui.class.getClassLoader(), IUI.class, new JsonRpcClient(), socket);
      } catch (IOException e) {
        logger.debug("MAIN: failed to connect to UI. Retrying...");
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
    }

    // Create GUI
    WatchGui guiDisplay;
    if (args.length == 1 && "--console".equals(args[0])) {
      guiDisplay = new AsciiWatchGui(uiProxy);
    } else {
      guiDisplay = new SwingWatchGui(uiProxy);
    }

    // create the RPC server to receive calls from the application
    StreamServer streamServer = null;
    try {
      streamServer =
          new StreamServer(
              new JsonRpcServer(guiDisplay, IUI.class), 50, new ServerSocket(SERVER_PORT));
      streamServer.start();
    } catch (IOException e) {
      logger.debug("MAIN: failed to start RPC server");
      e.printStackTrace();
    }

    // Display the GUI
    guiDisplay.display();
  }
}
