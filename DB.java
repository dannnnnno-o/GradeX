import java.sql.*;
import java.time.LocalDate;
import Models.*;

public class DB {
    static String url = "jdbc:postgresql://localhost:5432/GradeX";
    static String username = "postgres";
    static String password = "1234";

    static Connection connection;

    static public String query;
    static public PreparedStatement statement;
    static public ResultSet result;

    public DB() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
            init(); // Create tables if they don't exist
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws SQLException {
        Statement stmt = connection.createStatement();

        // Create Users Table
        stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(255) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "gpa DOUBLE PRECISION DEFAULT 0.00)");

        // Create Subjects Table
        stmt.execute("CREATE TABLE IF NOT EXISTS subjects (" +
                "id SERIAL PRIMARY KEY, " +
                "user_id INTEGER REFERENCES users(id) ON DELETE CASCADE, " +
                "name VARCHAR(255) NOT NULL, " +
                "rubrics INTEGER[] NOT NULL, " +
                "grade DOUBLE PRECISION DEFAULT 0.0)");

        // Create Tasks Table
        stmt.execute("CREATE TABLE IF NOT EXISTS tasks (" +
                "id SERIAL PRIMARY KEY, " +
                "subject_id INTEGER REFERENCES subjects(id) ON DELETE CASCADE, " +
                "name VARCHAR(255) NOT NULL, " +
                "type VARCHAR(20) NOT NULL, " +
                "details TEXT, " +
                "target_score DOUBLE PRECISION, " +
                "actual_score DOUBLE PRECISION, " +
                "due_date DATE, " +
                "is_completed BOOLEAN NOT NULL)");

        stmt.close();
    }

    public User authenticate(String user, String pass) {
        try {
            query = "SELECT * FROM users WHERE username=? AND password=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, user);
            statement.setString(2, pass);
            result = statement.executeQuery();

            if (result.next()) {
                User u = new User(result.getString("username"), result.getString("password"));
                u.setId(result.getInt("id"));
                u.setGpa(result.getDouble("gpa"));
                loadUserData(u, u.getId());
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Auth failed
    }

    public boolean register(String user, String pass) {
        try {
            query = "INSERT INTO users (username, password, gpa) VALUES (?, ?, 0.00) RETURNING id";
            statement = connection.prepareStatement(query);
            statement.setString(1, user);
            statement.setString(2, pass);
            result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            // Likely username exists
            return false;
        }
    }

    private void loadUserData(User user, int userId) throws SQLException {
        query = "SELECT * FROM subjects WHERE user_id=?";
        PreparedStatement subStmt = connection.prepareStatement(query);
        subStmt.setInt(1, userId);
        ResultSet subResult = subStmt.executeQuery();

        while (subResult.next()) {
            int subId = subResult.getInt("id");
            Array rubricArray = subResult.getArray("rubrics");
            Integer[] rubricsTyped = (Integer[]) rubricArray.getArray();
            int[] rubrics = new int[rubricsTyped.length];
            for (int i = 0; i < rubricsTyped.length; i++)
                rubrics[i] = rubricsTyped[i];

            Subject sub = new Subject(subResult.getString("name"), rubrics);
            sub.setGrade(subResult.getDouble("grade"));

            // Load tasks for this subject
            loadTasks(sub, subId);
            user.addSubject(sub);
        }
        subStmt.close();
        subResult.close();
    }

    private void loadTasks(Subject subject, int subjectId) throws SQLException {
        query = "SELECT * FROM tasks WHERE subject_id=?";
        PreparedStatement taskStmt = connection.prepareStatement(query);
        taskStmt.setInt(1, subjectId);
        ResultSet taskResult = taskStmt.executeQuery();

        while (taskResult.next()) {
            Task task;
            String typeStr = taskResult.getString("type");
            TaskType type = TaskType.valueOf(typeStr);
            String name = taskResult.getString("name");
            String details = taskResult.getString("details");
            double target = taskResult.getDouble("target_score");
            boolean completed = taskResult.getBoolean("is_completed");

            if (completed) {
                task = new CompletedTask(name, taskResult.getDouble("actual_score"), target, type, details);
            } else {
                Date d = taskResult.getDate("due_date");
                LocalDate dueDate = d != null ? d.toLocalDate() : null;
                task = new PendingTask(name, (int) target, type, details, dueDate);
            }
            subject.addTask(task);
        }
        taskStmt.close();
        taskResult.close();
    }

    public void saveUser(User user) {
        try {
            // First get user ID
            query = "SELECT id FROM users WHERE username=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            result = statement.executeQuery();
            if (!result.next())
                return;
            int userId = result.getInt("id");

            // Delete current data to refresh (simple sync strategy)
            query = "DELETE FROM subjects WHERE user_id=?";
            PreparedStatement delStmt = connection.prepareStatement(query);
            delStmt.setInt(1, userId);
            delStmt.executeUpdate();
            delStmt.close();

            // Save subjects and tasks
            for (Subject s : user.getSubjects()) {
                query = "INSERT INTO subjects (user_id, name, rubrics, grade) VALUES (?, ?, ?, ?) RETURNING id";
                PreparedStatement insSub = connection.prepareStatement(query);
                insSub.setInt(1, userId);
                insSub.setString(2, s.getName());

                Integer[] rubrics = new Integer[s.getRubrics().length];
                for (int i = 0; i < rubrics.length; i++)
                    rubrics[i] = s.getRubrics()[i];
                Array rbArray = connection.createArrayOf("INTEGER", rubrics);
                insSub.setArray(3, rbArray);
                insSub.setDouble(4, s.getGrade());

                ResultSet insRes = insSub.executeQuery();
                if (insRes.next()) {
                    int subId = insRes.getInt("id");
                    saveTasks(s, subId);
                }
                insSub.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveTasks(Subject subject, int subjectId) throws SQLException {
        for (Task t : subject.getAllTasks()) {
            query = "INSERT INTO tasks (subject_id, name, type, details, target_score, actual_score, due_date, is_completed) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insTask = connection.prepareStatement(query);
            insTask.setInt(1, subjectId);
            insTask.setString(2, t.getName());
            insTask.setString(3, t.getType().name());
            insTask.setString(4, t.getDescription());

            if (t instanceof CompletedTask) {
                CompletedTask ct = (CompletedTask) t;
                insTask.setDouble(5, ct.getMaxScore());
                insTask.setDouble(6, ct.getScore());
                insTask.setNull(7, Types.DATE);
                insTask.setBoolean(8, true);
            } else {
                PendingTask pt = (PendingTask) t;
                insTask.setDouble(5, pt.getMaxScore());
                insTask.setNull(6, Types.DOUBLE);
                insTask.setDate(7, pt.getDeadline() != null ? Date.valueOf(pt.getDeadline()) : null);
                insTask.setBoolean(8, false);
            }
            insTask.executeUpdate();
            insTask.close();
        }
    }

    static String GetUserNameByID(int id) throws SQLException {
        String name = "";
        query = "SELECT * FROM users WHERE id=?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        result = statement.executeQuery();
        if (result.next()) {
            name = result.getString("username");
        }

        statement.close();
        result.close();
        if (name.equals("")) {
            return "no users found";
        }
        return name;
    }
}
