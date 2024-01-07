public class Subtask extends Task {

    private int epicId;
    public Subtask(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
    }

    public Subtask(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
