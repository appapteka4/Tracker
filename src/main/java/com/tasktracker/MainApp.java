package com.tasktracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/tasktracker/view/LoginView.fxml"));
        Scene scene = new Scene(loader.load(), 420, 340);
        scene.getStylesheets().add(
                getClass().getResource("/com/tasktracker/css/style.css").toExternalForm());
        stage.setTitle("TaskTracker — Вход");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
