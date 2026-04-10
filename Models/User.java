package Models;

import java.util.ArrayList;

public class User {
    private int id;
    private String username;
    private String password;
    private double gpa;
    private ArrayList<Subject> subjects;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.gpa = 0.00;
        this.subjects = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public ArrayList<Subject> getSubjects() { return subjects; }
    public void setSubjects(ArrayList<Subject> subjects) { this.subjects = subjects; }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        this.subjects.remove(subject);
    }

    public int getGrade() {
        if (subjects.isEmpty()) return 0;
        double total = 0;
        for (Subject s : subjects) {
            total += s.getGrade();
        }
        return (int) Math.round(total / subjects.size());
    }
}
