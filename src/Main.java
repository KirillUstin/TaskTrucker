import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager manager = new TaskManager();
        AuxiliaryMethods auxMethods = new AuxiliaryMethods();

        testConnect.main();

        
        while(true){
            auxMethods.showMenu();
            int choice = auxMethods.inputAndCheckChoice();

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

                case 7:
                    manager.sortByDate();
                    break;

                case 0:
                    System.out.println("Выход из программы.");
                    return;
            
                default:
                    System.out.println("Выбрано неправильное действие. Повторите попытку\n");
            }
        }

    }
} 