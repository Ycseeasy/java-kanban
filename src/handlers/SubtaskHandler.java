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
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SubtaskHandler implements HttpHandler {

    private final TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public SubtaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String[] pathParts;
        String requestPath;
        switch (endpoint) {
            case SUBTASK_GET_FULL_LIST:
                List<Subtask> subTaskList = manager.getSubTasks();
                String jsonList = gson.toJson(subTaskList);
                writeResponse(exchange, jsonList, 200);
                break;
            case SUBTASK_CREATE_UPDATE:
                subtask = gson.fromJson(body, Subtask.class);
                if (subtask.getId() > 0) {
                    try {
                        manager.updSubTask(subtask);
                        responseCode(exchange);
                        break;
                    } catch (ManagerIntersectionTimeException e) {
                        String error = gson.toJson(new errorMassage("Подзадача пересекается с существующими"));
                        writeResponse(exchange, error, 406);
                        break;
                    } catch (ManagerUpdException | ManagerAddException e) {
                        String error = gson.toJson(new errorMassage("Подзадача для обновления не найдена"));
                        writeResponse(exchange, error, 404);
                        break;
                    }
                } else {
                    try {
                        manager.addSubTask(subtask);
                        responseCode(exchange);
                        break;
                    } catch (ManagerIntersectionTimeException e) {
                        String error = gson.toJson(new errorMassage("Подзадача пересекается с существующими"));
                        writeResponse(exchange, error, 406);
                        break;
                    } catch (ManagerUpdException | ManagerAddException e) {
                        String error = gson.toJson(new errorMassage("Произошла ошибка при создании Задачи"));
                        writeResponse(exchange, error, 401);
                        break;
                    }
                }
            case SUBTASK_DELETE:
                requestPath = exchange.getRequestURI().getPath();
                pathParts = requestPath.split("/");
                try {
                    manager.deleteTask(Integer.parseInt(pathParts[2]));
                    responseCode(exchange);
                    break;
                } catch (ManagerDeleteException e) {
                    String error = gson.toJson(new errorMassage("Подзадача для удаления не найдена"));
                    writeResponse(exchange, error, 404);
                    break;
                }
            case SUBTASK_SEARCH:
                requestPath = exchange.getRequestURI().getPath();
                pathParts = requestPath.split("/");
                Subtask searchedSubtask = (Subtask) manager.searchTask(Integer.parseInt(pathParts[2]));
                if (searchedSubtask == null) {
                    String error = gson.toJson(new errorMassage("Подзадача не найдена"));
                    writeResponse(exchange, error, 404);
                    break;
                }
                String jsonTask = gson.toJson(searchedSubtask);
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

        if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            if (Objects.equals(requestMethod, "GET")) {
                return Endpoint.SUBTASK_GET_FULL_LIST;
            }
            if (Objects.equals(requestMethod, "POST")) {
                return Endpoint.SUBTASK_CREATE_UPDATE;
            }

        }

        if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            if (Objects.equals(requestMethod, "DELETE")) {
                return Endpoint.SUBTASK_DELETE;
            }
            if (Objects.equals(requestMethod, "GET")) {
                return Endpoint.SUBTASK_SEARCH;
            }
        }
        return Endpoint.UNKNOWN;
    }

}
