package com.tasktracker.controller;

import com.tasktracker.MainApp;
import com.tasktracker.service.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField passwordConfirmField;
    @FXML private Label         confirmLabel;
    @FXML private Label         subtitleLabel;
    @FXML private Label         errorLabel;
    @FXML private Button        mainBtn;
    @FXML private Button        switchBtn;

    private final AuthService authService = new AuthService();
    private boolean isRegisterMode = false;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleMain() {
        String email    = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Заполните email и пароль");
            return;
        }

        if (isRegisterMode) {
            String confirm = passwordConfirmField.getText();
            if (password.length() < 6) {
                showError("Пароль должен быть не менее 6 символов");
                return;
            }
            if (!password.equals(confirm)) {
                showError("Пароли не совпадают");
                return;
            }
            mainBtn.setDisable(true);
            new Thread(() -> {
                String error = authService.register(email, password);
                Platform.runLater(() -> {
                    mainBtn.setDisable(false);
                    if (error == null) {
                        showError("Регистрация успешна! Войдите в систему.");
                        errorLabel.setStyle("-fx-text-fill: #27ae60;");
                        switchToLogin();
                    } else {
                        showError(error);
                    }
                });
            }).start();
        } else {
            mainBtn.setDisable(true);
            new Thread(() -> {
                String error = authService.login(email, password);
                Platform.runLater(() -> {
                    mainBtn.setDisable(false);
                    if (error == null) {
                        openMainWindow();
                    } else {
                        showError(error);
                    }
                });
            }).start();
        }
    }

    @FXML
    private void handleSwitch() {
        errorLabel.setVisible(false);
        passwordField.clear();
        passwordConfirmField.clear();
        if (isRegisterMode) {
            switchToLogin();
        } else {
            switchToRegister();
        }
    }

    private void switchToRegister() {
        isRegisterMode = true;
        subtitleLabel.setText("Создание учётной записи");
        mainBtn.setText("Зарегистрироваться");
        switchBtn.setText("Уже есть аккаунт? Войти");
        confirmLabel.setVisible(true);
        confirmLabel.setManaged(true);
        passwordConfirmField.setVisible(true);
        passwordConfirmField.setManaged(true);
    }

    private void switchToLogin() {
        isRegisterMode = false;
        subtitleLabel.setText("Трекер задач для команды");
        mainBtn.setText("Войти");
        switchBtn.setText("Нет аккаунта? Зарегистрироваться");
        confirmLabel.setVisible(false);
        confirmLabel.setManaged(false);
        passwordConfirmField.setVisible(false);
        passwordConfirmField.setManaged(false);
    }

    private void openMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/tasktracker/view/MainView.fxml"));
            Scene scene = new Scene(loader.load(), 900, 620);
            scene.getStylesheets().add(
                    getClass().getResource("/com/tasktracker/css/style.css").toExternalForm());
            MainApp.primaryStage.setTitle("TaskTracker — Задачи команды");
            MainApp.primaryStage.setScene(scene);
            MainApp.primaryStage.setResizable(true);
            MainApp.primaryStage.setMinWidth(750);
            MainApp.primaryStage.setMinHeight(500);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Ошибка открытия главного окна");
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setStyle("-fx-text-fill: #e74c3c;");
        errorLabel.setVisible(true);
    }
}