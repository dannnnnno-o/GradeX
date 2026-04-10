package Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RemoveTaskDialog extends JDialog {
    private boolean confirmed = false;

    public RemoveTaskDialog(JFrame parent, String title, String name) {
        super(parent, title, true);
        
        setSize(440, 280);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(35, 30, 20, 30));
        
        // Header
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Prompt using HTML for centering text within the label
        JLabel promptLabel = new JLabel("<html><center>Are you sure you want to remove<br><b>" + name + "</b>?</center></html>", SwingConstants.CENTER);
        promptLabel.setFont(new Font("Raleway", Font.PLAIN, 15));
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        promptLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        mainPanel.add(promptLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JButton cancelBtn = createStyledButton("Cancel", "#999999");
        JButton removeBtn = createStyledButton("Remove", "#d9534f");
        
        removeBtn.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        
        cancelBtn.addActionListener(e -> dispose());
        
        btnPanel.add(cancelBtn);
        btnPanel.add(removeBtn);
        
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

    public boolean isConfirmed() {
        return confirmed;
    }
}
