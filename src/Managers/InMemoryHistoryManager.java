package Managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class InMemoryHistoryManager implements HistoryManager {
    private Node head = null;
    private Node tail = null;
    HashMap<Integer, Node> hashId = new HashMap<>();

    @Override
    public void add(Task task) {
        if (hashId.containsKey(task.getId())) {
            remove(task.getId());
            hashId.remove(task.getId());
        }
            Node taskNode = linkedLast(task);
            int id = task.getId();
            hashId.put(id, taskNode);
    }


    public Node linkedLast(Task task) {
        Node temp = new Node(task);
        if (tail == null) {
            head = temp;
        } else {
            tail.next = temp;
            temp.prev = tail;
        }
        tail = temp;
        return temp;
    }

    private void removeFirst() {
        if (head == null) {
            return;
        }

        if (head == tail) {
            head = null;
            tail = null;
            return;
        }

        Node temp = head;
        head = head.next;
        head.prev = null;
        temp.next = null;
    }

    private void removeLast() {
        if (tail == null) {
            return;
        }

        if (head == tail) {
            head = null;
            tail = null;
            return;
        }

        Node temp = tail;
        tail = tail.prev;
        tail.next = null;
        temp.prev = null;
    }

    ArrayList<Task> getTasks() {
        ArrayList<Task> historyList = new ArrayList<>();
        Node temp = head;
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
            Node removeNode = hashId.get(id);
            if (head == null) {
                return;
            }

            if (removeNode == head) {
                removeFirst();
                return;
            }

            if (removeNode == tail) {
                removeLast();
                return;
            }

            removeNode.prev.next = removeNode.next;
            removeNode.next.prev = removeNode.prev;
            removeNode.prev = null;
            removeNode.next = null;
        }
    }
}


