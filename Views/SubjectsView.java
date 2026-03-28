package Views;

import Models.Subject;
import Components.SubjectCard;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class SubjectsView extends JPanel {
    private List<Subject> subjects;
    private JPanel cardsPanel;
    private Consumer<Subject> onSubjectViewDetails;
    private Runnable onDataChanged;

    public SubjectsView(List<Subject> subjects, Consumer<Subject> onSubjectViewDetails, Runnable onDataChanged) {
        this.subjects = subjects;
        this.onSubjectViewDetails = onSubjectViewDetails;
        this.onDataChanged = onDataChanged;

        setLayout(new BorderLayout());
        setBackground(Color.decode("#e9e9e9"));
        
        JLabel title = new JLabel("Subjects List", SwingConstants.CENTER);
        title.setFont(new Font("Raleway", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);
        
        // Use FlowLayout aligned to left, mimicking a wrap layout if it wraps
        cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        cardsPanel.setBackground(Color.decode("#e9e9e9"));
        
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        updateView();
    }

    public void updateView() {
        cardsPanel.removeAll();
        for (Subject sub : subjects) {
            SubjectCard card = new SubjectCard(sub);
            
            card.getRemoveBtn().addActionListener(e -> {
                int resp = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove " + sub.getName() + "?", "Remove Subject", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    subjects.remove(sub);
                    if (onDataChanged != null) onDataChanged.run();
                    updateView();
                }
            });
            
            card.getViewDetailsBtn().addActionListener(e -> {
                if (onSubjectViewDetails != null) {
                    onSubjectViewDetails.accept(sub);
                }
            });
            
            cardsPanel.add(card);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
}
