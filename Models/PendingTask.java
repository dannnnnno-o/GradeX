package Models;
import java.time.LocalDate;

public class PendingTask extends Task {
    private String description;
    private LocalDate deadline;

    public PendingTask(String name, double maxScore, TaskType type, String description, LocalDate deadline) {
        super(name, 0.0, maxScore, type);
        this.description = description;
        this.deadline = deadline;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}
