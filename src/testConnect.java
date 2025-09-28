import java.sql.Connection;
import java.sql.SQLException;

public class testConnect {
    public static void main() {
        try (Connection conn = DBConnection.getConnection()){
            System.out.println("Подключение прошло успешно");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
