package taskManagers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    Node<Task> head;
    Node<Task> tail;
    HashMap<Integer, Node<Task>> hashId = new HashMap<>();

    @Override
    public void add(Task task) {
        if (!hashId.containsKey(task.getId())) {
            Node<Task> taskNode = linkedLast(task);
            int id = task.getId();
            hashId.put(id, taskNode);
        }
    }

    public Node<Task> linkedLast(Task task) {
        Node<Task> temp = new Node<>(task);
        if (head == null)
            head = temp;
        else
            tail.next = temp;

        temp.prev = tail;
        tail = temp;
        return temp;
    }

    public void removeFirst() {
        if (head.next == null)
            tail = null;
        else
            head.next.prev = null;

        head = head.next;
    }

    public void removeLast() {
        if (head.next == null)
            head = null;
        else
            tail.prev.next = null;

        tail = tail.prev;
    }

    ArrayList<Task> getTasks() {
        ArrayList<Task> historyList = new ArrayList<>();
        Node<Task> temp = head;
        while (temp != null) {
            historyList.add(temp.task);
            temp = temp.next;
        }
        return historyList;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (hashId.containsKey(id)) {
            Node<Task> removeNode = hashId.get(id);
            if (removeNode == head)
                removeFirst();
            else
                removeNode.prev.next = removeNode.next;

            if (removeNode == tail)
                removeLast();
            else
                removeNode.next.prev = removeNode.prev;
        }
    }
}


