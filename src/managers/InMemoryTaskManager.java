package managers;

import exception.ManagerAddException;
import exception.ManagerUpdException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
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
    int id = 0;

    protected final InMemoryHistoryManager hisManager = Managers.getDefaultHistory();


    @Override
    public Task addTask(Task task) throws ManagerAddException {
        try {
            if (task.getStartTime() != null && task.getDuration() == Duration.ofMinutes(0)) {
                throw new ManagerAddException("При заданной продолжительности нет времени начала");
            }
            if (task.getStartTime() == null && task.getDuration() != Duration.ofMinutes(0)) {
                throw new ManagerAddException("При заданном времени начала нет продолжительности");
            }
            if (task.getStartTime() == null) {
                Task addedTask = new Task(id, task.getName(), task.getDescription(), task.getTaskStatus());
                tasks.put(addedTask.getId(), addedTask);
                id++;
                return addedTask;
            }
            Task addedTask = new Task(id, task.getName(), task.getDescription(), task.getTaskStatus(),
                    task.getDuration(), task.getStartTime());
            if (timeCheck(addedTask)) {
                tasks.put(addedTask.getId(), addedTask);
                id++;
                addToPrioritizedList(addedTask.getId());
                return addedTask;
            } else {
                throw new ManagerAddException("Ошибка пересечения времени");
            }
        } catch (ManagerAddException e) {
            e.printTextError();
            return null;
        }
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic addedEpic;
        if (epic.getStartTime() == null) {
            addedEpic = new Epic(id, epic.getName(), epic.getDescription(), epic.getTaskStatus());
        } else {
            addedEpic = new Epic(id, epic.getName(), epic.getDescription(),
                    epic.getTaskStatus(), epic.getStartTime());
        }
        epics.put(addedEpic.getId(), addedEpic);
        id++;
        return addedEpic;
    }

    @Override
    public List<Subtask> getEpicSubTaskList(int id) {
        Epic epic = (Epic) getTask(id);
        return epic.getEpicsSubtask();
    }

    @Override
    public Subtask addSubTask(Subtask subtask) {
        try {
            if (!epics.containsKey(subtask.getEpicId())) {
                throw new ManagerAddException("Не найден епик сабтаски на входе");
            }

            if (subtask.getStartTime() != null && subtask.getDuration() == Duration.ofMinutes(0)) {
                throw new ManagerAddException("При заданном времени начала нет продолжительности");
            }

            if (subtask.getStartTime() == null && subtask.getDuration() != Duration.ofMinutes(0)) {
                throw new ManagerAddException("При заданной продолжительности нет времени начала");
            }

            Epic motherEpic = (Epic) getTask(subtask.getEpicId());
            if (subtask.getStartTime() == null) {
                Subtask addedSubtask = new Subtask(id, subtask.getName(), subtask.getDescription(),
                        subtask.getTaskStatus(), subtask.getEpicId());
                id++;
                motherEpic.addToSubList(addedSubtask);
                Epic updatedEpic = updEpic(motherEpic);
                if (updatedEpic != null) {
                    updatedEpic.addToSubList(addedSubtask);
                    subTasks.put(addedSubtask.getId(), addedSubtask);
                    return addedSubtask;
                } else {
                    motherEpic.removeFromList(addedSubtask);
                    throw new ManagerAddException("Ошибка при обновлении Епика");
                }
            }

            Subtask addedSubtask = new Subtask(id, subtask.getName(), subtask.getDescription(),
                    subtask.getTaskStatus(), subtask.getEpicId(), subtask.getDuration(), subtask.getStartTime());
            id++;
            if (timeCheck(addedSubtask)) {
                motherEpic.addToSubList(addedSubtask);
                Epic updatedEpic = updEpic(motherEpic);
                if (updatedEpic != null) {
                    updatedEpic.addToSubList(addedSubtask);
                    subTasks.put(addedSubtask.getId(), addedSubtask);
                    addToPrioritizedList(addedSubtask.getId());
                    return addedSubtask;
                } else {
                    motherEpic.removeFromList(addedSubtask);
                    throw new ManagerAddException("Ошибка при обновлении Епика");
                }
            } else throw new ManagerAddException("Ошибка пересечения времени");
        } catch (ManagerAddException e) {
            e.printTextError();
            return null;
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
    public Task updTask(Task task) {
        try {
            if (!tasks.containsKey(task.getId())) {
                throw new ManagerUpdException("Не найден старый таск");
            }
            if (task.getStartTime() != null && task.getDuration() == Duration.ofMinutes(0)) {
                throw new ManagerUpdException("При заданном времени начала нет продолжительности");
            }
            if (task.getStartTime() == null && task.getDuration() != Duration.ofMinutes(0)) {
                throw new ManagerUpdException("При заданной продолжительности нет времени начала");
            }
            if (task.getStartTime() == null) {
                tasks.put(task.getId(), task);
                return task;
            }
            if (timeCheck(task)) {
                tasks.put(task.getId(), task);
                addToPrioritizedList(task.getId());
                return task;
            } else {
                throw new ManagerUpdException("Ошибка пересечения времени");
            }
        } catch (ManagerUpdException e) {
            e.printTextError();
            return null;
        }
    }

    @Override
    public Epic updEpic(Epic epic) {
        try {
            if (!epics.containsKey(epic.getId())) {
                throw new ManagerUpdException("Не найден старый епик");
            }
            Epic udatedEpic = checkEpic(epic);
            if (udatedEpic != null) {
                epics.put(udatedEpic.getId(), udatedEpic);
                if (udatedEpic.getStartTime() != null) {
                    addToPrioritizedList(udatedEpic.getId());
                }
                return udatedEpic;
            } else {
                throw new ManagerUpdException("Произошла ошибка при обновлении Епика");
            }
        } catch (ManagerUpdException e) {
            e.printTextError();
            return null;
        }
    }

    @Override
    public Subtask updSubTask(Subtask subtask) {
        try {
            if (!subTasks.containsKey(subtask.getId())) {
                throw new ManagerUpdException("Не найдена старая Сабка");
            }
            if (!epics.containsKey(subtask.getEpicId())) {
                throw new ManagerAddException("Не найден епик сабтаски на входе");
            }
            if (subtask.getStartTime() != null && subtask.getDuration() == null) {
                throw new ManagerUpdException("При заданном времени начала нет продолжительности");
            }
            if (subtask.getStartTime() == null && subtask.getDuration() != null) {
                throw new ManagerUpdException("При заданной продолжительности нет времени начала");
            }
            Epic motherEpic = (Epic) getTask(subtask.getEpicId());
            if (subtask.getStartTime() == null) {
                motherEpic.addToSubList(subtask);
                Epic updatedEpic = updEpic(motherEpic);
                if (updatedEpic != null) {
                    subTasks.put(subtask.getId(), subtask);
                    return subtask;
                } else {
                    motherEpic.removeFromList(subtask);
                    throw new ManagerAddException("Ошибка при обновлении Епика");
                }
            }
            if (timeCheck(subtask)) {
                motherEpic.addToSubList(subtask);
                Epic updatedEpic = updEpic(motherEpic);
                if (updatedEpic != null) {
                    updatedEpic.addToSubList(subtask);
                    subTasks.put(subtask.getId(), subtask);
                    addToPrioritizedList(subtask.getId());
                    return subtask;
                } else {
                    motherEpic.removeFromList(subtask);
                    throw new ManagerAddException("Ошибка при обновлении Епика");
                }
            } else throw new ManagerUpdException("Ошибка пересечения времени");
        } catch (ManagerAddException e) {
            e.printTextError();
            return null;
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

    public Epic checkEpic(Epic epic) {
        Epic updEpic = epic;
        if (epic.getEpicsSubtask().isEmpty()) {
            return epic;
        }
        try {
            if (epic.getStartTime() != null) {
                updEpic = updDurationEpic(epic);
                if (updEpic == null) {
                    throw new ManagerUpdException("Ошибка обновления длительности епика");
                }
            }
            Epic lastUpdEpic = updStatusEpic(updEpic);
            if (lastUpdEpic == null) {
                throw new ManagerUpdException("Ошибка обновления статуса епика");
            } else {
                return lastUpdEpic;
            }
        } catch (ManagerUpdException e) {
            e.printTextError();
            return null;
        }
    }


    protected Epic updDurationEpic(Epic epic) {
        List<Subtask> subtaskList = epic.getEpicsSubtask();
        if (subtaskList.isEmpty()) {
            return new Epic(epic.getId(), epic.getName(), epic.getDescription(),
                    epic.getTaskStatus(), Duration.ofMinutes(0), epic.getStartTime(), null);
        }

        subtaskList = subtaskList
                .stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .collect(Collectors.toList());

        TreeSet<Subtask> latestList = new TreeSet<>(new Comparator<Subtask>() {
            @Override
            public int compare(Subtask task1, Subtask task2) {
                if (task1.getEndTime().isAfter(task2.getEndTime())) return 1;
                if (task1.getEndTime().isBefore(task2.getEndTime())) return -1;
                return 0;
            }
        });
        latestList.addAll(subtaskList);
        Subtask latestEndSubtask = latestList.last();
        Duration epickDuration = Duration.ofMinutes(0);
        for (Subtask subtask : subtaskList) {
            epickDuration = epickDuration.plus(subtask.getDuration());
        }
        Epic updEpic = new Epic(epic.getId(), epic.getName(), epic.getDescription(),
                epic.getTaskStatus(), epickDuration, epic.getStartTime(), latestEndSubtask.getEndTime());

        for (Subtask subtask : subtaskList) {
            updEpic.addToSubList(subtask);
        }

        try {
            if (timeCheckEpic(updEpic)) {
                return updEpic;
            } else throw new ManagerUpdException("Ошибка пересечения времени");
        } catch (ManagerUpdException e) {
            e.printTextError();
            return null;
        }
    }

    protected Epic updStatusEpic(Epic epic) {
        List<Subtask> subtaskList = epic.getEpicsSubtask();

        List<Subtask> tasksDONE = subtaskList
                .stream()
                .filter(subtask -> subtask.getTaskStatus() == TaskStatus.DONE)
                .collect(Collectors.toList());

        List<Subtask> tasksNEW = subtaskList
                .stream()
                .filter(subtask -> subtask.getTaskStatus() == TaskStatus.NEW)
                .collect(Collectors.toList());

        if (subtaskList.isEmpty()) {
            return (new Epic(epic.getId(), epic.getName(), epic.getDescription(),
                    TaskStatus.NEW, epic.getDuration(), epic.getStartTime(), epic.getEndTime()));
        } else {
            if (tasksDONE.size() == subtaskList.size()) {
                return (new Epic(epic.getId(), epic.getName(), epic.getDescription(),
                        TaskStatus.DONE, epic.getDuration(), epic.getStartTime(), epic.getEndTime()));
            } else if (tasksNEW.size() == subtaskList.size()) {
                return (new Epic(epic.getId(), epic.getName(), epic.getDescription(),
                        TaskStatus.NEW, epic.getDuration(), epic.getStartTime(), epic.getEndTime()));
            } else {
                return (new Epic(epic.getId(), epic.getName(), epic.getDescription(),
                        TaskStatus.IN_PROGRESS, epic.getDuration(), epic.getStartTime(), epic.getEndTime()));
            }
        }
    }

    public void addToPrioritizedList(int id) {
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

