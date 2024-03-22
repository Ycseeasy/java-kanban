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

    @Test // Тест по 1 вопросу
    public void taskEquals() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        int task1id = manager.addTask(new Task("Гантели", "Поднимать гантели", TaskStatus.NEW));
        Task task = new Task(task1id,"Турник", "Подтягиваться на турнике",
                TaskStatus.NEW);
        assertEquals(manager.searchTask(task1id), task);
    }

    @Test // Тест по 2 вопросу
    public void epicEquals() throws IOException{
        FileBackedTaskManager manager = Managers.getDefaultFile();
        int epicId = manager.addEpic(new Epic("Утром", "Тренировка утром", TaskStatus.NEW));
        Epic epic2 = new Epic(epicId, "Вечером", "Тренировка вечером", TaskStatus.NEW);
        assertEquals(manager.searchTask(epicId), epic2);
    }

    @Test  // Тест по 2 вопросу
    public void subTaskEquals() throws IOException{
        FileBackedTaskManager manager = Managers.getDefaultFile();
        int epicId = manager.addEpic(new Epic("Утром", "Тренировка утром", TaskStatus.NEW));
        int subtask1Id = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс", TaskStatus.NEW, epicId));
        Subtask subtask2 = new Subtask(subtask1Id, "Бегит",
                "Бегит по кругу", TaskStatus.NEW, epicId);
        assertEquals(manager.searchTask(subtask1Id), subtask2);
    }

    @Test // Тест по 3 вопросу
    public void notAddSubTaskToNowhere() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        int subtaskid = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс", TaskStatus.NEW, 7633337));
        assertNull(manager.searchTask(subtaskid));
    }

    @Test // Тест по 4 вопросу
    public void notUpdSubTaskToEpicOrElse() throws IOException{
        FileBackedTaskManager manager = Managers.getDefaultFile();
        int epic1ID = manager.addEpic(new Epic("Утром", "Тренировка утром", TaskStatus.NEW));
        int subtask1ID = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс", TaskStatus.NEW, epic1ID));
        int epicSubtaskID = manager.updEpic(new Epic(subtask1ID,
                "Из сабтаска", "Превращаюсь в Эпик", TaskStatus.NEW));
        assertNull(manager.searchTask(epicSubtaskID));
    }

    @Test // Тест по 5 вопросу
    public void utilitarianClassCheck() throws IOException{
        FileBackedTaskManager manager = Managers.getDefaultFile();
        InMemoryHistoryManager hManager = Managers.getDefaultHistory();
        assertNotNull(manager);
        assertNotNull(hManager);
    }

    @Test
        // Тест по 6 вопросу
    void inMemoryTaskManagerWork() throws IOException{
        FileBackedTaskManager manager = Managers.getDefaultFile();
        int taskID = manager.addTask(new Task("Task", "new", TaskStatus.NEW));
        int epicID = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW));
        int subtaskID = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epicID));
        assertEquals(new Task(taskID,"Task", "new", TaskStatus.NEW), manager.searchTask(taskID));
        Epic epic1 = (Epic) manager.searchTask(epicID);
        assertEquals(new Epic(epicID,"Epic", "new", TaskStatus.NEW), epic1);
        assertEquals(new Subtask(subtaskID,"Subtask", "new",
                TaskStatus.NEW, epicID), manager.searchTask(subtaskID));
    }

    @Test
        // Тест по 7 вопросу
    void createIdAndGenerateId() throws IOException{
        FileBackedTaskManager manager = Managers.getDefaultFile();
        int task = manager.addTask(new Task("Task", "new", TaskStatus.NEW));
        int task2 = manager.addTask(new Task(task, "Task", "new", TaskStatus.NEW));
        assertNotEquals(task, task2);
    }

    @Test
    void historyNotSaveViewSimilarTask() throws IOException{
        FileBackedTaskManager manager = Managers.getDefaultFile();
        int task = manager.addTask(new Task("Task", "123", TaskStatus.NEW));
        int task2 = manager.addTask(new Task("Tusk", "321", TaskStatus.NEW));
        System.out.println(manager.getTasks());
        manager.searchTask(task);
        manager.searchTask(task);
        manager.searchTask(task2);
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task));
        assertEquals(history.get(1), manager.searchTask(task2));
    }

    @Test
    void shouldDoNotSaveOldIdINDeletedSubtaskInTaskManager() throws IOException{
        FileBackedTaskManager manager = Managers.getDefaultFile();
        int epicID = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW));

        int subtask1ID = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epicID));
        int subtask2ID = manager.addSubTask(new Subtask("Subtask1", "new",
                TaskStatus.NEW, epicID));
        int subtask3ID = manager.addSubTask(new Subtask("Subtask2", "new",
                TaskStatus.NEW, epicID));

        manager.deleteTask(subtask2ID);
        List<Subtask> subtasks = manager.getSubTasks();
        assertEquals(List.of(manager.searchTask(subtask1ID), manager.searchTask(subtask3ID)), subtasks);
    }
}
