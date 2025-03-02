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

  public Vinyl(String title, String artist, int year) {
    this.title = title;
    this.artist = artist;
    this.year = year;
    this.state = new AvailableState(this);
    this.observers = new ArrayList<>();
    this.markedForRemoval = false;
  }

  public void setState(VinylState state) {
    this.state = state;
    notifyObservers();
  }

  public void borrow(String borrower) {
    if (markedForRemoval && (getReserver() == null || !borrower.equals(getReserver()))) {
      throw new IllegalStateException("Model.Vinyl is marked for removal");
    }
    state.borrow(borrower);
  }

  public void reserve(String reserver) {
    if (markedForRemoval) {
      throw new IllegalStateException("Model.Vinyl is marked for removal");
    }
    state.reserve(reserver);
  }

  public void returnVinyl() {
    state.returnVinyl();
    if (markedForRemoval && getReserver() == null) {
      notifyRemoval();
    }
  }

  public String getTitle() { return title; }
  public String getArtist() { return artist; }
  public int getYear() { return year; }
  public String getBorrower() { return borrower; }
  public void setBorrower(String borrower) { this.borrower = borrower; }
  public String getReserver() { return reserver; }
  public void setReserver(String reserver) { this.reserver = reserver; }
  public String getStatus() { return state.getStatus(); }
  public boolean isMarkedForRemoval() { return markedForRemoval; }

  public void markForRemoval() {
    this.markedForRemoval = true;
    notifyObservers();
  }

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  private void notifyObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
  }

  private void notifyRemoval() {
    for (Observer observer : observers) {
      if (observer instanceof VinylLibrary) {
        ((VinylLibrary) observer).removeVinyl(this);
      }
    }
  }
}


