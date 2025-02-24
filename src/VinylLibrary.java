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
    addVinyl(new Vinyl("Dark Side of the Moon", "Pink Floyd", 1973));
    addVinyl(new Vinyl("Thriller", "Michael Jackson", 1982));
    addVinyl(new Vinyl("Abbey Road", "The Beatles", 1969));
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