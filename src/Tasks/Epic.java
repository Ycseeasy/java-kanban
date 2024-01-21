package Tasks;

import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subTaskList = new ArrayList<>();

    public Epic(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
    }

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }


    public ArrayList<Subtask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(ArrayList<Subtask> subTaskList) {
        this.subTaskList = subTaskList;
    }

}