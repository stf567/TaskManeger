package Console;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Serializable {
    private String description;
    private boolean isCompleted;
    private LocalDateTime createdDate;
    private LocalDateTime completedDate;

    public Task(String description) {
        this.description = description;
        this.isCompleted = false;
        this.createdDate = LocalDateTime.now();
        this.completedDate = null;
    }

    //сеттеры
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    // Геттеры
    public String getDescription() { return description; }
    public boolean isCompleted() { return isCompleted; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getCompletedDate() { return completedDate; }

    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public void setCompleted(boolean completed) { 
        isCompleted = completed;
        completedDate = completed ? LocalDateTime.now() : null;
    }
}