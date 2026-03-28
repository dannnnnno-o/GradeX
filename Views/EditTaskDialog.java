package Views;

import Models.*;
import Components.MyButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class EditTaskDialog extends JDialog {
    private PendingTask pendingTask;
    private JComboBox<TaskType> typeDropdown;
    private JTextField nameField;
    private JTextField descField;
    
    // Pending fields
    private JTextField pendingMaxScoreField;
    private JTextField pendingDeadlineField;

    public EditTaskDialog(JFrame parent, PendingTask task) {
        super(parent, "Edit Task", true);
        this.pendingTask = task;
        
        setSize(400, 450);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Edit Task");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Task Status (Disabled, since we can only edit Pending and use Complete button for Done)
        mainPanel.add(new JLabel("Task Status"));
        JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Pending"});
        statusDropdown.setEnabled(false);
        statusDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        statusDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(statusDropdown);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Task Type
        mainPanel.add(new JLabel("Task Type"));
        typeDropdown = new JComboBox<>(TaskType.values());
        typeDropdown.setSelectedItem(task.getType());
        typeDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        typeDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(typeDropdown);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Task Name
        mainPanel.add(new JLabel("Task Name"));
        nameField = new JTextField(task.getName());
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameField);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Description
        mainPanel.add(new JLabel("Description"));
        descField = new JTextField(task.getDescription() != null ? task.getDescription() : "");
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        descField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(descField);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Dynamic Panel (Only Pending needed)
        JPanel pendingPanel = new JPanel();
        pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.Y_AXIS));
        pendingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pendingPanel.add(new JLabel("Max Score (1-100)"));
        pendingMaxScoreField = new JTextField(String.valueOf(task.getMaxScore()));
        pendingMaxScoreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pendingPanel.add(pendingMaxScoreField);
        pendingPanel.add(Box.createVerticalStrut(10));
        
        pendingPanel.add(new JLabel("Deadline (YYYY-MM-DD)"));
        pendingDeadlineField = new JTextField(task.getDeadline() != null ? task.getDeadline().toString() : LocalDate.now().plusDays(1).toString());
        pendingDeadlineField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pendingPanel.add(pendingDeadlineField);
        
        mainPanel.add(pendingPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Action Button
        MyButton saveButton = new MyButton("Save Changes");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String desc = descField.getText().trim();
                TaskType type = (TaskType) typeDropdown.getSelectedItem();
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Task Name cannot be empty.");
                    return;
                }
                
                double maxScore = Double.parseDouble(pendingMaxScoreField.getText().trim());
                if (maxScore < 1 || maxScore > 100) {
                    JOptionPane.showMessageDialog(this, "Max score must be between 1 and 100.");
                    return;
                }
                
                LocalDate deadline = LocalDate.parse(pendingDeadlineField.getText().trim());
                
                // Mutate the original task
                pendingTask.setName(name);
                pendingTask.setDescription(desc);
                pendingTask.setType(type);
                pendingTask.setMaxScore(maxScore);
                pendingTask.setDeadline(deadline);
                
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });
        
        mainPanel.add(Box.createVerticalGlue()); // Push button to bottom
        mainPanel.add(saveButton);
        
        add(mainPanel);
    }
}
