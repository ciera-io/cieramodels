package gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import io.ciera.runtime.api.Domain;
import io.ciera.runtime.api.Event;
import io.ciera.runtime.api.EventTarget;
import io.ciera.runtime.api.Port;
import io.ciera.runtime.api.Timer;
import ui.shared.GoalCriteria;
import ui.shared.GoalSpan;
import ui.shared.IUI;
import ui.shared.Indicator;
import ui.shared.Unit;

public class AsciiWatchGui implements WatchGui, IUI {

  private static final int[][] DIGIT_COORDS = {
    new int[] {14, 6}, new int[] {17, 6}, new int[] {21, 6}, new int[] {24, 6}
  };

  private static final int DATA_X = 14;
  private static final int DATA_Y = 5;
  private static final int DATA_LEN = 13;

  private static final String[] UNIT_LABELS =
      new String[] {
        "km", "meters", "min/km", "km/h", "miles", "yds", "ft", "min/mile", "mph", "bpm", "laps"
      };

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TextImage[] numerals;
  private final TextImage watchFace;

  private Terminal terminal;
  private TextGraphics watchGraphics;

  private final IUI ui;

  public AsciiWatchGui(IUI ui) {
    // set to headless mode
    System.setProperty("java.awt.headless", "true");

    // set the gui connection
    this.ui = ui;

    // load resources
    numerals = loadNumerals();
    watchFace = loadWatchFace();

    try {
      // create terminal
      terminal = new DefaultTerminalFactory().createTerminal();
      terminal.setCursorVisible(false);
      terminal.clearScreen();

      // draw watch graphics
      watchGraphics = terminal.newTextGraphics();
      watchGraphics.drawImage(TerminalPosition.TOP_LEFT_CORNER, watchFace);

      // set up logger and standard error
      OutputStream out = new TerminalOutputStream(terminal, 0, watchFace.getSize().getRows());
      System.setErr(new PrintStream(out));

    } catch (IOException e) {
      /* do nothing */ }
  }

  @Override
  public void display() {
    setTime(0);
    setData(0, Unit.METERS);
    char command = ' ';
    while (command != 'x') {
      try {
        command = terminal.readInput().getCharacter();
        switch (command) {
          case 's':
            ui.startStopPressed();
            logger.debug("Sending start/stop");
            break;
          case 'r':
            ui.lapResetPressed();
            logger.debug("Sending reset/lap");
            break;
          case 'm':
            ui.modePressed();
            logger.debug("Sending mode");
            break;
        }
      } catch (IOException e) {
        command = 'x';
      }
    }
    logger.debug("Exiting...");

    // clear screen and exit
    try {
      terminal.setCursorPosition(0, 0);
      terminal.setCursorVisible(true);
      terminal.clearScreen();
    } catch (IOException e) {
      /* do nothing */ }
    System.exit(0);
  }

  @Override
  public void setData(double value, Unit unit) {
    String valueString = "";
    String unitString = UNIT_LABELS[unit.ordinal()];
    switch (unit) {
      case KM:
      case MILES:
      case KMPERHOUR:
      case MPH:
        valueString = String.format("%.2f", value);
        break;
      case METERS:
      case YARDS:
      case FEET:
      case BPM:
      case LAPS:
        valueString = String.format("%d", (int) value);
        break;
      case MINPERKM:
      case MINPERMILE:
        valueString = String.format("%d:%02d", (int) value % 60, (int) (60 * value) % 60);
        break;
      default:
        break;
    }
    int padding = DATA_LEN - (valueString.length() + unitString.length());
    if (padding < 1) {
      padding = 1;
    }
    String data = valueString;
    for (int i = 0; i < padding; i++) {
      data += " ";
    }
    data += unitString;
    if (data.length() > DATA_LEN) {
      data = data.substring(0, DATA_LEN);
    }
    while (data.length() < DATA_LEN) {
      data += " ";
    }
    watchGraphics.putString(new TerminalPosition(DATA_X, DATA_Y), data);
  }

