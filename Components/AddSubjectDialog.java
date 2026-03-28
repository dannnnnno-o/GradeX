package Components;

import Models.Subject;
import Models.TaskType;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class AddSubjectDialog extends JDialog {
    private Subject createdSubject;
    private JTextField nameField;
    private JTextField[] rubricFields;

    public AddSubjectDialog(JFrame parent) {
        super(parent, "Add Subject", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Add Subject");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Name Field
        JLabel nameLabel = new JLabel("Subject Name");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameLabel);
        
        nameField = new JTextField("e.g.: Computer Programming");
        nameField.setForeground(Color.GRAY);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("e.g.: Computer Programming")) {
                    nameField.setText("");
                    nameField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setForeground(Color.GRAY);
                    nameField.setText("e.g.: Computer Programming");
                }
            }
        });
        mainPanel.add(nameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Rubrics Section
        JLabel rubricsTitle = new JLabel("Grading Rubrics (%) (Optional)");
        rubricsTitle.setFont(new Font("Raleway", Font.BOLD, 14));
        rubricsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(rubricsTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        TaskType[] types = TaskType.values();
        rubricFields = new JTextField[types.length];
        
        JPanel rubricsPanel = new JPanel(new GridLayout(types.length, 2, 10, 10));
        rubricsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for(int i = 0; i < types.length; i++) {
            rubricsPanel.add(new JLabel(types[i].name()));
            rubricFields[i] = new JTextField("0");
            rubricsPanel.add(rubricFields[i]);
        }
        mainPanel.add(rubricsPanel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        JButton createBtn = new JButton("Create Subject");
        createBtn.setBackground(Color.decode("#c9c9c9"));
        createBtn.addActionListener(e -> attemptCreateSubject());
        
        btnPanel.add(cancelBtn);
        btnPanel.add(createBtn);
        mainPanel.add(btnPanel);

        add(mainPanel);
    }

    private void attemptCreateSubject() {
        String name = nameField.getText().trim();
        if (name.isEmpty() || name.equals("e.g.: Computer Programming")) {
            JOptionPane.showMessageDialog(this, "Please enter a subject name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[] rubrics = new int[TaskType.values().length];
        try {
            for(int i = 0; i < rubricFields.length; i++) {
                String val = rubricFields[i].getText().trim();
                if(val.isEmpty()) val = "0";
                rubrics[i] = Integer.parseInt(val);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Rubrics must be integer values.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        createdSubject = new Subject(name, rubrics);
        dispose();
    }

    public Subject getCreatedSubject() {
        return createdSubject;
    }
}
