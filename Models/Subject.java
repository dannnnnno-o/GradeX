package Models;
import java.util.ArrayList;

public class Subject {
    private String name;
    private ArrayList<ArrayList<Task>> tasks;
    private int[] rubrics;
    private double grade;

    public Subject(String name, int[] rubrics) {
        this.name = name;
        this.rubrics = rubrics;
        this.grade = 0.0;
        
        // Map indices to TaskType ordinals: 0=EXAM, 1=QUIZ, 2=ACTIVITY, 3=ASSIGNMENT, 4=PROJECT
        // Based on the user's example, rubrics maps perfectly to TaskType enum order if kept consistent.
        this.tasks = new ArrayList<>(TaskType.values().length);
        for (int i = 0; i < TaskType.values().length; i++) {
            this.tasks.add(new ArrayList<>());
        }
    }

    public void addTask(Task task) {
        if(task == null || task.getType() == null) return;
        int index = task.getType().ordinal();
        this.tasks.get(index).add(task);
    }
    
    public void removeTask(Task task) {
        if(task == null || task.getType() == null) return;
        int index = task.getType().ordinal();
        this.tasks.get(index).remove(task);
    }
    
    public ArrayList<Task> getTasksByType(TaskType type) {
        return this.tasks.get(type.ordinal());
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public ArrayList<ArrayList<Task>> getTasks() { return tasks; }
    
    public int[] getRubrics() { return rubrics; }
    public void setRubrics(int[] rubrics) { this.rubrics = rubrics; }
    
    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }
    
    // Returns all tasks flat
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> all = new ArrayList<>();
        for (ArrayList<Task> list : tasks) {
            all.addAll(list);
        }
        return all;
    }

    public int getCompletedTasksCount() {
        int count = 0;
        for (Task t : getAllTasks()) {
            if (t instanceof CompletedTask) count++;
        }
        return count;
    }

    public int getPendingTasksCount() {
        int count = 0;
        for (Task t : getAllTasks()) {
            if (t instanceof PendingTask) count++;
        }
        return count;
    }
}
