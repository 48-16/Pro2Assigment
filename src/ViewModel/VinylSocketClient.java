package ViewModel;

import Model.Vinyl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VinylSocketClient {
  private static VinylSocketClient instance;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private Gson gson;
  private String host;
  private int port;

  private VinylSocketClient() {
    this("localhost", 8888); // Default values
  }

  private VinylSocketClient(String host, int port) {
    this.host = host;
    this.port = port;
    gson = new Gson();
    try {
      socket = new Socket(host, port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static synchronized VinylSocketClient getInstance() {
    return getInstance("localhost", 8888);
  }

  public static synchronized VinylSocketClient getInstance(String host, int port) {
    if (instance == null) {
      instance = new VinylSocketClient(host, port);
    } else {
      // If a different host/port is requested, create a new instance
      if (!instance.host.equals(host) || instance.port != port) {
        instance.close();
        instance = new VinylSocketClient(host, port);
      }
    }
    return instance;
  }


  public CompletableFuture<List<Vinyl>> listVinyls() {
    return CompletableFuture.supplyAsync(() -> {
      JsonObject request = new JsonObject();
      request.addProperty("action", "LIST_VINYLS");
      out.println(gson.toJson(request));

      try {
        String response = in.readLine();
        return gson.fromJson(response, new TypeToken<List<Vinyl>>(){}.getType());
      } catch (IOException e) {
        e.printStackTrace();
        return new ArrayList<>();
      }
    });
  }

  public CompletableFuture<Boolean> borrowVinyl(String title, String borrower) {
    return CompletableFuture.supplyAsync(() -> {
      JsonObject request = new JsonObject();
      request.addProperty("action", "BORROW");
      request.addProperty("title", title);
      request.addProperty("borrower", borrower);
      out.println(gson.toJson(request));

      try {
        String response = in.readLine();
        JsonObject result = gson.fromJson(response, JsonObject.class);
        return result.get("status").getAsString().equals("success");
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    });
  }

  public CompletableFuture<Boolean> reserveVinyl(String title, String reserver) {
    return CompletableFuture.supplyAsync(() -> {
      JsonObject request = new JsonObject();
      request.addProperty("action", "RESERVE");
      request.addProperty("title", title);
      request.addProperty("reserver", reserver);
      out.println(gson.toJson(request));

      try {
        String response = in.readLine();
        JsonObject result = gson.fromJson(response, JsonObject.class);
        return result.get("status").getAsString().equals("success");
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    });
  }

  public CompletableFuture<Boolean> returnVinyl(String title) {
    return CompletableFuture.supplyAsync(() -> {
      JsonObject request = new JsonObject();
      request.addProperty("action", "RETURN");
      request.addProperty("title", title);
      out.println(gson.toJson(request));

      try {
        String response = in.readLine();
        JsonObject result = gson.fromJson(response, JsonObject.class);
        return result.get("status").getAsString().equals("success");
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    });
  }

  public void close() {
    try {
      if (socket != null) socket.close();
      if (in != null) in.close();
      if (out != null) out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

