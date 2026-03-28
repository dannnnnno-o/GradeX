package Models;
import java.time.LocalDate;

public class PendingTask extends Task {
    private LocalDate deadline;

    public PendingTask(String name, double maxScore, TaskType type, String description, LocalDate deadline) {
        super(name, 0.0, maxScore, type, description);
        this.deadline = deadline;
    }
    
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}
