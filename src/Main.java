import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.TaskStatus;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefaultFile();
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();


        Epic epic = manager.addEpic(new Epic("Epic", "new",
                TaskStatus.NEW, LocalDateTime.of(2001, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(gson.fromJson(gson.toJson(new Subtask("Subtask",
                "new", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(90),
                LocalDateTime.of(2002, 1, 1, 1, 0))), Subtask.class));
        Subtask subtask2 = manager.updSubTask(gson.fromJson(gson.toJson(new Subtask(subtask.getId(), "Subtask",
                "new", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(90),
                LocalDateTime.of(2002, 1, 1, 1, 0))), Subtask.class));
        System.out.println((Subtask) manager.searchTask(subtask2.getId()));
    }
}
