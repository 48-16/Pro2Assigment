package Model;

public class AvailableState implements VinylState {
  private final Vinyl vinyl;

  public AvailableState(Vinyl vinyl) {
    this.vinyl = vinyl;
  }

  @Override
  public void borrow(String borrower) {
    vinyl.setBorrower(borrower);
    vinyl.setState(new BorrowedState(vinyl));
  }

  @Override
  public void reserve(String reserver) {
    vinyl.setReserver(reserver);
    vinyl.setState(new ReservedState(vinyl));
  }

  @Override
  public void returnVinyl() {
  }

  @Override
  public String getStatus() {
    return "Available";
  }
}
