package TaskManagers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();
    int id = 0;

    private final InMemoryHistoryManager hisManager = Managers.getDefaultHistory();


    @Override
    public Task addTask(Task task) {
        task.setId(id++);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(id++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public ArrayList<Subtask> getEpicSubTaskList(int id) {
        return epics.get(id).getSubTaskList();
    }

    @Override
    public Subtask addSubTask(Subtask subtask) {
        subtask.setId(id++);
        int epicId = subtask.getEpicId();
        Epic subTaskEpic = epics.get(epicId);
        if (subTaskEpic == null) {
            return null;
        } else {
            subTaskEpic.getSubTaskList().add(subtask);
            statusCheckSubTask(subTaskEpic);
            subTasks.put(subtask.getId(), subtask);
            return subtask;
        }
    }


    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubTasks() {
        return new ArrayList<Subtask>(subTasks.values());
    }

    @Override
    public Task updTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return null;
        } else {
            tasks.put(task.getId(), task);
            return task;
        }
    }

    @Override
    public Epic updEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return null;
        } else {
            epics.put(epic.getId(), epic);
            return epic;
        }
    }

    @Override
    public Subtask updSubTask(Subtask subtask) {
        if (!subTasks.containsKey(subtask.getId())) {
            return null;
        } else {
            Epic subTaskEpic = getEpic(subtask.getEpicId());
            if (subTaskEpic == null) {
                return null;
            } else {
                ArrayList<Subtask> epicSubTaskList = subTaskEpic.getSubTaskList();

                for (int i = 0; i < epicSubTaskList.size(); i++) {
                    if (epicSubTaskList.get(i).getId() == subtask.getId()) {
                        epicSubTaskList.set(i, subtask);
                    }
                }

                statusCheckSubTask(subTaskEpic);
                subTasks.put(subtask.getId(), subtask);
                return subtask;
            }
        }
    }

    @Override
    public void removeAll() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTask(int id) {
        hisManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        hisManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubTask(int id) {
        hisManager.add(subTasks.get(id));
        return subTasks.get(id);
    }


    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        ArrayList<Subtask> deletedList = getEpic(id).getSubTaskList();
        for (Subtask sub : deletedList) {
            subTasks.remove(sub.getId());
        }
        deletedList.clear();
        epics.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        Subtask searchSub = getSubTask(id);
        int delEpicId = searchSub.getEpicId();
        Epic getEpic = getEpic(delEpicId);
        ArrayList<Subtask> delListEpicId = getEpic.getSubTaskList();
        delListEpicId.remove(searchSub);
        statusCheckSubTask(getEpic);
        subTasks.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return hisManager.getHistory();
    }

    public void statusCheckSubTask(Epic subTaskEpic) {
        ArrayList<Subtask> epicSubTaskList = subTaskEpic.getSubTaskList();
        int checkDone = 0;
        int chekNew = 0;
        for (Subtask subtask : epicSubTaskList) {
            if (subtask.getTaskStatus() == TaskStatus.DONE) {
                checkDone += 1;
            } else if (subtask.getTaskStatus() == TaskStatus.NEW) {
                chekNew += 1;
            }
        }
        if (epicSubTaskList.isEmpty()) {
            subTaskEpic.setTaskStatus(TaskStatus.NEW);
        } else {
            if (checkDone == epicSubTaskList.size()) {
                subTaskEpic.setTaskStatus(TaskStatus.DONE);
            } else if (chekNew == epicSubTaskList.size()) {
                subTaskEpic.setTaskStatus(TaskStatus.NEW);
            } else {
                subTaskEpic.setTaskStatus(TaskStatus.IN_PROGRESS);
            }

        }

    }
}

