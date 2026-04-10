package Views;

import Models.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationView extends JPanel {
    private List<Subject> subjects;
    private JPanel contentPanel;

    class RoundedPanel extends JPanel {
        private int radius;
        private Color borderColor;
        
        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
            setBackground(bgColor);
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }
            g2.dispose();
        }
    }

    public NotificationView(List<Subject> subjects) {
        this.subjects = subjects;
        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel title = new JLabel("Overdue Tasks");
        title.setFont(new Font("Raleway", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(20, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        setBorder(new EmptyBorder(0, 0, 0, 25)); // Add right padding for breathing space
        setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setUI(new Components.ModernScrollBarUI());
        scroll.getHorizontalScrollBar().setUI(new Components.ModernScrollBarUI());
        add(scroll, BorderLayout.CENTER);

        updateView();
    }

    public void updateView() {
        contentPanel.removeAll();
        boolean found = false;
        LocalDate today = LocalDate.now();

        for (Subject sub : subjects) {
            for (Task t : sub.getAllTasks()) {
                if (t instanceof PendingTask) {
                    PendingTask pt = (PendingTask) t;
                    if (pt.getDeadline() != null && pt.getDeadline().isBefore(today)) {
                        found = true;
                        contentPanel.add(createTaskCard(pt, sub.getName()));
                        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                    }
                }
            }
        }

        if (!found) {
            JLabel empty = new JLabel("<html><i>No overdue tasks found. <br>Keep up the good work!</i></html>");
            empty.setFont(new Font("Raleway", Font.PLAIN, 14));
            empty.setForeground(Color.GRAY);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(empty);
        }

        revalidate();
        repaint();
    }

    private JPanel createTaskCard(PendingTask pt, String subjectName) {
        // Red border for overdue
        RoundedPanel taskPanel = new RoundedPanel(10, Color.WHITE, Color.decode("#d9534f"));
        taskPanel.setLayout(new BorderLayout());
        taskPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Lessen width for breathing space (approx 85-90% of available)
        taskPanel.setMaximumSize(new Dimension(280, 120));
        taskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Top segment: Title and Tag
        JPanel topTask = new JPanel(new BorderLayout());
        topTask.setOpaque(false);
        
        JLabel nameLbl = new JLabel(pt.getName());
        nameLbl.setFont(new Font("Raleway", Font.BOLD, 14));
        
        // Tag
        JLabel tagLbl = new JLabel(pt.getType().toString());
        tagLbl.setFont(new Font("Raleway", Font.PLAIN, 10));
        tagLbl.setOpaque(true);
        tagLbl.setBackground(Color.decode("#e9e9e9"));
        tagLbl.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        
        topTask.add(nameLbl, BorderLayout.WEST);
        topTask.add(tagLbl, BorderLayout.EAST);
        
        // Body segment
        JPanel bodyTask = new JPanel();
        bodyTask.setLayout(new BoxLayout(bodyTask, BoxLayout.Y_AXIS));
        bodyTask.setOpaque(false);
        bodyTask.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        JLabel dueLbl = new JLabel("Due: " + pt.getDeadline().format(DateTimeFormatter.ofPattern("M/d/yyyy")));
        dueLbl.setFont(new Font("Raleway", Font.PLAIN, 12));
        dueLbl.setForeground(Color.decode("#d9534f")); // Red text for overdue
        
        JLabel subjectLbl = new JLabel("<html><div style='width:150px;'>Subject: " + subjectName + "</div></html>");
        subjectLbl.setFont(new Font("Raleway", Font.BOLD, 12));
        subjectLbl.setForeground(Color.decode("#555555"));

        JLabel descLbl = new JLabel(pt.getDescription());
        descLbl.setFont(new Font("Raleway", Font.PLAIN, 12));
        descLbl.setForeground(Color.BLACK);
        
        bodyTask.add(subjectLbl);
        bodyTask.add(Box.createRigidArea(new Dimension(0, 3)));
        bodyTask.add(dueLbl);
        bodyTask.add(Box.createRigidArea(new Dimension(0, 5)));
        bodyTask.add(descLbl);
        
        taskPanel.add(topTask, BorderLayout.NORTH);
        taskPanel.add(bodyTask, BorderLayout.CENTER);
        
        return taskPanel;
    }
}
