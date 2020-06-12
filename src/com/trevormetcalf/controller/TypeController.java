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
    This controller is correlated with the "view appointment by type" screen.
    It contains the methods and statements required to query data and populate a table view.
 */

public class TypeController implements Initializable {
    @FXML Button backButton;
    @FXML TableView<TypeFrame> typeTableView;
    @FXML TableColumn<TypeFrame, String> col_month;
    @FXML TableColumn<TypeFrame, String> col_type;
    @FXML TableColumn<TypeFrame, String> col_count;

    State state = State.getInstance();
    Stage stage;
    Parent root;

    ObservableList<TypeFrame> apptList = FXCollections.observableArrayList();


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

        // Initialize columns for table view.
        col_month.setCellValueFactory(new PropertyValueFactory<>("month"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_count.setCellValueFactory(new PropertyValueFactory<>("count"));

        // Get data from database for table view.
        String sql = "SELECT type, count(type) as 'count', start FROM appointment Group By type, Month(start)";
        Query.executeQuery(sql);
        ResultSet result = Query.getResult();
        try{
            while(result.next()) {
                apptList.add(new TypeFrame(result.getString("type"), result.getTimestamp("start").toLocalDateTime(), result.getInt("count")));
            }
        } catch(SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        typeTableView.setItems(apptList);
    }


    // Class TypeFrame is to provide getter and setter methods for the cell value factories.
    public class TypeFrame {
        String type, month;
        LocalDateTime date;
        int count;

        public TypeFrame(String type, LocalDateTime date, int count) {
            this.type = type;
            this.date = date;
            this.count = count;
            month = date.getMonth().toString();
        }

        public String getMonth() {
            return month;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
