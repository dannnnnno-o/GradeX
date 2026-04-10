package Components;

import Models.Subject;
import Models.TaskType;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EditSubjectDialog extends JDialog {
    private Subject subject;
    private boolean confirmed = false;
    private JTextField nameField;
    private JTextField[] rubricFields;
    private JLabel totalLabel;

    class RoundedField extends JTextField {
        private int radius;
        public RoundedField(int radius) {
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
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public EditSubjectDialog(JFrame parent, Subject subject) {
        super(parent, "Edit Subject", true);
        this.subject = subject;
        setSize(450, 600);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 45, 30, 45));

        JLabel titleLabel = new JLabel("Edit Subject Details");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel nameHeader = new JLabel("Subject Name");
        nameHeader.setFont(new Font("Raleway", Font.BOLD, 16));
        nameHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(nameHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        nameField = new RoundedField(20);
        nameField.setText(subject.getName());
        nameField.setFont(new Font("Raleway", Font.PLAIN, 14));
        nameField.setMaximumSize(new Dimension(300, 40));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(nameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel rubricsHeader = new JLabel("Grading Rubrics");
        rubricsHeader.setFont(new Font("Raleway", Font.BOLD, 16));
        rubricsHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(rubricsHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        TaskType[] types = TaskType.values();
        rubricFields = new JTextField[types.length];
        int[] rubrics = subject.getRubrics();

        JPanel rGrid = new JPanel(new GridBagLayout());
        rGrid.setOpaque(false);
        rGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 8, 0);

        for (int i = 0; i < types.length; i++) {
            gbc.gridy = i;
            gbc.gridx = 0;
            JLabel typeLbl = new JLabel(types[i].name().substring(0, 1).toUpperCase() + types[i].name().substring(1).toLowerCase());
            typeLbl.setFont(new Font("Raleway", Font.BOLD, 15));
            typeLbl.setPreferredSize(new Dimension(140, 32));
            rGrid.add(typeLbl, gbc);

            gbc.gridx = 1;
            rubricFields[i] = new RoundedField(15);
            rubricFields[i].setHorizontalAlignment(SwingConstants.CENTER);
            rubricFields[i].setPreferredSize(new Dimension(75, 35));
            rubricFields[i].setText(String.valueOf(rubrics[i]));
            rubricFields[i].addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) { updateTotal(); }
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    char c = evt.getKeyChar();
                    if (!Character.isDigit(c)) evt.consume();
                }
            });
            rGrid.add(rubricFields[i], gbc);

            gbc.gridx = 2;
            gbc.insets = new Insets(0, 10, 8, 0);
            JLabel ps = new JLabel("%");
            rGrid.add(ps, gbc);
            gbc.insets = new Insets(0, 0, 8, 0);
        }

        JPanel rWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        rWrapper.setOpaque(false);
        rWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        rWrapper.add(rGrid);
        rWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        mainPanel.add(rWrapper);

        totalLabel = new JLabel("Total: 0%");
        totalLabel.setFont(new Font("Raleway", Font.PLAIN, 12));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(totalLabel);
        updateTotal();

        mainPanel.add(Box.createVerticalGlue());

        JButton updateBtn = new JButton("Save Changes") {
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
        updateBtn.setFont(new Font("Raleway", Font.BOLD, 16));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.setContentAreaFilled(false);
        updateBtn.setBorderPainted(false);
        updateBtn.setPreferredSize(new Dimension(280, 45));
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.addActionListener(e -> attemptUpdate());

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.setOpaque(false);
        btnWrapper.add(updateBtn);
        mainPanel.add(btnWrapper);

        add(mainPanel);
    }

    private void updateTotal() {
        int sum = 0;
        for (JTextField f : rubricFields) {
            try {
                int v = Integer.parseInt(f.getText().trim());
                if (v < 0) { f.setText("0"); v = 0; }
                sum += v;
            } catch (NumberFormatException e) {
                // Ignore transient empty/invalid states during typing
            }
        }
        totalLabel.setText("Total: " + sum + "%");
        if (sum > 100) {
            totalLabel.setForeground(Color.decode("#d9534f")); // Red
            totalLabel.setText("Total: " + sum + "% (Limit: 100%)");
        } else if (sum == 100) {
            totalLabel.setForeground(Color.decode("#27ae60")); // Green
        } else {
            totalLabel.setForeground(Color.DARK_GRAY);
        }
    }

    private void attemptUpdate() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showCustomMessage("Subject name required", "Please enter a subject name.", true);
            return;
        }

        int[] newRubrics = new int[rubricFields.length];
        int sum = 0;
        try {
            for (int i = 0; i < rubricFields.length; i++) {
                int v = Integer.parseInt(rubricFields[i].getText().trim());
                newRubrics[i] = v;
                sum += v;
            }
        } catch (NumberFormatException ex) {
            showCustomMessage("Invalid Input", "Rubrics must be integers.", true);
            return;
        }

        if (sum > 100) {
            showCustomMessage("Limit Exceeded", "Total rubric percentage cannot exceed 100%. Currently: " + sum + "%", true);
            return;
        }

        if (sum != 100 && sum != 0) {
            boolean confirmed = CustomDialog.showConfirm(this, "Incomplete Rubrics", "Total remains " + sum + "%. Projected grade calculation will be paused until it reaches 100%. Proceed?");
            if (!confirmed) return;
        }

        subject.setName(name);
        subject.setRubrics(newRubrics);
        this.confirmed = true;
        dispose();
    }

    private void showCustomMessage(String title, String msg, boolean isError) {
        CustomDialog.showMessage(this, title, msg);
    }

    public boolean isConfirmed() { return confirmed; }
}
