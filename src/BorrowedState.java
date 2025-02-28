public class BorrowedState implements VinylState {
  private final Vinyl vinyl;

  public BorrowedState(Vinyl vinyl) {
    this.vinyl = vinyl;
  }

  @Override
  public void borrow(String borrower) {
    throw new IllegalStateException("Vinyl is already borrowed");
  }

  @Override
  public void reserve(String reserver) {
    vinyl.setReserver(reserver);
    vinyl.setState(new BorrowedAndReservedState(vinyl));
  }

  @Override
  public void returnVinyl() {
    vinyl.setBorrower(null);
    vinyl.setState(new AvailableState(vinyl));
  }

  @Override
  public String getStatus() {
    return "Borrowed by " + vinyl.getBorrower();
  }
}
