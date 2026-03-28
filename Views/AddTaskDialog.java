package Views;

import Models.*;
import Components.MyButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class AddTaskDialog extends JDialog {
    private Task createdTask;
    private JComboBox<String> statusDropdown;
    private JComboBox<TaskType> typeDropdown;
    private JTextField nameField;
    private JTextField descField;
    
    private JPanel dynamicPanel;
    private CardLayout dynamicCardLayout;
    
    // Pending fields
    private JTextField pendingMaxScoreField;
    private JTextField pendingDeadlineField; // Format: YYYY-MM-DD
    
    // Done fields
    private JTextField doneScoreField;
    private JTextField doneMaxScoreField;

    public AddTaskDialog(JFrame parent, Subject subject) {
        super(parent, "Add New Task", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Add New Task");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Task Status
        mainPanel.add(new JLabel("Task Status"));
        statusDropdown = new JComboBox<>(new String[]{"Pending", "Done"});
        statusDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        statusDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(statusDropdown);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Task Type
        mainPanel.add(new JLabel("Task Type"));
        typeDropdown = new JComboBox<>(TaskType.values());
        typeDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        typeDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(typeDropdown);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Task Name
        mainPanel.add(new JLabel("Task Name"));
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameField);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Description
        mainPanel.add(new JLabel("Description"));
        descField = new JTextField();
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        descField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(descField);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Dynamic Panel for Pending vs Done
        dynamicCardLayout = new CardLayout();
        dynamicPanel = new JPanel(dynamicCardLayout);
        dynamicPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // --- Pending Panel ---
        JPanel pendingPanel = new JPanel();
        pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.Y_AXIS));
        pendingPanel.add(new JLabel("Max Score (1-100)"));
        pendingMaxScoreField = new JTextField();
        pendingMaxScoreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pendingPanel.add(pendingMaxScoreField);
        pendingPanel.add(Box.createVerticalStrut(10));
        pendingPanel.add(new JLabel("Deadline (YYYY-MM-DD)"));
        pendingDeadlineField = new JTextField(LocalDate.now().plusDays(1).toString());
        pendingDeadlineField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pendingPanel.add(pendingDeadlineField);
        
        // --- Done Panel ---
        JPanel donePanel = new JPanel(new GridLayout(2, 2, 10, 5));
        donePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        donePanel.add(new JLabel("Score"));
        donePanel.add(new JLabel("Max Score"));
        doneScoreField = new JTextField();
        doneMaxScoreField = new JTextField();
        donePanel.add(doneScoreField);
        donePanel.add(doneMaxScoreField);
        
        // Wrap donePanel to keep layout clean vertically
        JPanel doneWrapper = new JPanel();
        doneWrapper.setLayout(new BoxLayout(doneWrapper, BoxLayout.Y_AXIS));
        doneWrapper.add(donePanel);

        dynamicPanel.add(pendingPanel, "Pending");
        dynamicPanel.add(doneWrapper, "Done");
        
        mainPanel.add(dynamicPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Status dropdown listener to swap cards
        statusDropdown.addActionListener(e -> {
            String selected = (String) statusDropdown.getSelectedItem();
            dynamicCardLayout.show(dynamicPanel, selected);
        });
        
        // Action Button
        MyButton addButton = new MyButton("Add Task");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String desc = descField.getText().trim();
                TaskType type = (TaskType) typeDropdown.getSelectedItem();
                String status = (String) statusDropdown.getSelectedItem();
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Task Name cannot be empty.");
                    return;
                }
                
                if ("Pending".equals(status)) {
                    double maxScore = Double.parseDouble(pendingMaxScoreField.getText().trim());
                    if (maxScore < 1 || maxScore > 100) {
                        JOptionPane.showMessageDialog(this, "Max score must be between 1 and 100.");
                        return;
                    }
                    LocalDate deadline = LocalDate.parse(pendingDeadlineField.getText().trim());
                    createdTask = new PendingTask(name, maxScore, type, desc, deadline);
                } else {
                    double score = Double.parseDouble(doneScoreField.getText().trim());
                    double maxScore = Double.parseDouble(doneMaxScoreField.getText().trim());
                    if (maxScore < 1) {
                        JOptionPane.showMessageDialog(this, "Max score must be at least 1.");
                        return;
                    }
                    createdTask = new CompletedTask(name, score, maxScore, type, desc);
                }
                
                dispose(); // Close dialog on success
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });
        
        mainPanel.add(Box.createVerticalGlue()); // Push button to bottom
        mainPanel.add(addButton);
        
        add(mainPanel);
    }

    public Task getCreatedTask() {
        return createdTask;
    }
}
