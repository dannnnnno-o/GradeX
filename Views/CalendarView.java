package Views;

import Models.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarView extends JPanel {
    private YearMonth currentYearMonth;
    private JPanel calendarGrid;
    private JLabel monthYearLabel;
    private JPanel taskDisplayPanel;
    private List<Subject> dummySubjects;

    public CalendarView(List<Subject> dummySubjects) {
        this.dummySubjects = dummySubjects;
        this.currentYearMonth = YearMonth.now();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.decode("#e9e9e9")); // match BackgroundColor

        // North: Navigation
        JPanel navPanel = new JPanel();
        navPanel.setBackground(Color.decode("#e9e9e9"));
        JButton prevBtn = new JButton("< Prev");
        prevBtn.addActionListener(e -> changeMonth(-1));
        
        JButton nextBtn = new JButton("Next >");
        nextBtn.addActionListener(e -> changeMonth(1));

        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("Raleway", Font.BOLD, 18));
        
        navPanel.add(prevBtn);
        navPanel.add(monthYearLabel);
        navPanel.add(nextBtn);
        
        add(navPanel, BorderLayout.NORTH);

        // Center: Calendar Grid
        calendarGrid = new JPanel(new GridLayout(0, 7, 5, 5));
        calendarGrid.setBackground(Color.decode("#e9e9e9"));
        calendarGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(calendarGrid, BorderLayout.CENTER);

        // East: Tasks display
        taskDisplayPanel = new JPanel();
        taskDisplayPanel.setLayout(new BoxLayout(taskDisplayPanel, BoxLayout.Y_AXIS));
        taskDisplayPanel.setPreferredSize(new Dimension(250, 0));
        taskDisplayPanel.setBackground(Color.WHITE);
        taskDisplayPanel.setBorder(BorderFactory.createTitledBorder("Tasks for Selected Date"));
        add(taskDisplayPanel, BorderLayout.EAST);

        updateCalendar();
    }

    private void changeMonth(int offset) {
        currentYearMonth = currentYearMonth.plusMonths(offset);
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.removeAll();
        monthYearLabel.setText(currentYearMonth.getMonth().name() + " " + currentYearMonth.getYear());

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Raleway", Font.BOLD, 14));
            calendarGrid.add(dayLabel);
        }

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue(); 
        int offset = dayOfWeekValue == 7 ? 0 : dayOfWeekValue;

        for (int i = 0; i < offset; i++) {
            calendarGrid.add(new JLabel("")); // empty
        }

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate date = currentYearMonth.atDay(i);
            JButton dayBtn = new JButton(String.valueOf(i));
            dayBtn.setBackground(Color.WHITE);
            dayBtn.setFocusPainted(false);
            dayBtn.addActionListener(e -> showTasksForDate(date));
            calendarGrid.add(dayBtn);
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    private void showTasksForDate(LocalDate date) {
        taskDisplayPanel.removeAll();
        JLabel header = new JLabel("Tasks ending on or after " + date.toString());
        header.setFont(new Font("Raleway", Font.BOLD, 11));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        taskDisplayPanel.add(header);
        taskDisplayPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        boolean found = false;
        
        for (Subject sub : dummySubjects) {
            for (Task t : sub.getAllTasks()) {
                if (t instanceof PendingTask) {
                    PendingTask pt = (PendingTask) t;
                    if (pt.getDeadline() != null && !pt.getDeadline().isBefore(date)) {
                        found = true;
                        JPanel taskPanel = new JPanel(new BorderLayout());
                        taskPanel.setBackground(Color.decode("#f4f4f4"));
                        taskPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                        taskPanel.setMaximumSize(new Dimension(230, 50));
                        
                        JLabel nameLbl = new JLabel(" " + pt.getName() + " (" + sub.getName() + ")");
                        nameLbl.setFont(new Font("Raleway", Font.BOLD, 12));
                        JLabel descLbl = new JLabel(" Due: " + pt.getDeadline());
                        descLbl.setFont(new Font("Raleway", Font.PLAIN, 10));
                        
                        taskPanel.add(nameLbl, BorderLayout.NORTH);
                        taskPanel.add(descLbl, BorderLayout.SOUTH);
                        
                        taskDisplayPanel.add(taskPanel);
                        taskDisplayPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                    }
                }
            }
        }

        if (!found) {
            JLabel empty = new JLabel("No pending tasks");
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskDisplayPanel.add(empty);
        }

        taskDisplayPanel.revalidate();
        taskDisplayPanel.repaint();
    }
}
