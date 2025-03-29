package Model;

import ViewModel.Observer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VinylLibrary implements Observer {
  private List<Vinyl> vinyls;
  private List<Observer> observers;

  public VinylLibrary() {
    // Initialize lists to empty ArrayLists, never null
    this.vinyls = new ArrayList<>();
    this.observers = new ArrayList<>();
  }

  // Defensive copy getter
  public List<Vinyl> getVinyls() {
    // Return a new ArrayList to prevent direct modification
    return new ArrayList<>(vinyls != null ? vinyls : new ArrayList<>());
  }

  public void initializeDefaultVinyls() {
    // Ensure method works even if list is somehow null
    if (vinyls == null) {
      vinyls = new ArrayList<>();
    }

    // This is now only used for initial setup or fallback
    // The actual data should come from the server
    if (vinyls.isEmpty()) {
      addVinyl(new Vinyl("test", "Lindemann", 1999));
      addVinyl(new Vinyl("test2", "sasha", 2000));
      addVinyl(new Vinyl("test3", "slava", 2001));
      addVinyl(new Vinyl("test4", "kristof", 2002));
      addVinyl(new Vinyl("test5", "Oksi", 2003));
    }
  }

  // Add a method to update from server data
  public void updateFromServerData(List<Vinyl> serverVinyls) {
    if (serverVinyls == null) {
      return;
    }

    // Create a map of existing vinyls by title for quick lookup
    Map<String, Vinyl> existingVinyls = new HashMap<>();
    if (vinyls != null) {
      for (Vinyl vinyl : vinyls) {
        existingVinyls.put(vinyl.getTitle(), vinyl);
      }
    } else {
      vinyls = new ArrayList<>();
    }

    // Create a new list to hold updated vinyls
    List<Vinyl> updatedVinyls = new ArrayList<>();

    // Process server vinyls
    for (Vinyl serverVinyl : serverVinyls) {
      String title = serverVinyl.getTitle();
      if (existingVinyls.containsKey(title)) {
        // Update existing vinyl with server data
        Vinyl existingVinyl = existingVinyls.get(title);
        existingVinyl.updateFromServerData(serverVinyl);
        updatedVinyls.add(existingVinyl);
      } else {
        // Add new vinyl from server
        serverVinyl.addObserver(() -> notifyObservers());
        updatedVinyls.add(serverVinyl);
      }
    }

    // Replace the vinyls list with updated list
    vinyls = updatedVinyls;
    notifyObservers();
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

  @Override
  public void update() {
    notifyObservers();
  }
}