package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
  private static Logger instance;
  private PrintWriter fileWriter;
  private static final String LOG_DIRECTORY = "logs";

  private Logger() {
    try {
      // Create logs directory if it doesn't exist
      File logDir = new File(LOG_DIRECTORY);
      if (!logDir.exists()) {
        logDir.mkdirs();
      }

      String filename = LOG_DIRECTORY + File.separator + "server_log_" +
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";

      File logFile = new File(filename);

      // Ensure the file exists
      if (!logFile.exists()) {
        logFile.createNewFile();
      }

      fileWriter = new PrintWriter(new FileWriter(logFile, true));
    } catch (IOException e) {
      // Fallback logging if file creation fails
      System.err.println("Error initializing logger: " + e.getMessage());
      e.printStackTrace();

      // Create a fallback PrintWriter to System.out to ensure logging still works
      fileWriter = new PrintWriter(System.out, true);
    }
  }

  public static synchronized Logger getInstance() {
    if (instance == null) {
      instance = new Logger();
    }
    return instance;
  }

  public void log(String message) {
    try {
      String timestamp = LocalDateTime.now().toString();
      String logEntry = timestamp + " - " + message;

      System.out.println(logEntry);  // Console log

      if (fileWriter != null) {
        fileWriter.println(logEntry);
        fileWriter.flush();
      } else {
        System.err.println("Logger file writer is null. Unable to log: " + logEntry);
      }
    } catch (Exception e) {
      System.err.println("Error logging message: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void close() {
    if (fileWriter != null) {
      fileWriter.close();
    }
  }
}