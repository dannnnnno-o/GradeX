package Models;

public abstract class Task {
    private String name;
    private double score;
    private double maxScore;
    private TaskType type;

    public Task() {
        this.name = null;
        this.score = 0.0;
        this.maxScore = 0.0;
        this.type = null;
    }

    public Task(String name, double score, double maxScore, TaskType type) {
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.type = type;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    
    public double getMaxScore() { return maxScore; }
    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }
    
    public TaskType getType() { return type; }
    public void setType(TaskType type) { this.type = type; }
}
