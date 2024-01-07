import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    int id = 0;


    public int addTask(Task task) {
        task = new Task(id++, task.getName(), task.getDescription(), task.getTaskStatus());
        tasks.put(task.getId(), task);
        return id;
    }

    public int addEpic(Epic epic) {
        epic = new Epic(id++, epic.getName(), epic.getDescription(), epic.getTaskStatus());
        epics.put(epic.getId(), epic);
        return id;
    }

    public ArrayList<Subtask> getEpicSubTaskList(int id) {
        return epics.get(id).getSubTaskList();
    }

    public int addSubTask(Subtask subtask, int epicId) {
        subtask = new Subtask(id++, subtask.getName(), subtask.getDescription(), subtask.getTaskStatus());
        subtask.setEpicId(epicId);
        Epic subTaskEpic = searchEpic(epicId);
        ArrayList<Subtask> epicSubTaskList = subTaskEpic.getSubTaskList();
        epics.get(epicId).getSubTaskList().add(subtask);
        statusCheckSubTask(subTaskEpic);
        subTasks.put(subtask.getId(), subtask);
        return id;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epics.values());
    }

    public ArrayList<Subtask> getSubTasks() {
        return new ArrayList<Subtask>(subTasks.values());
    }

    public void updTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updSubTask(Subtask subtask) {
        Epic subTaskEpic = searchEpic(subtask.getEpicId());
        ArrayList<Subtask> epicSubTaskList = subTaskEpic.getSubTaskList();

        for (int i = 0; i < epicSubTaskList.size(); i++) {
            if (epicSubTaskList.get(i).getId() == id) {
                epicSubTaskList.set(i, subtask);
            }
        }

        statusCheckSubTask(subTaskEpic);
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
        ArrayList<Subtask> deletedList = searchEpic(id).getSubTaskList();
        for (Subtask sub : deletedList) {
            subTasks.remove(sub);
        }
        deletedList.clear();
        epics.remove(id);
    }

    public void deleteSubTask(int id) {
        Subtask searchSub = searchSubTask(id);
        int delEpicId = searchSub.getEpicId();
        Epic searchEpic = searchEpic(delEpicId);
        ArrayList<Subtask> delListEpicId = searchEpic.getSubTaskList();
        delListEpicId.remove(searchSub);
        statusCheckSubTask(searchEpic);
        subTasks.remove(id);
    }

    public void statusCheckSubTask(Epic subTaskEpic) {
        ArrayList<Subtask> epicSubTaskList = subTaskEpic.getSubTaskList();
        int checkDone = 0;
        int chekNew = 0;
        for (Subtask subtask : epicSubTaskList) {
            if (subtask.getTaskStatus().equals(TaskStatus.DONE)) {
                checkDone += 1;
            } else if (subtask.getTaskStatus().equals(TaskStatus.NEW)) {
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

