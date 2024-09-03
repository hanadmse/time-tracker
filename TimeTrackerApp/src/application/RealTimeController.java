package application;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

public class RealTimeController {
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	@FXML
	private Parent root;
	@FXML
	private TextField taskInput;
	@FXML
	private TextField dateInput;
	@FXML
	private Button taskSubmitButton;
	@FXML
	private Button dateSubmitButton;
	@FXML
    private Label timer;
	@FXML 
	private Button startTaskButton;
	@FXML
	private Button resumeTaskButton;
	@FXML 
	private Button pauseTaskButton;
	@FXML 
	private Button endTaskButton;
	@FXML
	private Label taskNameLabel;
	@FXML
	private Label dateLabel;
	@FXML
	private Label durationLabel;
	@FXML
	private TextField finalTaskDetails;
	@FXML
	private Button clearTaskDetails;
	@FXML
	private Button submitTaskDetails;
	
	private Timeline timeline;
	private int elapsedSeconds = 0;
	private boolean isRunning = false;
    private String taskName;
    private String date;
    private String duration;
    
  
    
	
    //Method that gets task name from user
	@FXML
	public void getTaskNameInput(ActionEvent event) {
	    String userInput = taskInput.getText().trim(); // Trim leading/trailing whitespace
	    if (userInput.isEmpty()) {
	        showAlert("Error", "Task name cannot be empty.");
	    } else if (userInput.length() < 3 || userInput.length() > 50) {
	        showAlert("Error", "Task name length must be between 3 and 50 characters long.");
	    } else if (!isValidTaskName(userInput)) {
	        showAlert("Error", "Task names must contain only letters and spaces.");
	    } else {
	        taskName = userInput;
	        taskInput.setDisable(true);
	        taskSubmitButton.setDisable(true);
	        dateSubmitButton.setDisable(false);
	        dateInput.setDisable(false);
	        showCustomPopup("Success", "Task name successfully entered!");
	    }
	}
	
	//Method that validity of taskName
	private boolean isValidTaskName(String input) {
	    // Regular expression pattern to match valid task names
	    String pattern = "^[A-Za-z\\s]+$";
	    return input.matches(pattern);
	}
	
	//Method that gets user input for date
	@FXML
	public void getDateInput(ActionEvent event) {
	    String userInput = dateInput.getText().trim(); // Trim leading/trailing whitespace

        if (userInput.isEmpty()) {
            showAlert("Field is empty", "Please enter date.");
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
                dateInput.setDisable(true);
                dateSubmitButton.setDisable(true);
                startTaskButton.setDisable(false);
                showCustomPopup("Success", "Date successfully entered!");
            } catch (ParseException e) {
                showAlert("Error", "Invalid date format. Please enter the date using MM/DD/YYYY format.");
            }
        }
	}
	
	//Method that initializes the timer.
	@FXML
	public void initialize() {
		dateSubmitButton.setDisable(true);
		startTaskButton.setDisable(true);
	    pauseTaskButton.setDisable(true);
	    endTaskButton.setDisable(true);
		clearTaskDetails.setDisable(true);
		submitTaskDetails.setDisable(true);
		resumeTaskButton.setDisable(true);
	    timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
	        updateTimer();
	    }));
	    timeline.setCycleCount(Timeline.INDEFINITE);    
    }
	
	//Method that starts the timer.
	@FXML
	private void startTimer(ActionEvent event) {
		if (!isRunning) {
			timeline.play();
			isRunning = true;
			startTaskButton.setDisable(true);
			pauseTaskButton.setDisable(false);
			resumeTaskButton.setDisable(true);
			endTaskButton.setDisable(false);
		}
	}
	
	//Method that pauses the timer. 
	@FXML
	private void pauseTimer(ActionEvent event) {
		if (isRunning) {
            timeline.pause();
            isRunning = false;
            pauseTaskButton.setDisable(true);
            resumeTaskButton.setDisable(false);
        }
	}
	
	// Method that resumes the timer
    @FXML
    private void resumeTimer(ActionEvent event) {
    	if (!isRunning) {
            timeline.play();
            isRunning = true;
            pauseTaskButton.setDisable(false);
            resumeTaskButton.setDisable(true);
        }
    }

	//Method that stops the timer. 
	@FXML
	private void stopTimer(ActionEvent event) {;
		timeline.stop();
        isRunning = false;
        duration = timer.getText();
        timer.setText("00:00:00");
        taskInput.clear();
        dateInput.clear();
        pauseTaskButton.setDisable(true);
        endTaskButton.setDisable(true);
        if (elapsedSeconds == 0) {
    		 initialize();
    		 taskSubmitButton.setDisable(false);
    		 taskInput.setDisable(false);
    		 showAlert("Error", "Duration must be at least 1 second long!");
        }
        else {
        	clearTaskDetails.setDisable(false);
    		submitTaskDetails.setDisable(false);
    		finalTaskDetails.setText(String.format(" Task name: %-10s Date: %-10s Duration: %-10s", taskName, date, duration));
            elapsedSeconds = 0;
        }

	}
	
	 //Method that clears the task details
	 @FXML
	 public void clearTaskDetails(ActionEvent event) {
		 finalTaskDetails.clear();
		 clearTaskDetails.setDisable(true);
 		 submitTaskDetails.setDisable(true);
 		 taskSubmitButton.setDisable(false);
 		 taskInput.setDisable(false);
	 }
	 
	 //Method that submits the task details
	 @FXML
	 public void submitDetails(ActionEvent event) {
		boolean submitted = Database.insertTask(taskName, date, duration);
		if (submitted) {
			// Show success popup and clear text
			showCustomPopup("Success", "Submission successful!");
			finalTaskDetails.clear();
		} else {
			 showAlert("Error", "Submission failed, please try again!");
			 finalTaskDetails.clear();
		 }
		 clearTaskDetails.setDisable(true);
 		 submitTaskDetails.setDisable(true);
 		 taskSubmitButton.setDisable(false);
 		 taskInput.setDisable(false);
	 }
	 
	 //Method that updates the timer to reflect the correct time that has elapsed.
	 @FXML
	 private void updateTimer() {
		 elapsedSeconds++;
	     int hours = elapsedSeconds / 3600;
	     int minutes = (elapsedSeconds % 3600) / 60;
	     int seconds = elapsedSeconds % 60;
	     timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
	 }
	 
	//Method that switches to the main screen
	public void switchToMain(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//Method that switches back to the create a time entry page
	public void switchToCreate(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("CreateTimeEntry.fxml"));
		stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//Method that shows an alert message.
	private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	
	//Method that shows a custom popup, such as to inform user of succesful submission
	private void showCustomPopup(String title, String message) {
	    Dialog<Void> dialog = new Dialog<>();
	    dialog.setTitle(title);

	    // Remove the default header with icon and close button
	    dialog.setDialogPane(new DialogPane() {
	        {
	            getButtonTypes().addAll(ButtonType.OK);
	            setContentText(message);
	            setHeaderText(null); // Remove header text
	            setGraphic(null);    // Remove header icon
	            setMinHeight(Region.USE_PREF_SIZE);
	        }
	    });

	    dialog.showAndWait();
	}
	
}
