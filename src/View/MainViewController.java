package View;

import Model.SharedVinylState;
import Model.Vinyl;
import Server.Logger;
import ViewModel.VinylListViewModel;
import ViewModel.VinylSocketClient;
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
  private VinylSocketClient socketClient;

  public void initialize() {
    sharedState = SharedVinylState.getInstance();
    logger = Logger.getInstance();
    socketClient = VinylSocketClient.getInstance();

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
        String title = selected.getTitle();
        logger.log("Manual User attempting to reserve vinyl: " + title);

        // Use the socket client to send the request to the server
        socketClient.reserveVinyl(title, "Manual User")
                .thenAccept(success -> {
                  if (success) {
                    logger.log("Vinyl reserved successfully: " + title);
                    // Refresh the vinyl list to show updated status
                    socketClient.listVinyls().thenAccept(vinyls -> viewModel.refreshFromServer(vinyls));
                  } else {
                    logger.log("Reserve failed for: " + title);
                    showErrorOnJavaFXThread("Failed to reserve vinyl. Please try again.");
                  }
                })
                .exceptionally(ex -> {
                  logger.log("Reserve error: " + ex.getMessage());
                  showErrorOnJavaFXThread("Error: " + ex.getMessage());
                  return null;
                });
      } catch (Exception e) {
        logger.log("Reserve error: " + e.getMessage());
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
        String title = selected.getTitle();
        logger.log("Manual User attempting to borrow vinyl: " + title);

        // Use the socket client to send the request to the server
        socketClient.borrowVinyl(title, "Manual User")
                .thenAccept(success -> {
                  if (success) {
                    logger.log("Vinyl borrowed successfully: " + title);
                    // Refresh the vinyl list to show updated status
                    socketClient.listVinyls().thenAccept(vinyls -> viewModel.refreshFromServer(vinyls));
                  } else {
                    logger.log("Borrow failed for: " + title);
                    showErrorOnJavaFXThread("Failed to borrow vinyl. Please try again.");
                  }
                })
                .exceptionally(ex -> {
                  logger.log("Borrow error: " + ex.getMessage());
                  showErrorOnJavaFXThread("Error: " + ex.getMessage());
                  return null;
                });
      } catch (Exception e) {
        logger.log("Borrow error: " + e.getMessage());
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
        String title = selected.getTitle();
        logger.log("Manual User attempting to return vinyl: " + title);

        // Use the socket client to send the request to the server
        socketClient.returnVinyl(title)
                .thenAccept(success -> {
                  if (success) {
                    logger.log("Vinyl returned successfully: " + title);
                    // Refresh the vinyl list to show updated status
                    socketClient.listVinyls().thenAccept(vinyls -> viewModel.refreshFromServer(vinyls));
                  } else {
                    logger.log("Return failed for: " + title);
                    showErrorOnJavaFXThread("Failed to return vinyl. Please try again.");
                  }
                })
                .exceptionally(ex -> {
                  logger.log("Return error: " + ex.getMessage());
                  showErrorOnJavaFXThread("Error: " + ex.getMessage());
                  return null;
                });
      } catch (Exception e) {
        logger.log("Return error: " + e.getMessage());
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
      try {
        String title = selected.getTitle();
        logger.log("Manual User marking vinyl for removal: " + title);

        // Add new method to socket client for marking removal
        socketClient.markForRemoval(title)
                .thenAccept(success -> {
                  if (success) {
                    logger.log("Vinyl marked for removal: " + title);
                    // Refresh the vinyl list to show updated status
                    socketClient.listVinyls().thenAccept(vinyls -> viewModel.refreshFromServer(vinyls));
                  } else {
                    logger.log("Mark for removal failed for: " + title);
                    showErrorOnJavaFXThread("Failed to mark vinyl for removal. Please try again.");
                  }
                })
                .exceptionally(ex -> {
                  logger.log("Mark for removal error: " + ex.getMessage());
                  showErrorOnJavaFXThread("Error: " + ex.getMessage());
                  return null;
                });
      } catch (Exception e) {
        logger.log("Mark for removal error: " + e.getMessage());
        showError(e.getMessage());
      }
    } else {
      logger.log("Remove attempted without selecting a vinyl");
      showError("Please select a vinyl to remove");
    }
  }

  @FXML
  private void showDetails() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DetailsViewFXML.fxml"));
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

  private void showErrorOnJavaFXThread(String message) {
    javafx.application.Platform.runLater(() -> showError(message));
  }
}