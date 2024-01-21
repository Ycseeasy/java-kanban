package taskManagers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task addTask(Task task);

    Subtask addSubTask(Subtask subtask);

    Epic addEpic(Epic epic);

    ArrayList<Subtask> getEpicSubTaskList(int id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubTasks();

    Task updTask(Task task);

    Epic updEpic(Epic epic);

    Subtask updSubTask(Subtask subtask);

    void removeAll();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubTask(int id);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id);

    List<Task> getHistory();
}
