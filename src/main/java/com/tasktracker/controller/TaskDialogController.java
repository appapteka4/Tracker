package com.tasktracker.controller;

import com.tasktracker.model.Task;
import com.tasktracker.service.TaskService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TaskDialogController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descArea;
    @FXML
    private ComboBox<String> priorityCombo;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private TextField assigneeField;
    @FXML
    private Button saveBtn;
    @FXML
    private Label errorLabel;

    private final TaskService taskService = new TaskService();
    private Task existingTask = null;
    private boolean saved = false;

    @FXML
    public void initialize() {
        priorityCombo.getItems().addAll("Низкий", "Средний", "Высокий");
        priorityCombo.setValue("Средний");

        statusCombo.getItems().addAll("Новая", "В работе", "Готово");
        statusCombo.setValue("Новая");

        errorLabel.setVisible(false);
    }

    public void setTask(Task task) {
        if (task == null) return;
        existingTask = task;
        titleField.setText(task.getTitle());
        descArea.setText(task.getDescription());
        assigneeField.setText(task.getAssignee());

        priorityCombo.setValue(task.getPriorityLabel());
        statusCombo.setValue(task.getStatusLabel());
    }

    @FXML
    private void handleSave() {
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();
        String assignee = assigneeField.getText().trim();

        if (title.isEmpty()) {
            showError("Укажите название задачи");
            return;
        }
        if (assignee.isEmpty()) {
            showError("Укажите исполнителя");
            return;
        }

        String priority = switch (priorityCombo.getValue()) {
            case "Высокий" -> "high";
            case "Средний" -> "medium";
            default -> "low";
        };
        String status = switch (statusCombo.getValue()) {
            case "В работе" -> "in_progress";
            case "Готово" -> "done";
            default -> "new";
        };

        saveBtn.setDisable(true);
        errorLabel.setVisible(false);

        Task task = new Task(title, desc, status, priority, assignee);

        new Thread(() -> {
            boolean ok;
            if (existingTask == null) {
                ok = taskService.createTask(task);
            } else {
                existingTask.setTitle(title);
                existingTask.setDescription(desc);
                existingTask.setAssignee(assignee);
                existingTask.setPriority(priority);
                ok = taskService.updateStatus(existingTask.getId(), status);
            }
            final boolean success = ok;
            Platform.runLater(() -> {
                if (success) {
                    saved = true;
                    close();
                } else {
                    saveBtn.setDisable(false);
                    showError("Ошибка сохранения. Проверьте соединение.");
                }
            });
        }).start();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    public boolean isSaved() {
        return saved;
    }

    private void close() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}
