package View;

import Model.SharedVinylState;
import Model.Vinyl;
import Server.Logger;
import ViewModel.VinylListViewModel;
import ViewModel.VinylViewModel;
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
  private Logger logger;

  public void initialize() {
    sharedState = SharedVinylState.getInstance();
    logger = Logger.getInstance();

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
        logger.log("Manual User reserving vinyl: " + selected.getTitle());
        selected.getVinyl().reserve("Manual User");
        logger.log("Vinyl reserved successfully: " + selected.getTitle());
      } catch (IllegalStateException e) {
        logger.log("Reserve failed: " + e.getMessage());
        showError(e.getMessage());
      }
    } else {
      logger.log("Reserve attempted without selecting a vinyl");
      showError("Please select a vinyl to reserve");
    }
  }

  @FXML
  private void onBorrow() {
    VinylViewModel selected = vinylTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      try {
        logger.log("Manual User borrowing vinyl: " + selected.getTitle());
        selected.getVinyl().borrow("Manual User");
        logger.log("Vinyl borrowed successfully: " + selected.getTitle());
      } catch (IllegalStateException e) {
        logger.log("Borrow failed: " + e.getMessage());
        showError(e.getMessage());
      }
    } else {
      logger.log("Borrow attempted without selecting a vinyl");
      showError("Please select a vinyl to borrow");
    }
  }

  @FXML
  private void onReturn() {
    VinylViewModel selected = vinylTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      try {
        logger.log("Manual User returning vinyl: " + selected.getTitle());
        selected.getVinyl().returnVinyl();
        logger.log("Vinyl returned successfully: " + selected.getTitle());
      } catch (IllegalStateException e) {
        logger.log("Return failed: " + e.getMessage());
        showError(e.getMessage());
      }
    } else {
      logger.log("Return attempted without selecting a vinyl");
      showError("Please select a vinyl to return");
    }
  }

  @FXML
  private void onRemove() {
    VinylViewModel selected = vinylTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      logger.log("Manual User marking vinyl for removal: " + selected.getTitle());
      selected.getVinyl().markForRemoval();
      logger.log("Vinyl marked for removal: " + selected.getTitle());
    } else {
      logger.log("Remove attempted without selecting a vinyl");
      showError("Please select a vinyl to remove");
    }
  }

  @FXML
  private void showDetails() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("View/DetailsViewFXML.fxml"));
      Stage detailsStage = new Stage();
      detailsStage.initModality(Modality.APPLICATION_MODAL);
      detailsStage.setTitle("Vinyl Details");
      detailsStage.setScene(new Scene(loader.load()));

      DetailsViewController controller = loader.getController();
      Vinyl selectedVinyl = sharedState.getSelectedVinyl();

      if (selectedVinyl != null) {
        logger.log("Showing details for vinyl: " + selectedVinyl.getTitle());
        controller.setVinyl(selectedVinyl);
      } else {
        logger.log("Attempted to show details without a selected vinyl");
      }

      detailsStage.showAndWait();
    } catch (Exception e) {
      logger.log("Error showing vinyl details: " + e.getMessage());
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