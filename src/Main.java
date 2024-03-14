import taskManagers.FileBackedTaskManager;
import taskManagers.InMemoryTaskManager;
import taskManagers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager backedTaskManager = FileBackedTaskManager.loadFromFile(new File("src\\save\\save.csv"));
        System.out.println(backedTaskManager.getHistory());

        /* решить проблемы:
        Доделать исключение (Если нужно)
        Прописать тесты !
         */

    }
}
