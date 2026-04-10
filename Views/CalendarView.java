package Views;

import Models.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarView extends JPanel {
    private YearMonth currentYearMonth;
    private JPanel calendarGrid;
    private JLabel monthYearLabel;
    private JPanel taskDisplayContent;
    private JLabel taskDateLabel;
    private List<Subject> dummySubjects;
    private JPanel rightCalendarPanel;
    private LocalDate selectedDate;

    // Custom rounded panel for white containers
    class RoundedPanel extends JPanel {
        private int radius;
        private Color borderColor;
        
        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
            setBackground(bgColor);
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }
            g2.dispose();
        }
    }

    public CalendarView(List<Subject> dummySubjects, JPanel rightCalendarPanel, JFrame frame) {
        this.dummySubjects = dummySubjects;
        this.rightCalendarPanel = rightCalendarPanel;
        this.currentYearMonth = YearMonth.now();
        this.selectedDate = LocalDate.now();

        setLayout(new BorderLayout());
        setBackground(Color.decode("#e9e9e9"));

        // ===== Main Center View (Calendar) =====
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.decode("#e9e9e9"));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 20, 25));

        JLabel title = new JLabel("Calendar");
        title.setFont(new Font("Raleway", Font.BOLD, 32));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Track your pending tasks by deadline");
        subtitle.setFont(new Font("Raleway", Font.PLAIN, 16));
        subtitle.setForeground(Color.decode("#666666"));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(subtitle);
        add(headerPanel, BorderLayout.NORTH);

        // Calendar Container (White Box)
        RoundedPanel calendarContainer = new RoundedPanel(20, Color.WHITE, Color.decode("#d9d9d9"));
        calendarContainer.setLayout(new BorderLayout());
        calendarContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Navigation of Calendar Box
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        monthYearLabel = new JLabel("", SwingConstants.LEFT);
        monthYearLabel.setFont(new Font("Raleway", Font.BOLD, 18));
        
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        navButtons.setOpaque(false);
        
        JButton prevBtn = createNavButton("<");
        prevBtn.addActionListener(e -> changeMonth(-1));
        JButton nextBtn = createNavButton(">");
        nextBtn.addActionListener(e -> changeMonth(1));
        
        navButtons.add(prevBtn);
        navButtons.add(nextBtn);

        navPanel.add(monthYearLabel, BorderLayout.WEST);
        navPanel.add(navButtons, BorderLayout.EAST);
        calendarContainer.add(navPanel, BorderLayout.NORTH);

        // Grid
        calendarGrid = new JPanel(new GridLayout(0, 7, 8, 8));
        calendarGrid.setOpaque(false);
        calendarContainer.add(calendarGrid, BorderLayout.CENTER);

        // Add calendar container to center with margin
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        centerWrapper.add(calendarContainer, BorderLayout.NORTH); // Keep at top
        add(centerWrapper, BorderLayout.CENTER);

        // ===== Right Panel Configuration =====
        
        rightCalendarPanel.removeAll();
        
        // Tasks Details Display (White Box)
        RoundedPanel taskRightBox = new RoundedPanel(20, Color.WHITE, Color.decode("#d9d9d9"));
        taskRightBox.setLayout(new BorderLayout());
        taskRightBox.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        taskDateLabel = new JLabel("Date", SwingConstants.LEFT);
        taskDateLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        taskDateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        taskRightBox.add(taskDateLabel, BorderLayout.NORTH);
        
        taskDisplayContent = new JPanel();
        taskDisplayContent.setLayout(new BoxLayout(taskDisplayContent, BoxLayout.Y_AXIS));
        taskDisplayContent.setOpaque(false);
        
        JScrollPane taskScroll = new JScrollPane(taskDisplayContent);
        taskScroll.setBorder(null);
        taskScroll.setOpaque(false);
        taskScroll.getViewport().setOpaque(false);
        taskRightBox.add(taskScroll, BorderLayout.CENTER);

        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.setBorder(BorderFactory.createEmptyBorder(70, 0, 25, 10)); // Top margin increased to align with calendar box
        rightWrapper.add(taskRightBox, BorderLayout.CENTER);

        rightCalendarPanel.add(rightWrapper);

        updateView();
    }

    public void updateView() {
        updateCalendar();
        showTasksForDate(selectedDate);
    }
    
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Raleway", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setBorder(BorderFactory.createLineBorder(Color.decode("#c9c9c9"), 1));
        return btn;
    }

    private void changeMonth(int offset) {
        currentYearMonth = currentYearMonth.plusMonths(offset);
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.removeAll();
        monthYearLabel.setText(currentYearMonth.getMonth().name().substring(0, 1).toUpperCase() + 
                               currentYearMonth.getMonth().name().substring(1).toLowerCase() + 
                               " " + currentYearMonth.getYear());

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Raleway", Font.PLAIN, 14));
            dayLabel.setForeground(Color.DARK_GRAY);
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
            final LocalDate date = currentYearMonth.atDay(i);
            
            // Build text with task indicators
            int[] typeCounts = new int[TaskType.values().length];
            
            for (Subject sub : dummySubjects) {
                for (Task t : sub.getAllTasks()) {
                    if (t instanceof PendingTask) {
                        PendingTask pt = (PendingTask) t;
                        if (pt.getDeadline() != null && pt.getDeadline().isEqual(date)) {
                            typeCounts[pt.getType().ordinal()]++;
                        }
                    }
                }
            }
            
            StringBuilder indicator = new StringBuilder();
            for (TaskType type : TaskType.values()) {
                int count = typeCounts[type.ordinal()];
                if (count > 0) {
                    indicator.append(count).append(type.name().substring(0, 1)).append(" ");
                }
            }
            
            String htmlText = "<html><center><div style='font-size:12px; padding-top:2px;'>" + i + "</div>";
            if (indicator.length() > 0) {
                htmlText += "<div style='font-size:7px; color:#888888; margin-top:2px;'>" + indicator.toString().trim().replace(" ", ", ") + "</div>";
            }
            htmlText += "</center></html>";

            JButton dayBtn = new JButton(htmlText) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (date.isEqual(selectedDate)) {
                        g2.setColor(Color.decode("#f0f0ff"));
                        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                        g2.setColor(Color.decode("#6b4ce6"));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    } else {
                        g2.setColor(Color.WHITE);
                        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                        g2.setColor(Color.decode("#e0e0e0"));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    }
                    
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            
            dayBtn.setContentAreaFilled(false);
            dayBtn.setFocusPainted(false);
            dayBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            dayBtn.setPreferredSize(new Dimension(50, 50));
            dayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            dayBtn.addActionListener(e -> {
                selectedDate = date;
                showTasksForDate(date);
                updateCalendar(); // refresh borders
            });
            calendarGrid.add(dayBtn);
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    private void showTasksForDate(LocalDate date) {
        taskDisplayContent.removeAll();
        taskDateLabel.setText(date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));

        boolean found = false;
        
        for (Subject sub : dummySubjects) {
            for (Task t : sub.getAllTasks()) {
                if (t instanceof PendingTask) {
                    PendingTask pt = (PendingTask) t;
                    if (pt.getDeadline() != null && pt.getDeadline().isEqual(date)) {
                        found = true;
                        
                        RoundedPanel taskPanel = new RoundedPanel(10, Color.decode("#fafafa"), Color.decode("#e0e0e0"));
                        taskPanel.setLayout(new BorderLayout());
                        taskPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                        taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                        
                        // Top segment: Title and Tag
                        JPanel topTask = new JPanel(new BorderLayout());
                        topTask.setOpaque(false);
                        
                        JLabel nameLbl = new JLabel(pt.getName());
                        nameLbl.setFont(new Font("Raleway", Font.BOLD, 14));
                        
                        // Tag
                        JLabel tagLbl = new JLabel(pt.getType().toString());
                        tagLbl.setFont(new Font("Raleway", Font.PLAIN, 10));
                        tagLbl.setOpaque(true);
                        tagLbl.setBackground(Color.decode("#e9e9e9"));
                        tagLbl.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
                        
                        topTask.add(nameLbl, BorderLayout.WEST);
                        topTask.add(tagLbl, BorderLayout.EAST);
                        
                        // Body segment
                        JPanel bodyTask = new JPanel();
                        bodyTask.setLayout(new BoxLayout(bodyTask, BoxLayout.Y_AXIS));
                        bodyTask.setOpaque(false);
                        bodyTask.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
                        
                        JLabel dueLbl = new JLabel("Due: " + pt.getDeadline().format(DateTimeFormatter.ofPattern("M/d/yyyy")));
                        dueLbl.setFont(new Font("Raleway", Font.PLAIN, 12));
                        dueLbl.setForeground(Color.DARK_GRAY);
                        
                        JLabel subjectLbl = new JLabel("<html><div style='width:150px;'>Subject: " + sub.getName() + "</div></html>");
                        subjectLbl.setFont(new Font("Raleway", Font.BOLD, 12));
                        subjectLbl.setForeground(Color.decode("#555555"));

                        JLabel descLbl = new JLabel(pt.getDescription());
                        descLbl.setFont(new Font("Raleway", Font.PLAIN, 12));
                        descLbl.setForeground(Color.BLACK);
                        
                        bodyTask.add(subjectLbl);
                        bodyTask.add(Box.createRigidArea(new Dimension(0, 3)));
                        bodyTask.add(dueLbl);
                        bodyTask.add(Box.createRigidArea(new Dimension(0, 5)));
                        bodyTask.add(descLbl);
                        
                        taskPanel.add(topTask, BorderLayout.NORTH);
                        taskPanel.add(bodyTask, BorderLayout.CENTER);
                        
                        taskDisplayContent.add(taskPanel);
                        taskDisplayContent.add(Box.createRigidArea(new Dimension(0, 15)));
                    }
                }
            }
        }

        if (!found) {
            JLabel empty = new JLabel("No tasks due on this date.");
            empty.setFont(new Font("Raleway", Font.ITALIC, 14));
            empty.setForeground(Color.GRAY);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskDisplayContent.add(empty);
        }

        taskDisplayContent.revalidate();
        taskDisplayContent.repaint();
        rightCalendarPanel.revalidate();
        rightCalendarPanel.repaint();
    }
}
