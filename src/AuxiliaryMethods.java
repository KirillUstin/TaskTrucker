import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AuxiliaryMethods {
    Scanner scanner = new Scanner(System.in);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    //вывод меню (для главной функции main)
    public void showMenu(){
        System.out.println("-----Меню-----");
        System.out.println("1) Добавить задачу");
        System.out.println("2) Вывести список всех задач");
        System.out.println("3) Завершить задачу");
        System.out.println("4) Удалить задачу");
        System.out.println("5) Редактировать задачу");
        System.out.println("6) Отсортировать задачи по приоритету");
        System.out.println("7) Отсортировать задачи по дате, до которой надо выполнить");
        System.out.println("0) Выход");
        System.out.print("Выберите действие:");
    }

    //ввод и проверка введенного значения для взаимодействия с консолью (для главной функции main)
    public int inputAndCheckChoice(){
        int choice;
        while(true){
            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                if(choice >= 0 && choice <= 7){
                    break;
                } else{
                    System.out.println("Число не должно быть меньше 0 и больше 7!\nВведите число заново:");
                }

            } catch (InputMismatchException e) {
                System.out.println("Ошибка! Неверный формат ввода. Введите число от 0 до 7.");
                scanner.nextLine();
            }

        }
        
        return choice;
    }

    //ввод приоритетности и проверка
    public int inputAndCheckPriority(){
        int priority = scanner.nextInt();
        scanner.nextLine();

        if(priority > 3){
            System.out.println("Приоритет не может быть выше 3!\nВведите приоритет заново: ");
            priority = inputAndCheckPriority();
        } else {
            return priority;
        }

        return priority;
    }
    
    //ввод даты и проверка формата 
    public LocalDate inputAndCheckDate(){
        String input = scanner.nextLine();
        LocalDate dueDate = null;
        
        try {
            dueDate = LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты!\nВведите дату заново в формате 'дд.мм.гггг': ");
            dueDate = inputAndCheckDate();
        }
        
        return dueDate;
    }
    
    //ввод и проверка выполненности (для функции updateData в этом методе)
    public boolean inputAndCheckNewCompleted(){
        String strCompleted = scanner.nextLine();
        boolean result = false;

        if(strCompleted.length() > 1){
            System.out.println("Должен быть введен максимум один символ!\nСледуйте формату 'Y/n' и введите выполненность заново: ");
            return inputAndCheckNewCompleted();
        } else{
            if(strCompleted.equalsIgnoreCase("y")){
                result = true;
            } else if(strCompleted.equalsIgnoreCase("n")){
                result = false;
            } else{
                System.out.println("Неправильный формат ввода. Пожалуйста, введи выполненность заново в формате 'Y/n': ");
                return inputAndCheckNewCompleted();
            }
        }
        
        return result;
    }

    //проверка на изменения строк в базе
    public boolean checkChanges(java.sql.PreparedStatement stmt, String massage){
        boolean empty = false;

        try {
            int rows = stmt.executeUpdate();

            if(rows > 0){
                empty = true;
                System.out.println(massage);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empty;
    }

    //ввод данных (для функции addTask)
    public Task inputData(){
        System.out.println("Введите название задачи: ");
        String tit = scanner.nextLine();

        System.out.println("Введите полное описание задачи: ");
        String des = scanner.nextLine();

        System.out.println("Введите дату, до которой надо выполнить задание (дд.мм.гггг): ");
        LocalDate dueD = inputAndCheckDate();

        System.out.println("Введите приоритет задачи \n(1-высший, 2-средний, 3-низший): ");
        int prior = inputAndCheckPriority();

        boolean compl = false;

        return new Task(tit, des, dueD, prior, compl);
    }

    //обновление данных (для функции editTask)
    public Task updateData(String title){
        System.out.println("Введите новое описание задачи: ");
        String newDes = scanner.nextLine();

        System.out.println("Введите новую дату(дд.мм.гггг): ");
        LocalDate newDate = inputAndCheckDate();

        System.out.println("Введите новую приоритетность: ");
        int newPrior = inputAndCheckPriority();

        System.out.println("Задача выполнена?(y/n): ");
        boolean newCompleted = inputAndCheckNewCompleted();

        return new Task(title, newDes, newDate, newPrior, newCompleted);
    }

    //получение данных с базы (для функций showTask, sortByPriority и sortByDate)
    public Task getData(java.sql.ResultSet res){
        String title = null;
        String description = null;
        LocalDate dueDate = null;
        int priority = 0;
        boolean completed = false;

        try {
            title = res.getString("title");
            description = res.getString("description");
            dueDate = res.getDate("dueDate").toLocalDate();
            priority = res.getInt("priority");
            completed = res.getBoolean("completed");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Task(title, description , dueDate, priority, completed);
    }
}
