package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Subtask> epicsSubtask = new ArrayList<>();


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

    public Epic(int id, String name, String description, TaskStatus taskStatus,
                Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, description, taskStatus, duration, startTime, endTime);
        // Конструктор для создания из checkEpic()
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + super.getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", taskStatus=" + getTaskStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                ", size=" + getEpicsSubtask().size() +
                '}';
    }

    public List<Subtask> getEpicsSubtask() {
        return epicsSubtask;
    }

    public void addToSubList(Subtask subtask) {
        epicsSubtask.add(subtask);
    }

    public void removeFromList(Subtask subtask) {
        epicsSubtask.remove(subtask);
    }
}