package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(
                HelloApplication.class.getResourceAsStream("/fonts/Inter_18pt-Regular.ttf"),
                14
        );
        Font.loadFont(
                HelloApplication.class.getResourceAsStream("/fonts/Inter_18pt-SemiBold.ttf"),
                14
        );
        Font.loadFont(
                HelloApplication.class.getResourceAsStream("/fonts/Inter_18pt-Bold.ttf"),
                14
        );

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("login.fxml")
        );
        Scene scene = new Scene(loader.load(), 1440, 1024);
        stage.setTitle("Mony - Club Finance Management System");
        stage.setScene(scene);
        stage.show();
    }
}
