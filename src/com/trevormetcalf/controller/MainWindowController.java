package com.trevormetcalf.controller;

import com.trevormetcalf.trevortaskc195.State;
import com.trevormetcalf.utility.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;

/*
    This controller correlates to the main window.
    It provides the methods required to navigate to the rest of the screens in the application.
 */

public class MainWindowController {
    @FXML
    Button logoutButton;

    State state = State.getInstance();
    Stage stage;
    Parent root;

    // Provide the ability to open the log file in Notepad from within the application.
    public void handleReadLogButton() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "logfile.txt");
        pb.start();
    }

    // Confirm if user wants to logout or not.
    // Set selectedUser in state to null before going back to login screen.
    public void handleLogoutButton() throws IOException {
        Alerts.getAlert("confirmLogout").showAndWait();
        if(Alerts.getAlert("confirmLogout").getResult() == ButtonType.OK) {
            state.setCurrentUser(null);
            stage = (Stage) logoutButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/loginwindow.fxml"));
            root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    // Go to add customer screen.
    public void handleAddCustomerButton() throws IOException {
        stage = (Stage) logoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/addcustomerwindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Go to view customers screen.
    public void handleViewCustomersButton() throws IOException {
        stage = (Stage) logoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/viewcustomerswindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Go to view appointments screen.
    public void handleViewAppointmentsButton() throws IOException {
        stage = (Stage) logoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/viewappointmentswindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Go to add appointment screen.
    public void handleAddAppointmentButton() throws IOException {
        stage = (Stage) logoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/addappointmentwindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Go to the consultant schedules screen.
    public void handleConsultantSchedulesButton() throws IOException {
        stage = (Stage) logoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/schedulewindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Go to the appointments by type screen
    public void handleAppointmentsByTypeButton() throws IOException {
        stage = (Stage) logoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/typewindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleAppointmentsByLocationButton() throws IOException {
        stage = (Stage) logoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/locationwindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
