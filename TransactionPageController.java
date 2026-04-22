package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
