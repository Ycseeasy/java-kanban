import exception.ManagerAddException;
import exception.ManagerSaveException;
import exception.ManagerUpdException;
import managers.FileBackedTaskManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NewTest {
    @Test
    public void fileManagerEpicsCheck() throws IOException {
        TaskManager manager = Managers.getDefaultFile();
        // Дата + дюрейшон для новых епиков и тасков
        LocalDateTime timeEpic = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime timeSubtask = LocalDateTime.of(2023, 1, 1, 13, 0);
        LocalDateTime timeSubtask1 = LocalDateTime.of(2023, 1, 1, 17, 0);
        Duration durationSubtask = Duration.ofMinutes(90);
        Duration durationSubtask1 = Duration.ofMinutes(60);

        // Создаем епик с таской
        Epic epic0 = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, timeEpic));
        Subtask subtask1 = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epic0.getId(), durationSubtask, timeSubtask));
        // проверяем, что ендтайм сабки равен енд тайму епика, в который она входит
        assertEquals(manager.searchTask(epic0.getId()).getEndTime(),subtask1.getEndTime());

        //Добовляем новую сабку
        Subtask subtask2 = manager.addSubTask(new Subtask("Subtask1", "DONE",
                TaskStatus.DONE, epic0.getId(), durationSubtask1, timeSubtask1));
        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epic0.getId()).getEndTime(),
                manager.searchTask(subtask2.getId()).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epic0.getId()).getTaskStatus(), TaskStatus.IN_PROGRESS);

        // Обновляем сабку со статусом нью на доне
        Subtask subtask = manager.updSubTask(new Subtask(subtask1.getId(), "Subtask", "DONE",
                TaskStatus.DONE, epic0.getId(), durationSubtask, timeSubtask));

        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epic0.getId()).getEndTime(), manager.searchTask(subtask2.getId()).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epic0.getId()).getTaskStatus(), TaskStatus.DONE);

        // Меняем статусы сабок на ИН ПРОГРЕСС
        manager.updSubTask(new Subtask(1, "Subtask", "DONE",
                TaskStatus.IN_PROGRESS, 0, durationSubtask, timeSubtask));
        manager.updSubTask(new Subtask(2, "Subtask", "DONE",
                TaskStatus.IN_PROGRESS, 0, durationSubtask1, timeSubtask1));

        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(0).getEndTime(), manager.searchTask(subtask2.getId()).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(0).getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    public void InMemoryManagerEpicsCheck() throws IOException {
        TaskManager manager = Managers.getDefault();
        // Дата + дюрейшон для новых епиков и тасков
        LocalDateTime timeEpic = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime timeSubtask = LocalDateTime.of(2023, 1, 1, 13, 0);
        LocalDateTime timeSubtask1 = LocalDateTime.of(2023, 1, 1, 17, 0);
        Duration durationSubtask = Duration.ofMinutes(90);
        Duration durationSubtask1 = Duration.ofMinutes(60);

        // Создаем епик с таской
        Epic epic0 = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, timeEpic));
        Subtask subtask1 = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epic0.getId(), durationSubtask, timeSubtask));
        // проверяем, что ендтайм сабки равен енд тайму епика, в который она входит
        assertEquals(manager.searchTask(epic0.getId()).getEndTime(),subtask1.getEndTime());

        //Добовляем новую сабку
        Subtask subtask2 = manager.addSubTask(new Subtask("Subtask1", "DONE",
                TaskStatus.DONE, epic0.getId(), durationSubtask1, timeSubtask1));
        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epic0.getId()).getEndTime(),
                manager.searchTask(subtask2.getId()).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epic0.getId()).getTaskStatus(), TaskStatus.IN_PROGRESS);

        // Обновляем сабку со статусом нью на доне
        Subtask subtask = manager.updSubTask(new Subtask(subtask1.getId(), "Subtask", "DONE",
                TaskStatus.DONE, epic0.getId(), durationSubtask, timeSubtask));

        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epic0.getId()).getEndTime(), manager.searchTask(subtask2.getId()).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epic0.getId()).getTaskStatus(), TaskStatus.DONE);

        // Меняем статусы сабок на ИН ПРОГРЕСС
        manager.updSubTask(new Subtask(1, "Subtask", "DONE",
                TaskStatus.IN_PROGRESS, 0, durationSubtask, timeSubtask));
        manager.updSubTask(new Subtask(2, "Subtask", "DONE",
                TaskStatus.IN_PROGRESS, 0, durationSubtask1, timeSubtask1));

        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(0).getEndTime(), manager.searchTask(subtask2.getId()).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(0).getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
     public void fileIntersectionTest() throws IOException {
        TaskManager manager = Managers.getDefaultFile();

        LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime startTask2 = LocalDateTime.of(2023, 1, 1, 0, 1);
        LocalDateTime epicStart = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime epicStart2 = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime subStart = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime subStart2 = LocalDateTime.of(2024, 1, 1, 12, 1);
        LocalDateTime subStart3 = LocalDateTime.of(2024, 1, 2, 12, 1);
        Duration durationTask1 = Duration.ofMinutes(90);
        Duration durationTask2 = Duration.ofMinutes(60);

        // попробуем создать 2 таска друг за другом по дате
        Task task1 = manager.addTask(new Task("Task1",
                "new", TaskStatus.NEW, durationTask1, startTask1));
        Task task2 = new Task("Task2",
                "new", TaskStatus.NEW, durationTask2, startTask2);
        assertThrows(ManagerAddException.class, () -> manager.addTask(task2));

        // создаем 2 сабки от 1 епика в +- одно время
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, epicStart));
        Subtask subtask1 = manager.addSubTask(new Subtask("SubTask1",
                "new", TaskStatus.NEW, epic.getId(), durationTask1, subStart));
        Subtask subtask2 = new Subtask("SubTask2",
                "new", TaskStatus.NEW, epic.getId(), durationTask2, subStart2);
        assertThrows(ManagerAddException.class, () -> manager.addSubTask(subtask2));

        // создаем 1 эпик на границе с другим
        Epic epic2 = manager.addEpic(new Epic("Корявый", "new", TaskStatus.NEW, epicStart2));
        Subtask subtask3 = new Subtask("SubTask3",
                "new", TaskStatus.NEW, epic2.getId(), durationTask2, subStart3);
        // В таком формате сабтаска не должна создаваться, потому что эпик начнет перекрывать другой эпик.
        // Тут нужно только менять дату 2 эпика
        assertThrows(ManagerUpdException.class, () -> manager.addSubTask(subtask3));
    }

    @Test
    public void IntersectionTest() {
        TaskManager manager = Managers.getDefault();

        LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime startTask2 = LocalDateTime.of(2023, 1, 1, 0, 1);
        LocalDateTime epicStart = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime epicStart2 = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime subStart = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime subStart2 = LocalDateTime.of(2024, 1, 1, 12, 1);
        LocalDateTime subStart3 = LocalDateTime.of(2024, 1, 2, 12, 1);
        Duration durationTask1 = Duration.ofMinutes(90);
        Duration durationTask2 = Duration.ofMinutes(60);

        // попробуем создать 2 таска друг за другом по дате
        Task task1 = manager.addTask(new Task("Task1",
                "new", TaskStatus.NEW, durationTask1, startTask1));
        Task task2 = new Task("Task2",
                "new", TaskStatus.NEW, durationTask2, startTask2);
        assertThrows(ManagerAddException.class, () -> manager.addTask(task2));

        // создаем 2 сабки от 1 епика в +- одно время
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, epicStart));
        Subtask subtask1 = manager.addSubTask(new Subtask("SubTask1",
                "new", TaskStatus.NEW, epic.getId(), durationTask1, subStart));
        Subtask subtask2 = new Subtask("SubTask2",
                "new", TaskStatus.NEW, epic.getId(), durationTask2, subStart2);
        assertThrows(ManagerAddException.class, () -> manager.addSubTask(subtask2));

        // создаем 1 эпик на границе с другим
        Epic epic2 = manager.addEpic(new Epic("Корявый", "new", TaskStatus.NEW, epicStart2));
        Subtask subtask3 = new Subtask("SubTask3",
                "new", TaskStatus.NEW, epic2.getId(), durationTask2, subStart3);
        // В таком формате сабтаска не должна создаваться, потому что эпик начнет перекрывать другой эпик.
        // Тут нужно только менять дату 2 эпика
        assertThrows(ManagerUpdException.class, () -> manager.addSubTask(subtask3));
    }

    @Test
    public void emptyAndDoubleHistoryCheck() throws IOException {
        LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime startTask2 = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime startTask01 = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime startTask02 = LocalDateTime.of(2022, 1, 1, 0, 1);
        Duration durationTask1 = Duration.ofMinutes(90);
        Duration durationTask2 = Duration.ofMinutes(60);

        TaskManager manager = Managers.getDefaultFile();
        assertTrue(manager.getHistory().isEmpty());

        Task task = manager.addTask(new Task("Task", "123",
                TaskStatus.NEW, durationTask1, startTask1));
        Task task1 = manager.addTask(new Task("Task1", "321",
                TaskStatus.NEW, durationTask2, startTask2));
        manager.searchTask(task.getId());
        manager.searchTask(task.getId());
        manager.searchTask(task1.getId());
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task.getId()));
        assertEquals(history.get(1), manager.searchTask(task1.getId()));

        TaskManager manager2 = Managers.getDefault();
        assertTrue(manager2.getHistory().isEmpty());

        Task task0 = manager2.addTask(new Task("Task", "123",
                TaskStatus.NEW, durationTask1, startTask01));
        Task task01 = manager2.addTask(new Task("Task1", "321",
                TaskStatus.NEW, durationTask2, startTask02));
        manager2.searchTask(task0.getId());
        manager2.searchTask(task0.getId());
        manager2.searchTask(task01.getId());
        List<Task> history0 = manager2.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history0.get(0), manager2.searchTask(task0.getId()));
        assertEquals(history0.get(1), manager2.searchTask(task01.getId()));
    }


    @Test
    public void HistoryDeleteCheck() {
        LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime startTask2 = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime startTask3 = LocalDateTime.of(2025, 1, 1, 0, 1);
        Duration durationTask1 = Duration.ofMinutes(90);
        Duration durationTask2 = Duration.ofMinutes(60);
        Duration durationTask3 = Duration.ofMinutes(80);

        TaskManager manager = Managers.getDefault();
        assertTrue(manager.getHistory().isEmpty());

        // проверяем на дублирование
        Task task1 = manager.addTask(new Task("Task1", "123",
                TaskStatus.NEW, durationTask1, startTask1));
        Task task2 = manager.addTask(new Task("Tusk2", "321",
                TaskStatus.NEW, durationTask2, startTask2));
        Task task3 = manager.addTask(new Task("Tusk3", "321",
                TaskStatus.NEW, durationTask3, startTask3));

        manager.searchTask(task1.getId());
        manager.searchTask(task2.getId());
        manager.searchTask(task3.getId());
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 1 таск
        manager.deleteTask(task1.getId());
        history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task2.getId()));
        assertEquals(history.get(1), manager.searchTask(task3.getId()));

        Task newTask = manager.addTask(new Task("Task1", "123",
                TaskStatus.NEW, durationTask1, startTask1));

        manager.searchTask(newTask.getId());
        history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 2 таск
        manager.deleteTask(task3.getId());
        history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task2.getId()));
        assertEquals(history.get(1), manager.searchTask(newTask.getId()));

        Task againNewTask = manager.addTask(new Task("Tusk3", "321",
                TaskStatus.NEW, durationTask3, startTask3));

        manager.searchTask(againNewTask.getId());
        history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 3 таск
        manager.deleteTask(againNewTask.getId());
        history = manager.getHistory();
        assertEquals(history.get(0), manager.searchTask(task2.getId()));
        assertEquals(history.get(1), manager.searchTask(newTask.getId()));
    }

    @Test
    public void fileHistoryDeleteCheck() throws IOException {
        LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime startTask2 = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime startTask3 = LocalDateTime.of(2025, 1, 1, 0, 1);
        Duration durationTask1 = Duration.ofMinutes(90);
        Duration durationTask2 = Duration.ofMinutes(60);
        Duration durationTask3 = Duration.ofMinutes(80);

        TaskManager manager = Managers.getDefaultFile();
        assertTrue(manager.getHistory().isEmpty());

        // проверяем на дублирование
        Task task1 = manager.addTask(new Task("Task1", "123",
                TaskStatus.NEW, durationTask1, startTask1));
        Task task2 = manager.addTask(new Task("Tusk2", "321",
                TaskStatus.NEW, durationTask2, startTask2));
        Task task3 = manager.addTask(new Task("Tusk3", "321",
                TaskStatus.NEW, durationTask3, startTask3));

        manager.searchTask(task1.getId());
        manager.searchTask(task2.getId());
        manager.searchTask(task3.getId());
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 1 таск
        manager.deleteTask(task1.getId());
        history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task2.getId()));
        assertEquals(history.get(1), manager.searchTask(task3.getId()));

        Task newTask = manager.addTask(new Task("Task1", "123",
                TaskStatus.NEW, durationTask1, startTask1));

        manager.searchTask(newTask.getId());
        history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 2 таск
        manager.deleteTask(task3.getId());
        history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task2.getId()));
        assertEquals(history.get(1), manager.searchTask(newTask.getId()));

        Task againNewTask = manager.addTask(new Task("Tusk3", "321",
                TaskStatus.NEW, durationTask3, startTask3));

        manager.searchTask(againNewTask.getId());
        history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 3 таск
        manager.deleteTask(againNewTask.getId());
        history = manager.getHistory();
        assertEquals(history.get(0), manager.searchTask(task2.getId()));
        assertEquals(history.get(1), manager.searchTask(newTask.getId()));
    }

    @Test
    public void testException() {
        File tmpFile = new File("InvalidPath", "save.txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(tmpFile);
        assertThrows(ManagerSaveException.class, () -> {
            LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
            Duration durationTask1 = Duration.ofMinutes(90);
            Task task = new Task("Task1", "123",
                    TaskStatus.NEW, durationTask1, startTask1);
            manager.addTask(task);
        });
    }
}
