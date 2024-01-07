public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        System.out.println("+ Таски");
        int idTask1 = manager.addTask(new Task("Гантели", "Поднимать гантели", TaskStatus.NEW));
        int idTask2 = manager.addTask(new Task("Турник", "Подтягиваться на турнике", TaskStatus.NEW));

        System.out.println("+ Епики & Сабтаски");
        int idEpic1 = manager.addEpic(new Epic("Утром","Тренировка утром",TaskStatus.NEW));
        int idSubTask1 = manager.addSubTask(new Subtask("Пресс качат",
                "Качат пресс",TaskStatus.NEW,idEpic1));
        int idSubTask2 = manager.addSubTask(new Subtask("Бегит",
                "Бегит по кругу",TaskStatus.NEW,idEpic1));
        int idEpic2 = manager.addEpic(new Epic("Вечером","Тренировка вечером",TaskStatus.NEW));
        int idSubTask3 = manager.addSubTask(new Subtask("Анжуманя",
                "Анжуманя на полу",TaskStatus.NEW,idEpic2));

        System.out.println("Печатаем списки");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());

        System.out.println("Меняем статусы");
        manager.updTask(new Task(idTask1,"Гантели", "Поднимать гантели", TaskStatus.DONE));
        manager.updSubTask(new Subtask(idSubTask2,"Бегит", "Бегит по кругу",TaskStatus.DONE,idEpic1));
        manager.updSubTask(new Subtask(idSubTask3,"Анжуманя",
                "Анжуманя на полу",TaskStatus.DONE,idEpic2));

        System.out.println("Печатаем списки");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());

        System.out.println("Тестим удаление");
        manager.deleteEpic(idEpic1);
        manager.deleteTask(idTask2);

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
