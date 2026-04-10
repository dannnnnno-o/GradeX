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
    private JLabel totalLabel;

    class RoundedField extends JTextField {
        private int radius;
        public RoundedField(int radius, String placeholder) {
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.decode("#eeeeee"));
            setForeground(Color.GRAY);
            setText(placeholder);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setForeground(Color.GRAY);
                        setText(placeholder);
                    }
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public AddSubjectDialog(JFrame parent) {
        super(parent, "Add Subject", true);
        setSize(450, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Title
        JLabel titleLabel = new JLabel("Add Subject");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Subject Name
        JLabel nameHeader = new JLabel("Subject Name");
        nameHeader.setFont(new Font("Raleway", Font.BOLD, 16));
        nameHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        nameField = new RoundedField(20, "e.g.: Computer Programming");
        nameField.setFont(new Font("Raleway", Font.PLAIN, 14));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Grading Rubrics Header
        JLabel rubricsHeader = new JLabel("Grading Rubrics");
        rubricsHeader.setFont(new Font("Raleway", Font.BOLD, 16));
        rubricsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(rubricsHeader);
        
        JLabel rubricsSub = new JLabel("Leave at 0% if you want to set this later");
        rubricsSub.setFont(new Font("Raleway", Font.PLAIN, 12));
        rubricsSub.setForeground(Color.GRAY);
        rubricsSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(rubricsSub);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        TaskType[] types = TaskType.values();
        rubricFields = new JTextField[types.length];
        
        JPanel rGrid = new JPanel(new GridBagLayout());
        rGrid.setOpaque(false);
        rGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 8, 0);

        for(int i = 0; i < types.length; i++) {
            gbc.gridy = i;
            
            // Label
            gbc.gridx = 0;
            gbc.weightx = 0;
            JLabel typeLbl = new JLabel(types[i].name().substring(0,1).toUpperCase() + types[i].name().substring(1).toLowerCase());
            typeLbl.setFont(new Font("Raleway", Font.BOLD, 15));
            typeLbl.setPreferredSize(new Dimension(140, 32));
            rGrid.add(typeLbl, gbc);
            
            // Input
            gbc.gridx = 1;
            gbc.weightx = 0;
            rubricFields[i] = new RoundedField(15, "0");
            rubricFields[i].setHorizontalAlignment(SwingConstants.CENTER);
            rubricFields[i].setPreferredSize(new Dimension(75, 35));
            rubricFields[i].setText("0");
            rubricFields[i].setForeground(Color.BLACK);
            
            final int idx = i;
            rubricFields[i].addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    updateTotal();
                }
            });
            rGrid.add(rubricFields[i], gbc);
            
            // Percent
            gbc.gridx = 2;
            gbc.insets = new Insets(0, 10, 8, 0);
            JLabel ps = new JLabel("%");
            ps.setFont(new Font("Raleway", Font.PLAIN, 14));
            rGrid.add(ps, gbc);
            
            gbc.insets = new Insets(0, 0, 8, 0); // reset
        }
        
        // Align grid to the left
        JPanel rWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rWrapper.setOpaque(false);
        rWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        rWrapper.add(rGrid);
        mainPanel.add(rWrapper);
        
        totalLabel = new JLabel("Total: 0%");
        totalLabel.setFont(new Font("Raleway", Font.PLAIN, 12));
        totalLabel.setForeground(Color.DARK_GRAY);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(totalLabel);
        
        mainPanel.add(Box.createVerticalGlue());
        
        // Add Button Centered Wrapper
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.setOpaque(false);
        btnWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JButton createBtn = new JButton("Add Subject") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#1a1a1a"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        createBtn.setFont(new Font("Raleway", Font.BOLD, 16));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        createBtn.setContentAreaFilled(false);
        createBtn.setBorderPainted(false);
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createBtn.setPreferredSize(new Dimension(280, 45));
        
        createBtn.addActionListener(e -> attemptCreateSubject());
        
        btnWrapper.add(createBtn);
        mainPanel.add(btnWrapper);
        add(mainPanel);
    }

    private void updateTotal() {
        int sum = 0;
        for (JTextField f : rubricFields) {
            String val = f.getText().trim();
            if (val.isEmpty() || val.equals("0")) continue;
            try {
                sum += Integer.parseInt(val);
            } catch (NumberFormatException e) {}
        }
        totalLabel.setText("Total: " + sum + "%");
    }

    private void attemptCreateSubject() {
        String name = nameField.getText().trim();
        if (name.isEmpty() || name.equals("e.g.: Computer Programming")) {
            JOptionPane.showMessageDialog(this, "Please enter a subject name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[] rubrics = new int[TaskType.values().length];
        int sum = 0;
        try {
            for(int i = 0; i < rubricFields.length; i++) {
                String val = rubricFields[i].getText().trim();
                if(val.isEmpty()) val = "0";
                int v = Integer.parseInt(val);
                rubrics[i] = v;
                sum += v;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Rubrics must be integer values.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (sum != 0 && sum != 100) {
            int resp = JOptionPane.showConfirmDialog(this, "The total rubric percentage is " + sum + "%. Are you sure you want to proceed? (It should be 100% for automatic grade calculation)", "Warning", JOptionPane.YES_NO_OPTION);
            if (resp != JOptionPane.YES_OPTION) return;
        }
        createdSubject = new Subject(name, rubrics);
        dispose();
    }

    public Subject getCreatedSubject() {
        return createdSubject;
    }
}
