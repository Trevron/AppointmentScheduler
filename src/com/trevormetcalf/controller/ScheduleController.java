package com.trevormetcalf.controller;

import com.trevormetcalf.model.Appointment;
import com.trevormetcalf.trevortaskc195.State;
import com.trevormetcalf.utility.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/*
    This class correlates with the view schedules by consultant screen.
    It contains the necessary methods to retrieve information from the database
    and populate the table view.
 */

public class ScheduleController implements Initializable {
    @FXML Button backButton;
    @FXML TableView<Appointment> scheduleTableView;
    @FXML TableColumn<Appointment, String> col_customer;
    @FXML TableColumn<Appointment, String> col_type;
    @FXML TableColumn<Appointment, String> col_start;
    @FXML ComboBox<String> consultantBox;
    State state = State.getInstance();
    Stage stage;
    Parent root;

    ObservableList<Appointment> apptList = FXCollections.observableArrayList();

    // Get userId from database using selected consultant's name.
    private int getUserID(){
        int userID = -1;
        String consultant = consultantBox.getValue();
        String sql = "Select * from user where userName = '" + consultant + "'";
        Query.executeQuery(sql);
        ResultSet result = Query.getResult();
        try {
            while(result.next()) {
                userID = result.getInt("userId");
            }
        } catch(SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return userID;
    }

    // Update the table view using appointments based off selected consultant's userId.
    public void updateTableData() {
        apptList.clear();
        scheduleTableView.refresh();
        String sql1 = "SELECT * FROM appointment where userId = " + getUserID();
        Query.executeQuery(sql1);
        ResultSet result = Query.getResult();
        try {
            while(result.next()) {
                apptList.add(new Appointment(result.getInt("appointmentId"), result.getInt("customerId"),
                        result.getInt("userId"), result.getString("title"), result.getString("description"),
                        result.getString("location"), result.getString("contact"), result.getString("type"),
                        result.getTimestamp("start").toLocalDateTime(), result.getTimestamp("end").toLocalDateTime()));
            }
        } catch(SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        scheduleTableView.setItems(apptList);
    }

    // Populate consultant combo box.
    private void populateConsultants() {
        ObservableList<String> consultantList = FXCollections.observableArrayList();
        consultantList.addAll("Ryan Gosling", "Harrison Ford", "Sylvia Hoeks", "Ana de Armas");
        consultantBox.setItems(consultantList);
        // Set default selection.
        consultantBox.getSelectionModel().select(0);
    }

    // Go back to the main screen.
    public void backButtonHandler() throws IOException {
        stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/mainwindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateConsultants();
        updateTableData();

        col_customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_start.setCellValueFactory(new PropertyValueFactory<>("start"));
    }
}


