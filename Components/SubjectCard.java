package Components;

import Models.Subject;
import javax.swing.*;
import java.awt.*;

public class SubjectCard extends JPanel {
    private Subject subject;
    private JButton viewDetailsBtn;
    private JButton removeBtn;

    public SubjectCard(Subject subject) {
        this.subject = subject;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        setPreferredSize(new Dimension(300, 150));
        setMaximumSize(new Dimension(300, 150));

        // Top Panel: Name (Left), Remove (Right)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel nameLabel = new JLabel(subject.getName());
        nameLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        
        removeBtn = new JButton("X");
        removeBtn.setFocusPainted(false);
        removeBtn.setBackground(Color.decode("#ff6b6b"));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setMargin(new Insets(2, 5, 2, 5));
        
        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(removeBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Grade 
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel gradeLabel = new JLabel("Grade: " + String.format("%.2f", subject.getGrade()));
        gradeLabel.setFont(new Font("Raleway", Font.PLAIN, 14));
        centerPanel.add(gradeLabel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel: Counts and View Details Button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        JLabel countsLabel = new JLabel(subject.getCompletedTasksCount() + " completed   " + subject.getPendingTasksCount() + " pending");
        countsLabel.setFont(new Font("Raleway", Font.PLAIN, 12));
        countsLabel.setForeground(Color.DARK_GRAY);

        viewDetailsBtn = new JButton("View Details");
        viewDetailsBtn.setFocusPainted(false);
        viewDetailsBtn.setBackground(Color.decode("#c9c9c9"));

        bottomPanel.add(countsLabel, BorderLayout.WEST);
        bottomPanel.add(viewDetailsBtn, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public JButton getViewDetailsBtn() { return viewDetailsBtn; }
    public JButton getRemoveBtn() { return removeBtn; }
    public Subject getSubject() { return subject; }
}
