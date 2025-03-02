package View;

import Model.SharedVinylState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class MainViewController {
  @FXML
  private TableView<VinylViewModel> vinylTable;
  @FXML
  private TableColumn<VinylViewModel, String> titleColumn;
  @FXML
  private TableColumn<VinylViewModel, String> artistColumn;
  @FXML
  private TableColumn<VinylViewModel, String> statusColumn;

  private VinylListViewModel viewModel;
  private SharedVinylState sharedState;

  public void initialize() {
    sharedState = SharedVinylState.getInstance();

    titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
    statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

    vinylTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        sharedState.setSelectedVinyl(newVal.getVinyl());
      }
    });
  }

  public void setViewModel(VinylListViewModel viewModel) {
    this.viewModel = viewModel;
    vinylTable.itemsProperty().bind(viewModel.vinylsProperty());
  }

  @FXML
  private void onReserve() {
    VinylViewModel selected = vinylTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      try {

        selected.getVinyl().reserve("Manual User");
      } catch (IllegalStateException e) {
        showError(e.getMessage());
      }
    }
  }

  @FXML
  private void onBorrow() {
    VinylViewModel selected = vinylTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      try {
        selected.getVinyl().borrow("Manual User");
      } catch (IllegalStateException e) {
        showError(e.getMessage());
      }
    }
  }

  @FXML
  private void onReturn() {
    VinylViewModel selected = vinylTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      try {
        selected.getVinyl().returnVinyl();
      } catch (IllegalStateException e) {
        showError(e.getMessage());
      }
    }
  }

  @FXML
  private void onRemove() {
    VinylViewModel selected = vinylTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      selected.getVinyl().markForRemoval();
    }
  }

  @FXML
  private void showDetails() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("View/DetailsViewFXML.fxml"));
      Stage detailsStage = new Stage();
      detailsStage.initModality(Modality.APPLICATION_MODAL);
      detailsStage.setTitle("Model.Vinyl Details");
      detailsStage.setScene(new Scene(loader.load()));

      DetailsViewController controller = loader.getController();
      controller.setVinyl(sharedState.getSelectedVinyl());

      detailsStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}