import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class Storage {
    public static void save(List<Task> list, String filename){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(list);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Task> load(String filename){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Task>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
