package Model;

import ViewModel.Observer;

import java.util.ArrayList;
import java.util.List;

public class VinylLibrary {
  private List<Vinyl> vinyls;
  private List<Observer> observers;

  public VinylLibrary() {
    this.vinyls = new ArrayList<>();
    this.observers = new ArrayList<>();
    initializeDefaultVinyls();
  }

  private void initializeDefaultVinyls() {
    addVinyl(new Vinyl("test", "Lindemann", 1999));
    addVinyl(new Vinyl("test2", "sasha", 2000));
    addVinyl(new Vinyl("test3", "slava", 2001));
    addVinyl(new Vinyl("test4", "kristof", 2002));
    addVinyl(new Vinyl("test5", "Oksi", 2003));

  }

  public void addVinyl(Vinyl vinyl) {
    if (vinyls.size() < 10) {
      vinyls.add(vinyl);
      vinyl.addObserver(() -> notifyObservers());
      notifyObservers();
    }
  }

  public void removeVinyl(Vinyl vinyl) {
    vinyls.remove(vinyl);
    notifyObservers();
  }

  public List<Vinyl> getVinyls() {
    return new ArrayList<>(vinyls);
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
}