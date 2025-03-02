package Model;

import java.util.List;
import java.util.Random;

public class VinylUser implements Runnable {
  private String name;
  private VinylLibrary library;
  private Random random;

  public VinylUser(String name, VinylLibrary library) {
    this.name = name;
    this.library = library;
    this.random = new Random();
  }

  @Override
  public void run() {
    while (!Thread.interrupted()) {
      try {
        Thread.sleep(random.nextInt(3000) + 2000);

        List<Vinyl> vinyls = library.getVinyls();
        if (!vinyls.isEmpty()) {
          Vinyl randomVinyl = vinyls.get(random.nextInt(vinyls.size()));

          int action = random.nextInt(3);
          try {
            switch (action) {
              case 0:
                randomVinyl.reserve(name);
                break;
              case 1:
                randomVinyl.borrow(name);
                break;
              case 2:
                if (name.equals(randomVinyl.getBorrower())) {
                  randomVinyl.returnVinyl();
                }
                break;
            }
          } catch (IllegalStateException e) {
          }
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }
}