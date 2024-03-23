package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVTaskFormatter {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm");

    public String fromTask(Task task) {
        StringBuilder sb = new StringBuilder();
        int id = task.getId();
        String type;
        switch (task.getClass().toString()) {
            case "class tasks.Task":
                type = "Task";
                break;
            case "class tasks.Epic":
                type = "Epic";
                break;
            case "class tasks.Subtask":
                type = "Subtask";
                break;
            default:
                type = null;
                break;
        }
        LocalDateTime endTime = task.getEndTime();
        String name = task.getName();
        String taskStatus = task.getTaskStatus().toString();
        String description = task.getDescription();
        long duration = task.getDuration().toMinutes();
        String startTimeS = null;
        if (task.getStartTime() != null) {
            LocalDateTime startTime = task.getStartTime();
            startTimeS = startTime.format(formatter);
        }
        sb.append(id);
        sb.append(",");
        sb.append(type);
        sb.append(",");
        sb.append(name);
        sb.append(",");
        sb.append(taskStatus);
        sb.append(",");
        sb.append(description);
        sb.append(",");
        sb.append(duration);
        sb.append(",");
        sb.append(startTimeS);
        sb.append(",");

        if (endTime != null) {
            String endTimeS = endTime.format(formatter);
            sb.append(endTimeS);
        }

        if (task.getClass() == Subtask.class) {
            int epicId = ((Subtask) task).getEpicId();
            sb.append(epicId);
        }
        sb.append("\r\n");

        return sb.toString();
    }

    public Task fromString(String[] texts) {
        int id = Integer.parseInt(texts[0]);
        TaskStatus status = TaskStatus.valueOf(texts[3]);
        Duration duration = Duration.ofMinutes(Long.parseLong(texts[5]));
        LocalDateTime startTime = LocalDateTime.parse(texts[6], formatter);
        switch (texts[1]) {
            case "Task":
                return new Task(id, texts[2], texts[4], status, duration, startTime);
            case "Epic":
                LocalDateTime endTime = LocalDateTime.parse(texts[7], formatter);
                return new Epic(id, texts[2], texts[4], status, duration, startTime, endTime);
            case "Subtask":
                int epicId = Integer.parseInt(texts[7]);
                return new Subtask(id, texts[2], texts[4], status, epicId, duration, startTime);
            default:
                return null;
        }
    }

}
