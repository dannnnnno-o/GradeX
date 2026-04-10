package Components;

import Models.Subject;
import javax.swing.*;
import java.awt.*;

public class SubjectCard extends JPanel {
    private Subject subject;
    private JButton viewDetailsBtn;
    private JButton removeBtn;
    private JButton editBtn;

    public SubjectCard(Subject subject) {
        this.subject = subject;
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(300, 350));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // 1. Top Panel: Subject Name & Remove Button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Wrap title text so long titles break lines gracefully
        JLabel nameLabel = new JLabel("<html><div style='width: 140px;'>" + subject.getName() + "</div></html>");
        nameLabel.setFont(new Font("Raleway", Font.BOLD, 18));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setVerticalAlignment(SwingConstants.TOP);

        // Custom Pill shaped Remove Button
        removeBtn = new JButton("Remove") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#fcb6b9")); // Light pink background
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        removeBtn.setFocusPainted(false);
        removeBtn.setContentAreaFilled(false);
        removeBtn.setBorderPainted(false);
        removeBtn.setForeground(Color.decode("#d9534f")); // Darker red-pink text
        removeBtn.setFont(new Font("Raleway", Font.PLAIN, 12));
        removeBtn.setMargin(new Insets(0, 0, 0, 0));
        removeBtn.setPreferredSize(new Dimension(70, 25));

        // Custom Pill shaped Edit Button
        editBtn = new JButton("Edit") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#efffbd")); // Light yellow
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        editBtn.setFocusPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setBorderPainted(false);
        editBtn.setForeground(Color.decode("#b8af51")); // Darker yellow text
        editBtn.setFont(new Font("Raleway", Font.PLAIN, 12));
        editBtn.setMargin(new Insets(0, 0, 0, 0));
        editBtn.setPreferredSize(new Dimension(70, 25));

        // Column for action buttons on the right
        JPanel actionColumn = new JPanel();
        actionColumn.setLayout(new BoxLayout(actionColumn, BoxLayout.Y_AXIS));
        actionColumn.setOpaque(false);
        
        JPanel removeRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        removeRow.setOpaque(false);
        removeRow.add(removeBtn);
        
        JPanel editRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        editRow.setOpaque(false);
        editRow.add(editBtn);

        actionColumn.add(removeRow);
        actionColumn.add(Box.createRigidArea(new Dimension(0, 5)));
        actionColumn.add(editRow);

        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(actionColumn, BorderLayout.EAST);
        content.add(topPanel);

        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. Grade
        JLabel gradeLabel = new JLabel("Grade: " + subject.calculateProjectedGrade());
        gradeLabel.setFont(new Font("Raleway", Font.PLAIN, 13));
        gradeLabel.setForeground(Color.DARK_GRAY);
        gradeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(gradeLabel);

        content.add(Box.createRigidArea(new Dimension(0, 15)));

        // 3. Rubrics Status
        boolean isComplete = subject.isRubricComplete();
        JLabel statusLbl = new JLabel(isComplete ? "Complete rubrics" : "Incomplete rubrics");
        statusLbl.setFont(new Font("Raleway", Font.PLAIN, 13));
        statusLbl.setForeground(isComplete ? Color.decode("#27ae60") : Color.decode("#e87373"));
        statusLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subStatusLbl = new JLabel(isComplete ? "Calculation active" : "Grade calculation paused");
        subStatusLbl.setFont(new Font("Raleway", Font.PLAIN, 11));
        subStatusLbl.setForeground(Color.GRAY);
        subStatusLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(statusLbl);
        content.add(Box.createRigidArea(new Dimension(0, 2)));
        content.add(subStatusLbl);

        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // 4. Tasks Counts
        JLabel tasksLbl = new JLabel("Tasks");
        tasksLbl.setFont(new Font("Raleway", Font.PLAIN, 13));
        tasksLbl.setForeground(Color.GRAY);
        tasksLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel countsLbl = new JLabel(
                subject.getCompletedTasksCount() + " completed, " + subject.getPendingTasksCount() + " pending");
        countsLbl.setFont(new Font("Raleway", Font.PLAIN, 14));
        countsLbl.setForeground(Color.BLACK);
        countsLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(tasksLbl);
        content.add(Box.createRigidArea(new Dimension(0, 2)));
        content.add(countsLbl);

        content.add(Box.createVerticalGlue());

        // 5. View Details Button
        viewDetailsBtn = new JButton("View Details") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.setColor(Color.decode("#dcdcdc"));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        viewDetailsBtn.setFocusPainted(false);
        viewDetailsBtn.setContentAreaFilled(false);
        viewDetailsBtn.setBorderPainted(false);
        viewDetailsBtn.setFont(new Font("Raleway", Font.BOLD, 14));
        viewDetailsBtn.setForeground(Color.BLACK);
        viewDetailsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewDetailsBtn.setMaximumSize(new Dimension(200, 35));
        viewDetailsBtn.setPreferredSize(new Dimension(150, 35));

        // Center button wrapper
        JPanel btnWrapper = new JPanel();
        btnWrapper.setOpaque(false);
        btnWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.add(viewDetailsBtn);
        btnWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(btnWrapper);

        add(content, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        g2.setColor(Color.decode("#d9d9d9"));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        g2.dispose();
    }

    public JButton getViewDetailsBtn() {
        return viewDetailsBtn;
    }

    public JButton getRemoveBtn() {
        return removeBtn;
    }

    public JButton getEditBtn() {
        return editBtn;
    }

    public Subject getSubject() {
        return subject;
    }
}
