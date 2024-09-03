Time Tracker Application

The Time Tracker App is a Java application designed to help users track and manage their time spent on tasks and tracks information related to task names, dates, and durations. This README provides an overview of the project structure, functionalities, and how to set up and run the app.

Getting Started Before you begin, ensure you have the following:

1.) Java Development Kit (JDK) installed.

2.) Any Java IDE of your choice for code development.

3.) SQLite3 database engine.

Installation 

1.) Clone this repository to your local machine using Git

2.) Open the project in your Java IDE. 

3.) Configure your IDE to use the JavaFX library if not set up already. 

4.) Set up the SQLite3 database.

Usage and Features 

1.) Launch the application and use it to: 

2.) Add new tasks, providing task names, dates, and durations. This can be entered in real-time using a timer that has start, pause, resume, and stop to track the duration. In addition, there
is an option to add details related to past events as well.

3.) After submission, there are options to either view the submissions in totality or search for specific tasks based on details. The search allows user to search by keywords such task name, date, and duration. 

5.) Use the navigation buttons within the app to switch between different views.

Project Structure The project is structured as follows:

1.) src/application: Contains the Java code for the application.

2.) src/application/resources: Contains the FXML files for the user interface.

3.) tasks.db: SQLite database file for storing task data.
