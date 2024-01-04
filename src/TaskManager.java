import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static HashMap<Integer, Task> tasks = new HashMap<>();
    private static HashMap<Integer, Epic> epics = new HashMap<>();
    private static HashMap<Integer, Subtask> subTasks = new HashMap<>();
    static int id = 0;

    public Task task(String name, String description, TaskStatus taskStatus) {
        return new Task(id++, name, description, taskStatus);
    }

    public Epic epic(String name, String description, TaskStatus taskStatus) {
        return new Epic(id++, name, description, TaskStatus.NEW);
    }

    public Subtask subTask(String name, String description, TaskStatus taskStatus) {
        return new Subtask(id++, name, description, taskStatus);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public ArrayList<Subtask> epicSubTaskList(int id) {

        for (int i = 0; i < epics.size(); i++) {
            if (id == epics.get(i).getId()) {
                return epics.get(i).getSubTaskList();
            }
        }

        return null;
    }

    public void addSubTask(Subtask subtask, int epicId) {
        subtask.setEpicId(epicId);
        Epic subTaskEpic = searchEpic(epicId);
        ArrayList<Subtask> epicSubTaskList = subTaskEpic.getSubTaskList();

        for (int i = 0; i < epics.size(); i++) {
            if (epics.get(i).getId() == epicId) {
                epics.get(i).getSubTaskList().add(subtask);
            }
        }

        statusCheckSubTask(epicSubTaskList, subTaskEpic);
        subTasks.put(subtask.getId(), subtask);
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubTasks() {
        return subTasks;
    }

    public void updTask(Task task, int id) {
        task.setId(id);
        tasks.put(task.getId(), task);
    }

    public void updEpic(Epic epic, int id) {
        epic.setSubTaskList(searchEpic(id).getSubTaskList());
        epic.setId(id);
        epics.put(epic.getId(), epic);
    }

    public void updSubTask(Subtask subtask, int id, int epicId) {

        Epic subTaskEpic = searchEpic(epicId);
        ArrayList<Subtask> epicSubTaskList = subTaskEpic.getSubTaskList();

        for (int i = 0; i < epicSubTaskList.size(); i++) {
            if (epicSubTaskList.get(i).getId() == id) {
                epicSubTaskList.set(i, subtask);
            }
        }

        statusCheckSubTask(epicSubTaskList, subTaskEpic);
        subtask.setId(id);
        subTasks.put(subtask.getId(), subtask);
    }

    public void removeAll() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    public Task searchTask(int id) {
        return tasks.get(id);
    }

    public Epic searchEpic(int id) {
        return epics.get(id);
    }

    public Subtask searchSubTask(int id) {
        return subTasks.get(id);
    }


    public void delete(int id) {
        if (tasks.get(id) != null) {
            tasks.remove(id);
        } else if (epics.get(id) != null) {
            epics.remove(id);
        } else if (subTasks.get(id) != null) {
            subTasks.remove(id);
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        searchEpic(id).getSubTaskList().clear();
        epics.remove(id);
    }

    public void deleteSubTask(int id) {
        int delEpicId = searchSubTask(id).getEpicId();
        ArrayList<Subtask> delListEpicId = searchEpic(delEpicId).getSubTaskList();
        delListEpicId.remove(searchSubTask(id));
        statusCheckSubTask(delListEpicId, searchEpic(delEpicId));
        subTasks.remove(id);
    }

    public void statusCheckSubTask(ArrayList<Subtask> epicSubTaskList, Epic subTaskEpic) {

        int checkDone = 0;
        int chekNew = 0;
        for (Subtask subtask : epicSubTaskList) {
            if (subtask.getTaskStatus().equals(TaskStatus.DONE)) {
                checkDone += 1;
            } else if (subtask.getTaskStatus().equals(TaskStatus.NEW)) {
                chekNew += 1;
            }
        }

        if (checkDone == epicSubTaskList.size()) {
            subTaskEpic.setTaskStatus(TaskStatus.DONE);
        } else if (chekNew == epicSubTaskList.size()) {
            subTaskEpic.setTaskStatus(TaskStatus.NEW);
        } else {
            subTaskEpic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }

    }

}

