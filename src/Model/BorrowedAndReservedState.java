package Model;

public class BorrowedAndReservedState implements VinylState {
  private final Vinyl vinyl;

  public BorrowedAndReservedState(Vinyl vinyl) {
    this.vinyl = vinyl;
  }

  @Override
  public void borrow(String borrower) {
    throw new IllegalStateException("Model.Vinyl is already borrowed");
  }

  @Override
  public void reserve(String reserver) {
    throw new IllegalStateException("Model.Vinyl is already reserved");
  }

  @Override
  public void returnVinyl() {
    vinyl.setBorrower(null);
    vinyl.setState(new ReservedState(vinyl));
  }

  @Override
  public String getStatus() {
    return "Borrowed by " + vinyl.getBorrower() + " and Reserved by " + vinyl.getReserver();
  }
}
