import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private String title; //задача
    private String description; //описание задачи
    private LocalDate dueDate; //дата, до которой надо выполнить задачу
    private int priority; //приоритет (1-высший, 2-средний, 3-низший)
    private boolean completed; //заверешенность задачи

    public Task(String t, String d, LocalDate dd, int p, boolean c){
        this.title = t;
        this.description = d;
        this.dueDate = dd;
        this.priority = p;
        this.completed = c;
    }

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public LocalDate getDueDate(){
        return dueDate;
    }
    public int getPriority(){
        return priority;
    }
    public boolean getCompleted(){
        return completed;
    }

    public void setTitle(String t){
        this.title = t;
    }
    public void setDescription(String d){
        this.description = d;
    }
    public void setDueDate(LocalDate dd){
        this.dueDate = dd;
    }
    public void setPriority(int p){
        this.priority = p;
    }
    public void setCompleted(boolean c){
        this.completed = c;
    }

    public void markCompleted(){
        this.completed = true;
    }

    @Override
    public String toString(){
        return (completed ? "[+]" : "[ ]") + title + ":\n\tОписание:" + description + "\n\tДедлайн: " + dueDate + "\n\tПриоритет: " + priority + "\n\tВыполненность: " + completed + "\n";
    }
}
