package Views;

import Models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class AddTaskDialog extends JDialog {
    private Task createdTask;
    private JComboBox<String> statusDropdown;
    private JComboBox<TaskType> typeDropdown;
    private JTextField nameField;
    private JTextArea descArea;

    private JPanel dynamicPanel;
    private CardLayout dynamicCardLayout;

    // Pending fields
    private JTextField pendingMaxScoreField;
    private JTextField pendingDeadlineField;

    // Completed fields
    private JTextField doneScoreField;
    private JTextField doneMaxScoreField;
    private ScrollablePanel contentPanel;

    class RoundedField extends JTextField {
        private int radius;

        public RoundedField(int radius, String placeholder) {
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.decode("#eeeeee"));
            setForeground(Color.GRAY);
            setText(placeholder);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                public void focusLost(java.awt.event.FocusEvent e) {
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
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
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
            setForeground(Color.GRAY);
            setText(placeholder);
            setLineWrap(true);
            setWrapStyleWord(true);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                public void focusLost(java.awt.event.FocusEvent e) {
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
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
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
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    class ScrollablePanel extends JPanel implements Scrollable {
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 40;
        }

        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    public AddTaskDialog(JFrame parent, Subject subject) {
        super(parent, "Add New Task", true);
        setSize(480, 650);
        setLocationRelativeTo(parent);
        setResizable(false);

        contentPanel = new ScrollablePanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 45, 10, 45));

        JLabel titleLabel = new JLabel("Add New Task");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Task Status
        contentPanel.add(createHeaderLabel("Task Status"));
        statusDropdown = createCleanCombo(new String[] { "Pending", "Completed" });
        RoundedWrapper statusWrapper = new RoundedWrapper(20, statusDropdown);
        statusWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(statusWrapper);
        contentPanel.add(Box.createVerticalStrut(10));

        // Task Type
        contentPanel.add(createHeaderLabel("Task Type"));
        typeDropdown = createCleanCombo(TaskType.values());
        RoundedWrapper typeWrapper = new RoundedWrapper(20, typeDropdown);
        typeWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(typeWrapper);
        contentPanel.add(Box.createVerticalStrut(10));

        // Task Name
        contentPanel.add(createHeaderLabel("Task Name"));
        nameField = new RoundedField(20, "e.g.: Midterm Exam");
        nameField.setFont(new Font("Raleway", Font.PLAIN, 14));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(nameField);
        contentPanel.add(Box.createVerticalStrut(10));

        // Description
        contentPanel.add(createHeaderLabel("Description"));
        descArea = new RoundedArea(20, "Task details...");
        descArea.setFont(new Font("Raleway", Font.PLAIN, 14));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        descArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateHeight();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateHeight();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateHeight();
            }

            private void updateHeight() {
                try {
                    int lineCount = descArea.getLineCount();
                    int newHeight = Math.max(40, Math.min(200, (lineCount * 20) + 20));
                    descArea.setPreferredSize(new Dimension(descArea.getWidth(), newHeight));
                    contentPanel.revalidate();
                } catch (Exception ex) {
                }
            }
        });

        contentPanel.add(descArea);
        contentPanel.add(Box.createVerticalStrut(10));

        // Dynamic Panel
        dynamicCardLayout = new CardLayout();
        dynamicPanel = new JPanel(dynamicCardLayout);
        dynamicPanel.setOpaque(false);
        dynamicPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- Pending Panel ---
        JPanel pendingPanel = new JPanel();
        pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.Y_AXIS));
        pendingPanel.setOpaque(false);
        pendingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        pendingPanel.add(createHeaderLabel("Max Score"));
        pendingMaxScoreField = new RoundedField(20, "100");
        pendingMaxScoreField.setText("100");
        pendingMaxScoreField.setForeground(Color.BLACK);
        pendingMaxScoreField.setAlignmentX(Component.LEFT_ALIGNMENT);
        pendingPanel.add(pendingMaxScoreField);
        pendingPanel.add(Box.createVerticalStrut(10));

        pendingPanel.add(createHeaderLabel("Deadline"));
        pendingDeadlineField = new RoundedField(20, "mm/dd/yyyy");
        pendingDeadlineField.setText(
                LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        pendingDeadlineField.setForeground(Color.BLACK);
        pendingDeadlineField.setAlignmentX(Component.LEFT_ALIGNMENT);
        pendingPanel.add(pendingDeadlineField);

        // --- Completed Panel ---
        JPanel completedPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        completedPanel.setOpaque(false);
        completedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        completedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel scoreCol = new JPanel();
        scoreCol.setLayout(new BoxLayout(scoreCol, BoxLayout.Y_AXIS));
        scoreCol.setOpaque(false);
        scoreCol.setAlignmentX(Component.LEFT_ALIGNMENT);
        scoreCol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        scoreCol.add(createHeaderLabel("Score"));
        doneScoreField = new RoundedField(20, "0");
        doneScoreField.setText("0");
        doneScoreField.setForeground(Color.BLACK);
        doneScoreField.setPreferredSize(new Dimension(0, 40));
        doneScoreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        doneScoreField.setAlignmentX(Component.LEFT_ALIGNMENT);
        scoreCol.add(doneScoreField);

        JPanel maxScoreCol = new JPanel();
        maxScoreCol.setLayout(new BoxLayout(maxScoreCol, BoxLayout.Y_AXIS));
        maxScoreCol.setOpaque(false);
        maxScoreCol.setAlignmentX(Component.LEFT_ALIGNMENT);
        maxScoreCol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        maxScoreCol.add(createHeaderLabel("Max Score"));
        doneMaxScoreField = new RoundedField(20, "100");
        doneMaxScoreField.setText("100");
        doneMaxScoreField.setForeground(Color.BLACK);
        doneMaxScoreField.setPreferredSize(new Dimension(0, 40));
        doneMaxScoreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        doneMaxScoreField.setAlignmentX(Component.LEFT_ALIGNMENT);
        maxScoreCol.add(doneMaxScoreField);

        completedPanel.add(scoreCol);
        completedPanel.add(maxScoreCol);

        dynamicPanel.add(pendingPanel, "Pending");
        dynamicPanel.add(completedPanel, "Completed");

        contentPanel.add(dynamicPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);

        statusDropdown.addActionListener(e -> {
            String selected = (String) statusDropdown.getSelectedItem();
            dynamicCardLayout.show(dynamicPanel, selected);
            if ("Pending".equals(selected)) {
                setSize(480, 650);
            } else {
                setSize(480, 560);
            }
            revalidate();
            repaint();
        });

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.setOpaque(false);
        btnWrapper.setBackground(Color.WHITE);
        btnWrapper.setBorder(new EmptyBorder(0, 0, 25, 0));

        JButton addButton = new JButton("Add Task") {
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
        addButton.setFont(new Font("Raleway", Font.BOLD, 16));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setPreferredSize(new Dimension(280, 45));
        addButton.addActionListener(e -> attemptAddTask());

        btnWrapper.add(addButton);

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

        // Hide default arrow button
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

        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return combo;
    }

    private void attemptAddTask() {
        try {
            String name = nameField.getText().trim();
            String desc = descArea.getText().trim();
            TaskType type = (TaskType) typeDropdown.getSelectedItem();
            String status = (String) statusDropdown.getSelectedItem();

            if (name.isEmpty() || name.equals("e.g.: Midterm Exam")) {
                JOptionPane.showMessageDialog(this, "Task Name cannot be empty.");
                return;
            }

            if ("Pending".equals(status)) {
                String maxStr = pendingMaxScoreField.getText().trim();
                double maxScore = Double.parseDouble(maxStr.isEmpty() ? "100" : maxStr);
                LocalDate deadline = LocalDate.parse(pendingDeadlineField.getText().trim(),
                        java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                
                if (deadline.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, "Deadline cannot be in the past.");
                    return;
                }
                
                createdTask = new PendingTask(name, maxScore, type, desc, deadline);
            } else {
                double score = Double.parseDouble(doneScoreField.getText().trim());
                double maxScore = Double.parseDouble(doneMaxScoreField.getText().trim());
                createdTask = new CompletedTask(name, score, maxScore, type, desc);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check the score or date format (MM/DD/YYYY).");
        }
    }

    public Task getCreatedTask() {
        return createdTask;
    }
}
