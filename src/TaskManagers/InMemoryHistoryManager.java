package TaskManagers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

     private final List<Task> taskHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        Task taskForHistory = new Task(task.getId(), task.getName(), task.getDescription(), task.getTaskStatus());
        if (taskHistory.size() >= 10) {
            taskHistory.add(0, taskForHistory);
            taskHistory.remove(taskHistory.size() - 1);
        } else {
            taskHistory.add(taskForHistory);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}
