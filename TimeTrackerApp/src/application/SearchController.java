package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class SearchController {
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	@FXML
	private Parent root;
	@FXML
	private TextField taskNameField;
	@FXML
	private TextField durationField;
	
	@FXML
	private Button taskButton;
	
	@FXML
	private Button searchButton;
	
	@FXML 
	private Button dateButton;
	
	@FXML
	private Button durationButton;

	@FXML
	private TextField dateField;

	@FXML
	private TableView<Task> tableView;

	@FXML
	private TableColumn<Task, Integer> taskIdColumn;

	@FXML
	private TableColumn<Task, String> taskNameColumn;

	@FXML
	private TableColumn<Task, String> dateColumn;

	@FXML
	private TableColumn<Task, String> durationColumn;
	
	private String taskName = "";
	private String date = "";
	private String duration = "";


	// Set up cell value factories for TableView columns
	public void initialize() {
		taskIdColumn.setCellValueFactory(cellData -> cellData.getValue().taskIdProperty().asObject());
		taskNameColumn.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
		searchButton.setDisable(true);
		
	}

	// Switch to the main view
	public void switchToMain(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// Switch to the ViewSubmits view
	public void switchToSubmits(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ViewSubmits.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// Handle task name input
	@FXML
	public void getTaskNameInput(ActionEvent event) {
		String userInput = taskNameField.getText().trim(); // Trim leading/trailing whitespace

		if (userInput.isEmpty()) {
			showAlert("Error", "Task name cannot be empty when entering.");
			taskNameField.clear();
		} else if (userInput.length() < 3 || userInput.length() > 50) {
			showAlert("Error", "Task name length must be between 3 and 50 characters long.");
			taskNameField.clear();
		} else if (!isValidTaskName(userInput)) {
			showAlert("Error", "Invalid task name. Task names must contain only letters and spaces.");
			taskNameField.clear();
		} else {
			taskName = userInput;
			searchButton.setDisable(false);
			taskNameField.setDisable(true);
			taskButton.setDisable(true);
			
		}
	}

	// Handle date input
	@FXML
	public void getDateInput(ActionEvent event) {
		String userInput = dateField.getText().trim(); // Trim leading/trailing whitespace

		if (userInput.isEmpty()) {
			showAlert("Error", "Date cannot be empty when entering.");
		} else {
			// Perform data type validation (date)
			try {
				// Check for MM/DD/YYYY format
				if (!userInput.matches("^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/(\\d{4})$")) {
					showAlert("Error",
							"Invalid date format. Please use MM/DD/YYYY. Add a zero before entering single digit days and months.");
					dateField.clear();
					return; // Exit method if format is invalid
				}

				SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
				inputFormat.setLenient(false);

				SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");

				// Parse the input and format it with leading zeros
				String formattedDate = outputFormat.format(inputFormat.parse(userInput));

				dateField.setText(formattedDate); // Update the input field
				date = formattedDate;
				dateButton.setDisable(true);
				dateField.setDisable(true);
				searchButton.setDisable(false);

			} catch (ParseException e) {
				showAlert("Error",
						"Invalid date format. Please enter a valid date using MM/DD/YYYY.");
				dateField.clear();
			}
		}
	}

	// Handle duration input
	@FXML
	public void getDurationInput(ActionEvent event) {
		String userInput = durationField.getText().trim(); // Trim leading/trailing whitespace

		if (userInput.isEmpty()) {
			showAlert("Error", "Duration cannot be empty when entering.");
			durationField.clear();
		} else if (!isValidDurationFormat(userInput)) {
			showAlert("Error", "Invalid duration format. Please use HH:mm:ss.");
			durationField.clear();
		} else if (userInput.equalsIgnoreCase("00:00:00")) {
			showAlert("Error", "Duration must be at least 1 second long.");
		} else {
			duration = userInput;
			durationButton.setDisable(true);
			durationField.setDisable(true);
			searchButton.setDisable(false);
		}

	}

	// Check if the input matches a valid duration format (HH:mm:ss)
	private boolean isValidDurationFormat(String input) {
		// Regular expression pattern to match valid duration format (HH:mm:ss)
		String pattern = "^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

		return input.matches(pattern);
	}

	// Check if the input contains only letters and spaces
	private boolean isValidTaskName(String input) {
		// This method checks if the input contains only letters and spaces

		// Regular expression pattern to match valid task names
		String pattern = "^[A-Za-z\\s]+$";
		return input.matches(pattern);
	}

	// Searches database for tasks entered by user.
	@FXML
	public void searchAndUpload(ActionEvent event) {
		ObservableList<Task> searchResults = Database.searchDatabase(taskName, date, duration);
		tableView.setItems(searchResults);
		if (searchResults.size() == 0) showAlert("Alert", "No results found for your search.");
		taskName = "";
		date = "";
		duration = "";
		taskNameField.clear();
		dateField.clear();
		durationField.clear();
		taskNameField.setDisable(false);
		taskButton.setDisable(false);
		dateButton.setDisable(false);
		dateField.setDisable(false);
		durationButton.setDisable(false);
		durationField.setDisable(false);
		searchButton.setDisable(true);
	}

	// Shows an alert message
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
