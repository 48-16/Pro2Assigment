package Model;

public class ReservedState implements VinylState {
  private final Vinyl vinyl;

  public ReservedState(Vinyl vinyl) {
    this.vinyl = vinyl;
  }

  @Override
  public void borrow(String borrower) {
    if (borrower.equals(vinyl.getReserver())) {
      vinyl.setBorrower(borrower);
      vinyl.setReserver(null);
      vinyl.setState(new BorrowedState(vinyl));
    } else {
      throw new IllegalStateException("Model.Vinyl is reserved by someone else");
    }
  }

  @Override
  public void reserve(String reserver) {
    throw new IllegalStateException("Model.Vinyl is already reserved");
  }

  @Override
  public void returnVinyl() {
  }

  @Override
  public String getStatus() {
    return "Reserved by " + vinyl.getReserver();
  }
}
