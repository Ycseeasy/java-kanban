import org.junit.jupiter.api.Test;
import managers.FileBackedTaskManager;
import managers.InMemoryHistoryManager;
import managers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    public void taskEquals() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW));
        Task task2 = new Task(task.getId(), "Task", "new", TaskStatus.NEW);
        assertEquals(manager.searchTask(task.getId()), task2);
    }

    @Test
    public void epicEquals() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW));
        Epic epic2 = new Epic(epic.getId(), "Epic", "new", TaskStatus.NEW);
        assertEquals(manager.searchTask(epic.getId()), epic2);
    }

    @Test
    public void subTaskEquals() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask",
                "new", TaskStatus.NEW, epic.getId()));
        Subtask subtask2 = new Subtask(subtask.getId(), "Subtask",
                "new", TaskStatus.NEW, epic.getId());
        assertEquals(manager.searchTask(subtask.getId()), subtask2);
    }

    @Test // Тест по 3 вопросу
    public void notAddSubTaskToNowhere() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        Subtask subtask = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс", TaskStatus.NEW, 7633337));
        assertNull(manager.searchTask(0));
    }

    @Test // Тест по 4 вопросу
    public void notUpdSubTaskToEpicOrElse() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask",
                "new", TaskStatus.NEW, epic.getId()));
        Epic newEpic = new Epic(subtask.getId(),
                "Из сабтаска", "Превращаюсь в Эпик", TaskStatus.NEW);
        manager.updEpic(newEpic);
        assertNotEquals(manager.searchTask(subtask.getId()), newEpic);
    }

    @Test // Тест по 5 вопросу
    public void utilitarianClassCheck() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        InMemoryHistoryManager hManager = Managers.getDefaultHistory();
        assertNotNull(manager);
        assertNotNull(hManager);
    }

    @Test
        // Тест по 6 вопросу
    void inMemoryTaskManagerWork() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        Task task = manager.addTask( new Task("Task", "new", TaskStatus.NEW));
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask",
                "new", TaskStatus.NEW, 1));
        assertEquals(new Task(task.getId(), "Task", "new", TaskStatus.NEW),
                manager.searchTask(task.getId()));
        assertEquals(new Epic(epic.getId(), "Epic", "new", TaskStatus.NEW), epic);
        assertEquals(new Subtask(subtask.getId(), "Subtask", "new",
                TaskStatus.NEW, epic.getId()),subtask);
    }

    @Test
        // Тест по 7 вопросу
    void createIdAndGenerateId() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        Task task = manager.addTask(new Task(123, "Task", "new", TaskStatus.NEW));
        Task task1 = new Task(123, "Task", "new", TaskStatus.NEW);
        assertNotEquals(task.getId(), task1.getId());
    }

    @Test
    void historyNotSaveViewSimilarTask() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW));
        Task task1 = manager.addTask(new Task("Task", "new", TaskStatus.NEW));
        manager.searchTask(task.getId());
        manager.searchTask(task.getId());
        manager.searchTask(task1.getId());
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(task.getId()), manager.searchTask(task.getId()));
        assertEquals(history.get(task1.getId()), manager.searchTask(task1.getId()));
    }

    @Test
    void shouldDoNotSaveOldIdINDeletedSubtaskInTaskManager() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();

        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId()));
        Subtask subtask1 = manager.addSubTask(new Subtask("Subtask1", "new", TaskStatus.NEW, epic.getId()));
        Subtask subtask2 = manager.addSubTask(new Subtask("Subtask2", "new", TaskStatus.NEW, epic.getId()));
        manager.deleteTask(subtask.getId());
        List<Subtask> subtasks = manager.getSubTasks();
        assertEquals(List.of(manager.searchTask(subtask1.getId()), manager.searchTask(subtask2.getId())), subtasks);
    }
}
