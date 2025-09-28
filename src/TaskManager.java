import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class TaskManager {
    Scanner scanner = new Scanner(System.in, "UTF-8");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    //добавление задачи в базу данных
    public void addTask(){
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

        String sql = "INSERT INTO tasks (title, description, dueDate, priority, completed) VALUES (?, ?, ?, ?, ?)";

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement stmt = connect.prepareStatement(sql);

            stmt.setString(1, title);
            stmt.setString(2, description);
            if(dueDate != null){
                stmt.setDate(3, java.sql.Date.valueOf(dueDate));
            } else{
                stmt.setNull(3, java.sql.Types.DATE);
            }
            stmt.setInt(4, priority);
            stmt.setBoolean(5, completed);

            stmt.executeUpdate();
            System.out.println("Задача добавлена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //вывод всех задач
    public void showTask(){
        String sql = "SELECT * FROM tasks";

        try (Connection connect = DBConnection.getConnection()){
            java.sql.Statement stmt = connect.createStatement();
            java.sql.ResultSet res = stmt.executeQuery(sql);

            boolean empty = true;

            while(res.next()){
                empty = false;
                
                String title = res.getString("title");
                String description = res.getString("description");
                LocalDate dueDate = res.getDate("dueDate") != null ? res.getDate("dueDate").toLocalDate() : null;
                int priority = res.getInt("priority");
                boolean completed = res.getBoolean("completed");

                Task task = new Task(title, description, dueDate, priority, completed);

                System.out.println(task);
            }

            if(empty){
                System.out.println("База данных пуста");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //пометить задачу как выполненную
    public void completedTask(){
        String sql = "UPDATE tasks SET completed = ? WHERE title = ?";

        System.out.println("Введите название задачи, которую хотите завершить: ");
        String titleCompl = scanner.nextLine();

        boolean newCompleted = true;
        boolean empty = true;

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement pstmt = connect.prepareStatement(sql);

            pstmt.setBoolean(1, newCompleted);
            pstmt.setString(2, titleCompl);
            
            int rows = pstmt.executeUpdate();
            
            if(rows > 0){
                empty = false;
                System.out.println("Задача [" + titleCompl + "] завершена");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(empty){
            System.out.println("Такой задачи нет в базе");
        }
    }
    
    //удаление задачи из базы данных
    public void deleteTask(){
        String sql = "DELETE FROM tasks WHERE title = ?";

        System.out.println("Введите название задачи, которую хотите удалить: ");
        String nameDel = scanner.nextLine();
        boolean found = false;

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement pstmt = connect.prepareStatement(sql);

            pstmt.setString(1, nameDel);
            found = true;

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!found){
            System.out.println("Такой задачи нет в базе");
        } else{
            System.out.println("Задача удалена");
        }
    }

    //редактирование задачи
    public void editTask(){
        String sql = "UPDATE tasks SET description = ?, dueDate = ?, priority = ?, completed = ? WHERE title = ?";
        String sqlCheckTitle = "SELECT 1 FROM tasks WHERE title = ?";

        System.out.println("Введите название задачи, которую хотите отредактировать");
        String nameEditTask = scanner.nextLine();
        boolean found = false;

        //проверка на наличие задачи
        try (Connection con = DBConnection.getConnection()){
            java.sql.PreparedStatement tempPstmt = con.prepareStatement(sqlCheckTitle);
            
            tempPstmt.setString(1, nameEditTask);

            try (java.sql.ResultSet res = tempPstmt.executeQuery()){
                if(res.next()){
                    found = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!found){
            System.out.println("Задачи с таким названием нет. Попробуйте снова");
            return;
        }
        
        System.out.println("Введите новое описание задачи: ");
        String newDes = scanner.nextLine();

        System.out.println("Введите новую дату(дд.мм.гггг): ");
        String tempNewDate = scanner.nextLine();
        LocalDate newDate = null;
        
        try {
            newDate = LocalDate.parse(tempNewDate, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты");
        }

        System.out.println("Введите новую приоритетность: ");
        int newPrior = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Задача выполнена?(y/n): ");
        String tempStrCompl = scanner.nextLine();
        boolean newCompleted = false;

        if(tempStrCompl.length() > 1){
            System.out.println("Количество введенных символов не должно превышать 1!");
        } else{
            if(tempStrCompl == "y" || tempStrCompl == "Y"){
                newCompleted = true;
            } else if(tempStrCompl == "n" && tempStrCompl == "N"){
                newCompleted = false;
            } else{
                System.out.println("Неверный формат ввода! Следуйте примеру в скобках.");
            }
        }

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement pstmt = connect.prepareStatement(sql);

            pstmt.setString(1, newDes);
            if(newDate != null){
                pstmt.setDate(2, java.sql.Date.valueOf(newDate));
            } else{
                pstmt.setNull(2, java.sql.Types.DATE);
            }
            pstmt.setInt(3, newPrior);
            pstmt.setBoolean(4, newCompleted);
            pstmt.setString(5, nameEditTask);

            int rows = pstmt.getUpdateCount();

            if(rows > 1){
                found = true;
                System.out.println("Задача успешно изменена!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!found){
            System.out.println("Задачи с таким названием нет в базе");
        }
    }

    //сортировка задач по приоритету
    public void sortByPriority(){
        String sql = "SELECT * FROM tasks ORDER BY priority ASC";

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement pstmt = connect.prepareStatement(sql);
            java.sql.ResultSet res = pstmt.executeQuery();

            while(res.next()){
                String t = res.getString("title");
                String d = res.getString("description");
                LocalDate dD = res.getDate("dueDate") != null ? res.getDate("dueDate").toLocalDate() : null;
                int p = res.getInt("priority");
                boolean c = res.getBoolean("completed");

                Task task = new Task(t, d, dD, p, c);

                System.out.println(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sortByData(){
        String sql = "SELECT * FROM tasks ORDER BY dueDate DESC";

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement pstmt = connect.prepareStatement(sql);
            java.sql.ResultSet res = pstmt.executeQuery();

            while(res.next()){
                String t = res.getString("title");
                String d = res.getString("description");
                LocalDate dD = res.getDate("dueDate") != null ? res.getDate("dueDate").toLocalDate() : null;
                int p = res.getInt("priority");
                boolean c = res.getBoolean("completed");

                Task task = new Task(t, d, dD, p, c);

                System.out.println(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}