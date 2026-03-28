import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Components.*;
import Models.*;
import Views.*;

public class Main {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        // Window Properties
        int WindowWidth = 1280;
        int WindowHeight = 720;
        String BackgroundColorHex = "#e9e9e9";
        Color BackgroundColor = Color.decode(BackgroundColorHex);
        String HeaderBGHex = "#f4f4f4";
        Color HeaderBG = Color.decode(HeaderBGHex);

        // Header
        int HeaderX = 0;
        int HeaderY = 0;
        int HeaderWidth = 1280;
        int HeaderHeight = 80;

        // Left Panel Properties
        int LeftPanelX = 0;
        int LeftPanelY = HeaderHeight;
        int LeftPanelWidth = (int) Math.round(WindowWidth * 0.25);
        int LeftPanelHeight = WindowHeight;

        // Mid Panel Properties
        int MidPanelX = LeftPanelWidth;
        int MidPanelY = HeaderHeight;
        int MidPanelWidth = (int) Math.round(WindowWidth * 0.5);
        int MidPanelHeight = WindowHeight;

        // Right Panel Properties
        // int RightPanelX = LeftPanelWidth + MidPanelWidth;
        int RightPanelX = 0;
        int RightPanelY = HeaderHeight;
        int RightPanelWidth = (int) Math.round(WindowWidth * 0.25);
        int RightPanelHeight = WindowHeight;

        // Frame Properties
        JFrame frame = new JFrame("GradeX");
        frame.setSize(WindowWidth, WindowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setResizable(false);

        // Header
        JPanel Header = new JPanel(new BorderLayout());
        Header.setPreferredSize(new Dimension(WindowWidth, 80));
        Header.setBackground(HeaderBG);

        JLabel hero = new JLabel("GradeX");
        hero.setFont(new Font("Raleway", Font.BOLD, 24));
        hero.setHorizontalAlignment(SwingConstants.LEFT); // left horizontally
        hero.setVerticalAlignment(SwingConstants.CENTER); // center vertically

        String BorderBottomHex = "#c9c9c9";
        Color BorderBottom = Color.decode(BorderBottomHex);

        Border LeftMargin = new EmptyBorder(0, 25, 0, 0);
        Border BottomBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, BorderBottom);

        Header.setBorder(BorderFactory.createCompoundBorder(BottomBorder, LeftMargin));
        Header.add(hero, BorderLayout.WEST);

        // Left Panel
        JPanel LeftPanel = new JPanel();
        LeftPanel.setPreferredSize(new Dimension(WindowWidth / 4, WindowHeight));
        LeftPanel.setBackground(BackgroundColor);
        LeftPanel.setLayout(new BoxLayout(LeftPanel, BoxLayout.Y_AXIS));
        LeftPanel.setBorder(new EmptyBorder(20, 0, 0, 10)); // Top 20, Right 10

        // Middle Panel
        JPanel MidPanel = new JPanel();
        MidPanel.setBackground(BackgroundColor);
        CardLayout cardLayout = new CardLayout();
        MidPanel.setLayout(cardLayout);

        // --- Dummy Data Setup ---
        List<Subject> dummySubjects = new ArrayList<>();
        
        Subject math = new Subject("Mathematics", new int[]{20, 10, 30, 40, 0});
        PendingTask mathHW = new PendingTask("Algebra Homework", 100, TaskType.ASSIGNMENT, "Do page 42", LocalDate.now().plusDays(2));
        PendingTask mathExam = new PendingTask("Midterm Exam", 100, TaskType.EXAM, "Chapters 1-5", LocalDate.now().plusDays(-1));
        PendingTask futureQuiz = new PendingTask("Pop Quiz", 10, TaskType.QUIZ, "Vectors", LocalDate.now().plusDays(5));
        CompletedTask oldQuiz = new CompletedTask("Intro Quiz", 9.5, 10, TaskType.QUIZ);
        math.addTask(mathHW);
        math.addTask(mathExam);
        math.addTask(futureQuiz);
        math.addTask(oldQuiz);
        math.setGrade(95.0);
        dummySubjects.add(math);

