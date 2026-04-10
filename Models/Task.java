package Models;

public abstract class Task {
    private String name;
    private double score;
    private double maxScore;
    private TaskType type;
    private String description;

    public Task() {
        this.name = null;
        this.score = 0.0;
        this.maxScore = 0.0;
        this.type = null;
        this.description = null;
    }

    public Task(String name, double score, double maxScore, TaskType type, String description) {
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String convertTo11Point(double percentage) {
        int p = (int) Math.round(percentage);
        if (p >= 99) return "1.00";
        if (p >= 96) return "1.25";
        if (p >= 93) return "1.50";
        if (p >= 90) return "1.75";
        if (p >= 87) return "2.00";
        if (p >= 84) return "2.25";
        if (p >= 81) return "2.50";
        if (p >= 78) return "2.75";
        if (p >= 75) return "3.00";
        if (p >= 60) return "4.00";
        return "5.00";
    }
}
