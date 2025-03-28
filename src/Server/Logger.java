package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
  private static Logger instance;
  private PrintWriter fileWriter;
  private static final String LOG_DIRECTORY = "logs/";

  private Logger() {
    try {
      String filename = LOG_DIRECTORY + "server_log_" +
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
      fileWriter = new PrintWriter(new FileWriter(filename, true));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static synchronized Logger getInstance() {
    if (instance == null) {
      instance = new Logger();
    }
    return instance;
  }

  public void log(String message) {
    String timestamp = LocalDateTime.now().toString();
    String logEntry = timestamp + " - " + message;

    System.out.println(logEntry);  // Console log

    fileWriter.println(logEntry);
    fileWriter.flush();
  }

  public void close() {
    if (fileWriter != null) {
      fileWriter.close();
    }
  }
}