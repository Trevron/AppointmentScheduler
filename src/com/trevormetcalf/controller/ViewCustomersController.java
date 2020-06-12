package com.trevormetcalf.controller;

import com.trevormetcalf.model.Customer;
import com.trevormetcalf.trevortaskc195.State;
import com.trevormetcalf.utility.Alerts;
import com.trevormetcalf.utility.Query;
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
import java.util.ResourceBundle;

/*
    This controller correlates to the view customers window.
    It contains the methods and statements necessary to query data from database and
    then populate the table view with customer information.
 */

public class ViewCustomersController implements Initializable {

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> col_name;
    @FXML private TableColumn<Customer, String> col_address;
    @FXML private TableColumn<Customer, String> col_address2;
    @FXML private TableColumn<Customer, String> col_city;
    @FXML private TableColumn<Customer, String> col_postalCode;
    @FXML private TableColumn<Customer, String> col_country;
    @FXML private TableColumn<Customer, String> col_phone;
    @FXML private Button backButton;

    Parent root;
    Stage stage;
    State state = State.getInstance();

    ObservableList<Customer> custList = FXCollections.observableArrayList();

    // Go back to the main window.
    public void backButtonHandler() throws IOException {
        stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/mainwindow.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Update the selected customer.
    public void updateButtonHandler() throws IOException {
        // Check to see if there is a customer selected.
        if(customerTableView.getSelectionModel().getSelectedItem() != null) {

            // Set selected customer in State.
            state.setSelectedCustomer(customerTableView.getSelectionModel().getSelectedItem());

            // Switch to update customer screen.
            stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/updatecustomerwindow.fxml"));
            root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alerts.getAlert("nullSelectCustomer").show();
        }

    }
    // Delete selected customer.
    public void deleteButtonHandler() {
        // Check to see if a customer is selected.
        if(customerTableView.getSelectionModel().getSelectedItem() != null) {

            // Set selected customer in State.
            state.setSelectedCustomer(customerTableView.getSelectionModel().getSelectedItem());

            // If deletion is confirmed, check to see if there are any appointments involving that customer.
            Alerts.getAlert("confirmDeletion").showAndWait();
            if(Alerts.getAlert("confirmDeletion").getResult() == ButtonType.OK) {
                int exists = -1;
                ResultSet result;
                String sql1 = "Select * FROM appointment where customerId = " + state.getSelectedCustomer().getCustomerID();
                try {
                    Query.executeQuery(sql1);
                    result = Query.getResult();
                    while(result.next()) {
                        exists = result.getInt("appointmentId");
                    }
                } catch(SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                // If any appointments are found, tell user to delete appointments and then try again.
                // Otherwise, delete selected customer.
                if(exists > -1) {
                    Alerts.getAlert("appointmentsExist").show();
                } else if (exists == -1) {
                    String sql2 = "DELETE FROM customer WHERE customerId = " + state.getSelectedCustomer().getCustomerID();
                    Query.executeQuery(sql2);
                    updateTableData();
                }
            }
        }
    }

    // Reload table data from database
    private void updateTableData() {
        // Initialize customer list array
        custList.clear();
        customerTableView.refresh();

        // Get customer table from database and then bind to tableview
        try{
            String sqlCustomers = "SELECT a.customerId, a.customerName, a.addressId, b.address, b.address2, b.postalCode, b.phone, b.cityId, c.city, c.countryId, d.country \n" +
                    "\tFROM customer a JOIN address b on a.addressId = b.addressId\n" +
                    "\tJOIN city c on b.cityId = c.cityId\n" +
                    "\tJOIN country d on c.countryId = d.countryId";
            Query.executeQuery(sqlCustomers);
            ResultSet result = Query.getResult();
            while(result.next()) {
                custList.add(new Customer(result.getString("a.customerId"), result.getString("a.customerName"),
                        result.getString("a.addressId"), result.getString("b.address"), result.getString("b.address2"),
                        result.getString("b.postalCode"), result.getString("b.phone"), result.getString("c.city"),
                        result.getString("b.cityId"), result.getString("d.country"), result.getString("c.countryId")));
            }
        } catch(SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        customerTableView.setItems(custList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize columns and table data
        updateTableData();
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_address2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        col_country.setCellValueFactory(new PropertyValueFactory<>("country"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }
}
