package application;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PastEventController {
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	@FXML
	private Parent root;
	@FXML
	private Label taskNameLabel;
	@FXML
	private Label dateLabel;
	@FXML
	private Label durationLabel;
	@FXML
	private Label submitDataLabel;
	@FXML
	private Button clearDataButton;
	@FXML
	private Button taskSubmitButton;
	@FXML
	private Button finalSubmitButton;
	@FXML
	private Button dateSubmitButton;
	@FXML
	private Button durationSubmitButton;
	@FXML
	private TextField taskInput;
	@FXML
	private TextField dateInput;
	@FXML
	private TextField durationInput;

	private String taskName;
	private String date;
	private String duration;

	// Method to switch back to the Main screen
	public void switchToMain(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// Method to switch to the Create Time Entry screen
	public void switchToCreate(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("CreateTimeEntry.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	public void initialize() {
		dateSubmitButton.setDisable(true);
		dateInput.setDisable(true);
		durationSubmitButton.setDisable(true);
		durationInput.setDisable(true);
		clearDataButton.setDisable(true);
		finalSubmitButton.setDisable(true);
		submitDataLabel.setDisable(true);
    }

	// Method to handle task name input
	@FXML
	public void getTaskNameInput(ActionEvent event) {
		String userInput = taskInput.getText().trim(); // Trim leading/trailing whitespace

		if (userInput.isEmpty()) {
			showAlert("Error", "Task name cannot be empty.");
		} else if (userInput.length() < 3 || userInput.length() > 50) {
			showAlert("Error", "Task name length must be between 3 and 50 characters long.");
		} else if (!isValidTaskName(userInput)) {
			showAlert("Error", "Invalid task name. Task names must contain only letters and spaces.");
		} else {
			taskName = userInput;
			dateSubmitButton.setDisable(false);
			dateInput.setDisable(false);
			taskSubmitButton.setDisable(true);
			taskInput.setDisable(true);
			showCustomPopup("Success", "Task name entry successful!");
		}
	}

	// Method to check if a task name is valid
	private boolean isValidTaskName(String input) {
		// This method checks if the input contains only letters and spaces

		// Regular expression pattern to match valid task names
		String pattern = "^[A-Za-z\\s]+$";

		return input.matches(pattern);
	}

	// Method to handle date input
	@FXML
	public void getDateInput(ActionEvent event) {
		String userInput = dateInput.getText().trim(); // Trim leading/trailing whitespace

		if (userInput.isEmpty()) {
			showAlert("Error", "Date cannot be empty.");
		} else {
			// Perform data type validation (date)
			try {
				// Check for MM/DD/YYYY format
				if (!userInput.matches("^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/(\\d{4})$")) {
					showAlert("Error", "Invalid date entered. Please enter a valid date using MM/DD/YYYY format.");
					return; // Exit method if format is invalid
				}

				SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
				inputFormat.setLenient(false);

				SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");

				// Parse the input and format it with leading zeros
				String formattedDate = outputFormat.format(inputFormat.parse(userInput));

				dateInput.setText(formattedDate); // Update the input field
				date = formattedDate;
				dateSubmitButton.setDisable(true);
				dateInput.setDisable(true);
				durationSubmitButton.setDisable(false);
				durationInput.setDisable(false);
				showCustomPopup("Success", "Date entry successful!");
				
			} catch (ParseException e) {
				showAlert("Error","Invalid date format. Please enter the date using MM/DD/YYYY format.");
			}
		}
	}

	// Method to handle duration input
	@FXML
	public void getDurationInput(ActionEvent event) {
		String userInput = durationInput.getText().trim(); // Trim leading/trailing whitespace

		if (userInput.isEmpty()) {
			showAlert("Error", "Duration cannot be empty.");
		} else if (!isValidDurationFormat(userInput)) {
			showAlert("Error", "Invalid duration format. Please enter duration using  HH:MM:SS format.");
		} else if (userInput.equalsIgnoreCase("00:00:00")) {
			showAlert("Error", "Duration must be at least 1 second long.");
		} else {
			duration = userInput;
			durationSubmitButton.setDisable(true);
			durationInput.setDisable(true);
			clearDataButton.setDisable(false);
			finalSubmitButton.setDisable(false);
			submitDataLabel.setDisable(false);
			submitDataLabel.setText(" Task Name: " + taskName + "	Date: " + date + "	Duration: " + userInput);
		}

	}

	// Method to check if duration format is valid
	private boolean isValidDurationFormat(String input) {
		// Regular expression pattern to match valid duration format (HH:mm:ss)
		String pattern = "^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

		return input.matches(pattern);
	}

	// Method to submit data
	@FXML
	public void submitData(ActionEvent event) {
			boolean submitted = Database.insertTask(taskName, date, duration);
			if (submitted) showCustomPopup("Success", "Submission successful!");
		    else showAlert("Error", "Submission failed, please try again!");
		    
			taskInput.clear();
			dateInput.clear();
			durationInput.clear();
			submitDataLabel.setText("");
			dateSubmitButton.setDisable(true);
			dateInput.setDisable(true);
			durationSubmitButton.setDisable(true);
			durationInput.setDisable(true);
			submitDataLabel.setDisable(true);
			clearDataButton.setDisable(true);
			finalSubmitButton.setDisable(true);
			taskInput.setDisable(false);
			taskSubmitButton.setDisable(false);
	}

	// Method to clear data
	@FXML
	public void clearData(ActionEvent event) {
		taskInput.clear();
		dateInput.clear();
		durationInput.clear();
		submitDataLabel.setText("");
		dateSubmitButton.setDisable(true);
		dateInput.setDisable(true);
		durationSubmitButton.setDisable(true);
		durationInput.setDisable(true);
		submitDataLabel.setDisable(true);
		clearDataButton.setDisable(true);
		finalSubmitButton.setDisable(true);
		taskInput.setDisable(false);
		taskSubmitButton.setDisable(false);
	}

	// Method to show an error alert
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Method to show a custom popup dialog, such as to inform user that submission
	// was succesful
	private void showCustomPopup(String title, String message) {
		Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle(title);

		// Remove the default header with icon and close button
		dialog.setDialogPane(new DialogPane() {
			{
				getButtonTypes().addAll(ButtonType.OK);
				setContentText(message);
				setHeaderText(null); // Remove header text
				setGraphic(null); // Remove header icon
				setMinHeight(Region.USE_PREF_SIZE);
			}
		});

		dialog.showAndWait();
	}
}
