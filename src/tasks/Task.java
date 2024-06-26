package tasks;

import enums.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final int id;
    private final String name;
    private final String description;
    private final TaskStatus taskStatus;
    private final long duration;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public Task(String name, String description, TaskStatus taskStatus, Duration duration) {
        this.duration = duration.toMinutes();
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        // Для создания Таска/Сабки в мейне без стартайма
        startTime = null;
        endTime = null;
        this.id = 0;
    }

    public Task(String name, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        this.duration = duration.toMinutes();
        this.startTime = startTime;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.endTime = startTime.plus(duration);
        // Для создания Таска/Сабки в мейне
        this.id = 0;
    }

    public Task(int id, String name, String description, TaskStatus taskStatus, Duration duration) {
        this.duration = duration.toMinutes();
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        // Для создания Таска/Сабки в InMemoryTaskManager без стартайма
        startTime = null;
        endTime = null;
    }

    public Task(int id, String name, String description,
                TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        this.duration = duration.toMinutes();
        this.startTime = startTime;
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.endTime = startTime.plus(duration);
        // Для создания Таска/Сабки в InMemoryTaskManager + ендтайм появляется
    }

    public Task(String name, String description, TaskStatus taskStatus, LocalDateTime startTime) {
        this.startTime = startTime;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        // Для создание Епика в мейне
        duration = 0;
        endTime = null;
        this.id = 0;
    }

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        // Для создание Епика в мейне без стартайма
        duration = 0;
        startTime = null;
        endTime = null;
        this.id = 0;
    }

    public Task(int id, String name, String description, TaskStatus taskStatus, LocalDateTime startTime) {
        this.id = id;
        this.startTime = startTime;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        // Для создания Епика в InMemoryTaskManager
        duration = 0;
        endTime = null;
    }

    public Task(int id, String name, String description, TaskStatus taskStatus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        // Для создания Епика в InMemoryTaskManager без стартайма
        duration = 0;
        startTime = null;
        endTime = null;
    }

    public Task(int id, String name, String description,
                TaskStatus taskStatus, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.duration = duration.toMinutes();
        this.startTime = startTime;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.endTime = endTime;
        // Для создания Епика в checkEpic()
    }


    public int getId() {
        return id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Tasks.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (id == task.id) return true;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && taskStatus == task.taskStatus && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Duration getDuration() {
        return Duration.ofMinutes(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}



