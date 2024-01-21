package taskManagers;

import tasks.Task;

import java.util.List;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> taskHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        Task taskForHistory = new Task(task.getId(), task.getName(), task.getDescription(), task.getTaskStatus());
        if (taskHistory.size() >= 10) {
            taskHistory.removeFirst();
            taskHistory.add(taskForHistory);
        } else {
            taskHistory.add(taskForHistory);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}
