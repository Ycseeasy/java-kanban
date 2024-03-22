package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        // Конструктор для создания из мейна без даты старта
    }

    public Epic(String name, String description, TaskStatus taskStatus, LocalDateTime startTime) {
        super(name, description, taskStatus, startTime);
        // Конструктор для создания из мейна
    }

    public Epic(int id, String name, String description,
                TaskStatus taskStatus, LocalDateTime startTime) {
        super(id, name, description, taskStatus, startTime);
        // Конструктор для создания из InMemoryTaskManager + для апдейта
    }
    public Epic(int id, String name, String description,
                TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
        // Конструктор для создания из InMemoryTaskManager без стартайма + для апдейта
    }

    public Epic(int id, String name, String description,
                TaskStatus taskStatus, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, description, taskStatus, duration, startTime, endTime);
        // Конструктор для создания из checkEpic()
    }

    @Override
    public String toString() {
        return "Tasks.Task{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", taskStatus=" + getTaskStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                '}';
    }
}