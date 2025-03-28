package ViewModel;

import Model.SharedVinylState;
import Model.Vinyl;
import Model.VinylLibrary;
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

    // Ensure vinyls is always initialized
    this.vinyls = new SimpleListProperty<>(FXCollections.observableArrayList());
    this.sharedState = SharedVinylState.getInstance();
    updateVinylList();
  }

  private void updateVinylList() {
    Platform.runLater(() -> {
      // Null-safe list update
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
    // Ensure property is never null
    return vinyls != null ? vinyls : new SimpleListProperty<>(FXCollections.observableArrayList());
  }
}