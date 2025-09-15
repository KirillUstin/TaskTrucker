import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Scanner scanner = new Scanner(System.in, "UTF-8");
        
        while(true){
            System.out.println("-----Меню-----");
            System.out.println("1) Добавить задачу");
            System.out.println("2) Вывести список всех задач");
            System.out.println("3) Завершить задачу");
            System.out.println("4) Удалить задачу");
            System.out.println("5) Редактировать задачу");
            System.out.println("6) Отсортировать задачи по приоритету");
            System.out.println("0) Выход");
            System.out.print("Выберите действие:");
            int choice;

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (NumberFormatException e) {
                System.out.println("Ошибка. Неверный ввод. Введите число от 1 до 4.");
                continue;
            }

            switch (choice) {
                case 1:
                    manager.addTask();
                    break;

                case 2:
                    manager.showTask();
                    break;

                case 3:
                    manager.completedTask();
                    break;

                case 4:
                    manager.deleteTask();
                    break;

                case 5:
                    manager.editTask();
                    break;

                case 6:
                    manager.sortByPriority();
                    break;

                case 0:
                    System.out.println("Выход из программы.");
                    return;
            
                default:
                    System.out.println("Выбрано неправильное действие. Повторите попытку");
            }
        }

    }
} 