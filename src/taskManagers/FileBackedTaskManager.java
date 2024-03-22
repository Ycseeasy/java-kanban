package taskManagers;

import Exception.ManagerSaveException;
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
                                    manager.prioritizedCheck(task.getId());
                                }
                            }
                            break;
                        case "Epic":
                            Epic epic = (Epic) csv.fromString(row);
                            manager.epics.put(epic.getId(), epic);
                            if (epic.getStartTime() != null) {
                                if (manager.timeCheckEpic(epic)) {
                                    manager.prioritizedCheck(epic.getId());
                                }
                            }
                            break;
                        case "Subtask":
                            Subtask subtask = (Subtask) csv.fromString(row);
                            int epicId = subtask.getEpicId();
                            if (manager.epics.containsKey(epicId)) {
                                Epic epic2 = manager.epics.get(epicId);
                                manager.subTasks.put(subtask.getId(), subtask);
                                manager.checkEpic(epic2);
                                if (subtask.getStartTime() != null) {
                                    if (manager.timeCheck(subtask)) {
                                        manager.prioritizedCheck(subtask.getId());
                                    }
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
    public int addTask(Task task) {
        int trueNumber = super.addTask(task);
        save();
        return trueNumber;
    }

    @Override
    public int addEpic(Epic epic) {
        int trueNumber = super.addEpic(epic);
        save();
        return trueNumber;
    }

    @Override
    public List<Subtask> getEpicSubTaskList(int id) {
        return super.getEpicSubTaskList(id);
    }

    @Override
    public int addSubTask(Subtask subtask) {
        int trueNumber = super.addSubTask(subtask);
        save();
        return trueNumber;
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
    public int updTask(Task task) {
        int newId = super.updTask(task);
        save();
        return newId;
    }

    @Override
    public int updEpic(Epic epic) {
        int newId = super.updEpic(epic);
        save();
        return newId;
    }

    @Override
    public int updSubTask(Subtask subtask) {
        int trueNumber = super.updSubTask(subtask);
        save();
        return trueNumber;
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
