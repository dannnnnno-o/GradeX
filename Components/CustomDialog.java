package Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomDialog extends JDialog {
    private boolean confirmed = false;

    public CustomDialog(Window parent, String title, String message, boolean isConfirm) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        setSize(400, 220);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());
        setUndecorated(false);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(35, 30, 20, 30));

        JLabel msgLabel = new JLabel("<html><center style='font-family: Raleway; font-size: 13pt; color: #222222;'>" + message + "</center></html>");
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        msgLabel.setMaximumSize(new Dimension(350, 100));
        content.add(msgLabel);
        content.add(Box.createVerticalGlue());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(true);
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        if (isConfirm) {
            JButton yesBtn = createStyledButton("Yes", "#222222", Color.WHITE);
            yesBtn.addActionListener(e -> { confirmed = true; dispose(); });
            
            JButton noBtn = createStyledButton("No", "#f0f0f0", Color.DARK_GRAY);
            noBtn.addActionListener(e -> { confirmed = false; dispose(); });
            
            btnPanel.add(yesBtn);
            btnPanel.add(noBtn);
        } else {
            JButton okBtn = createStyledButton("OK", "#222222", Color.WHITE);
            okBtn.addActionListener(e -> { confirmed = true; dispose(); });
            btnPanel.add(okBtn);
        }

        add(content, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        getContentPane().setBackground(Color.WHITE);
    }

    private JButton createStyledButton(String text, String bgHex, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode(bgHex));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Raleway", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public boolean isConfirmed() { return confirmed; }

    public static void showMessage(Window parent, String title, String msg) {
        new CustomDialog(parent, title, msg, false).setVisible(true);
    }

    public static boolean showConfirm(Window parent, String title, String msg) {
        CustomDialog cd = new CustomDialog(parent, title, msg, true);
        cd.setVisible(true);
        return cd.isConfirmed();
    }
}
