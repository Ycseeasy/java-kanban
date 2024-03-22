package Managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    int addTask(Task task);

    int addSubTask(Subtask subtask);

    int addEpic(Epic epic);

    List<Subtask> getEpicSubTaskList(int id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubTasks();

    List<Task> getPrioritizedTasks();

    int updTask(Task task);

    int updEpic(Epic epic);

    int updSubTask(Subtask subtask);

    void removeAll();

    Task searchTask(int id);

    void deleteTask(int id);

    List<Task> getHistory();
}
