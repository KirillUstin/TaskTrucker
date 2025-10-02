import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class TaskManager {
    Scanner scanner = new Scanner(System.in, "UTF-8");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    AuxiliaryMethods auxMethods = new AuxiliaryMethods();

    //добавление задачи в базу данных
    public void addTask(){     
        Task task = new Task(null, null, null, 0, false);
 
        task = auxMethods.inputData();
        
        String sql = "INSERT INTO tasks (title, description, dueDate, priority, completed) VALUES (?, ?, ?, ?, ?)";

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement stmt = connect.prepareStatement(sql);

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(task.getDueDate()));
            stmt.setInt(4, task.getPriority());
            stmt.setBoolean(5, task.getCompleted());

            stmt.executeUpdate();
            System.out.println("Задача добавлена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //вывод всех задач
    public void showTask(){
        String sql = "SELECT * FROM tasks";
        boolean empty = true;

        try (Connection connect = DBConnection.getConnection()){
            java.sql.Statement stmt = connect.createStatement();
            java.sql.ResultSet res = stmt.executeQuery(sql);

            while(res.next()){
                empty = false;
                
                Task task = new Task(null, null, null, 0, false);
                task = auxMethods.getData(res);

                System.out.println(task);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(empty){
            System.out.println("База данных пуста");
        }
    }

    //отметить задачу как выполненную
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
            
            empty = auxMethods.checkChanges(pstmt, "Задача завершена");

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
        boolean found = true;

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement pstmt = connect.prepareStatement(sql);

            pstmt.setString(1, nameDel);

            found = auxMethods.checkChanges(pstmt, "Задача удалена");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(found){
            System.out.println("Задача удалена");
        }
    }

    //редактирование задачи
    public void editTask(){
        String sql = "UPDATE tasks SET description = ?, dueDate = ?, priority = ?, completed = ? WHERE title = ?";
        String sqlCheckTitle = "SELECT 1 FROM tasks WHERE title = ?";

        System.out.println("Введите название задачи, которую хотите отредактировать");
        String nameEditTask = scanner.nextLine();
        
        //проверка на наличие задачи
        try (Connection con = DBConnection.getConnection()){
            java.sql.PreparedStatement tempPstmt = con.prepareStatement(sqlCheckTitle);
            boolean availability = false;
            
            tempPstmt.setString(1, nameEditTask);

            try (java.sql.ResultSet res = tempPstmt.executeQuery()){
                if(res.next()){
                    availability = true;
                }
            }

            if(!availability){
                System.out.println("Задачи с таким названием нет. Попробуйте снова");
                editTask();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Task task = new Task(null, null, null, 0, false);
        task = auxMethods.updateData(nameEditTask);

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement pstmt = connect.prepareStatement(sql);

            pstmt.setString(1, task.getDescription());
            pstmt.setDate(2, java.sql.Date.valueOf(task.getDueDate()));
            pstmt.setInt(3, task.getPriority());
            pstmt.setBoolean(4, task.getCompleted());
            pstmt.setString(5, nameEditTask);

            auxMethods.checkChanges(pstmt, "Задача успешно изменена!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //сортировка задач по приоритету (сначала высший)
    public void sortByPriority(){
        String sql = "SELECT * FROM tasks ORDER BY priority ASC";

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement pstmt = connect.prepareStatement(sql);
            java.sql.ResultSet res = pstmt.executeQuery();

            while(res.next()){
                Task task = new Task(null, null, null, 0, false);

                task = auxMethods.getData(res);

                System.out.println(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //сортировка задач по дедлайну (сначала ближе всех к сегодняшнему дню)
    public void sortByDate(){
        String sql = "SELECT * FROM tasks ORDER BY dueDate DESC";

        try (Connection connect = DBConnection.getConnection()){
            java.sql.PreparedStatement pstmt = connect.prepareStatement(sql);
            java.sql.ResultSet res = pstmt.executeQuery();

            while(res.next()){
                Task task = new Task(null, null, null, 0, false);

                task = auxMethods.getData(res);

                System.out.println(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}