package View;

import Model.Vinyl;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DetailsViewController {
  @FXML
  private Label titleLabel;
  @FXML
  private Label artistLabel;
  @FXML
  private Label yearLabel;
  @FXML
  private Label statusLabel;

  public void setVinyl(Vinyl vinyl) {
    if (vinyl != null) {
      titleLabel.setText(vinyl.getTitle());
      artistLabel.setText(vinyl.getArtist());
      yearLabel.setText(String.valueOf(vinyl.getYear()));
      statusLabel.setText(vinyl.getStatus());
    }
  }
}