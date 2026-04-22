package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ProjectsController {

    @FXML private Button createProjectBtn;
    @FXML private Label usernameLabel;
    @FXML private Label sidebarUsername;

    @FXML
    private void handleCreateProject() {
        System.out.println("Create project clicked");
    }

    @FXML
    private void handleTransactions() {
        System.out.println("Transactions clicked");
    }

    public void setUsername(String username) {
        if (usernameLabel != null) usernameLabel.setText(username);
        if (sidebarUsername != null) sidebarUsername.setText(username);
    }
}