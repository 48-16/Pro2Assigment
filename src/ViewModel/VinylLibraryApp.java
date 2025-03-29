package ViewModel;

import Model.VinylLibrary;
import View.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VinylLibraryApp extends Application {
  private String host = "localhost";
  private int port = 8888;

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Parse parameters if provided
    Parameters params = getParameters();
    if (!params.getRaw().isEmpty()) {
      if (params.getRaw().size() >= 1) {
        host = params.getRaw().get(0);
      }
      if (params.getRaw().size() >= 2) {
        port = Integer.parseInt(params.getRaw().get(1));
      }
    }

    // Initialize socket client with specific host and port
    VinylSocketClient.getInstance(host, port);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainViewFXML.fxml"));
    primaryStage.setScene(new Scene(loader.load()));

    MainViewController controller = loader.getController();
    VinylLibrary lib = new VinylLibrary();
    // Remove lib.initializeDefaultVinyls(); - We'll rely on server data
    VinylListViewModel viewModel = new VinylListViewModel(lib);
    controller.setViewModel(viewModel);

    primaryStage.setTitle("Networked Vinyl Library");
    primaryStage.show();
  }

  @Override
  public void stop() {
    // Close socket connection
    VinylSocketClient.getInstance().close();
  }

  public static void main(String[] args) {
    launch(args);
  }
}