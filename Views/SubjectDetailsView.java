package Views;

import Models.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SubjectDetailsView extends JPanel {
    private JLabel titleLabel;
    private JPanel cardsPanel;
    private JPanel tasksListPanel;
    private Subject currentSubject;
    
    public SubjectDetailsView() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#e9e9e9"));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#e9e9e9"));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Subject Name Title
        titleLabel = new JLabel("Select a subject");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        mainPanel.add(Box.createVerticalStrut(25));
        
        // Task Type Cards Container
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        cardsPanel.setBackground(Color.decode("#e9e9e9"));
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(cardsPanel);
        
        mainPanel.add(Box.createVerticalStrut(25));
        
        // Tasks List Label
        JLabel tasksHeader = new JLabel("Tasks");
        tasksHeader.setFont(new Font("Raleway", Font.BOLD, 20));
        tasksHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(tasksHeader);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Tasks List Container
        tasksListPanel = new JPanel();
        tasksListPanel.setLayout(new BoxLayout(tasksListPanel, BoxLayout.Y_AXIS));
        tasksListPanel.setBackground(Color.decode("#e9e9e9"));
        tasksListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(tasksListPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.decode("#e9e9e9"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        updateView();
    }
    
    public Subject getSubject() {
        return this.currentSubject;
    }

    public void updateView() {
        if(currentSubject == null) return;
        
        titleLabel.setText(currentSubject.getName());
        
        // Update Cards
        cardsPanel.removeAll();
        int[] rubrics = currentSubject.getRubrics();
        TaskType[] types = TaskType.values();
        for(int i = 0; i < types.length; i++) {
            if (rubrics[i] > 0) {
                JPanel card = createCard(types[i].name(), rubrics[i] + "%");
                cardsPanel.add(card);
            }
        }
        
        // Update Tasks List
        tasksListPanel.removeAll();
        for(Task task : currentSubject.getAllTasks()) {
            JPanel taskPanel = createTaskRow(task);
            tasksListPanel.add(taskPanel);
            tasksListPanel.add(Box.createVerticalStrut(10));
        }
        
        revalidate();
        repaint();
    }
    
    private JPanel createCard(String title, String weight) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Raleway", Font.BOLD, 14));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel weightLbl = new JLabel(weight);
        weightLbl.setFont(new Font("Raleway", Font.PLAIN, 18));
        weightLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(weightLbl);
        
        return card;
    }
    
    private JPanel createTaskRow(Task task) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        // Left side: Task name and description
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLbl = new JLabel(task.getName());
        nameLbl.setFont(new Font("Raleway", Font.BOLD, 16));
        
        JLabel descLbl = new JLabel(task.getType().name() + " | " + (task.getDescription() != null ? task.getDescription() : ""));
        descLbl.setFont(new Font("Raleway", Font.PLAIN, 12));
        descLbl.setForeground(Color.DARK_GRAY);
        
        infoPanel.add(nameLbl);
        infoPanel.add(descLbl);
        row.add(infoPanel, BorderLayout.WEST);
        
        // Right side: Score or Deadline
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(Color.WHITE);
        
        if (task instanceof PendingTask) {
            PendingTask pt = (PendingTask) task;
            JLabel statusLbl = new JLabel("Pending");
            statusLbl.setForeground(Color.decode("#e67e22")); // Orange
            statusLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
            
            JLabel deadlineLbl = new JLabel("Due: " + pt.getDeadline().toString());
            deadlineLbl.setFont(new Font("Raleway", Font.PLAIN, 12));
            deadlineLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
            
            statusPanel.add(statusLbl);
            statusPanel.add(deadlineLbl);
        } else if (task instanceof CompletedTask) {
            JLabel statusLbl = new JLabel("Completed");
            statusLbl.setForeground(Color.decode("#27ae60")); // Green
            statusLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
            
            JLabel scoreLbl = new JLabel("Score: " + task.getScore() + " / " + task.getMaxScore());
            scoreLbl.setFont(new Font("Raleway", Font.PLAIN, 12));
            scoreLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
            
            statusPanel.add(statusLbl);
            statusPanel.add(scoreLbl);
        }
        
        row.add(statusPanel, BorderLayout.EAST);
        
        return row;
    }
}
