package Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WelcomeView extends JPanel {
    public WelcomeView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.decode("#e9e9e9"));
        setBorder(new EmptyBorder(40, 20, 0, 40));

        JLabel title = new JLabel("Welcome to GradeX");
        title.setFont(new Font("Raleway", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea description = new JTextArea("In GradeX, you can manage your academic workload by keeping track of your subjects and tasks with built-in automatic grade calculator");
        description.setFont(new Font("Raleway", Font.PLAIN, 16));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setOpaque(false);
        description.setFocusable(false);
        description.setMaximumSize(new Dimension(300, 200));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        description.setBorder(new EmptyBorder(10, 0, 0, 0));

        add(title);
        add(description);
    }
}
