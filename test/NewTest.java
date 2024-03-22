import org.junit.jupiter.api.Test;
import managers.FileBackedTaskManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import exception.ManagerSaveException;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NewTest {
    @Test
    public void fileManagerEpicsCheck() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();
        // Дата + дюрейшон для новых епиков и тасков
        LocalDateTime timeEpic = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime timeSubtask = LocalDateTime.of(2023, 1, 1, 13, 0);
        LocalDateTime timeSubtask1 = LocalDateTime.of(2023, 1, 1, 17, 0);
        Duration durationSubtask = Duration.ofMinutes(90);
        Duration durationSubtask1 = Duration.ofMinutes(60);

        // Создаем епик с таской
        int epicID = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, timeEpic));
        int subtask1ID = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epicID, durationSubtask, timeSubtask));

        // проверяем, что ендтайм сабки равен енд тайму епика, в который она входит
        assertEquals(manager.searchTask(epicID).getEndTime(), manager.searchTask(subtask1ID).getEndTime());

        //Добовляем новую сабку
        int subtask2ID = manager.addSubTask(new Subtask("Subtask1", "DONE",
                TaskStatus.DONE, epicID, durationSubtask1, timeSubtask1));

        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epicID).getEndTime(), manager.searchTask(subtask2ID).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epicID).getTaskStatus(), TaskStatus.IN_PROGRESS);

        // Обновляем сабку со статусом нью на доне
        int newSubtask1ID = manager.updSubTask(new Subtask(subtask1ID, "Subtask", "DONE",
                TaskStatus.DONE, epicID, durationSubtask, timeSubtask));

        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epicID).getEndTime(), manager.searchTask(subtask2ID).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epicID).getTaskStatus(), TaskStatus.DONE);

        // Меняем статусы сабок на ИН ПРОГРЕСС
        int newInProgressSub1 = manager.updSubTask(new Subtask(subtask1ID, "Subtask", "DONE",
                TaskStatus.IN_PROGRESS, epicID, durationSubtask, timeSubtask));
        int newInProgressSub2 = manager.updSubTask(new Subtask(subtask2ID, "Subtask", "DONE",
                TaskStatus.IN_PROGRESS, epicID, durationSubtask1, timeSubtask1));

        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epicID).getEndTime(), manager.searchTask(subtask2ID).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epicID).getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    public void InMemoryManagerEpicsCheck() throws IOException {
        InMemoryTaskManager manager = Managers.getDefault();
        // Дата + дюрейшон для новых епиков и тасков
        LocalDateTime timeEpic = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime timeSubtask = LocalDateTime.of(2023, 1, 1, 13, 0);
        LocalDateTime timeSubtask1 = LocalDateTime.of(2023, 1, 1, 17, 0);
        Duration durationSubtask = Duration.ofMinutes(90);
        Duration durationSubtask1 = Duration.ofMinutes(60);

        // Создаем епик с таской
        int epicID = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, timeEpic));
        int subtask1ID = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epicID, durationSubtask, timeSubtask));

        // проверяем, что ендтайм сабки равен енд тайму епика, в который она входит
        assertEquals(manager.searchTask(epicID).getEndTime(), manager.searchTask(subtask1ID).getEndTime());

        //Добовляем новую сабку
        int subtask2ID = manager.addSubTask(new Subtask("Subtask1", "DONE",
                TaskStatus.DONE, epicID, durationSubtask1, timeSubtask1));

        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epicID).getEndTime(), manager.searchTask(subtask2ID).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epicID).getTaskStatus(), TaskStatus.IN_PROGRESS);

        // Обновляем сабку со статусом нью на доне
        int newSubtask1ID = manager.updSubTask(new Subtask(subtask1ID, "Subtask", "DONE",
                TaskStatus.DONE, epicID, durationSubtask, timeSubtask));
        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epicID).getEndTime(), manager.searchTask(subtask2ID).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epicID).getTaskStatus(), TaskStatus.DONE);

        // Меняем статусы сабок на ИН ПРОГРЕСС
        int newInProgressSub1 = manager.updSubTask(new Subtask(subtask1ID, "Subtask", "DONE",
                TaskStatus.IN_PROGRESS, epicID, durationSubtask, timeSubtask));
        int newInProgressSub2 = manager.updSubTask(new Subtask(subtask2ID, "Subtask", "DONE",
                TaskStatus.IN_PROGRESS, epicID, durationSubtask1, timeSubtask1));

        // проверяем, что ендтайм сабки, с самой поздней датой, равен енд тайму епика
        assertEquals(manager.searchTask(epicID).getEndTime(), manager.searchTask(subtask2ID).getEndTime());
        // проверяем сменился ли статус на нужный у епика
        assertEquals(manager.searchTask(epicID).getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    public void fileIntersectionTest() throws IOException {
        FileBackedTaskManager manager = Managers.getDefaultFile();

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
        int idTask1 = manager.addTask(new Task("Task1",
                "new", TaskStatus.NEW, durationTask1, startTask1));
        int idTask2 = manager.addTask(new Task("Task2",
                "new", TaskStatus.NEW, durationTask2, startTask2));
        assertEquals(idTask2, -1);

        // создаем 2 сабки от 1 епика в +- одно время
        int epicId = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, epicStart));
        int subId1 = manager.addSubTask(new Subtask("SubTask1",
                "new", TaskStatus.NEW, epicId, durationTask1, subStart));
        int subId2 = manager.addSubTask(new Subtask("SubTask2",
                "new", TaskStatus.NEW, epicId, durationTask2, subStart2));
        assertEquals(subId2, -1);

        // создаем 1 эпик на границе с другим
        int epicId2 = manager.addEpic(new Epic("Epic2", "new", TaskStatus.NEW, epicStart2));
        int subId3 = manager.addSubTask(new Subtask("SubTask3",
                "new", TaskStatus.NEW, epicId2, durationTask2, subStart3));
        // В таком формате сабтаска не должна создаваться, потому что эпик начнет перекрывать другой эпик.
        // Тут нужно только менять дату 2 эпика
        assertEquals(subId3, -1);
    }

    @Test
    public void IntersectionTest() {
        InMemoryTaskManager manager = Managers.getDefault();

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
        int idTask1 = manager.addTask(new Task("Task1",
                "new", TaskStatus.NEW, durationTask1, startTask1));
        int idTask2 = manager.addTask(new Task("Task2",
                "new", TaskStatus.NEW, durationTask2, startTask2));
        assertEquals(idTask2, -1);

        // создаем 2 сабки от 1 епика в +- одно время
        int epicId = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, epicStart));
        int subId1 = manager.addSubTask(new Subtask("SubTask1",
                "new", TaskStatus.NEW, epicId, durationTask1, subStart));
        int subId2 = manager.addSubTask(new Subtask("SubTask2",
                "new", TaskStatus.NEW, epicId, durationTask2, subStart2));
        assertEquals(subId2, -1);

        // создаем 1 эпик на границе с другим
        int epicId2 = manager.addEpic(new Epic("Epic2", "new", TaskStatus.NEW, epicStart2));
        int subId3 = manager.addSubTask(new Subtask("SubTask3",
                "new", TaskStatus.NEW, epicId2, durationTask2, subStart3));
        // В таком формате сабтаска не должна создаваться, потому что эпик начнет перекрывать другой эпик.
        // Тут нужно только менять дату 2 эпика
        assertEquals(subId3, -1);
    }

    @Test
    public void emptyAndDoubleHistoryCheck() throws IOException {
        LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime startTask2 = LocalDateTime.of(2024, 1, 1, 0, 1);
        Duration durationTask1 = Duration.ofMinutes(90);
        Duration durationTask2 = Duration.ofMinutes(60);

        FileBackedTaskManager manager = Managers.getDefaultFile();
        assertTrue(manager.getHistory().isEmpty());

        int task = manager.addTask(new Task("Task", "123",
                TaskStatus.NEW, durationTask1, startTask1));
        int task2 = manager.addTask(new Task("Tusk", "321",
                TaskStatus.NEW, durationTask2, startTask2));
        manager.searchTask(task);
        manager.searchTask(task);
        manager.searchTask(task2);
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task));
        assertEquals(history.get(1), manager.searchTask(task2));

        InMemoryTaskManager manager2 = Managers.getDefault();
        assertTrue(manager2.getHistory().isEmpty());

        int task21 = manager2.addTask(new Task("Task", "123",
                TaskStatus.NEW, durationTask1, startTask1));
        int task22 = manager2.addTask(new Task("Tusk", "321",
                TaskStatus.NEW, durationTask2, startTask2));
        manager2.searchTask(task);
        manager2.searchTask(task);
        manager2.searchTask(task2);
        List<Task> history2 = manager2.getHistory();
        assertEquals(history2.size(), 2);
        assertEquals(history.get(0), manager2.searchTask(task21));
        assertEquals(history.get(1), manager2.searchTask(task22));

    }


    @Test
    public void HistoryDeleteCheck() {
        LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime startTask2 = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime startTask3 = LocalDateTime.of(2025, 1, 1, 0, 1);
        Duration durationTask1 = Duration.ofMinutes(90);
        Duration durationTask2 = Duration.ofMinutes(60);
        Duration durationTask3 = Duration.ofMinutes(80);

        InMemoryTaskManager manager = Managers.getDefault();
        assertTrue(manager.getHistory().isEmpty());

        // проверяем на дублирование
        int task = manager.addTask(new Task("Task1", "123",
                TaskStatus.NEW, durationTask1, startTask1));
        int task2 = manager.addTask(new Task("Tusk2", "321",
                TaskStatus.NEW, durationTask2, startTask2));
        int task3 = manager.addTask(new Task("Tusk3", "321",
                TaskStatus.NEW, durationTask3, startTask3));
        manager.searchTask(task);
        manager.searchTask(task2);
        manager.searchTask(task3);
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 1 таск
        manager.deleteTask(task);
        history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task2));
        assertEquals(history.get(1), manager.searchTask(task3));

        int newTask = manager.addTask(new Task("Task1", "123",
                TaskStatus.NEW, durationTask1, startTask1));
        manager.searchTask(newTask);
        history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 2 таск
        manager.deleteTask(task3);
        history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task2));
        assertEquals(history.get(1), manager.searchTask(newTask));

        int againNewTask = manager.addTask(new Task("Tusk3", "321",
                TaskStatus.NEW, durationTask3, startTask3));
        manager.searchTask(againNewTask);
        history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 3 таск
        manager.deleteTask(againNewTask);
        history = manager.getHistory();
        assertEquals(history.get(0), manager.searchTask(task2));
        assertEquals(history.get(1), manager.searchTask(newTask));
    }

    @Test
    public void fileHistoryDeleteCheck() throws IOException {
        LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime startTask2 = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime startTask3 = LocalDateTime.of(2025, 1, 1, 0, 1);
        Duration durationTask1 = Duration.ofMinutes(90);
        Duration durationTask2 = Duration.ofMinutes(60);
        Duration durationTask3 = Duration.ofMinutes(80);

        FileBackedTaskManager manager = Managers.getDefaultFile();
        assertTrue(manager.getHistory().isEmpty());

        // проверяем на дублирование
        int task = manager.addTask(new Task("Task1", "123",
                TaskStatus.NEW, durationTask1, startTask1));
        int task2 = manager.addTask(new Task("Tusk2", "321",
                TaskStatus.NEW, durationTask2, startTask2));
        int task3 = manager.addTask(new Task("Tusk3", "321",
                TaskStatus.NEW, durationTask3, startTask3));
        manager.searchTask(task);
        manager.searchTask(task2);
        manager.searchTask(task3);
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 1 таск
        manager.deleteTask(task);
        history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task2));
        assertEquals(history.get(1), manager.searchTask(task3));

        int newTask = manager.addTask(new Task("Task1", "123",
                TaskStatus.NEW, durationTask1, startTask1));
        manager.searchTask(newTask);
        history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 2 таск
        manager.deleteTask(task3);
        history = manager.getHistory();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0), manager.searchTask(task2));
        assertEquals(history.get(1), manager.searchTask(newTask));

        int againNewTask = manager.addTask(new Task("Tusk3", "321",
                TaskStatus.NEW, durationTask3, startTask3));
        manager.searchTask(againNewTask);
        history = manager.getHistory();
        assertEquals(history.size(), 3);

        // Удаляем 3 таск
        manager.deleteTask(againNewTask);
        history = manager.getHistory();
        assertEquals(history.get(0), manager.searchTask(task2));
        assertEquals(history.get(1), manager.searchTask(newTask));
    }

    @Test
    public void testException() {
        File tmpFile = new File("InvalidPath", "save.txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(tmpFile);
        assertThrows(ManagerSaveException.class, () -> {
            LocalDateTime startTask1 = LocalDateTime.of(2023, 1, 1, 0, 0);
            Duration durationTask1 = Duration.ofMinutes(90);
            int task = manager.addTask(new Task("Task1", "123",
                    TaskStatus.NEW, durationTask1, startTask1));
        });
    }
}
