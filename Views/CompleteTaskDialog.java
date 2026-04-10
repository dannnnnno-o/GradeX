package Views;

import Models.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CompleteTaskDialog extends JDialog {
    private Double resultScore = null;
    private double maxScore;
    private JTextField scoreField;

    class RoundedField extends JTextField {
        private int radius;
        public RoundedField(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.decode("#eeeeee"));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setHorizontalAlignment(JTextField.CENTER);
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

    public CompleteTaskDialog(JFrame parent, Task task) {
        super(parent, "Complete Task", true);
        this.maxScore = task.getMaxScore();
        
        setSize(440, 320);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(35, 30, 20, 30));
        
        // Header
        JLabel titleLabel = new JLabel("Complete Task", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Prompt using HTML for centering text within the label
        JLabel promptLabel = new JLabel("<html><center>Enter final score for '<b>" + task.getName() + "</b>'<br>(Max: " + maxScore + ")</center></html>", SwingConstants.CENTER);
        promptLabel.setFont(new Font("Raleway", Font.PLAIN, 15));
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        promptLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        mainPanel.add(promptLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        scoreField = new RoundedField(20);
        scoreField.setFont(new Font("Raleway", Font.BOLD, 16));
        scoreField.setMaximumSize(new Dimension(280, 45));
        scoreField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scoreField);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JButton cancelBtn = createStyledButton("Cancel", "#999999");
        JButton compBtn = createStyledButton("Complete", "#27ae60");
        
        compBtn.addActionListener(e -> {
            try {
                double val = Double.parseDouble(scoreField.getText().trim());
                if (val < 0 || val > maxScore) {
                    JOptionPane.showMessageDialog(this, "Score must be between 0 and " + maxScore);
                    return;
                }
                resultScore = val;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid score. Please enter a number.");
            }
        });
        
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(cancelBtn);
        btnPanel.add(compBtn);
        
        mainPanel.add(btnPanel);
        add(mainPanel);
    }

    private JButton createStyledButton(String text, String colorHex) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode(colorHex));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Raleway", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 42));
        return btn;
    }

    public Double getResultScore() {
        return resultScore;
    }
}
