import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subTaskList;

    public Epic(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
        subTaskList = new ArrayList<>();
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