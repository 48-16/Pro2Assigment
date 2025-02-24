import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

// Shared ViewState
public class SharedVinylState {
  private static SharedVinylState instance;
  private Vinyl selectedVinyl;

  private SharedVinylState() {}

  public static SharedVinylState getInstance() {
    if (instance == null) {
      instance = new SharedVinylState();
    }
    return instance;
  }

  public void setSelectedVinyl(Vinyl vinyl) {
    this.selectedVinyl = vinyl;
  }

  public Vinyl getSelectedVinyl() {
    return selectedVinyl;
  }
}

// Main ViewModel

