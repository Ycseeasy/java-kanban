package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import exception.*;
import managers.TaskManager;
import tasks.Epic;
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

public class EpicHandler implements HttpHandler {

    private final TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic;
        String requestPath;
        String[] pathParts;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        switch (endpoint) {
            case EPIC_GET_FULL_LIST:
                List<Epic> epicList = manager.getEpics();
                String jsonList = gson.toJson(epicList);
                writeResponse(exchange, jsonList, 200);
                break;
            case EPIC_CREATE:
                epic = gson.fromJson(body, Epic.class);
                manager.addEpic(epic);
                responseCode(exchange);
                break;
            case EPIC_DELETE:
                requestPath = exchange.getRequestURI().getPath();
                pathParts = requestPath.split("/");
                try {
                    manager.deleteTask(Integer.parseInt(pathParts[2]));
                    responseCode(exchange);
                    break;
                } catch (ManagerDeleteException e) {
                    String error = gson.toJson(new Massage("Задача для удаления не найдена"));
                    writeResponse(exchange, error,
                            404);
                    break;
                }
            case EPIC_SEARCH:
                requestPath = exchange.getRequestURI().getPath();
                pathParts = requestPath.split("/");
                Epic searchedEpic = (Epic) manager.searchTask(Integer.parseInt(pathParts[2]));
                if (searchedEpic == null) {
                    String error = gson.toJson(new Massage("Задача не найдена"));
                    writeResponse(exchange, error, 404);
                    break;
                }
                String jsonTask = gson.toJson(searchedEpic);
                writeResponse(exchange, jsonTask, 200);
                break;
            case EPIC_GET_SUBTASKS_LIST:
                try {
                    requestPath = exchange.getRequestURI().getPath();
                    pathParts = requestPath.split("/");
                    List<Subtask> listSubtasks = manager.getEpicSubTaskList(Integer.parseInt(pathParts[2]));
                    String jsonListSub = gson.toJson(listSubtasks);
                    writeResponse(exchange, jsonListSub, 200);
                    break;
                } catch (ManagerEpicSubtaskListException e) {
                    String error = gson.toJson(new Massage("Задача не найдена"));
                    writeResponse(exchange, error, 404);
                    break;
                }
            case UNKNOWN:
                String error = gson.toJson(new Massage("Некорректный эндпойнт"));
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
        if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            if (Objects.equals(requestMethod, "GET")) {
                return Endpoint.EPIC_GET_FULL_LIST;
            }
            if (Objects.equals(requestMethod, "POST")) {
                return Endpoint.EPIC_CREATE;
            }
        }

        if (pathParts.length == 3 && pathParts[1].equals("epics")) {
            if (Objects.equals(requestMethod, "DELETE")) {
                return Endpoint.EPIC_DELETE;
            }
            if (Objects.equals(requestMethod, "GET")) {
                return Endpoint.EPIC_SEARCH;
            }
        }

        if (pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) {
            return Endpoint.EPIC_GET_SUBTASKS_LIST;
        }
        return Endpoint.UNKNOWN;
    }
}
