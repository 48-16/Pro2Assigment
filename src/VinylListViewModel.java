import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class VinylListViewModel implements Observer {
  private VinylLibrary library;
  private SimpleListProperty<VinylViewModel> vinyls;
  private SharedVinylState sharedState;

  public VinylListViewModel(VinylLibrary library) {
    this.library = library;
    this.library.addObserver(this);
    this.vinyls = new SimpleListProperty<>(FXCollections.observableArrayList());
    this.sharedState = SharedVinylState.getInstance();
    updateVinylList();
  }

  private void updateVinylList() {
    Platform.runLater(() -> {
      vinyls.clear();
      for (Vinyl vinyl : library.getVinyls()) {
        vinyls.add(new VinylViewModel(vinyl));
      }
    });
  }

  @Override
  public void update() {
    updateVinylList();
  }

  public SimpleListProperty<VinylViewModel> vinylsProperty() {
    return vinyls;
  }
}
