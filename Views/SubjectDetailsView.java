package Views;

import Models.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SubjectDetailsView extends JPanel {
    private JLabel titleLabel;
    private JLabel statusLabel;
    private JPanel rubricsPanel;
    private JPanel tasksContainerPanel;
    private Subject currentSubject;
    private String currentFilter = "All Tasks";
    private boolean sortPendingByDeadline = false;
    private JPanel filterPanel;
    private PillButton sortToggleBtn;
    private Runnable onDataChanged;
    private Runnable onBack;

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

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

    class FilterButton extends JButton {
        private boolean active;

        public FilterButton(String text, boolean active) {
            super(text);
            this.active = active;
            setFont(new Font("Raleway", Font.PLAIN, 14));
            setForeground(Color.BLACK);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setMargin(new Insets(5, 15, 5, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (active) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    class PillButton extends JButton {
        private boolean active;
        private boolean isToggle;
        private Color bgColor, fgColor;

        // Constructor for toggles (sorting)
        public PillButton(String text, boolean active) {
            super(text);
            this.active = active;
            this.isToggle = true;
            setFont(new Font("Raleway", Font.BOLD, 13));
            setForeground(active ? Color.BLACK : Color.GRAY);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(8, 15, 8, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        // Constructor for fixed colors (Edit, Remove, Complete)
        public PillButton(String text, Color bg, Color fg) {
            super(text);
            this.isToggle = false;
            this.bgColor = bg;
            this.fgColor = fg;
            setFont(new Font("Raleway", Font.BOLD, 13));
            setForeground(fg);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(8, 15, 8, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void setActive(boolean active) {
            if (isToggle) {
                this.active = active;
                setForeground(active ? Color.BLACK : Color.GRAY);
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isToggle) {
                if (active) {
                    g2.setColor(Color.decode("#dfdfdf"));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                } else {
                    g2.setColor(Color.decode("#d9d9d9"));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                }
            } else {
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public SubjectDetailsView() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#e9e9e9"));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.decode("#e9e9e9"));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Back Button
        JButton backBtn = new JButton("\u2190 Back to Subjects");
        backBtn.setFont(new Font("Raleway", Font.BOLD, 14));
        backBtn.setForeground(Color.GRAY);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onBack != null)
                onBack.run();
        });

        mainContent.add(backBtn);
        mainContent.add(Box.createVerticalStrut(10));

        titleLabel = new JLabel("Select a subject");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(titleLabel);

        statusLabel = new JLabel("Incomplete rubrics - Grade calculation paused");
        statusLabel.setFont(new Font("Raleway", Font.PLAIN, 18));
        statusLabel.setForeground(Color.decode("#f08080"));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(Box.createVerticalStrut(5));
        mainContent.add(statusLabel);

        mainContent.add(Box.createVerticalStrut(30));

        rubricsPanel = new RoundedPanel(20, Color.decode("#fcfcfc"), Color.decode("#d9d9d9"));
        rubricsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 25));
        rubricsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rubricsPanel.setMaximumSize(new Dimension(800, 150));
        mainContent.add(rubricsPanel);

        mainContent.add(Box.createVerticalStrut(30));

        filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filterPanel.setOpaque(false);
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel filterBg = new RoundedPanel(20, Color.decode("#dfdfdf"), null);
        filterBg.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filterBg.setBorder(new EmptyBorder(5, 5, 5, 5));

        FilterButton allBtn = new FilterButton("All Tasks", true);
        FilterButton compBtn = new FilterButton("Completed", false);
        FilterButton pendBtn = new FilterButton("Pending", false);

        allBtn.addActionListener(e -> {
            setFilter("All Tasks", allBtn, compBtn, pendBtn);
        });
        compBtn.addActionListener(e -> {
            setFilter("Completed", compBtn, allBtn, pendBtn);
        });
        pendBtn.addActionListener(e -> {
            setFilter("Pending", pendBtn, allBtn, compBtn);
        });

        filterBg.add(allBtn);
        filterBg.add(compBtn);
        filterBg.add(pendBtn);
        filterPanel.add(filterBg);

        filterPanel.add(Box.createHorizontalStrut(20));

        sortToggleBtn = new PillButton("Sort by Deadline", false);
        sortToggleBtn.setVisible(false); // Only for pending
        sortToggleBtn.addActionListener(e -> {
            sortPendingByDeadline = !sortPendingByDeadline;
            sortToggleBtn.setActive(sortPendingByDeadline);
            updateView();
        });
        filterPanel.add(sortToggleBtn);

        mainContent.add(filterPanel);

        mainContent.add(Box.createVerticalStrut(25));

        tasksContainerPanel = new JPanel();
        tasksContainerPanel.setLayout(new BoxLayout(tasksContainerPanel, BoxLayout.Y_AXIS));
        tasksContainerPanel.setOpaque(false);
        tasksContainerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainContent.add(tasksContainerPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.decode("#e9e9e9"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void setFilter(String filter, FilterButton active, FilterButton... others) {
        currentFilter = filter;
        active.setActive(true);
        for (FilterButton b : others)
            b.setActive(false);

        // Only show sort for Pending
        sortToggleBtn.setVisible(filter.equals("Pending"));

        updateView();
    }

    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        updateView();
    }

    public Subject getSubject() {
        return this.currentSubject;
    }

    public void setOnDataChanged(Runnable onDataChanged) {
        this.onDataChanged = onDataChanged;
    }

    public void updateView() {
        if (currentSubject == null)
            return;

        titleLabel.setText(currentSubject.getName());

        rubricsPanel.removeAll();
        int[] rubrics = currentSubject.getRubrics();
        TaskType[] types = TaskType.values();
        for (int i = 0; i < types.length; i++) {
            JPanel card = createRubricCard(
                    types[i].name().substring(0, 1).toUpperCase() + types[i].name().substring(1).toLowerCase(),
                    rubrics[i] + "%");
            rubricsPanel.add(card);
        }

        tasksContainerPanel.removeAll();
        boolean hasAnyTasks = false;
        List<Task> allTasks = currentSubject.getAllTasks();

        for (int i = 0; i < types.length; i++) {
            TaskType currentType = types[i];
            List<Task> filteredTasks = new ArrayList<>();

            for (Task t : allTasks) {
                if (t.getType() == currentType) {
                    if (currentFilter.equals("All Tasks"))
                        filteredTasks.add(t);
                    else if (currentFilter.equals("Completed") && t instanceof CompletedTask)
                        filteredTasks.add(t);
                    else if (currentFilter.equals("Pending") && t instanceof PendingTask)
                        filteredTasks.add(t);
                }
            }

            if (currentFilter.equals("Pending") && sortPendingByDeadline) {
                // Stable sort by deadline, preserves original order for ties
                filteredTasks.sort((t1, t2) -> {
                    PendingTask p1 = (PendingTask) t1;
                    PendingTask p2 = (PendingTask) t2;
                    return p1.getDeadline().compareTo(p2.getDeadline());
                });
            }

            if (!filteredTasks.isEmpty()) {
                hasAnyTasks = true;
                JPanel categoryPanel = createCategorySection(
                        currentType.name().substring(0, 1).toUpperCase()
                                + currentType.name().substring(1).toLowerCase(),
                        filteredTasks);
                tasksContainerPanel.add(categoryPanel);
                tasksContainerPanel.add(Box.createVerticalStrut(25));
            }
        }

        if (!hasAnyTasks) {
            JLabel emptyMsg = new JLabel("No tasks found for this filter. Time to add some!");
            emptyMsg.setFont(new Font("Raleway", Font.PLAIN, 18));
            emptyMsg.setForeground(Color.GRAY);
            emptyMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
            emptyMsg.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            tasksContainerPanel.add(emptyMsg);
        } else {
            tasksContainerPanel.add(Box.createVerticalGlue()); // Push everything up
        }

        revalidate();
        repaint();
    }

    private JPanel createRubricCard(String title, String weight) {
        RoundedPanel card = new RoundedPanel(15, Color.decode("#fdfdfd"), Color.decode("#d9d9d9"));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Raleway", Font.PLAIN, 18));
        titleLbl.setForeground(Color.DARK_GRAY);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel weightLbl = new JLabel(weight);
        weightLbl.setFont(new Font("Raleway", Font.PLAIN, 18));
        weightLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(5));
        card.add(weightLbl);

        return card;
    }

    private JPanel createCategorySection(String title, List<Task> tasks) {
        RoundedPanel section = new RoundedPanel(15, Color.decode("#fcfcfc"), Color.decode("#d9d9d9"));
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setBorder(new EmptyBorder(25, 25, 25, 25));
        section.setMaximumSize(new Dimension(800, 10000)); // Large max height is fine if we have glue

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Raleway", Font.BOLD, 22));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(titleLbl);
        section.add(Box.createVerticalStrut(20));

        for (Task task : tasks) {
            section.add(createTaskRow(task));
            section.add(Box.createVerticalStrut(15));
        }

        return section;
    }

    private JPanel createTaskRow(Task task) {
        RoundedPanel row = new RoundedPanel(15, Color.WHITE, Color.decode("#d9d9d9"));
        row.setLayout(new BorderLayout());
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(new EmptyBorder(15, 20, 15, 20));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        row.setPreferredSize(new Dimension(600, 100)); // Default preferred height

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topRow.setOpaque(false);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLbl = new JLabel(task.getName());
        nameLbl.setFont(new Font("Raleway", Font.BOLD, 18));
        topRow.add(nameLbl);
        topRow.add(Box.createHorizontalStrut(10)); // Add gap between name and pill instead

        if (task instanceof CompletedTask) {
            JLabel pill = new JLabel("Completed");
            pill.setFont(new Font("Raleway", Font.PLAIN, 11));
            pill.setForeground(Color.decode("#27ae60"));
            pill.setOpaque(true);
            pill.setBackground(Color.decode("#a5eaaf"));
            pill.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            topRow.add(pill);
        } else {
            JLabel pill = new JLabel("Pending");
            pill.setFont(new Font("Raleway", Font.PLAIN, 11));
            pill.setForeground(Color.decode("#b8af51"));
            pill.setOpaque(true);
            pill.setBackground(Color.decode("#efffbd"));
            pill.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            topRow.add(pill);
        }

        leftPanel.add(topRow);
        leftPanel.add(Box.createVerticalStrut(5));

        JLabel descLbl = new JLabel(task.getDescription() != null ? task.getDescription() : "");
        descLbl.setFont(new Font("Raleway", Font.PLAIN, 13));
        descLbl.setForeground(Color.GRAY);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(descLbl);
        leftPanel.add(Box.createVerticalStrut(5));

        if (task instanceof CompletedTask) {
            JLabel scoreLbl = new JLabel("Grade: " + String.format("%.2f", task.getScore()));
            scoreLbl.setFont(new Font("Raleway", Font.PLAIN, 14));
            scoreLbl.setForeground(Color.DARK_GRAY);
            scoreLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(scoreLbl);
        } else {
            PendingTask pt = (PendingTask) task;
            JLabel deadlineLbl = new JLabel(
                    "Deadline: " + pt.getDeadline().format(DateTimeFormatter.ofPattern("M/d/yyyy")));
            deadlineLbl.setFont(new Font("Raleway", Font.PLAIN, 14));
            deadlineLbl.setForeground(Color.DARK_GRAY);
            deadlineLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(deadlineLbl);
        }

        row.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        if (task instanceof PendingTask) {
            PendingTask pTask = (PendingTask) task;
            PillButton editBtn = new PillButton("Edit", Color.decode("#efffbd"), Color.decode("#b8af51"));
            PillButton removeBtn = new PillButton("Remove", Color.decode("#fcb6b9"), Color.decode("#d9534f"));
            PillButton compBtn = new PillButton("Complete", Color.decode("#a5eaaf"), Color.decode("#27ae60"));

            editBtn.addActionListener(e -> {
                JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                EditTaskDialog dialog = new EditTaskDialog(parent, pTask);
                dialog.setVisible(true);
                updateView();
            });

            removeBtn.addActionListener(e -> {
                JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                RemoveTaskDialog dialog = new RemoveTaskDialog(parent, "Remove Task", task.getName());
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    currentSubject.removeTask(task);
                    if (onDataChanged != null)
                        onDataChanged.run();
                    updateView();
                }
            });

            compBtn.addActionListener(e -> {
                JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                CompleteTaskDialog dialog = new CompleteTaskDialog(parent, task);
                dialog.setVisible(true);
                Double score = dialog.getResultScore();
                if (score != null) {
                    currentSubject.removeTask(task);
                    CompletedTask cTask = new CompletedTask(task.getName(), score, task.getMaxScore(),
                            task.getType(), task.getDescription());
                    currentSubject.addTask(cTask);
                    if (onDataChanged != null)
                        onDataChanged.run();
                    updateView();
                }
            });

            rightPanel.add(editBtn);
            rightPanel.add(removeBtn);
            rightPanel.add(compBtn);
        } else {
            PillButton removeBtn = new PillButton("Remove", Color.decode("#fcb6b9"), Color.decode("#d9534f"));
            removeBtn.addActionListener(e -> {
                int resp = JOptionPane.showConfirmDialog(this, "Remove " + task.getName() + "?", "Remove Task",
                        JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    currentSubject.removeTask(task);
                    if (onDataChanged != null)
                        onDataChanged.run();
                    updateView();
                }
            });
            rightPanel.add(removeBtn);
        }

        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }
}
