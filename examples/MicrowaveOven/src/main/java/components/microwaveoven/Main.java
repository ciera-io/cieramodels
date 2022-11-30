package components.microwaveoven;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ciera.runtime.SimpleScheduler;
import io.ciera.runtime.api.Architecture;

public class Main {
  
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    logger.debug("MAIN: loading config");
    if (args.length > 0 && Files.exists(Path.of(args[0]))) {
      Architecture.getInstance().loadConfig(Path.of(args[0]));
    }

    // create domain
    logger.debug("MAIN: creating domains");
    final MicrowaveOven mo = new MicrowaveOven();

    // initialize domain
    logger.debug("MAIN: initializing domains");
    mo.initialize();

    // schedule tasks forever in a single thread
    logger.debug("MAIN: starting up...");
    final Iterator<Runnable> scheduler = new SimpleScheduler(mo);
    while (scheduler.hasNext()) {
      scheduler.next().run();
    }
    logger.debug("MAIN: shutting down...");
  }
}
