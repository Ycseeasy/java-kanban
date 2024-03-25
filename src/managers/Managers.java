package managers;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefaultFile() throws IOException {
        return FileBackedTaskManager.loadFromFile(new File("save.csv"));
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
