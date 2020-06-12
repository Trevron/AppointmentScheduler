package com.trevormetcalf.controller;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/*
    This controller is correlated to the view appointments by locations screen.
    Contains the methods and statements necessary to query database and populate a table view.
 */

public class LocationController implements Initializable {
    @FXML Button backButton;
    @FXML TableView<LocationFrame> locationTableView;
    @FXML TableColumn<LocationFrame, String> col_location;
    @FXML TableColumn<LocationFrame, String> col_count;

    State state = State.getInstance();
    Stage stage;
    Parent root;

    ObservableList<LocationFrame> apptList = FXCollections.observableArrayList();



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
        col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        col_count.setCellValueFactory(new PropertyValueFactory<>("count"));

        String sql = "Select location, count(location) as 'count' from appointment group by location";
        Query.executeQuery(sql);
        ResultSet result = Query.getResult();
        try{
            while(result.next()) {
                apptList.add(new LocationFrame(result.getString("location"), result.getInt("count")));
            }
        } catch(SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        locationTableView.setItems(apptList);
    }


    // Class Location is used to provide getter and setter methods for the cell value factories.
    public class LocationFrame {
        String location;
        int count;

        public LocationFrame(String location, int count) {
            this.location = location;
            this.count = count;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
