package taskManagers;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static FileBackedTaskManager getDefaultFile() throws IOException {
        return FileBackedTaskManager.loadFromFile(new File("save.csv"));
    }

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
