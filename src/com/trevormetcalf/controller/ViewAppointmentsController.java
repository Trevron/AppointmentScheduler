package com.trevormetcalf.controller;

import com.trevormetcalf.model.Appointment;
import com.trevormetcalf.trevortaskc195.State;
import com.trevormetcalf.utility.Alerts;
import com.trevormetcalf.utility.Query;
import com.trevormetcalf.utility.TimeConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ResourceBundle;

/*
    This controller correlates to the view appointments screen.
    It contains the methods and statements necessary to query data from the database and
    then populate a table view with appointment information.
 */

public class ViewAppointmentsController implements Initializable {
    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, String> col_title;
    @FXML private TableColumn<Appointment, String> col_customer;
    @FXML private TableColumn<Appointment, String> col_location;
    @FXML private TableColumn<Appointment, String> col_contact;
    @FXML private TableColumn<Appointment, String> col_type;
    @FXML private TableColumn<Appointment, LocalDateTime> col_start;
    @FXML private Button backButton;
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label localLabel;
    @FXML ToggleGroup appointments;
    @FXML RadioButton allButton;
    @FXML RadioButton weekButton;
    @FXML RadioButton monthButton;

    Parent root;
    Stage stage;
    State state = State.getInstance();
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ObservableList<Appointment> apptList = FXCollections.observableArrayList();


    // Use radio buttons to modify sql query and display appointments by week, month, or all.
    // Called every time a radio button is selected.
    public void radioHandler() {
        // Depending on which button is selected, use temporal adjusters to restrict SQL results.
        if(allButton.isSelected()) {
            updateTableData("SELECT * FROM appointment");
        } else if (weekButton.isSelected()) {
            // Gets appointments from now into the next week.
            updateTableData("SELECT * FROM appointment where start < '" + TimeConverter.format(TimeConverter.toUTC(LocalDateTime.now().with(TemporalAdjusters.next(LocalDateTime.now().getDayOfWeek())))) + "'");
        } else if (monthButton.isSelected()) {
            // Gets appointments for the current month only.
            updateTableData("SELECT * FROM appointment where start < '" + TimeConverter.format(TimeConverter.toUTC(LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()))) + "'");
        }
    }

    // Go back to the main window.
    public void backButtonHandler() throws IOException {
        stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/mainwindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Update selected appointment.
    public void updateButtonHandler() throws IOException{
        // Check if an appointment is selected.
        if(appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            // Set selected appointment in State, then go to update window.
            state.setSelectedAppointment(appointmentTableView.getSelectionModel().getSelectedItem());
            stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/updateappointmentwindow.fxml"));
            root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alerts.getAlert("nullSelectAppointment").show();
        }
    }

    // Delete selected appointment
    public void deleteButtonHandler() {
        // Check if an appointment is selected.
        if(appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            // Set selected appointment in State.
            state.setSelectedAppointment(appointmentTableView.getSelectionModel().getSelectedItem());
            // If deletion is confirmed, delete selected appointment in database.
            Alerts.getAlert("confirmDeletion").showAndWait();
            if(Alerts.getAlert("confirmDeletion").getResult() == ButtonType.OK) {
                String sql1 = "DELETE FROM appointment WHERE appointmentId = " + state.getSelectedAppointment().getAppointmentId();
                Query.executeQuery(sql1);
                // Reload table data and set selected appointment in State to null.
                updateTableData();
                state.setSelectedAppointment(null);
            }
        }
    }

    // Updates table view by clearing list and doing a fresh database query.
    private void updateTableData() {
        // Initialize table before retrieving data.
        apptList.clear();
        // Get customer table from database and then bind to table view.
        try{
            String sql1 = "SELECT * FROM appointment";
            Query.executeQuery(sql1);
            ResultSet result = Query.getResult();
            while(result.next()) {
                // CHECK TO MAKE SURE ALL TIMES MAKE SENSE, MAYBE USE TIME CONVERTER
                apptList.add(new Appointment(result.getInt("appointmentId"), result.getInt("customerId"),
                        result.getInt("userId"), result.getString("title"), result.getString("description"),
                        result.getString("location"), result.getString("contact"), result.getString("type"),
                        result.getTimestamp("start").toLocalDateTime(), result.getTimestamp("end").toLocalDateTime()));
            }
        } catch(SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        appointmentTableView.setItems(apptList);
    }

    // Overridden table updater. Used when selecting radio buttons.
    private void updateTableData(String sqlStatement) {
        // Initialize table before retrieving data.
        apptList.clear();
        // Get customer table from database and then bind to tableview.
        try{
            String sql1 = sqlStatement;
            Query.executeQuery(sql1);
            ResultSet result = Query.getResult();
            while(result.next()) {
                // getTimestamp or getDate? I have no idea result.getDate("start").toLocalDate() <- doesnt have times
                apptList.add(new Appointment(result.getInt("appointmentId"), result.getInt("customerId"),
                        result.getInt("userId"), result.getString("title"), result.getString("description"),
                        result.getString("location"), result.getString("contact"), result.getString("type"),
                        result.getTimestamp("start").toLocalDateTime(), result.getTimestamp("end").toLocalDateTime()));
            }
        } catch(SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        appointmentTableView.setItems(apptList);
    }

    // Set labels under table view to show more details about the appointment.
    public void updateLabelHandler() {
        if(appointmentTableView.getSelectionModel().getSelectedItem() != null) {
            titleLabel.setText(appointmentTableView.getSelectionModel().getSelectedItem().getTitle());
            descriptionLabel.setText(appointmentTableView.getSelectionModel().getSelectedItem().getDescription());
            localLabel.setText(TimeConverter.format(TimeConverter.fromUTC(appointmentTableView.getSelectionModel().getSelectedItem().getStart())));
        } else {
            titleLabel.setText("");
            descriptionLabel.setText("");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize columns and table data.
        updateTableData();
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        col_contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_start.setCellValueFactory(new PropertyValueFactory<>("start"));
    }
}
