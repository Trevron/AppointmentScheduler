package com.trevormetcalf.trevortaskc195;

import com.trevormetcalf.utility.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/loginwindow.fxml"));
        primaryStage.setTitle("TrevorTask C195");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {

        try {
            DBConnection.makeConnection();
            launch(args);
            DBConnection.closeConnection();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
