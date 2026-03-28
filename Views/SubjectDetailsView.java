package Views;

import Models.Subject;
import javax.swing.*;
import java.awt.*;

public class SubjectDetailsView extends JPanel {
    private JLabel titleLabel;
    
    public SubjectDetailsView() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#e9e9e9"));
        
        titleLabel = new JLabel("Subject Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 24));
        add(titleLabel, BorderLayout.CENTER);
    }
    
    public void setSubject(Subject subject) {
        if(subject != null) {
            titleLabel.setText("Details for: " + subject.getName());
        }
    }
}
