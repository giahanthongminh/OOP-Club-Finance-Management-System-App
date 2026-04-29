package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TransactionPageController {

    @FXML private Label usernameLabel;
    @FXML private Label sidebarUsername;
    @FXML private Label projectNameLabel;

    private Project project;
    private String username;
    private ProjectsController projectsController;

    public void setContext(Project project, String username, ProjectsController projectsController) {
        this.project = project;
        this.username = username;
        this.projectsController = projectsController;

        if (usernameLabel != null) usernameLabel.setText(username == null ? "" : username);
        if (sidebarUsername != null) sidebarUsername.setText(username == null ? "" : username);
        if (projectNameLabel != null) {
            projectNameLabel.setText(project == null ? "All Transactions" : project.getProjectName());
        }
    }

    @FXML
    private void handleGoToProjects() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("projects.fxml"));
            Parent root = loader.load();

            ProjectsController ctrl = loader.getController();
            if (projectsController != null) {
                ctrl.restoreFrom(projectsController);
            } else if (username != null) {
                ctrl.setUsername(username);
            }

            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToProjectDetail() {
        if (project == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("projectPage.fxml"));
            Parent root = loader.load();

            ProjectPageController ctrl = loader.getController();
            ctrl.setProject(project, username, projectsController);

            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to log out?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Logout");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                UserSession.clear();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                    Stage stage = (Stage) usernameLabel.getScene().getWindow();
                    stage.getScene().setRoot(loader.load());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleViewProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profilePage.fxml"));
            Parent root = loader.load();
            ProfileController ctrl = loader.getController();
            ctrl.setContext(username, projectsController);
            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
