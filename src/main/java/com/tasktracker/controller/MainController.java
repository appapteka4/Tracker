package com.tasktracker.controller;

import com.tasktracker.MainApp;
import com.tasktracker.model.Task;
import com.tasktracker.service.AuthService;
import com.tasktracker.service.TaskService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;

public class MainController {

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, Integer>  colId;
    @FXML private TableColumn<Task, String>   colTitle;
    @FXML private TableColumn<Task, String>   colAssignee;
    @FXML private TableColumn<Task, String>   colPriority;
    @FXML private TableColumn<Task, String>   colStatus;
    @FXML private TableColumn<Task, Void>     colActions;

    @FXML private ComboBox<String> filterCombo;

    @FXML private Label statusBar;

    private final TaskService taskService = new TaskService();
    private final AuthService authService = new AuthService();
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        setupFilter();
        loadTasks(null);
    }


    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setPrefWidth(230);

        colAssignee.setCellValueFactory(new PropertyValueFactory<>("assignee"));
        colAssignee.setPrefWidth(140);

        colPriority.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPriorityLabel()));
        colPriority.setPrefWidth(90);

        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatusLabel()));
        colStatus.setPrefWidth(100);
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                Task task = getTableView().getItems().get(getIndex());
                setStyle(switch (task.getStatus()) {
                    case "done"        -> "-fx-text-fill: #27ae60; -fx-font-weight: bold;";
                    case "in_progress" -> "-fx-text-fill: #e67e22; -fx-font-weight: bold;";
                    default            -> "-fx-text-fill: #2980b9; -fx-font-weight: bold;";
                });
            }
        });

        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit   = new Button("✎");
            private final Button btnDelete = new Button("✕");
            private final HBox box = new HBox(6, btnEdit, btnDelete);

            {
                btnEdit.getStyleClass().add("btn-icon");
                btnDelete.getStyleClass().addAll("btn-icon", "btn-danger");
                btnEdit.setOnAction(e -> {
                    Task t = getTableView().getItems().get(getIndex());
                    openEditDialog(t);
                });
                btnDelete.setOnAction(e -> {
                    Task t = getTableView().getItems().get(getIndex());
                    handleDelete(t);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
        colActions.setPrefWidth(90);

        taskTable.setItems(taskList);
        taskTable.setPlaceholder(new Label("Задач нет. Создайте первую!"));
    }


    private void setupFilter() {
        filterCombo.setItems(FXCollections.observableArrayList(
                "Все", "Новые", "В работе", "Готово"));
        filterCombo.setValue("Все");
        filterCombo.setOnAction(e -> {
            String val = filterCombo.getValue();
            String status = switch (val) {
                case "Новые"  -> "new";
                case "В работе" -> "in_progress";
                case "Готово"   -> "done";
                default -> null;
            };
            loadTasks(status);
        });
    }


    private void loadTasks(String statusFilter) {
        statusBar.setText("Загрузка...");
        new Thread(() -> {
            List<Task> tasks = (statusFilter == null)
                    ? taskService.getAllTasks()
                    : taskService.getTasksByStatus(statusFilter);
            Platform.runLater(() -> {
                taskList.setAll(tasks);
                statusBar.setText("Задач: " + tasks.size());
            });
        }).start();
    }


    @FXML
    private void handleNewTask() {
        openTaskDialog(null);
    }


    private void handleDelete(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление задачи");
        alert.setHeaderText("Удалить задачу #" + task.getId() + "?");
        alert.setContentText(task.getTitle());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            new Thread(() -> {
                taskService.deleteTask(task.getId());
                Platform.runLater(() -> loadTasks(null));
            }).start();
        }
    }


    private void openTaskDialog(Task taskToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/tasktracker/view/TaskDialog.fxml"));
            Scene scene = new Scene(loader.load(), 420, 360);
            scene.getStylesheets().add(
                    getClass().getResource("/com/tasktracker/css/style.css").toExternalForm());

            TaskDialogController controller = loader.getController();
            controller.setTask(taskToEdit);

            javafx.stage.Stage dialog = new javafx.stage.Stage();
            dialog.setTitle(taskToEdit == null ? "Новая задача" : "Редактировать задачу");
            dialog.setScene(scene);
            dialog.initOwner(MainApp.primaryStage);
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setResizable(false);
            dialog.showAndWait();

            if (controller.isSaved()) {
                loadTasks(null);
                filterCombo.setValue("Все");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openEditDialog(Task task) {
        openTaskDialog(task);
    }


    @FXML
    private void handleRefresh() {
        filterCombo.setValue("Все");
        loadTasks(null);
    }


    @FXML
    private void handleLogout() {
        authService.logout();
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/tasktracker/view/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 420, 340);
            scene.getStylesheets().add(
                    getClass().getResource("/com/tasktracker/css/style.css").toExternalForm());
            MainApp.primaryStage.setTitle("TaskTracker — Вход");
            MainApp.primaryStage.setScene(scene);
            MainApp.primaryStage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