  @Override
  public void setTime(int time) {
    int min = (time % 3600) / 60;
    int sec = time % 60;
    watchGraphics.drawImage(
        new TerminalPosition(DIGIT_COORDS[0][0], DIGIT_COORDS[0][1]), numerals[(min / 10) % 10]);
    watchGraphics.drawImage(
        new TerminalPosition(DIGIT_COORDS[1][0], DIGIT_COORDS[1][1]), numerals[min % 10]);
    watchGraphics.drawImage(
        new TerminalPosition(DIGIT_COORDS[2][0], DIGIT_COORDS[2][1]), numerals[(sec / 10) % 10]);
    watchGraphics.drawImage(
        new TerminalPosition(DIGIT_COORDS[3][0], DIGIT_COORDS[3][1]), numerals[sec % 10]);
  }

  @Override
  public void setIndicator(Indicator value) {
    // not implemented
  }

  private TextImage[] loadNumerals() {
    List<TextImage> numerals = new ArrayList<>();
    List<String> lines = new ArrayList<>();
    ;
    try {
      Scanner sc = new Scanner(getClass().getModule().getResourceAsStream("gui/txt/numbers.txt"));
      while (sc.hasNextLine()) {
        if (lines.size() >= 3) {
          numerals.add(fromLines(lines));
          lines = new ArrayList<>();
        }
        lines.add(sc.nextLine());
      }
      numerals.add(fromLines(lines));
      sc.close();
    } catch (IOException e) {
      /* do nothing */ }
    return numerals.toArray(new TextImage[0]);
  }

  private TextImage loadWatchFace() {
    List<String> lines = new ArrayList<>();
    try {
      Scanner sc = new Scanner(getClass().getModule().getResourceAsStream("gui/txt/watchface.txt"));
      while (sc.hasNextLine()) {
        lines.add(sc.nextLine());
      }
      sc.close();
    } catch (IOException e) {
      /* do nothing */ }
    return fromLines(lines);
  }

  private TextImage fromLines(List<String> lines) {
    int numColumns = lines.stream().max((a, b) -> a.length() - b.length()).get().length();
    int numLines = lines.size();
    TextImage newImage = new BasicTextImage(numColumns, numLines);
    newImage.setAll(TextCharacter.fromCharacter(' ')[0]);
    for (int y = 0; y < lines.size(); y++) {
      for (int x = 0; x < lines.get(y).length(); x++) {
        newImage.setCharacterAt(x, y, TextCharacter.fromCharacter(lines.get(y).charAt(x))[0]);
      }
    }
    return newImage;
  }

  private class TerminalOutputStream extends OutputStream {

    private final int initCursorX;
    private int cursorX;
    private int cursorY;
    private Terminal terminal;
    private StringBuilder buffer;

    public TerminalOutputStream(Terminal t, int x, int y) {
      terminal = t;
      initCursorX = x;
      cursorX = x;
      cursorY = y;
      buffer = new StringBuilder();
    }

    @Override
    public void write(int b) {
      char c = (char) b;
      buffer.append(c);
      if (c == '\n') {
        try {
          flush();
        } catch (IOException e) {
          // do nothing
        }
      }
    }

    @Override
    public void flush() throws IOException {
      TextGraphics tg = terminal.newTextGraphics();
      tg.putCSIStyledString(cursorX, cursorY, buffer.toString());
      if (buffer.indexOf("\n") != -1) {
        cursorX = initCursorX;
        cursorY++;
      } else {
        cursorX += buffer.length();
      }
      buffer = new StringBuilder();
    }
  }

  @Override
  public void setPeer(Port peer) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Port getPeer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void generate(Function<Object[], Event> eventBuilder, EventTarget target, Object... data) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void generateAccelerated(
      Function<Object[], Event> eventBuilder, EventTarget target, Object... data) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Timer schedule(
      Function<Object[], Event> eventBuilder, EventTarget target, Duration delay, Object... data) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Timer schedule(
      Function<Object[], Event> eventBuilder,
      EventTarget target,
      Instant expiration,
      Object... data) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Timer scheduleRecurring(
      Function<Object[], Event> eventBuilder,
      EventTarget target,
      Duration delay,
      Duration period,
      Object... data) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Timer scheduleRecurring(
      Function<Object[], Event> eventBuilder,
      EventTarget target,
      Instant expiration,
      Duration period,
      Object... data) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Domain getDomain() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void lapResetPressed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void lightPressed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void modePressed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void newGoalSpec(
      GoalSpan p_spanType,
      GoalCriteria p_criteriaType,
      double p_span,
      double p_maximum,
      double p_minimum,
      int p_sequenceNumber) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTargetPressed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void startStopPressed() {
    throw new UnsupportedOperationException();
  }
}
