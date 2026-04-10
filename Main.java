import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import Components.*;
import Models.*;
import Views.*;

public class Main {
    private static JFrame frame;
    private static JPanel LeftPanel, MidPanel, RightPanel;
    private static CardLayout midCardLayout, rightCardLayout;
    private static DB db;
    private static User currentUser;

    public static void main(String[] args) {
        db = new DB();

        // Window Properties
        int WindowWidth = 1366;
        int WindowHeight = 720;
        String BackgroundColorHex = "#e9e9e9";
        Color BackgroundColor = Color.decode(BackgroundColorHex);
        String HeaderBGHex = "#f4f4f4";
        Color HeaderBG = Color.decode(HeaderBGHex);

        // Frame Properties
        frame = new JFrame("GradeX");
        frame.setSize(WindowWidth, WindowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        // Panels Setup
        LeftPanel = new JPanel();
        LeftPanel.setPreferredSize(new Dimension(WindowWidth / 4, WindowHeight));
        LeftPanel.setBackground(BackgroundColor);
        LeftPanel.setLayout(new BoxLayout(LeftPanel, BoxLayout.Y_AXIS));
        LeftPanel.setBorder(new EmptyBorder(20, 0, 0, 10));

        MidPanel = new JPanel();
        MidPanel.setBackground(BackgroundColor);
        midCardLayout = new CardLayout();
        MidPanel.setLayout(midCardLayout);

        RightPanel = new JPanel();
        RightPanel.setPreferredSize(new Dimension(WindowWidth / 4, WindowHeight));
        RightPanel.setBackground(BackgroundColor);
        rightCardLayout = new CardLayout();
        RightPanel.setLayout(rightCardLayout);

        frame.add(Header, BorderLayout.NORTH);
        frame.add(LeftPanel, BorderLayout.WEST);
        frame.add(MidPanel, BorderLayout.CENTER);
        frame.add(RightPanel, BorderLayout.EAST);

        showAuthView();

        frame.setVisible(true);
    }

    private static void showAuthView() {
        LeftPanel.removeAll();
        MidPanel.removeAll();
        RightPanel.removeAll();

        MyButton loginTab = new MyButton("Login");
        MyButton signupTab = new MyButton("Signup");

        int LeftPanelWidth = frame.getWidth() / 4;
        Dimension leftBtnSize = new Dimension(LeftPanelWidth / 2, 40);
        loginTab.setMinimumSize(leftBtnSize);
        loginTab.setMaximumSize(leftBtnSize);
        loginTab.setPreferredSize(leftBtnSize);
        loginTab.setAlignmentX(Component.RIGHT_ALIGNMENT);
        loginTab.setActive(true);

        signupTab.setMinimumSize(leftBtnSize);
        signupTab.setMaximumSize(leftBtnSize);
        signupTab.setPreferredSize(leftBtnSize);
        signupTab.setAlignmentX(Component.RIGHT_ALIGNMENT);
        signupTab.setActive(false);

        LeftPanel.add(loginTab);
        LeftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        LeftPanel.add(signupTab);

        LoginView loginView = new LoginView((user, pass) -> {
            User authenticated = db.authenticate(user, pass);
            if (authenticated != null) {
                currentUser = authenticated;
                showDashboardView();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        SignupView signupView = new SignupView((user, pass) -> {
            if (db.register(user, pass)) {
                JOptionPane.showMessageDialog(frame, "Account created successfully! Please login.", "Signup Success",
                        JOptionPane.INFORMATION_MESSAGE);
                midCardLayout.show(MidPanel, "LOGIN");
                loginTab.setActive(true);
                signupTab.setActive(false);
            } else {
                JOptionPane.showMessageDialog(frame, "Username already exists or database error", "Signup Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        WelcomeView welcomeView = new WelcomeView();

        MidPanel.add(loginView, "LOGIN");
        MidPanel.add(signupView, "SIGNUP");
        RightPanel.add(welcomeView, "WELCOME");

        loginTab.addActionListener(e -> {
            midCardLayout.show(MidPanel, "LOGIN");
            loginTab.setActive(true);
            signupTab.setActive(false);
        });

        signupTab.addActionListener(e -> {
            midCardLayout.show(MidPanel, "SIGNUP");
            signupTab.setActive(true);
            loginTab.setActive(false);
        });

        LeftPanel.revalidate();
        LeftPanel.repaint();
        MidPanel.revalidate();
        MidPanel.repaint();
        RightPanel.revalidate();
        RightPanel.repaint();
    }

    private static void showDashboardView() {
        LeftPanel.removeAll();
        MidPanel.removeAll();
        RightPanel.removeAll();

        // Dashboard Navigation
        MyButton subjectBtn = new MyButton("Subjects");
        MyButton calendarBtn = new MyButton("Calendar");
        MyButton logoutBtn = new MyButton("Logout");

        int LeftPanelWidth = frame.getWidth() / 4;
        Dimension leftBtnSize = new Dimension(LeftPanelWidth / 2, 40);

        subjectBtn.setMinimumSize(leftBtnSize);
        subjectBtn.setMaximumSize(leftBtnSize);
        subjectBtn.setPreferredSize(leftBtnSize);
        subjectBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        subjectBtn.setActive(true);

        calendarBtn.setMinimumSize(leftBtnSize);
        calendarBtn.setMaximumSize(leftBtnSize);
        calendarBtn.setPreferredSize(leftBtnSize);
        calendarBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        calendarBtn.setActive(false);

        logoutBtn.setMinimumSize(leftBtnSize);
        logoutBtn.setMaximumSize(leftBtnSize);
        logoutBtn.setPreferredSize(leftBtnSize);
        logoutBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        logoutBtn.setActive(false);

        LeftPanel.add(subjectBtn);
        LeftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        LeftPanel.add(calendarBtn);
        LeftPanel.add(Box.createVerticalGlue());
        LeftPanel.add(logoutBtn);
        LeftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Mid Panel Views
        SubjectDetailsView subjectDetailsView = new SubjectDetailsView();

        // Right Panel - Default View
        JPanel RightDefaultPanel = new JPanel();
        RightDefaultPanel.setLayout(new BoxLayout(RightDefaultPanel, BoxLayout.Y_AXIS));
        RightDefaultPanel.setBackground(Color.decode("#e9e9e9"));
        RightDefaultPanel.setBorder(new EmptyBorder(20, 10, 0, 0));

        // Right Panel - Subject Details View
        JPanel RightSubjectPanel = new JPanel();
        RightSubjectPanel.setLayout(new BoxLayout(RightSubjectPanel, BoxLayout.Y_AXIS));
        RightSubjectPanel.setBackground(Color.decode("#e9e9e9"));
        RightSubjectPanel.setBorder(new EmptyBorder(20, 10, 0, 0));

        // Right Panel - Calendar View
        JPanel RightCalendarPanel = new JPanel();
        RightCalendarPanel.setLayout(new BoxLayout(RightCalendarPanel, BoxLayout.Y_AXIS));
        RightCalendarPanel.setBackground(Color.decode("#e9e9e9"));
        RightCalendarPanel.setBorder(new EmptyBorder(20, 10, 0, 0));
        CalendarView calendarView = new CalendarView(currentUser.getSubjects(), RightCalendarPanel, frame);

        final SubjectsView[] subjectsViewRef = new SubjectsView[1];

        int RightPanelWidth = frame.getWidth() / 4;
        Dimension rightBtnSize = new Dimension((int) (RightPanelWidth * 0.6), 40);

        NotificationView notificationView = new NotificationView(currentUser.getSubjects());

        MyButton AddSubject = new MyButton("+ Add Subject");
        AddSubject.setActive(true);
        AddSubject.setMinimumSize(rightBtnSize);
        AddSubject.setMaximumSize(rightBtnSize);
        AddSubject.setPreferredSize(rightBtnSize);
        AddSubject.setAlignmentX(Component.LEFT_ALIGNMENT);
        AddSubject.addActionListener(e -> {
            AddSubjectDialog dialog = new AddSubjectDialog(frame);
            dialog.setVisible(true);
            Subject created = dialog.getCreatedSubject();
            if (created != null) {
                currentUser.addSubject(created);
                db.saveUser(currentUser);
                notificationView.updateView();
                if (subjectsViewRef[0] != null) subjectsViewRef[0].updateView();
            }
        });
        RightDefaultPanel.add(AddSubject);
        RightDefaultPanel.add(Box.createVerticalStrut(20));
        notificationView.setAlignmentX(Component.LEFT_ALIGNMENT);
        RightDefaultPanel.add(notificationView);

        MyButton AddTask = new MyButton("+ Add Task");
        AddTask.setActive(true);
        AddTask.setMinimumSize(rightBtnSize);
        AddTask.setMaximumSize(rightBtnSize);
        AddTask.setPreferredSize(rightBtnSize);
        AddTask.setAlignmentX(Component.LEFT_ALIGNMENT);
        AddTask.addActionListener(e -> {
            Subject currentSubject = subjectDetailsView.getSubject();
            if (currentSubject != null) {
                AddTaskDialog dialog = new AddTaskDialog(frame, currentSubject);
                dialog.setVisible(true);
                Task created = dialog.getCreatedTask();
                if (created != null) {
                    currentSubject.addTask(created);
                    db.saveUser(currentUser);
                    subjectDetailsView.updateView();
                    notificationView.updateView();
                    if (subjectsViewRef[0] != null) subjectsViewRef[0].updateView();
                }
            }
        });
        RightSubjectPanel.add(AddTask);

        subjectsViewRef[0] = new SubjectsView(currentUser.getSubjects(), (sub) -> {
            subjectDetailsView.setSubject(sub);
            midCardLayout.show(MidPanel, "DETAILS");
            rightCardLayout.show(RightPanel, "SUBJECT");
            subjectBtn.setActive(false); // Remove highlight when inside details
        }, () -> {
            db.saveUser(currentUser);
            if (subjectsViewRef[0] != null) subjectsViewRef[0].updateView();
            calendarView.updateView();
            notificationView.updateView();
            frame.repaint();
        });

        subjectDetailsView.setOnDataChanged(() -> {
            db.saveUser(currentUser);
            if (subjectsViewRef[0] != null) subjectsViewRef[0].updateView();
            calendarView.updateView();
            notificationView.updateView();
            frame.repaint();
        });

        subjectDetailsView.setOnBack(() -> {
            midCardLayout.show(MidPanel, "SUBJECTS");
            rightCardLayout.show(RightPanel, "DEFAULT");
            subjectBtn.setActive(true);
            calendarBtn.setActive(false);
            notificationView.updateView();
            if (subjectsViewRef[0] != null) subjectsViewRef[0].updateView();
        });

        SubjectsView subjectsView = subjectsViewRef[0];

        MidPanel.add(subjectsView, "SUBJECTS");
        MidPanel.add(calendarView, "CALENDAR");
        MidPanel.add(subjectDetailsView, "DETAILS");

        RightPanel.add(RightDefaultPanel, "DEFAULT");
        RightPanel.add(RightSubjectPanel, "SUBJECT");
        RightPanel.add(RightCalendarPanel, "CALENDAR");

        subjectBtn.addActionListener(e -> {
            midCardLayout.show(MidPanel, "SUBJECTS");
            rightCardLayout.show(RightPanel, "DEFAULT");
            subjectBtn.setActive(true);
            calendarBtn.setActive(false);
            if (subjectsViewRef[0] != null) subjectsViewRef[0].updateView();
        });

        calendarBtn.addActionListener(e -> {
            midCardLayout.show(MidPanel, "CALENDAR");
            rightCardLayout.show(RightPanel, "CALENDAR");
            calendarBtn.setActive(true);
            subjectBtn.setActive(false);
            calendarView.updateView();
        });

        logoutBtn.addActionListener(e -> {
            currentUser = null;
            showAuthView();
        });

        LeftPanel.revalidate();
        LeftPanel.repaint();
        MidPanel.revalidate();
        MidPanel.repaint();
        RightPanel.revalidate();
        RightPanel.repaint();

        midCardLayout.show(MidPanel, "SUBJECTS");
        rightCardLayout.show(RightPanel, "DEFAULT");
    }
}
