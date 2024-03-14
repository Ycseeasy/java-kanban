package taskManagers;

import java.io.File;

public class Managers {
    public static FileBackedTaskManager getDefault() {
        return new FileBackedTaskManager(new File("src\\save\\save.csv"));
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
