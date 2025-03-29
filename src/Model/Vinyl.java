package Model;

import ViewModel.Observer;
import java.util.ArrayList;
import java.util.List;

public class Vinyl {
  private String title;
  private String artist;
  private int year;
  private VinylState state;
  private String borrower;
  private String reserver;
  private boolean markedForRemoval;
  private List<Observer> observers;

  // Primary constructor
  public Vinyl(String title, String artist, int year) {
    this.title = title;
    this.artist = artist;
    this.year = year;
    this.observers = new ArrayList<>();
    this.markedForRemoval = false;
    // Ensure state is initialized
    this.setState(new AvailableState(this));
  }

  // No-arg constructor for potential serialization
  public Vinyl() {
    this.observers = new ArrayList<>();
    this.markedForRemoval = false;
    this.setState(new AvailableState(this));
  }

  // Method to update this vinyl with data from server
  public void updateFromServerData(Vinyl serverVinyl) {
    if (serverVinyl == null) {
      return;
    }

    this.artist = serverVinyl.getArtist();
    this.year = serverVinyl.getYear();
    this.borrower = serverVinyl.getBorrower();
    this.reserver = serverVinyl.getReserver();
    this.markedForRemoval = serverVinyl.isMarkedForRemoval();

    // Set the appropriate state based on server data
    String status = serverVinyl.getStatus();
    if (status.contains("Borrowed") && status.contains("Reserved")) {
      this.setState(new BorrowedAndReservedState(this));
    } else if (status.contains("Borrowed")) {
      this.setState(new BorrowedState(this));
    } else if (status.contains("Reserved")) {
      this.setState(new ReservedState(this));
    } else {
      this.setState(new AvailableState(this));
    }

    notifyObservers();
  }

  public void setState(VinylState state) {
    this.state = state;
    notifyObservers();
  }

  // The following methods are kept for compatibility but should not be called directly
  // from the client UI - should use VinylSocketClient instead

  public void borrow(String borrower) {
    if (markedForRemoval && (getReserver() == null || !borrower.equals(getReserver()))) {
      throw new IllegalStateException("Vinyl is marked for removal");
    }
    state.borrow(borrower);
  }

  public void reserve(String reserver) {
    if (markedForRemoval) {
      throw new IllegalStateException("Vinyl is marked for removal");
    }
    state.reserve(reserver);
  }

  public void returnVinyl() {
    state.returnVinyl();
    if (markedForRemoval && getReserver() == null) {
      notifyRemoval();
    }
  }

  // Ensure getStatus always works
  public String getStatus() {
    if (state == null) {
      this.setState(new AvailableState(this));
    }
    return state.getStatus();
  }

  // Getters and Setters
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getArtist() { return artist; }
  public void setArtist(String artist) { this.artist = artist; }

  public int getYear() { return year; }
  public void setYear(int year) { this.year = year; }

  public String getBorrower() { return borrower; }
  public void setBorrower(String borrower) { this.borrower = borrower; }

  public String getReserver() { return reserver; }
  public void setReserver(String reserver) { this.reserver = reserver; }

  public boolean isMarkedForRemoval() { return markedForRemoval; }

  public void markForRemoval() {
    this.markedForRemoval = true;
    notifyObservers();
  }

  // Observer-related methods
  public void addObserver(Observer observer) {
    if (observers == null) {
      observers = new ArrayList<>();
    }
    observers.add(observer);
  }

  public void removeObserver(Observer observer) {
    if (observers != null) {
      observers.remove(observer);
    }
  }

  private void notifyObservers() {
    if (observers != null) {
      for (Observer observer : observers) {
        observer.update();
      }
    }
  }

  private void notifyRemoval() {
    if (observers != null) {
      for (Observer observer : observers) {
        if (observer instanceof VinylLibrary) {
          ((VinylLibrary) observer).removeVinyl(this);
        }
      }
    }
  }

  // Ensure state is always accessible
  public VinylState getState() {
    if (state == null) {
      this.setState(new AvailableState(this));
    }
    return state;
  }
}