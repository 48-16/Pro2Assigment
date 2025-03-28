package ViewModel;

import Model.Vinyl;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.List;

public class VinylListViewModel implements Observer {
  private SimpleListProperty<VinylViewModel> vinyls;
  private VinylSocketClient socketClient;

  public VinylListViewModel() {
    this.socketClient = VinylSocketClient.getInstance();
    this.vinyls = new SimpleListProperty<>(FXCollections.observableArrayList());
    updateVinylList();
  }

  private void updateVinylList() {
    socketClient.listVinyls().thenAccept(vinylList -> {
      Platform.runLater(() -> {
        vinyls.clear();
        for (Vinyl vinyl : vinylList) {
          vinyls.add(new VinylViewModel(vinyl));
        }
      });
    });
  }

  @Override
  public void update() {
    updateVinylList();
  }

  public SimpleListProperty<VinylViewModel> vinylsProperty() {
    return vinyls;
  }

  public void borrowVinyl(VinylViewModel vinyl) {
    socketClient.borrowVinyl(vinyl.getVinyl().getTitle(), "Manual User")
        .thenAccept(success -> {
          if (success) {
            updateVinylList();
          }
        });
  }

  public void reserveVinyl(VinylViewModel vinyl) {
    socketClient.reserveVinyl(vinyl.getVinyl().getTitle(), "Manual User")
        .thenAccept(success -> {
          if (success) {
            updateVinylList();
          }
        });
  }

  public void returnVinyl(VinylViewModel vinyl) {
    socketClient.returnVinyl(vinyl.getVinyl().getTitle())
        .thenAccept(success -> {
          if (success) {
            updateVinylList();
          }
        });
  }
}