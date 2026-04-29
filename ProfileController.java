package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfileController {

    @FXML private Label usernameLabel;
    @FXML private Label sidebarUsername;
    @FXML private Label profileUsernameLabel;

    private String username;
    private ProjectsController projectsController;

    public void setContext(String username, ProjectsController projectsController) {
        this.username = username;
        this.projectsController = projectsController;
        if (usernameLabel != null) usernameLabel.setText(username);
        if (sidebarUsername != null) sidebarUsername.setText(username);
        if (profileUsernameLabel != null) profileUsernameLabel.setText(username);
    }

    @FXML
    private void handleChangePassword() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Change Password");

        VBox content = new VBox(14);
        content.setStyle("-fx-padding: 32; -fx-background-color: white;");
        content.setPrefWidth(380);

        Label title = new Label("Change Password");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: 700;");

        Label currentLabel = new Label("Current Password");
        currentLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        PasswordField currentField = new PasswordField();
        currentField.setPromptText("Enter current password");
        styleField(currentField);

        Label newLabel = new Label("New Password");
        newLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        PasswordField newField = new PasswordField();
        newField.setPromptText("Enter new password");
        styleField(newField);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e53935; -fx-font-size: 12px;");

        Button saveBtn = new Button("Save");
        saveBtn.setPrefWidth(316);
        saveBtn.setStyle("-fx-background-color: #299D91; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 700; -fx-padding: 12 0 12 0; -fx-background-radius: 8; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> {
            String current = currentField.getText();
            String newPass = newField.getText();
            if (current.isEmpty() || newPass.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }
            if (!current.equals(UserSession.getPassword())) {
                errorLabel.setText("Current password is incorrect.");
                return;
            }
            UserSession.setPassword(newPass);
            popup.close();
            showInfo("Password changed successfully.");
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(316);
        cancelBtn.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 14px; -fx-padding: 10 0 10 0; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> popup.close());

        content.getChildren().addAll(title, currentLabel, currentField, newLabel, newField, errorLabel, saveBtn, cancelBtn);
        popup.setScene(new Scene(content));
        popup.showAndWait();
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
                    Stage stage = (Stage) sidebarUsername.getScene().getWindow();
                    stage.getScene().setRoot(loader.load());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleGoToProjects() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("projects.fxml"));
            Parent root = loader.load();
            ProjectsController ctrl = loader.getController();
            if (projectsController != null) {
                ctrl.restoreFrom(projectsController);
            } else {
                ctrl.setUsername(username);
            }
            Stage stage = (Stage) sidebarUsername.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("transactionPage.fxml"));
            Parent root = loader.load();
            TransactionPageController ctrl = loader.getController();
            ctrl.setContext(null, username, projectsController);
            Stage stage = (Stage) sidebarUsername.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewProfile() {
        // already on profile page
    }

    private void styleField(PasswordField field) {
        field.setStyle("-fx-font-size: 13px; -fx-padding: 10 14 10 14; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #e0e0e0;");
        field.setPrefWidth(316);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
