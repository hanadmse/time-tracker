## Time Tracker Application

The Time Tracker App is a Java application designed to help users track and manage their time spent on tasks, including task names, dates, and durations. Built using JavaFX, and SQLite, it offers a lightweight and efficient solution for storing and managing task data locally, while providing a user-friendly interface for easy interaction. This README provides an overview of the project structure, functionalities, and how to set up and run the app.

## Getting Started Before you begin, ensure you have the following:

- Java Development Kit (JDK) installed.

- Any Java IDE of your choice for code development.

- SQLite database engine.

## Installation 

- Clone this repository to your local machine using Git

- Open the project in your Java IDE. 

- Configure your IDE to use the JavaFX library if not set up already. 


## Usage and Features 

- Launch the application and use it to: 

- Add new tasks, providing task names, dates, and durations. This can be entered in real-time using a timer that has start, pause, resume, and stop to track the duration. In addition, there
is an option to add details related to past events as well.

- After submission, there are options to either view the submissions in totality or search for specific tasks based on details. The search allows user to search by keywords such task name, date, and duration. 

- Use the navigation buttons within the app to switch between different views.


## Project Structure The project is structured as follows:

- src/application: Contains the Java code for the application.

- src/application/resources: Contains the FXML files for the user interface.

- tasks.db: SQLite database file for storing task data.
