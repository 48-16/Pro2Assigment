package Model;

import ViewModel.Observer;
import java.util.ArrayList;
import java.util.List;

public class VinylLibrary {
  private List<Vinyl> vinyls;
  private List<Observer> observers;

  public VinylLibrary() {
    // Initialize lists to empty ArrayLists, never null
    this.vinyls = new ArrayList<>();
    this.observers = new ArrayList<>();
    initializeDefaultVinyls();
  }

  // Defensive copy getter
  public List<Vinyl> getVinyls() {
    // Return a new ArrayList to prevent direct modification
    return new ArrayList<>(vinyls != null ? vinyls : new ArrayList<>());
  }

  public  void initializeDefaultVinyls() {
    // Ensure method works even if list is somehow null
    if (vinyls == null) {
      vinyls = new ArrayList<>();
    }

    addVinyl(new Vinyl("test", "Lindemann", 1999));
    addVinyl(new Vinyl("test2", "sasha", 2000));
    addVinyl(new Vinyl("test3", "slava", 2001));
    addVinyl(new Vinyl("test4", "kristof", 2002));
    addVinyl(new Vinyl("test5", "Oksi", 2003));
  }

  public void addVinyl(Vinyl vinyl) {
    // Ensure list is initialized
    if (vinyls == null) {
      vinyls = new ArrayList<>();
    }

    if (vinyls.size() < 10) {
      vinyls.add(vinyl);
      vinyl.addObserver(() -> notifyObservers());
      notifyObservers();
    }
  }

  public void removeVinyl(Vinyl vinyl) {
    // Null-safe removal
    if (vinyls != null) {
      vinyls.remove(vinyl);
      notifyObservers();
    }
  }

  public void addObserver(Observer observer) {
    // Ensure observers list is initialized
    if (observers == null) {
      observers = new ArrayList<>();
    }
    observers.add(observer);
  }

  public void removeObserver(Observer observer) {
    // Null-safe removal
    if (observers != null) {
      observers.remove(observer);
    }
  }

  private void notifyObservers() {
    // Null-safe notification
    if (observers != null) {
      for (Observer observer : observers) {
        observer.update();
      }
    }
  }
}