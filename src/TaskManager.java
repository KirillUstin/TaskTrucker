import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class TaskManager {
    private static final String FILE_NAME = "tasks.dat";

    List<Task> task = new ArrayList<>();
    Scanner scanner = new Scanner(System.in, "UTF-8");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void addTask(){
        List<Task> t = Storage.load(FILE_NAME);

        System.out.println("Введите название задачи: ");
        String title = scanner.nextLine();

        System.out.println("Введите полное описание задачи: ");
        String description = scanner.nextLine();

        System.out.println("Введите дату, до которой надо выполнить задание (дд.мм.гггг): ");
        String in = scanner.nextLine();
        LocalDate dueDate = null;
        try {
            dueDate = LocalDate.parse(in, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты!");
        }

        System.out.println("Введите приоритет задачи \n(1-высший, 2-средний, 3-низший): ");
        int priority = scanner.nextInt();
        scanner.nextLine();
        if(priority > 3){
            System.out.println("Приоритет не может быть больше 3.");
            priority = 0;
        }

        boolean completed = false;

        Task temp = new Task(title, description, dueDate, priority, completed);
        t.add(temp);

        Storage.save(t, FILE_NAME);
    }

    public void showTask(){
        List<Task> t = Storage.load(FILE_NAME);
        if(t.isEmpty()){
            System.out.println("Задач нет");
        } else{
            for(int i = 0; i < t.size(); i++){
                System.out.println((i + 1) + ". " + t.get(i));
            }
        }
    }

    public void completedTask(){
        List<Task> t = Storage.load(FILE_NAME);

        System.out.println("Введите название задачи, которую хотите завершить: ");
        String compl = scanner.nextLine();

        boolean found = false;

        if(t.isEmpty()){
            System.out.println("Задач нет");
        } else{
            for(int i = 0; i < t.size(); i++){
                if(t.get(i).getTitle().equals(compl)){
                    t.get(i).markCompleted();
                    System.out.println("Задача завершена.");
                    found = true;
                }
            }
        }
        
        if(found){
            Storage.save(t, FILE_NAME);
        } else{
            System.out.println("Такой задачи нет в списке");
        }
    }

    public List<Task> getTask() {
        return task;
    }

    public void setTask(List<Task> t){
        this.task = t;
    }

    public void deleteTask(){
        List<Task> task = Storage.load(FILE_NAME);
        boolean found = false;

        System.out.println("Введите название задачи, которую хотите удалить:");
        String nameDelTask = scanner.nextLine();

        for(int i = 0; i < task.size(); i++){
            if(task.get(i).getTitle().equals(nameDelTask)){
                task.remove(i);
                System.out.println("Задача '" + task.get(i).getTitle() + "' удалена.");
                found = true;
                break;
            }
        }

        if(found){
            Storage.save(task, FILE_NAME);
        } else {
            System.out.println("Такой задачи нет в списке.");
        }
    }

    public void editTask(){
        List<Task> task = Storage.load(FILE_NAME);
        boolean found = false;

        System.out.println("Введите название задачи, которую хотите отредактировать");
        String nameEditTask = scanner.nextLine();

        for(int i = 0; i < task.size(); i++){
            if(task.get(i).getTitle().equals(nameEditTask)){
                System.out.println("Введите новое описание задачи:");
                String newDes = scanner.nextLine();
                task.get(i).setDescription(newDes);

                System.out.println("Введите новую дату, до которой надо выполнить задачу (дд.мм.гггг):");
                String in = scanner.nextLine();
                LocalDate newDate = null;
                try {
                    newDate = LocalDate.parse(in, formatter);
                    task.get(i).setDueDate(newDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Неверный формат даты!");
                }

                System.out.println("Введите новый приоритет:");
                int newPrior = scanner.nextInt();
                scanner.nextLine();
                if(newPrior > 3){
                    System.out.println("Приоритет не может быть больше 3");
                } else{
                    task.get(i).setPriority(newPrior);
                }

                System.out.println("Задача выполнена?(y/n): ");
                String tempCompl = scanner.nextLine();
                if(tempCompl.equals("y")){
                    task.get(i).setCompleted(true);
                } else if(tempCompl.equals("n")) {
                    task.get(i).setCompleted(false);
                } else {
                    System.out.println("Неверный формат!");
                }

                System.out.println("\nЗадача успешно изменена.");
                found = true;
                break;
            }
        }

        if(found){
            Storage.save(task, FILE_NAME);
        } else {
            System.out.println("Такой задачи нет в списке.");
        }
    }

    public void sortByPriority(){
        List<Task> task = Storage.load(FILE_NAME);

        if(task.isEmpty()){
            System.out.println("Список пуст");
        } else {
            for(int i = 0; i < task.size() - 1; i++){
                for(int j = 0; j < task.size() - 1 - i; j++){
                    if(task.get(j).getPriority() > task.get(j + 1).getPriority()){
                        Task temp = task.get(j);
                        task.set(j, task.get(j + 1));
                        task.set(j + 1, temp);
                    }
                }
            }

            System.out.println("Задачи отсортированы");
        }
        Storage.save(task, FILE_NAME);
    }
}
