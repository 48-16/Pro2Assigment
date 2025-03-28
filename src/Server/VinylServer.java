package Server;

import Model.Vinyl;
import Model.VinylLibrary;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VinylServer {
  private static VinylLibrary library;
  private ServerSocket serverSocket;
  private ExecutorService threadPool;
  private static Logger logger; // Logger instance

  public VinylServer(int port) {
    library = new VinylLibrary();
    logger = Logger.getInstance(); // Initialize logger

    try {
      serverSocket = new ServerSocket(port);
      threadPool = Executors.newCachedThreadPool();
      logger.log("Vinyl Server started on port " + port);
    } catch (IOException e) {
      logger.log("Error starting server: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void start() {
    try {
      while (!serverSocket.isClosed()) {
        Socket clientSocket = serverSocket.accept();
        logger.log("New client connected: " + clientSocket.getInetAddress());
        threadPool.execute(new ClientHandler(clientSocket));
      }
    } catch (IOException e) {
      logger.log("Server connection error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson;

    public ClientHandler(Socket socket) {
      this.clientSocket = socket;
      this.gson = new Gson();

      try {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
      } catch (IOException e) {
        logger.log("Error setting up client handler: " + e.getMessage());
        e.printStackTrace();
      }
    }

    @Override
    public void run() {
      try {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          processRequest(inputLine);
        }
      } catch (IOException e) {
        logger.log("Client communication error: " + e.getMessage());
        e.printStackTrace();
      } finally {
        try {
          clientSocket.close();
          logger.log("Client connection closed: " + clientSocket.getInetAddress());
        } catch (IOException e) {
          logger.log("Error closing client socket: " + e.getMessage());
          e.printStackTrace();
        }
      }
    }

    private void processRequest(String requestJson) {
      try {
        JsonObject request = gson.fromJson(requestJson, JsonObject.class);
        String action = request.get("action").getAsString();

        switch (action) {
          case "LIST_VINYLS":
            logger.log("Received LIST_VINYLS request");
            sendVinylList();
            break;
          case "BORROW":
            String borrowTitle = request.get("title").getAsString();
            String borrower = request.get("borrower").getAsString();
            logger.log("Received BORROW request: Vinyl '" + borrowTitle + "' by " + borrower);
            borrowVinyl(request);
            break;
          case "RESERVE":
            String reserveTitle = request.get("title").getAsString();
            String reserver = request.get("reserver").getAsString();
            logger.log("Received RESERVE request: Vinyl '" + reserveTitle + "' by " + reserver);
            reserveVinyl(request);
            break;
          case "RETURN":
            String returnTitle = request.get("title").getAsString();
            logger.log("Received RETURN request: Vinyl '" + returnTitle + "'");
            returnVinyl(request);
            break;
        }
      } catch (Exception e) {
        logger.log("Error processing request: " + e.getMessage());
        sendErrorResponse("Invalid request: " + e.getMessage());
      }
    }

    private void sendVinylList() {
      List<Vinyl> vinyls = library.getVinyls();
      String jsonResponse = gson.toJson(vinyls);
      out.println(jsonResponse);
      logger.log("Sent vinyl list to client");
    }

    private void borrowVinyl(JsonObject request) {
      try {
        String title = request.get("title").getAsString();
        String borrower = request.get("borrower").getAsString();

        Vinyl vinylToBorrow = library.getVinyls().stream()
            .filter(v -> v.getTitle().equals(title))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Vinyl not found"));

        vinylToBorrow.borrow(borrower);

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.addProperty("message", "Vinyl borrowed successfully");
        out.println(gson.toJson(response));

        logger.log("Vinyl '" + title + "' successfully borrowed by " + borrower);
      } catch (IllegalStateException | IllegalArgumentException e) {
        logger.log("Borrow error: " + e.getMessage());
        sendErrorResponse(e.getMessage());
      }
    }

    private void reserveVinyl(JsonObject request) {
      try {
        String title = request.get("title").getAsString();
        String reserver = request.get("reserver").getAsString();

        Vinyl vinylToReserve = library.getVinyls().stream()
            .filter(v -> v.getTitle().equals(title))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Vinyl not found"));

        vinylToReserve.reserve(reserver);

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.addProperty("message", "Vinyl reserved successfully");
        out.println(gson.toJson(response));

        logger.log("Vinyl '" + title + "' successfully reserved by " + reserver);
      } catch (IllegalStateException | IllegalArgumentException e) {
        logger.log("Reserve error: " + e.getMessage());
        sendErrorResponse(e.getMessage());
      }
    }

    private void returnVinyl(JsonObject request) {
      try {
        String title = request.get("title").getAsString();

        Vinyl vinylToReturn = library.getVinyls().stream()
            .filter(v -> v.getTitle().equals(title))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Vinyl not found"));

        vinylToReturn.returnVinyl();

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.addProperty("message", "Vinyl returned successfully");
        out.println(gson.toJson(response));

        logger.log("Vinyl '" + title + "' successfully returned");
      } catch (IllegalStateException | IllegalArgumentException e) {
        logger.log("Return error: " + e.getMessage());
        sendErrorResponse(e.getMessage());
      }
    }

    private void sendErrorResponse(String errorMessage) {
      JsonObject errorResponse = new JsonObject();
      errorResponse.addProperty("status", "error");
      errorResponse.addProperty("message", errorMessage);
      out.println(gson.toJson(errorResponse));
      logger.log("Sent error response: " + errorMessage);
    }
  }

  public static void main(String[] args) {
    VinylServer server = new VinylServer(8888);
    server.start();
  }
}