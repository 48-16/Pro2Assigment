package ViewModel;

import View.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VinylLibraryApp extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainViewFXML.fxml"));
    primaryStage.setScene(new Scene(loader.load()));

    MainViewController controller = loader.getController();
    VinylListViewModel viewModel = new VinylListViewModel();
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