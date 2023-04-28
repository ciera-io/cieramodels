package gps_watch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.googlecode.jsonrpc4j.ProxyUtil;
import com.googlecode.jsonrpc4j.StreamServer;

import heartratemonitor.heartratemonitor.HeartRateMonitor;
import io.ciera.runtime.AbstractPort;
import io.ciera.runtime.CommandLine;
import io.ciera.runtime.CommandLine.Conditionality;
import io.ciera.runtime.CommandLine.Multiplicity;
import io.ciera.runtime.SimpleScheduler;
import io.ciera.runtime.api.Architecture;
import location.location.Location;
import tracking.tracking.Tracking;
import ui.shared.GoalCriteria;
import ui.shared.GoalSpan;
import ui.shared.IUI;
import ui.shared.Indicator;
import ui.shared.Unit;

public class GPS_Watch {

  private static final Logger logger = LoggerFactory.getLogger(GPS_Watch.class);

  private static final int UI_PORT = 1421;
  private static final int SERVER_PORT = 1420;

  public static void main(String[] args) {

    // process command line
    logger.debug("MAIN: processing arguments");
    CommandLine.initialize(args);
    CommandLine.getInstance()
        .registerValue(
            "-config",
            "Config file path",
            Conditionality.Optional,
            "path",
            Conditionality.Required,
            Multiplicity.Single);

    // load configuration
    logger.debug("MAIN: loading config");
    final String configPath = CommandLine.getInstance().getOptionValue("-config");
    if (configPath != "" && Files.exists(Path.of(configPath))) {
      logger.debug("MAIN: loading configuration");
      Architecture.getInstance().loadConfig(Path.of(configPath));
    }

    // create domains
    logger.debug("MAIN: creating domains");
    final HeartRateMonitor heartratemonitor = ServiceLoader.load(HeartRateMonitor.class).findFirst().orElseThrow();
    final Location location = ServiceLoader.load(Location.class).findFirst().orElseThrow();
    final Tracking tracking = ServiceLoader.load(Tracking.class).findFirst().orElseThrow();

    // create scheduler
    final SimpleScheduler scheduler = new SimpleScheduler(heartratemonitor, location, tracking);

    // create satisfactions
    AbstractPort.satisfy(tracking.HR(), heartratemonitor.HR());
    AbstractPort.satisfy(tracking.LOC(), location.LOC());

    // create the RPC server to receive calls from the UI
    StreamServer streamServer = null;
    try {
      streamServer =
          new StreamServer(
              new JsonRpcServer(new UIProxy(scheduler, tracking.UI()), IUI.class),
              50,
              new ServerSocket(SERVER_PORT));
      streamServer.start();
    } catch (IOException e) {
      logger.debug("MAIN: failed to start RPC server");
      e.printStackTrace();
    }

    // create RPC client to proxy UI interface
    IUI uiProxy = null;
    while (uiProxy == null) {
      try {
        logger.debug("MAIN: Connecting to the UI...");
        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), UI_PORT), 1000);
        uiProxy =
            ProxyUtil.createClientProxy(
                tracking.getClass().getClassLoader(), IUI.class, new JsonRpcClient(), socket);
        tracking.UI().setPeer(uiProxy);
      } catch (IOException e) {
        logger.debug("MAIN: failed to connect to UI. Retrying.");
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
    }

    // initialize domains
    logger.debug("MAIN: initializing domains");
    scheduler.execute(heartratemonitor::initialize);
    scheduler.execute(location::initialize);
    scheduler.execute(tracking::initialize);

    // schedule tasks forever in a single thread
    logger.debug("MAIN: starting up...");
    while (scheduler.hasNext()) {
      scheduler.next().run();
    }
    logger.debug("MAIN: shutting down...");

    if (streamServer != null) {
      try {
        streamServer.stop();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private static final class UIProxy extends AbstractPort implements IUI {

    private final Executor executor;
    private final IUI ui;

    public UIProxy(final Executor executor, final IUI ui) {
      super("", null);
      this.executor = executor;
      this.ui = ui;
    }

    @Override
    public void lapResetPressed() {
      executor.execute(() -> ui.lapResetPressed());
    }

    @Override
    public void lightPressed() {
      executor.execute(() -> ui.lightPressed());
    }

    @Override
    public void modePressed() {
      executor.execute(() -> ui.modePressed());
    }

    @Override
    public void newGoalSpec(
        GoalSpan p_spanType,
        GoalCriteria p_criteriaType,
        double p_span,
        double p_maximum,
        double p_minimum,
        int p_sequenceNumber) {
      executor.execute(
          () ->
              ui.newGoalSpec(
                  p_spanType, p_criteriaType, p_span, p_maximum, p_minimum, p_sequenceNumber));
    }

    @Override
    public void setTargetPressed() {
      executor.execute(() -> ui.setTargetPressed());
    }

    @Override
    public void startStopPressed() {
      executor.execute(() -> ui.startStopPressed());
    }

    @Override
    public void setData(double p_value, Unit p_unit) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setIndicator(Indicator p_indicator) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setTime(int p_time) {
      throw new UnsupportedOperationException();
    }
  }
}
