package com.kirk;

import java.util.Scanner;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class AuxiliaryMethods {
    private Scanner scanner;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT);

    public AuxiliaryMethods(Scanner scanner){
        this.scanner = scanner;
    }

    //вывод меню (для главной функции main)
    public void showMenu(){
        System.out.print("""
        \n-----Меню-----
        1) Добавить задачу
        2) Вывести список всех задач
        3) Завершить задачу
        4) Удалить задачу
        5) Редактировать задачу
        6) Отсортировать задачи по приоритету
        7) Отсортировать задачи по дате, до которой надо выполнить
        0) Выход
        Выберите действие:""");
    }

    //ввод значения для взаимодействия с консолью (для главной функции main)
    public int inputChoice(){
        Integer choice = 0;
        boolean isValid = false;

        while(!isValid){
            String entered_string = scanner.nextLine();

            choice = parser(entered_string);

            isValid = validator(choice, 0, 7);
        }
        
        return choice;
    }

    //ввод приоритетности
    public int inputPriority(){
        boolean isValid = false;
        Integer priority_number = null;

        while(!isValid){
            String entereted_string = scanner.nextLine();

            priority_number = parser(entereted_string);

            if(validator(priority_number, 1, 3)){
                isValid = true;
            } else{
                System.out.println("Введите число заново: ");
            }
        }

        return (int) priority_number;
    }

    //парсинг строки, полученной с консоли (для функций inputChoice и inputPriority)
    public Integer parser(String input){
        Integer parseInt = null;
        
        try{
            parseInt = Integer.parseInt(input);
        } catch(NumberFormatException e){
            System.out.println("Ошибка! Неверный формат ввода. Введите число");
            System.out.println(e.getMessage());
        }
        
        return parseInt;
    }
    
    //проверка валидности значения (для функций inputChoice и inputPriority)
    public boolean validator(Integer choice, int lower_limit, int upper_limit){
        if(choice == null){
            return false;
        }

        if(lower_limit > upper_limit){
            int temp = lower_limit;
            lower_limit = upper_limit;
            upper_limit = temp;
        }

        if(choice >= lower_limit && choice <= upper_limit){
            return true;
        } else{
            System.out.println("Число не должно быть меньше " + lower_limit + " и больше " + upper_limit + "!");
            return false;
        }
    }
    
    //ввод даты
    public LocalDate inputDate(){
        boolean validInput = true;
        String entereted_string;
        LocalDate dueDate = null;

        while(validInput){
            entereted_string = scanner.nextLine();

            dueDate = parserDate(entereted_string);

            if(dueDate != null){
                validInput = false;
            } else{
                System.out.println("Введите дату заново в формате 'дд.мм.гггг': ");
            }
        }
        
        return dueDate;
    }

    //парсинг введеной даты в формате строки
    public LocalDate parserDate(String input){
        LocalDate dueDate = null;

        try{
            dueDate = LocalDate.parse(input, formatter);
        }catch(DateTimeParseException e){
            System.out.println("Неверный формат даты!");
            System.out.println(e.getMessage());
        }
        
        return dueDate;
    }
    
    //ввод выполненности (для функции updateData в этом методе)
    public boolean inputNewCompleted(){
        Boolean result = null;
        boolean isValid = false;
        
        while(!isValid){
            String strCompleted = scanner.nextLine();
            result = validatorNewCompleted(strCompleted);
            
            if(result == true || result == false){
                isValid = true;
            } else{
                System.out.println("Следуйте формату 'Y/n' и введите заново: ");
            }
        }
        
        return (boolean) result;
    }

    //проверка валидности введеной строки для указания выполненности задачи
    public Boolean validatorNewCompleted(String input){
        if(input == null){
            return null;
        }

        input = input.trim();

        if(input.isEmpty() || input.length() > 1){
            System.out.println("Должен быть введен один символ.");
            return null;
        } else{
            if(input.equalsIgnoreCase("y")){
                return true;
            } else if(input.equalsIgnoreCase("n")){
                return false;
            } else{
                System.out.println("Неправильный формат ввода.");
                return null;
            }
        }
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
        LocalDate dueD = inputDate();

        System.out.println("Введите приоритет задачи \n(1-высший, 2-средний, 3-низший): ");
        int prior = inputPriority();

        boolean compl = false;

        return new Task(tit, des, dueD, prior, compl);
    }

    //обновление данных (для функции editTask)
    public Task updateData(String title){
        System.out.println("Введите новое описание задачи: ");
        String newDes = scanner.nextLine();

        System.out.println("Введите новую дату(дд.мм.гггг): ");
        LocalDate newDate = inputDate();

        System.out.println("Введите новую приоритетность: ");
        int newPrior = inputPriority();

        System.out.println("Задача выполнена?(y/n): ");
        boolean newCompleted = inputNewCompleted();

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
