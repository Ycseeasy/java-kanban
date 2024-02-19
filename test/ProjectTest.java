import org.junit.jupiter.api.DisplayName;
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
        Epic epic1 = manager.addEpic(new Epic("Утром", "Тренировка утром", TaskStatus.NEW));
        Epic epic2 = new Epic(epic1.getId(), "Вечером", "Тренировка вечером", TaskStatus.NEW);
        assertEquals(epic1, epic2);
    }

    @Test  // Тест по 2 вопросу
    public void subTaskEquals() {
        InMemoryTaskManager manager = Managers.getDefault();
        Epic epic1 = manager.addEpic(new Epic("Утром", "Тренировка утром", TaskStatus.NEW));
        Subtask subtask1 = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс", TaskStatus.NEW, epic1.getId()));
        Subtask subtask2 = new Subtask(subtask1.getId(), "Бегит",
                "Бегит по кругу", TaskStatus.NEW, epic1.getId());
        assertEquals(subtask1, subtask2);
    }

    @Test // Тест по 3 вопросу
    public void notAddSubTaskToNowhere() {
        InMemoryTaskManager manager = Managers.getDefault();
        Subtask subtask1 = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс", TaskStatus.NEW, 767));
        assertNull(subtask1);
    }

    @Test // Тест по 4 вопросу
    public void notUpdSubTaskToEpicOrElse() {
        InMemoryTaskManager manager = Managers.getDefault();
        Epic epic1 = manager.addEpic(new Epic("Утром", "Тренировка утром", TaskStatus.NEW));
        Subtask subtask1 = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс", TaskStatus.NEW, epic1.getId()));
        Epic epicSubtask = manager.updEpic(new Epic(subtask1.getId(),
                "Из сабтаска", "Превращаюсь в Эпик", TaskStatus.NEW));
        assertNull(epicSubtask);
    }

    @Test // Тест по 5 вопросу
    public void utilitarianClassCheck() {
        InMemoryTaskManager manager = Managers.getDefault();
        InMemoryHistoryManager hManager = Managers.getDefaultHistory();
        assertNotNull(manager);
        assertNotNull(hManager);
    }

    @Test
        // Тест по 6 вопросу
    void inMemoryTaskManagerWork() {
        InMemoryTaskManager manager = Managers.getDefault();
        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW));
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epic.getId()));
        assertEquals(task, manager.getTask(task.getId()));
        assertEquals(epic, manager.getEpic(epic.getId()));
        assertEquals(subtask, manager.getSubTask(subtask.getId()));
    }

    @Test
        // Тест по 7 вопросу
    void createIdAndGenerateId() {
        InMemoryTaskManager manager = Managers.getDefault();
        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW));
        Task task2 = manager.addTask(new Task(task.getId(), "Task", "new", TaskStatus.NEW));
        assertNotEquals(task.getId(), task2.getId());
    }

    @Test
    void historyNotSaveViewSimilarTask() {
        InMemoryTaskManager manager = Managers.getDefault();
        Task task = manager.addTask(new Task("Task", "123", TaskStatus.NEW));
        Task task2 = manager.addTask(new Task("Tusk", "321", TaskStatus.NEW));
        manager.getTask(task.getId());
        manager.getTask(task.getId());
        manager.getTask(task2.getId());
        List<Task> history = manager.getHistory();
        assertNotEquals(history.get(0), history.get(1));
        assertEquals(history.size(), 2);
    }

    @Test
    void shouldDoNotSaveOldIdINDeletedSubtaskInTaskManage() {
        InMemoryTaskManager manager = Managers.getDefault();
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW));

        Subtask subtask1 = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epic.getId()));
        Subtask subtask2 = manager.addSubTask(new Subtask("Subtask1", "new",
                TaskStatus.NEW, epic.getId()));
        Subtask subtask3 = manager.addSubTask(new Subtask("Subtask2", "new",
                TaskStatus.NEW, epic.getId()));

        manager.deleteSubTask(subtask2.getId());
        List<Subtask> subtasks = manager.getSubTasks();
        assertEquals(List.of(subtask1, subtask3), subtasks);
    }
}
