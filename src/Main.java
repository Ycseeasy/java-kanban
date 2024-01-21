import taskManagers.InMemoryTaskManager;
import taskManagers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = Managers.getDefault();
        System.out.println("+ Таски");
        Task Task1 = manager.addTask(new Task("Гантели", "Поднимать гантели", TaskStatus.NEW));
        Task Task2 = manager.addTask(new Task("Турник", "Подтягиваться на турнике", TaskStatus.NEW));

        System.out.println("+ Епики & Сабтаски");
        Epic Epic1 = manager.addEpic(new Epic("Утром","Тренировка утром",TaskStatus.NEW));
        Subtask idSubTask1 = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс",TaskStatus.NEW,Epic1.getId()));
        Subtask SubTask2 = manager.addSubTask(new Subtask("Бегит",
                "Бегит по кругу",TaskStatus.NEW,Epic1.getId()));
        Epic Epic2 = manager.addEpic(new Epic("Вечером","Тренировка вечером",TaskStatus.NEW));
        Subtask SubTask3 = manager.addSubTask(new Subtask("Анжуманя",
                "Анжуманя на полу",TaskStatus.NEW,Epic2.getId()));

        System.out.println("Печатаем списки");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());

        System.out.println("Меняем статусы");
        manager.updTask(new Task(Task1.getId(),"Гантели", "Поднимать гантели", TaskStatus.DONE));
        manager.updSubTask(new Subtask(SubTask2.getId(),"Бегит", "Бегит по кругу",TaskStatus.DONE,Epic1.getId()));
        manager.updSubTask(new Subtask(SubTask3.getEpicId(),"Анжуманя",
                "Анжуманя на полу",TaskStatus.DONE,Epic2.getId()));

        System.out.println("Печатаем списки");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());

        System.out.println("Тестим удаление");
        manager.deleteEpic(Epic1.getId());
        manager.deleteTask(Task2.getId());

        System.out.println("Печатаем списки");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());

        System.out.println("Удаляем все");
        manager.removeAll();

        System.out.println("Печатаем списки");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());
    }
}
