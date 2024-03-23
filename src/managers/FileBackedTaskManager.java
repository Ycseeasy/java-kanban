package managers;

import exception.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        CSVTaskFormatter csv = new CSVTaskFormatter();
        try {
            PrintWriter pw = new PrintWriter(file);
            StringBuilder sb = new StringBuilder("id,type,name,status,description,duration,startTime,endTime,epicID\n");

            super.getTasks()
                    .stream()
                    .forEach(task -> sb.append(csv.fromTask(task)));

            super.getEpics()
                    .stream()
                    .forEach(epic -> sb.append(csv.fromTask(epic)));

            super.getSubTasks()
                    .stream()
                    .forEach(subtask -> sb.append(csv.fromTask(subtask)));

            sb.append("\n");

            getHistory()
                    .stream()
                    .forEach(task -> sb.append(task.getId()).append(","));

            pw.write(sb.toString());
            pw.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        CSVTaskFormatter csv = new CSVTaskFormatter();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (!line.isBlank()) {
                line = br.readLine();
                String[] row = line.split(",");
                if (!row[0].equals("id")) {
                    switch (row[1]) {
                        case "Task":
                            Task task = csv.fromString(row);
                            manager.tasks.put(task.getId(), task);
                            if (task.getStartTime() != null) {
                                if (manager.timeCheck(task)) {
                                    manager.addToPrioritizedList(task.getId());
                                }
                            }
                            break;
                        case "Epic":
                            Epic epic = (Epic) csv.fromString(row);
                            for (Subtask subtask : manager.getSubTasks()) {
                                if (subtask.getEpicId() == epic.getId()) {
                                    epic.addToSubList(subtask);
                                }
                            }
                            manager.epics.put(epic.getId(), epic);
                            if (epic.getStartTime() != null) {
                                if (manager.timeCheckEpic(epic)) {
                                    manager.addToPrioritizedList(epic.getId());
                                }
                            }
                            break;
                        case "Subtask":
                            Subtask subtask = (Subtask) csv.fromString(row);
                            Epic motherEpic = (Epic) manager.getTask(subtask.getEpicId());
                            if (motherEpic != null) {
                                motherEpic.addToSubList(subtask);
                                manager.subTasks.put(subtask.getId(), subtask);
                            }
                            if (subtask.getStartTime() != null) {
                                if (manager.timeCheck(subtask)) {
                                    manager.addToPrioritizedList(subtask.getId());
                                }
                            }
                            break;
                    }

                }
            }
            String[] row = line.split(",");
            if (!row[0].isBlank()) {
                Arrays.stream(row).map(s -> {
                    int id = Integer.parseInt(s);
                    manager.hisManager.add(manager.getTask(id));
                    return s;
                });
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка");
        }
        return manager;
    }


    @Override
    public Task addTask(Task task) {
        Task addTask = super.addTask(task);
        save();
        return addTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic addEpic = super.addEpic(epic);
        save();
        return addEpic;
    }

    @Override
    public List<Subtask> getEpicSubTaskList(int id) {
        return super.getEpicSubTaskList(id);
    }

    @Override
    public Subtask addSubTask(Subtask subtask) {
        Subtask addSubTask = super.addSubTask(subtask);
        save();
        return addSubTask;
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public Task updTask(Task task) {
        Task updTask = super.updTask(task);
        save();
        return updTask;
    }

    @Override
    public Epic updEpic(Epic epic) {
        Epic updEpic = super.updEpic(epic);
        save();
        return updEpic;
    }

    @Override
    public Subtask updSubTask(Subtask subtask) {
        Subtask updSubTask = super.updSubTask(subtask);
        save();
        return updSubTask;
    }

    @Override
    public void removeAll() {
        super.removeAll();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }
}
