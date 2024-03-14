package taskManagers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class CSVTaskFormatter {

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
        String name = task.getName();
        String taskStatus = task.getTaskStatus().toString();
        String description = task.getDescription();

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
        switch (texts[1]) {
            case "Task":
                return new Task(id, texts[2], texts[4], status);
            case "Epic":
                return new Epic(id, texts[2], texts[4], status);
            case "Subtask":
                int epicId = Integer.parseInt(texts[5]);
                return new Subtask(id, texts[2], texts[4], status, epicId);
            default:
                return null;
        }
    }

}
