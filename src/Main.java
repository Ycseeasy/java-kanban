public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Epic jopa = manager.epic("Сходить в туалет", "", TaskStatus.NEW);
        Subtask kakat = manager.subTask("Какать", "", TaskStatus.NEW);
        Subtask smivat = manager.subTask("Смывать", "", TaskStatus.NEW);
        Subtask ynitaz = manager.subTask("Открыть унитаз", "", TaskStatus.NEW);
        manager.addEpic(jopa);
        manager.addSubTask(kakat, jopa.getId());
        manager.addSubTask(smivat, jopa.getId());
        manager.addSubTask(ynitaz, jopa.getId());
        System.out.println(manager.epicSubTaskList(jopa.getId()));
        Subtask smivaning = manager.subTask("Смывать", "", TaskStatus.IN_PROGRESS);
        manager.updSubTask(smivaning, smivat.getId(), jopa.getId());
        System.out.println(manager.epicSubTaskList(jopa.getId()));
        System.out.println(jopa.getTaskStatus());
        Subtask smivanie = manager.subTask("Смывать", "", TaskStatus.NEW);
        manager.updSubTask(smivanie, smivat.getId(), jopa.getId());
        System.out.println(jopa.getTaskStatus());
        Subtask alreadyPokakal = manager.subTask("Какать", "", TaskStatus.DONE);
        Subtask alreadysmil = manager.subTask("Смывать", "", TaskStatus.DONE);
        Subtask ynitazOtkril = manager.subTask("Открыть унитаз", "", TaskStatus.DONE);
        manager.updSubTask(alreadyPokakal, kakat.getId(), jopa.getId());
        manager.updSubTask(alreadysmil, smivanie.getId(), jopa.getId());
        manager.updSubTask(ynitazOtkril, ynitaz.getId(), jopa.getId());
        System.out.println(jopa.getTaskStatus());
        Subtask popuPomit = manager.subTask("Помыть Попу", "", TaskStatus.NEW);
        manager.addSubTask(popuPomit, jopa.getId());
        System.out.println(manager.epicSubTaskList(jopa.getId()));
        System.out.println(jopa.getTaskStatus());
        Task task = manager.task("","",TaskStatus.NEW);
        manager.addTask(task);
        manager.delete(task.getId());
        System.out.println(manager.searchTask(task.getId()));
        System.out.println("Статус до удаления у Жопы " + jopa.getTaskStatus());
        manager.deleteSubTask(popuPomit.getId());
        System.out.println("Статус после удаления у Жопы " + jopa.getTaskStatus());

    }
}
