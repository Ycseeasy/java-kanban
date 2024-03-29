import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import enums.TaskStatus;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerTest {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    TaskManager manager = Managers.getDefaultFile();
    HttpTaskServer server = new HttpTaskServer(manager);

    public ServerTest() throws IOException {
    }


    @BeforeEach
    void setUp() {
        server.start();
        manager.removeAll();
    }

    @AfterEach
    void endUp() {
        server.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {

        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 0, 0)));
        Task task2 = manager.addTask(new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), taskType);
        assertNotNull(response);
        assertEquals(2, tasks.size());
        assertEquals(tasks.get(0).getId(), task.getId());
        assertEquals(tasks.get(0).getName(), task.getName());
        assertEquals(tasks.get(0).getDescription(), task.getDescription());
        assertEquals(tasks.get(0).getTaskStatus(), task.getTaskStatus());
        assertEquals(tasks.get(0).getDuration(), task.getDuration());
        assertEquals(tasks.get(0).getStartTime(), task.getStartTime());
        assertEquals(tasks.get(0).getEndTime(), task.getEndTime());
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task task1 = gson.fromJson(response.body(), Task.class);
        assertNotNull(response);
        assertEquals(task1.getId(), task.getId());
        assertEquals(task1.getName(), task.getName());
        assertEquals(task1.getDescription(), task.getDescription());
        assertEquals(task1.getTaskStatus(), task.getTaskStatus());
        assertEquals(task1.getDuration(), task.getDuration());
        assertEquals(task1.getStartTime(), task.getStartTime());
        assertEquals(task1.getEndTime(), task.getEndTime());

    }

    @Test
    void addTask() throws IOException, InterruptedException {
        Task task = new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 0, 0));

        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasks = manager.getTasks();
        assertNotNull(response);
        assertEquals(1, tasks.size());
        assertEquals(tasks.get(0).getId(), 1);
        assertEquals(tasks.get(0).getName(), task.getName());
        assertEquals(tasks.get(0).getDescription(), task.getDescription());
        assertEquals(tasks.get(0).getTaskStatus(), task.getTaskStatus());
        assertEquals(tasks.get(0).getDuration(), task.getDuration());
        assertEquals(tasks.get(0).getStartTime(), task.getStartTime());
        assertEquals(tasks.get(0).getEndTime(), task.getEndTime());
    }

    @Test
    void updTask() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 0, 0)));
        Task newTask = new Task(task.getId(), "newTask", "done", TaskStatus.DONE,
                Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 0, 0));

        String taskJson = gson.toJson(newTask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasks = manager.getTasks();
        assertNotNull(response);
        assertEquals(1, tasks.size());
        assertEquals(tasks.get(0).getId(), newTask.getId());
        assertEquals(tasks.get(0).getName(), newTask.getName());
        assertEquals(tasks.get(0).getDescription(), newTask.getDescription());
        assertEquals(tasks.get(0).getTaskStatus(), newTask.getTaskStatus());
        assertEquals(tasks.get(0).getDuration(), newTask.getDuration());
        assertEquals(tasks.get(0).getStartTime(), newTask.getStartTime());
        assertEquals(tasks.get(0).getEndTime(), newTask.getEndTime());
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {

        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Task> tasks = manager.getTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {

        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), subtaskType);
        assertNotNull(response);
        assertEquals(1, subtasks.size());
        assertEquals(subtasks.get(0).getId(), subtask.getId());
        assertEquals(subtasks.get(0).getName(), subtask.getName());
        assertEquals(subtasks.get(0).getDescription(), subtask.getDescription());
        assertEquals(subtasks.get(0).getTaskStatus(), subtask.getTaskStatus());
        assertEquals(subtasks.get(0).getDuration(), subtask.getDuration());
        assertEquals(subtasks.get(0).getStartTime(), subtask.getStartTime());
        assertEquals(subtasks.get(0).getEndTime(), subtask.getEndTime());
        assertEquals(subtasks.get(0).getEpicId(), subtask.getEpicId());
    }

    @Test
    void getSubtask() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Subtask subtask1 = gson.fromJson(response.body(), Subtask.class);

        assertNotNull(response);
        assertEquals(subtask.getId(), subtask1.getId());
        assertEquals(subtask.getName(), subtask1.getName());
        assertEquals(subtask.getDescription(), subtask1.getDescription());
        assertEquals(subtask.getTaskStatus(), subtask1.getTaskStatus());
        assertEquals(subtask.getDuration(), subtask1.getDuration());
        assertEquals(subtask.getStartTime(), subtask1.getStartTime());
        assertEquals(subtask.getEndTime(), subtask1.getEndTime());
        assertEquals(subtask.getEpicId(), subtask1.getEpicId());
    }

    @Test
    void addSubtask() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0));
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> list = manager.getSubTasks();
        assertNotNull(response);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getId(), 2);
        assertEquals(list.get(0).getName(), subtask.getName());
        assertEquals(list.get(0).getDescription(), subtask.getDescription());
        assertEquals(list.get(0).getTaskStatus(), subtask.getTaskStatus());
        assertEquals(list.get(0).getDuration(), subtask.getDuration());
        assertEquals(list.get(0).getStartTime(), subtask.getStartTime());
        assertEquals(list.get(0).getEndTime(), subtask.getEndTime());
        assertEquals(list.get(0).getEpicId(), subtask.getEpicId());
    }

    @Test
    void updSubtask() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));
        Subtask newSubtask = new Subtask(subtask.getId(), "newSubtask", "done",
                TaskStatus.DONE, epic.getId(), Duration.ofMinutes(90),
                LocalDateTime.of(2001, 1, 1, 0, 0));

        String SubtaskJson = gson.toJson(newSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(SubtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> list = manager.getSubTasks();
        assertNotNull(response);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getId(), 2);
        assertEquals(list.get(0).getName(), newSubtask.getName());
        assertEquals(list.get(0).getDescription(), newSubtask.getDescription());
        assertEquals(list.get(0).getTaskStatus(), newSubtask.getTaskStatus());
        assertEquals(list.get(0).getDuration(), newSubtask.getDuration());
        assertEquals(list.get(0).getStartTime(), newSubtask.getStartTime());
        assertEquals(list.get(0).getEndTime(), newSubtask.getEndTime());
        assertEquals(list.get(0).getEpicId(), newSubtask.getEpicId());
    }

    @Test
    void deleteSubtask() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Subtask> subtasks = manager.getSubTasks();
        assertEquals(0, subtasks.size());
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(response.body(), epicType);
        Epic actualEpic = (Epic) manager.searchTask(subtask.getEpicId());
        assertNotNull(response);
        assertEquals(1, epics.size());
        assertEquals(epics.get(0).getId(), actualEpic.getId());
        assertEquals(epics.get(0).getName(), actualEpic.getName());
        assertEquals(epics.get(0).getDescription(), actualEpic.getDescription());
        assertEquals(epics.get(0).getTaskStatus(), actualEpic.getTaskStatus());
        assertEquals(epics.get(0).getDuration(), actualEpic.getDuration());
        assertEquals(epics.get(0).getStartTime(), actualEpic.getStartTime());
        assertEquals(epics.get(0).getEndTime(), actualEpic.getEndTime());
    }

    @Test
    void getEpic() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic epic1 = gson.fromJson(response.body(), Epic.class);
        Epic actualEpic = (Epic) manager.searchTask(subtask.getEpicId());
        assertNotNull(response);
        assertEquals(epic1.getId(), actualEpic.getId());
        assertEquals(epic1.getName(), actualEpic.getName());
        assertEquals(epic1.getDescription(), actualEpic.getDescription());
        assertEquals(epic1.getTaskStatus(), actualEpic.getTaskStatus());
        assertEquals(epic1.getDuration(), actualEpic.getDuration());
        assertEquals(epic1.getStartTime(), actualEpic.getStartTime());
        assertEquals(epic1.getEndTime(), actualEpic.getEndTime());
    }

    @Test
    void addEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0));
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, 1,
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        Epic actualEpic = (Epic) manager.searchTask(subtask.getEpicId());
        List<Epic> epics = manager.getEpics();
        assertNotNull(response);
        assertEquals(1, epics.size());
        assertEquals(epics.get(0).getId(), actualEpic.getId());
        assertEquals(epics.get(0).getName(), actualEpic.getName());
        assertEquals(epics.get(0).getDescription(), actualEpic.getDescription());
        assertEquals(epics.get(0).getTaskStatus(), actualEpic.getTaskStatus());
        assertEquals(epics.get(0).getDuration(), actualEpic.getDuration());
        assertEquals(epics.get(0).getStartTime(), actualEpic.getStartTime());
        assertEquals(epics.get(0).getEndTime(), actualEpic.getEndTime());
    }

    @Test
    void getEpicSubtasks() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/" + subtask.getEpicId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> listFromInternet = gson.fromJson(response.body(), subtaskType);
        List<Subtask> list = manager.getEpicSubTaskList(subtask.getEpicId());
        assertNotNull(response);
        assertEquals(listFromInternet.size(), list.size());
        assertEquals(listFromInternet.get(0).getId(), list.get(0).getId());
        assertEquals(listFromInternet.get(0).getName(), list.get(0).getName());
        assertEquals(listFromInternet.get(0).getDescription(), list.get(0).getDescription());
        assertEquals(listFromInternet.get(0).getTaskStatus(), list.get(0).getTaskStatus());
        assertEquals(listFromInternet.get(0).getDuration(), list.get(0).getDuration());
        assertEquals(listFromInternet.get(0).getStartTime(), list.get(0).getStartTime());
        assertEquals(listFromInternet.get(0).getEndTime(), list.get(0).getEndTime());
        assertEquals(listFromInternet.get(0).getEpicId(), list.get(0).getEpicId());
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/" + subtask.getEpicId());
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Epic> epics = manager.getEpics();
        assertEquals(0, epics.size());
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 0, 0)));
        Task task2 = manager.addTask(new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));
        manager.searchTask(task.getId());
        manager.searchTask(task2.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> hisList = manager.getHistory();

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasksHis = gson.fromJson(response.body(), taskType);
        assertNotNull(response);
        assertEquals(hisList.size(), tasksHis.size());
        assertEquals(tasksHis.get(0).getId(), hisList.get(0).getId());
        assertEquals(tasksHis.get(0).getName(), hisList.get(0).getName());
        assertEquals(tasksHis.get(0).getDescription(), hisList.get(0).getDescription());
        assertEquals(tasksHis.get(0).getTaskStatus(), hisList.get(0).getTaskStatus());
        assertEquals(tasksHis.get(0).getDuration(), hisList.get(0).getDuration());
        assertEquals(tasksHis.get(0).getStartTime(), hisList.get(0).getStartTime());
        assertEquals(tasksHis.get(0).getEndTime(), hisList.get(0).getEndTime());

    }

    @Test
    void getPrioritizedTask() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2088, 1, 1, 0, 0)));
        Task task2 = manager.addTask(new Task("Task", "new", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2089, 1, 1, 0, 0)));
        Epic epic = manager.addEpic(new Epic("Epic", "new", TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0)));
        Subtask subtask = manager.addSubTask(new Subtask("Subtask", "new", TaskStatus.NEW, epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.of(2001, 1, 1, 0, 0)));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasksPrior = gson.fromJson(response.body(), taskType);
        List<Task> listPrior = manager.getPrioritizedTasks();

        assertNotNull(response);
        assertEquals(tasksPrior.size(), listPrior.size());
        assertEquals(tasksPrior.get(0).getId(), listPrior.get(0).getId());
        assertEquals(tasksPrior.get(0).getName(), listPrior.get(0).getName());
        assertEquals(tasksPrior.get(0).getDescription(), listPrior.get(0).getDescription());
        assertEquals(tasksPrior.get(0).getTaskStatus(), listPrior.get(0).getTaskStatus());
        assertEquals(tasksPrior.get(0).getDuration(), listPrior.get(0).getDuration());
        assertEquals(tasksPrior.get(0).getStartTime(), listPrior.get(0).getStartTime());
        assertEquals(tasksPrior.get(0).getEndTime(), listPrior.get(0).getEndTime());
    }
}
