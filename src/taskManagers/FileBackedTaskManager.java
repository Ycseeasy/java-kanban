package taskManagers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public void save() {
        /// save file
    }


    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask addSubTask(Subtask subtask) {
        super.addSubTask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task updTask(Task task) {
        super.updTask(task);
        save();
        return task;
    }

    @Override
    public Epic updEpic(Epic epic) {
        super.updEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask updSubTask(Subtask subtask) {
        super.updSubTask(subtask);
        save();
        return subtask;
    }

    @Override
    public void removeAll() {
        super.removeAll();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task searchTask = super.getTask(id);
        save();
        return searchTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic searchEpic = super.getEpic(id);
        save();
        return searchEpic;
    }

    @Override
    public Subtask getSubTask(int id) {
        Subtask searchSubtask = super.getSubTask(id);
        save();
        return searchSubtask;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

}
