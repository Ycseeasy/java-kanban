package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
                        responseCode(exchange);
                        break;
                    } catch (ManagerIntersectionTimeException e) {
                        String error = gson.toJson(new errorMassage("Задача пересекается с существующими"));
                        writeResponse(exchange, error,
                                406);
                        break;
                    } catch (ManagerUpdException | ManagerAddException e) {
                        String error = gson.toJson(new errorMassage("Задача для обновления не найдена"));
                        writeResponse(exchange, error, 404);
                        break;
                    }
                } else {
                    try {
                        manager.addTask(task);
                        responseCode(exchange);
                        break;
                    } catch (ManagerIntersectionTimeException e) {
                        String error = gson.toJson(new errorMassage("Задача пересекается с существующими"));
                        writeResponse(exchange, error,
                                406);
                        break;
                    } catch (ManagerUpdException | ManagerAddException e) {
                        String error = gson.toJson(new errorMassage("Произошла ошибка при создании Задачи"));
                        writeResponse(exchange, error, 401);
                        break;
                    }
                }
            case TASK_DELETE:
                requestPath = exchange.getRequestURI().getPath();
                pathParts = requestPath.split("/");
                try {
                    manager.deleteTask(Integer.parseInt(pathParts[2]));
                    responseCode(exchange);
                    break;
                } catch (ManagerDeleteException e) {
                    String error = gson.toJson(new errorMassage("Задача для удаления не найдена"));
                    writeResponse(exchange, error,
                            404);
                    break;
                }
            case TASK_SEARCH:
                requestPath = exchange.getRequestURI().getPath();
                pathParts = requestPath.split("/");
                Task searchedTask = manager.searchTask(Integer.parseInt(pathParts[2]));
                if (searchedTask == null) {
                    String error = gson.toJson(new errorMassage("Задача не найдена"));
                    writeResponse(exchange, error, 404);
                    break;
                }
                String jsonTask = gson.toJson(searchedTask);
                writeResponse(exchange, jsonTask, 200);
                break;
            case UNKNOWN:
                String error = gson.toJson(new errorMassage("Некорректный эндпойнт"));
                writeResponse(exchange, error, 401);
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

    private void responseCode(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, 0);
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
