// State interface
public interface VinylState {
  void borrow(String borrower);
  void reserve(String reserver);
  void returnVinyl();
  String getStatus();
}

