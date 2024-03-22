package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String name, String description, TaskStatus taskStatus, int epicId) {

        super(id, name, description, taskStatus);
        this.epicId = epicId;

    }

    public Subtask(String name, String description, TaskStatus taskStatus, int epicId) {
        super(name, description, taskStatus);
        this.epicId = epicId;

    }

    public Subtask(int id, String name, String description, TaskStatus taskStatus,
                   int epicId, Duration duration, LocalDateTime startTime) {
        super(id, name, description, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus taskStatus,
                   int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, taskStatus, duration, startTime);
        this.epicId = epicId;
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
                ", epic ID=" + epicId +
                '}';
    }

    public int getEpicId() {
        return epicId;
    }

}
