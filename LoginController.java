package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        UserSession.set(username, password);

        try {
            FXMLLoader loader = new FXMLLoader(
                    LoginController.class.getResource("projects.fxml")
            );

            HBox root = loader.load();

            ProjectsController controller = loader.getController();
            controller.setUsername(username);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            errorLabel.setText("Load failed. Check console.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/register.fxml"));
            StackPane root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}