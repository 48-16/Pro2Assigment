import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VinylLibraryApp extends Application {
  private VinylLibrary library;
  private Thread bobThread;
  private Thread wendyThread;

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Initialize the model
    library = new VinylLibrary();

    // Create and start simulation threads
    VinylUser bob = new VinylUser("Bob", library);
    VinylUser wendy = new VinylUser("Wendy", library);

    bobThread = new Thread(bob);
    wendyThread = new Thread(wendy);

    bobThread.setDaemon(true);
    wendyThread.setDaemon(true);

    bobThread.start();
    wendyThread.start();

    // Initialize view and viewmodel
    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainViewFXML.fxml"));
    primaryStage.setScene(new Scene(loader.load()));

    MainViewController controller = loader.getController();
    VinylListViewModel viewModel = new VinylListViewModel(library);
    controller.setViewModel(viewModel);

    primaryStage.setTitle("Vinyl Library");
    primaryStage.show();
  }

  @Override
  public void stop() {
    if (bobThread != null) {
      bobThread.interrupt();
    }
    if (wendyThread != null) {
      wendyThread.interrupt();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}