package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import exception.ManagerAddException;
import exception.ManagerDeleteException;
import exception.ManagerIntersectionTimeException;
import exception.ManagerUpdException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TaskHandler implements HttpHandler {

    private final TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String[] pathParts;
        String requestPath;
        switch (endpoint) {
            case TASK_GET_FULL_LIST:
                List<Task> taskList = manager.getTasks();
                String jsonList = gson.toJson(taskList);
                writeResponse(exchange, jsonList, 200);
                break;
            case TASK_CREATE_UPDATE:
                task = gson.fromJson(body, Task.class);
                if (task.getId() > 0) {
                    try {
                        manager.updTask(task);
                        writeResponse(exchange, "Задача успешно обновлена", 201);
                        break;
                    } catch (ManagerIntersectionTimeException e) {
                        writeResponse(exchange, "Задача пересекается с существующими",
                                406);
                        break;
                    } catch (ManagerUpdException | ManagerAddException e) {
                        writeResponse(exchange, "Задача для обновления не найдена", 404);
                        break;
                    }
                } else {
                    try {
                        manager.addTask(task);
                        writeResponse(exchange, "Задача успешно добавлена", 201);
                        break;
                    } catch (ManagerIntersectionTimeException e) {
                        writeResponse(exchange, "Задача пересекается с существующими",
                                406);
                        break;
                    } catch (ManagerUpdException | ManagerAddException e) {
                        writeResponse(exchange, "Произошла ошибка при создании Задачи", 401);
                        break;
                    }
                }
            case TASK_DELETE:
                requestPath = exchange.getRequestURI().getPath();
                pathParts = requestPath.split("/");
                try {
                    manager.deleteTask(Integer.parseInt(pathParts[2]));
                    writeResponse(exchange, "Задача успешно удалена",
                            200);
                    break;
                } catch (ManagerDeleteException e) {
                    writeResponse(exchange, "Задача для удаления не найдена",
                            404);
                    break;
                }
            case TASK_SEARCH:
                requestPath = exchange.getRequestURI().getPath();
                pathParts = requestPath.split("/");
                Task searchedTask = manager.searchTask(Integer.parseInt(pathParts[2]));
                if (searchedTask == null) {
                    writeResponse(exchange,
                            "Задача не найдена", 404);
                    break;
                }
                String jsonTask = gson.toJson(searchedTask);
                writeResponse(exchange, jsonTask, 200);
                break;
            case UNKNOWN:
                writeResponse(exchange,
                        "Эндпоинт был составлен некорректно", 401);
                break;
        }
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            if (Objects.equals(requestMethod, "GET")) {
                return Endpoint.TASK_GET_FULL_LIST;
            }
            if (Objects.equals(requestMethod, "POST")) {
                return Endpoint.TASK_CREATE_UPDATE;
            }
        }

        if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            if (Objects.equals(requestMethod, "DELETE")) {
                return Endpoint.TASK_DELETE;
            }
            if (Objects.equals(requestMethod, "GET")) {
                return Endpoint.TASK_SEARCH;
            }
        }
        return Endpoint.UNKNOWN;
    }
}
