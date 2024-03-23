import managers.InMemoryTaskManager;
import managers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        InMemoryTaskManager manager = Managers.getDefault();

        LocalDateTime epicStart = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime epicStart2 = LocalDateTime.of(2024, 1, 1, 0, 1);
        LocalDateTime subStart = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime subStart2 = LocalDateTime.of(2024, 1, 1, 12, 1);
        LocalDateTime subStart3 = LocalDateTime.of(2024, 1, 2, 12, 1);
        Duration durationTask1 = Duration.ofMinutes(90);
        Duration durationTask2 = Duration.ofMinutes(60);

        // создаем 2 сабки от 1 епика в +- одно время
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, epicStart));
        Subtask subtask1 = manager.addSubTask(new Subtask("SubTask1",
                "new", TaskStatus.NEW, epic.getId(), durationTask1, subStart));
        Subtask subtask2 = new Subtask("SubTask2",
                "new", TaskStatus.NEW, epic.getId(), durationTask2, subStart2);

        // создаем 1 эпик на границе с другим
        Epic epic2 = manager.addEpic(new Epic("Корявый", "new", TaskStatus.NEW, epicStart2));
        Subtask subtask3 = manager.addSubTask(new Subtask("SubTask3",
                "new", TaskStatus.NEW, epic2.getId(), durationTask2, subStart3));
        // В таком формате сабтаска не должна создаваться, потому что эпик начнет перекрывать другой эпик.
        // Тут нужно только менять дату 2 эпика
        System.out.println(subtask3);
    }
}