        Subject science = new Subject("Science", new int[]{15, 15, 20, 50, 0});
        PendingTask lab = new PendingTask("Volcano Lab", 50, TaskType.ACTIVITY, "Build model", LocalDate.now().plusDays(4));
        CompletedTask oldLab = new CompletedTask("Cell Lab", 48, 50, TaskType.ACTIVITY);
        science.addTask(lab);
        science.addTask(oldLab);
        science.setGrade(96.0);
        dummySubjects.add(science);
        
        Subject history = new Subject("History", new int[]{10, 20, 30, 40, 0});
        PendingTask essay = new PendingTask("WW2 Essay", 100, TaskType.PROJECT, "Write 5 pages", LocalDate.now().plusDays(10));
        history.addTask(essay);
        history.setGrade(88.5);
        dummySubjects.add(history);

        SubjectDetailsView subjectDetailsView = new SubjectDetailsView();
        
        SubjectsView subjectsView = new SubjectsView(dummySubjects, (sub) -> {
            subjectDetailsView.setSubject(sub);
            cardLayout.show(MidPanel, "DETAILS");
        }, () -> {
            frame.repaint();
        });
        
        CalendarView calendarView = new CalendarView(dummySubjects);

        MidPanel.add(subjectsView, "SUBJECTS");
        MidPanel.add(calendarView, "CALENDAR");
        MidPanel.add(subjectDetailsView, "DETAILS");

        // Right Panel
        JPanel RightPanel = new JPanel();
        RightPanel.setPreferredSize(new Dimension(WindowWidth / 4, WindowHeight));
        RightPanel.setBackground(BackgroundColor);
        RightPanel.setLayout(new BoxLayout(RightPanel, BoxLayout.Y_AXIS));
        RightPanel.setBorder(new EmptyBorder(20, 10, 0, 0)); // Top 20, Left 10

        // Left Panel Components
        MyButton subject = new MyButton("Subjects");
        MyButton calendar = new MyButton("Calendar");

        Dimension leftBtnSize = new Dimension(LeftPanelWidth / 2, 40);
        subject.setMinimumSize(leftBtnSize);
        subject.setMaximumSize(leftBtnSize);
        subject.setPreferredSize(leftBtnSize);
        subject.setAlignmentX(Component.RIGHT_ALIGNMENT);

        calendar.setMinimumSize(leftBtnSize);
        calendar.setMaximumSize(leftBtnSize);
        calendar.setPreferredSize(leftBtnSize);
        calendar.setAlignmentX(Component.RIGHT_ALIGNMENT);

        subject.addActionListener(e -> cardLayout.show(MidPanel, "SUBJECTS"));
        calendar.addActionListener(e -> cardLayout.show(MidPanel, "CALENDAR"));

        LeftPanel.add(subject);
        LeftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        LeftPanel.add(calendar);

        // Mid Panel Components

        // Right Panel Components
        MyButton AddSubject = new MyButton("+ Add Subject");

        Dimension rightBtnSize = new Dimension((int) (RightPanelWidth * 0.6), 40);
        AddSubject.setMinimumSize(rightBtnSize);
        AddSubject.setMaximumSize(rightBtnSize);
        AddSubject.setPreferredSize(rightBtnSize);
        AddSubject.setAlignmentX(Component.LEFT_ALIGNMENT);

        AddSubject.addActionListener(e -> {
            AddSubjectDialog dialog = new AddSubjectDialog(frame);
            dialog.setVisible(true);
            Subject created = dialog.getCreatedSubject();
            if (created != null) {
                dummySubjects.add(created);
                subjectsView.updateView();
            }
        });
        
        RightPanel.add(AddSubject);

        // Header.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        // LeftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // MidPanel.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        // RightPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        // Add panels to the frame
        frame.add(Header, BorderLayout.NORTH);
        frame.add(LeftPanel, BorderLayout.WEST);
        frame.add(MidPanel, BorderLayout.CENTER);
        frame.add(RightPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }
}
