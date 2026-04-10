package Views;

import Models.*;
import Components.MyButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class EditTaskDialog extends JDialog {
    private PendingTask pendingTask;
    private JComboBox<String> statusDropdown;
    private JComboBox<TaskType> typeDropdown;
    private JTextField nameField;
    private JTextArea descArea;
    private ScrollablePanel contentPanel;
    
    // Pending fields
    private JTextField pendingMaxScoreField;
    private JTextField pendingDeadlineField;

    class RoundedField extends JTextField {
        private int radius;
        public RoundedField(int radius, String placeholder) {
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.decode("#eeeeee"));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    class RoundedArea extends JTextArea {
        private int radius;
        public RoundedArea(int radius, String placeholder) {
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.decode("#eeeeee"));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setLineWrap(true);
            setWrapStyleWord(true);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    class RoundedWrapper extends JPanel {
        private int radius;
        public RoundedWrapper(int radius, Component c) {
            this.radius = radius;
            setLayout(new BorderLayout());
            setOpaque(false);
            setBackground(Color.decode("#eeeeee"));
            setBorder(BorderFactory.createEmptyBorder(2, 15, 2, 10));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            add(c, BorderLayout.CENTER);
            
            JLabel arrow = new JLabel("v");
            arrow.setFont(new Font("Monospaced", Font.PLAIN, 12));
            arrow.setForeground(Color.GRAY);
            add(arrow, BorderLayout.EAST);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    class ScrollablePanel extends JPanel implements Scrollable {
        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 40; }
        public boolean getScrollableTracksViewportWidth() { return true; }
        public boolean getScrollableTracksViewportHeight() { return false; }
    }

    public EditTaskDialog(JFrame parent, PendingTask task) {
        super(parent, "Edit Task", true);
        this.pendingTask = task;
        
        setSize(480, 650);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        contentPanel = new ScrollablePanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 45, 10, 45));
        
        JLabel titleLabel = new JLabel("Edit Pending Task");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Task Status (Disabled for Edit Pending)
        contentPanel.add(createHeaderLabel("Task Status"));
        statusDropdown = createCleanCombo(new String[]{"Pending"});
        statusDropdown.setEnabled(false);
        RoundedWrapper statusWrapper = new RoundedWrapper(20, statusDropdown);
        statusWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(statusWrapper);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Task Type
        contentPanel.add(createHeaderLabel("Task Type"));
        typeDropdown = createCleanCombo(TaskType.values());
        typeDropdown.setSelectedItem(task.getType());
        RoundedWrapper typeWrapper = new RoundedWrapper(20, typeDropdown);
        typeWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(typeWrapper);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Task Name
        contentPanel.add(createHeaderLabel("Task Name"));
        nameField = new RoundedField(20, "");
        nameField.setText(task.getName());
        nameField.setFont(new Font("Raleway", Font.PLAIN, 14));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(nameField);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Description
        contentPanel.add(createHeaderLabel("Description"));
        descArea = new RoundedArea(20, "");
        descArea.setText(task.getDescription() != null ? task.getDescription() : "");
        descArea.setFont(new Font("Raleway", Font.PLAIN, 14));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        descArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateHeight(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateHeight(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateHeight(); }
            private void updateHeight() {
                try {
                    int lineCount = descArea.getLineCount();
                    int newHeight = Math.max(40, Math.min(200, (lineCount * 20) + 20));
                    descArea.setPreferredSize(new Dimension(descArea.getWidth(), newHeight));
                    contentPanel.revalidate();
                } catch (Exception ex) {}
            }
        });
        
        // Set initial height
        int initialLineCount = (task.getDescription() != null) ? (int)task.getDescription().chars().filter(ch -> ch == '\n').count() + 1 : 1;
        descArea.setPreferredSize(new Dimension(0, Math.max(40, Math.min(200, (initialLineCount * 20) + 20))));

        contentPanel.add(descArea);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Max Score
        contentPanel.add(createHeaderLabel("Max Score"));
        pendingMaxScoreField = new RoundedField(20, "");
        pendingMaxScoreField.setText(String.valueOf(task.getMaxScore()));
        pendingMaxScoreField.setForeground(Color.BLACK);
        pendingMaxScoreField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(pendingMaxScoreField);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Deadline
        contentPanel.add(createHeaderLabel("Deadline"));
        pendingDeadlineField = new RoundedField(20, "");
        pendingDeadlineField.setText(task.getDeadline() != null ? task.getDeadline().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy")) : "");
        pendingDeadlineField.setForeground(Color.BLACK);
        pendingDeadlineField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(pendingDeadlineField);
        contentPanel.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.setOpaque(false);
        btnWrapper.setBackground(Color.WHITE);
        btnWrapper.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        JButton updateButton = new JButton("Update Task") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#1a1a1a"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        updateButton.setFont(new Font("Raleway", Font.BOLD, 16));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setContentAreaFilled(false);
        updateButton.setBorderPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setPreferredSize(new Dimension(280, 45));
        updateButton.addActionListener(e -> attemptUpdateTask());
        
        btnWrapper.add(updateButton);
        
        JPanel mainBoard = new JPanel(new BorderLayout());
        mainBoard.setBackground(Color.WHITE);
        mainBoard.add(scrollPane, BorderLayout.CENTER);
        mainBoard.add(btnWrapper, BorderLayout.SOUTH);
        
        add(mainBoard);
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Raleway", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return label;
    }

    private <T> JComboBox<T> createCleanCombo(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setFont(new Font("Raleway", Font.BOLD, 16));
        combo.setBackground(Color.decode("#eeeeee"));
        combo.setBorder(null);
        combo.setFocusable(false);
        
        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton b = new JButton();
                b.setBorder(null);
                b.setContentAreaFilled(false);
                b.setPreferredSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
        });
        
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return combo;
    }

    private void attemptUpdateTask() {
        try {
            String name = nameField.getText().trim();
            String desc = descArea.getText().trim();
            TaskType type = (TaskType) typeDropdown.getSelectedItem();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task Name cannot be empty.");
                return;
            }
            
            double maxScore = Double.parseDouble(pendingMaxScoreField.getText().trim());
            LocalDate deadline = LocalDate.parse(pendingDeadlineField.getText().trim(), 
                java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            
            pendingTask.setName(name);
            pendingTask.setDescription(desc);
            pendingTask.setType(type);
            pendingTask.setMaxScore(maxScore);
            pendingTask.setDeadline(deadline);
            
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check the score or date format (MM/DD/YYYY).");
        }
    }
}
