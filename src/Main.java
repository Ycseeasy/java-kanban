import taskManagers.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Main {

    public static void main(String[] args) throws IOException {

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(new File("save.csv"));
        LocalDateTime timeEpic = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime timeSubtask = LocalDateTime.of(2023, 1, 1, 13, 0);
        LocalDateTime timeSubtask1 = LocalDateTime.of(2023, 1, 1, 17, 0);
        Duration durationSubtask = Duration.ofMinutes(90);
        Duration durationSubtask1 = Duration.ofMinutes(60);

        // Создаем епик с таской
        int epicID = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW, timeEpic));
        int subtask1ID = manager.addSubTask(new Subtask("Subtask", "new",
                TaskStatus.NEW, epicID, durationSubtask, timeSubtask));
        System.out.println("МЕЙН");
        System.out.println(epicID);
        System.out.println(subtask1ID);
        // проверяем, что ендтайм сабки равен енд тайму епика, в который она входит
        assertEquals(manager.searchTask(epicID).getEndTime(), manager.searchTask(subtask1ID).getEndTime());

        //Добовляем новую сабку
        int subtask2ID = manager.addSubTask(new Subtask("Subtask1", "DONE",
                TaskStatus.DONE, epicID, durationSubtask1, timeSubtask1));

        /* Задачи на проект
        доделать приоретизацию
        допилить новые тесты NewTest

         */

    }
}
