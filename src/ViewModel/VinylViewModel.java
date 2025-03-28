package ViewModel;

import Model.Vinyl;
import javafx.beans.property.SimpleStringProperty;

public class VinylViewModel {
  private transient Vinyl vinyl; // Add 'transient' keyword
  private SimpleStringProperty title;
  private SimpleStringProperty artist;
  private SimpleStringProperty status;

  public VinylViewModel(Vinyl vinyl) {
    this.vinyl = vinyl;
    this.title = new SimpleStringProperty(vinyl.getTitle());
    this.artist = new SimpleStringProperty(vinyl.getArtist());
    this.status = new SimpleStringProperty(vinyl.getStatus());

    if (vinyl.isMarkedForRemoval()) {
      this.status.set(this.status.get() + " (Marked for removal)");
    }
  }

  public SimpleStringProperty titleProperty() { return title; }
  public SimpleStringProperty artistProperty() { return artist; }
  public SimpleStringProperty statusProperty() { return status; }

  // Remove direct Vinyl access or modify to return only necessary info
  public String getTitle() { return vinyl.getTitle(); }
  public String getArtist() { return vinyl.getArtist(); }
  public String getStatus() { return vinyl.getStatus(); }
  public Vinyl getVinyl() { return vinyl; }
}