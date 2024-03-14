package taskManagers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
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
            StringBuilder sb = new StringBuilder("id,type,name,status,description,epic\n");

            for (Task task : super.getTasks()) {
                sb.append(csv.fromTask(task));
            }

            for (Epic epic : super.getEpics()) {
                sb.append(csv.fromTask(epic));
            }

            for (Subtask subtask : super.getSubTasks()) {
                sb.append(csv.fromTask(subtask));
            }
            sb.append("\n");

            for (Task task : getHistory()) {
                sb.append(task.getId()).append(",");
            }
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
            while (br.ready()) {
                line = br.readLine();
                String[] row = line.split(",");
                if (!line.isBlank()) {
                    switch (row[1]) {
                        case "Task":
                            Task task = csv.fromString(row);
                            manager.tasks.put(task.getId(), task);
                            break;
                        case "Epic":
                            Epic epic = (Epic) csv.fromString(row);
                            manager.epics.put(epic.getId(), epic);
                            break;
                        case "Subtask":
                            Subtask subtask = (Subtask) csv.fromString(row);
                            int epicId = subtask.getEpicId();
                            Epic subTaskEpic = manager.epics.get(epicId);
                            if (subTaskEpic != null) {
                                subTaskEpic.getSubTaskList().add(subtask);
                                manager.statusCheckSubTask(subTaskEpic);
                                manager.subTasks.put(subtask.getId(), subtask);
                            }
                            break;
                    }
                }
            }
            String[] row = line.split(",");
            for (String s : row) {
                int id = Integer.parseInt(s);
                manager.hisManager.add(manager.getTask(id));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка");
        }
        return manager;
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
    public List<Subtask> getEpicSubTaskList(int id) {
        return super.getEpicSubTaskList(id);
    }

    @Override
    public Subtask addSubTask(Subtask subtask) {
        Subtask newSubtask = super.addSubTask(subtask);
        save();
        return newSubtask;
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
    public List<Subtask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public Task updTask(Task task) {
        Task newTask = super.updTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic updEpic(Epic epic) {
        Epic newEpic = super.updEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask updSubTask(Subtask subtask) {
        Subtask newSubtask = super.updSubTask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public void removeAll() {
        super.removeAll();
        save();
    }

    @Override
    public Task getTask(int id) {
        return super.getTask(id);
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

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void statusCheckSubTask(Epic subTaskEpic) {
        super.statusCheckSubTask(subTaskEpic);
    }

}
