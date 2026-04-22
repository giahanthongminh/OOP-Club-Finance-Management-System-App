package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/com/example/demo/login.fxml"));
        Scene scene = new Scene(loader.load(), 1440, 1024);
        stage.setTitle("Mony - Club Finance Management System");
        stage.setScene(scene);
        stage.show();
    }
}