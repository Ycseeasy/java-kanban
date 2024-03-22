package taskManagers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subTasks = new HashMap<>();
    protected final Set<Task> prioritizedTime = new TreeSet<>(new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.getStartTime().isAfter(task2.getStartTime())) return 1;
            if (task1.getStartTime().isBefore(task2.getStartTime())) return -1;
            return 0;
        }
    });
    protected final HashMap<LocalDateTime, Boolean> timeKart = new HashMap<>();
    int id = 0;

    protected final InMemoryHistoryManager hisManager = Managers.getDefaultHistory();


    @Override
    public int addTask(Task task) {
        task.setId(id++);
        if (task.getStartTime() == null) {
            tasks.put(task.getId(), task);
            return task.getId();
        } else {
            if (timeCheck(task)) {
                tasks.put(task.getId(), task);
                prioritizedCheck(task.getId());
                return task.getId();
            } else {
                return -1;
            }
        }
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(id++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public List<Subtask> getEpicSubTaskList(int id) {
        List<Subtask> subList = new ArrayList<>(subTasks.values());
        return subList
                .stream()
                .filter(subtask -> subtask.getEpicId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public int addSubTask(Subtask subtask) {
        subtask.setId(id++);
        int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            if (subtask.getStartTime() != null) {
                if (timeCheck(subtask)) {
                    subTasks.put(subtask.getId(), subtask);
                    prioritizedCheck(subtask.getId());
                    if (checkEpic(epic) != -1) {
                        return subtask.getId();
                    } else return -1;
                } else {
                    return -1;
                }
            } else {
                subTasks.put(subtask.getId(), subtask);
                if (checkEpic(epic) != -1) {
                    return subtask.getId();
                } else return -1;
            }
        } else {
            return -1;
        }
    }


    @Override
    public List<Task> getTasks() {
        return new ArrayList<Task>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<Epic>(epics.values());
    }

    @Override
    public List<Subtask> getSubTasks() {
        return new ArrayList<Subtask>(subTasks.values());
    }

    @Override
    public int updTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return -1;
        } else {
            if (task.getStartTime() != null) {
                if (timeCheck(task)) {
                    tasks.put(task.getId(), task);
                    prioritizedCheck(task.getId());
                } else return -1;
            } else tasks.put(task.getId(), task);
            return task.getId();
        }
    }

    @Override
    public int updEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return -1;
        } else {
            return checkEpic(epic);
        }
    }

    @Override
    public int updSubTask(Subtask subtask) {
        if (!subTasks.containsKey(subtask.getId())) {
            return -1;
        } else {
            if (!epics.containsKey(subtask.getEpicId())) {
                return -1;
            } else {
                Epic subTaskEpic = (Epic) getTask(subtask.getEpicId());
                if (subtask.getStartTime() != null) {
                    if (timeCheck(subtask)) {
                        subTasks.put(subtask.getId(), subtask);
                        prioritizedCheck(subtask.getId());
                    } else return -1;

                } else subTasks.put(subtask.getId(), subtask);
                if (checkEpic(subTaskEpic) != -1) {
                    return subtask.getId();
                } else return -1;
            }
        }
    }

    @Override
    public void removeAll() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        prioritizedTime.clear();
    }


    protected Task getTask(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else return subTasks.getOrDefault(id, null);
    }

    @Override
    public Task searchTask(int id) {
        if (tasks.containsKey(id)) {
            hisManager.add(tasks.get(id));
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            hisManager.add(epic);
            return epic;
        } else if (subTasks.containsKey(id)) {
            Subtask subtask = subTasks.get(id);
            hisManager.add(subtask);
            return subtask;
        } else {
            return null;
        }
    }

    @Override
    public void deleteTask(int id) {
        hisManager.remove(id);
        prioritizedTime.remove(getTask(id));
        if (epics.containsKey(id)) {
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            Subtask subtask = (Subtask) getTask(id);
            Epic epic = (Epic) getTask(subtask.getEpicId());
            subTasks.remove(id);
            checkEpic(epic);
        } else {
            tasks.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return hisManager.getHistory();
    }

    public int checkEpic(Epic subTaskEpic) {
        List<Subtask> subList = new ArrayList<>(subTasks.values());
        Duration epickDuration = Duration.ofMinutes(0);
        List<LocalDateTime> latestDateList = new ArrayList<>();
        List<Subtask> epicSubTaskList = subList
                .stream()
                .filter(subtask -> subtask.getEpicId() == subTaskEpic.getId())
                .filter(subtask -> subtask.getEndTime() != null)
                .map(subtask -> {
                    if (latestDateList.isEmpty()) {
                        latestDateList.add(0, subtask.getEndTime());
                    }
                    if (subtask.getStartTime().isAfter(latestDateList.get(0))) {
                        latestDateList.add(0, subtask.getEndTime());
                    }
                    return subtask;
                })
                .collect(Collectors.toList());

        for (Subtask subtask : epicSubTaskList) {
            epickDuration = epickDuration.plus(subtask.getDuration());
        }
        LocalDateTime latestDate = null;
        if (!latestDateList.isEmpty()) {
            latestDate = latestDateList.get(0);
        }
        Epic newEpic = new Epic(subTaskEpic.getId(), subTaskEpic.getName(), subTaskEpic.getDescription(),
                subTaskEpic.getTaskStatus(), epickDuration, subTaskEpic.getStartTime(), latestDate);
        if (latestDate != null) {
            if (timeCheckEpic(newEpic)) {
                prioritizedTime.add(newEpic);
            } else return -1;
        }

        List<Subtask> tasksDONE = epicSubTaskList
                .stream()
                .filter(subtask -> subtask.getTaskStatus() == TaskStatus.DONE)
                .collect(Collectors.toList());

        List<Subtask> tasksNEW = epicSubTaskList
                .stream()
                .filter(subtask -> subtask.getTaskStatus() == TaskStatus.NEW)
                .collect(Collectors.toList());

        if (epicSubTaskList.isEmpty()) {
            newEpic = (new Epic(subTaskEpic.getId(), subTaskEpic.getName(), subTaskEpic.getDescription(),
                    TaskStatus.NEW, epickDuration, subTaskEpic.getStartTime(), latestDate));
            epics.put(newEpic.getId(), newEpic);
            return newEpic.getId();
        } else {
            if (tasksDONE.size() == epicSubTaskList.size()) {
                newEpic = new Epic(subTaskEpic.getId(), subTaskEpic.getName(), subTaskEpic.getDescription(),
                        TaskStatus.DONE, epickDuration, subTaskEpic.getStartTime(), latestDate);
                epics.put(newEpic.getId(), newEpic);
                return newEpic.getId();
            } else if (tasksNEW.size() == epicSubTaskList.size()) {
                newEpic = new Epic(subTaskEpic.getId(), subTaskEpic.getName(), subTaskEpic.getDescription(),
                        TaskStatus.NEW, epickDuration, subTaskEpic.getStartTime(), latestDate);
                epics.put(newEpic.getId(), newEpic);
                return newEpic.getId();
            } else {
                newEpic = new Epic(subTaskEpic.getId(), subTaskEpic.getName(), subTaskEpic.getDescription(),
                        TaskStatus.IN_PROGRESS, epickDuration, subTaskEpic.getStartTime(), latestDate);
                epics.put(newEpic.getId(), newEpic);
                return newEpic.getId();
            }
        }
    }

    public void prioritizedCheck(int id) {
        prioritizedTime.add(getTask(id));
    }

    public boolean timeCheck(Task task) {
        if (prioritizedTime.isEmpty()) return true;
        for (Task t : prioritizedTime) {
            if (t.getId() != task.getId()) {
                if (!epics.containsKey(t.getId())) {
                    if (task.getStartTime().isBefore(t.getEndTime())
                            && task.getEndTime().isAfter(t.getStartTime())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean timeCheckEpic(Epic epic) {
        if (prioritizedTime.isEmpty()) return true;
        for (Task t : prioritizedTime) {
            if (t.getId() != epic.getId()) {
                if (epics.containsKey(t.getId())) {
                    if (epic.getStartTime().isBefore(t.getEndTime())
                            && epic.getEndTime().isAfter(t.getStartTime())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTime);
    }
}

