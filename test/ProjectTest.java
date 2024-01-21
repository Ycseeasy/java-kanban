import taskManagers.InMemoryHistoryManager;
import taskManagers.InMemoryTaskManager;
import taskManagers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProjectTest {

    @Test // Тест по 1 вопросу
    public void taskEquals() {
        InMemoryTaskManager manager = Managers.getDefault();
        Task task1 = manager.addTask(new Task("Гантели", "Поднимать гантели", TaskStatus.NEW));
        Task task2 = new Task(task1.getId(), "Турник", "Подтягиваться на турнике",
                TaskStatus.NEW);
        assertEquals(task1, task2);
    }

    @Test // Тест по 2 вопросу
    public void epicEquals() {
        InMemoryTaskManager manager = Managers.getDefault();
        Epic epic1 = manager.addEpic(new Epic("Утром","Тренировка утром",TaskStatus.NEW));
        Epic epic2 = new Epic(epic1.getId(), "Вечером","Тренировка вечером",TaskStatus.NEW);
        assertEquals(epic1, epic2);
    }

    @Test  // Тест по 2 вопросу
    public void subTaskEquals() {
        InMemoryTaskManager manager = Managers.getDefault();
        Epic epic1 = manager.addEpic(new Epic("Утром","Тренировка утром",TaskStatus.NEW));
        Subtask subtask1 = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс",TaskStatus.NEW,epic1.getId()));
        Subtask subtask2 = new Subtask(subtask1.getId(), "Бегит",
                "Бегит по кругу",TaskStatus.NEW,epic1.getId());
        assertEquals(subtask1, subtask2);
    }

    @Test // Тест по 3 вопросу
    public void notAddSubTaskToNowhere() {
        InMemoryTaskManager manager = Managers.getDefault();
        Subtask subtask1 = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс",TaskStatus.NEW,767));
        assertNull(subtask1);
    }

    @Test // Тест по 4 вопросу
    public void notUpdSubTaskToEpicOrElse() {
        InMemoryTaskManager manager = Managers.getDefault();
        Epic epic1 = manager.addEpic(new Epic("Утром","Тренировка утром",TaskStatus.NEW));
        Subtask subtask1 = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс",TaskStatus.NEW,epic1.getId()));
        Epic epicSubtask = manager.updEpic(new Epic(subtask1.getId(),
                "Из сабтаска", "Превращаюсь в Эпик", TaskStatus.NEW));
        assertNull(epicSubtask);
    }

    @Test // Тест по 5 вопросу
    public void utilitarianClassCheck () {
        InMemoryTaskManager manager = Managers.getDefault();
        InMemoryHistoryManager hManager = Managers.getDefaultHistory();
        assertNotNull(manager);
        assertNotNull(hManager);
    }

    @Test // Тест по 6 вопросу
    void inMemoryTaskManagerWork() {
        InMemoryTaskManager manager = Managers.getDefault();
        Task task = manager.addTask(new Task("Task","new",TaskStatus.NEW));
        Epic epic = manager.addEpic(new Epic("Epic","new",TaskStatus.NEW));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epic.getId()));
        assertEquals(task, manager.getTask(task.getId()));
        assertEquals(epic, manager.getEpic(epic.getId()));
        assertEquals(subtask, manager.getSubTask(subtask.getId()));
    }

    @Test // Тест по 7 вопросу
    void createIdAndGenerateId() {
        InMemoryTaskManager manager = Managers.getDefault();
        Task task = manager.addTask(new Task("Task","new",TaskStatus.NEW));
        Task task2 = manager.addTask(new Task(task.getId(), "Task","new",TaskStatus.NEW));
        assertNotEquals(task.getId(),task2.getId());
    }

    @Test // Тест по 8 вопросу
    void historySaveFirstVersion() {
        InMemoryTaskManager manager = Managers.getDefault();
        Task task = manager.addTask(new Task("Task","new",TaskStatus.NEW));
        manager.getTask(task.getId());
        manager.updTask(new Task(task.getId(),"Task2","new2",TaskStatus.DONE));
        manager.getTask(task.getId());
        List<Task> history = manager.getHistory();
        assertNotEquals(history.get(0).getName(),history.get(1).getName());
        assertNotEquals(history.get(0).getDescription(),history.get(1).getDescription());
        assertNotEquals(history.get(0).getTaskStatus(),history.get(1).getTaskStatus());
    }
}
