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

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.decode("#e9e9e9"));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 10, 25));

        JLabel title = new JLabel("Subjects");
        title.setFont(new Font("Raleway", Font.BOLD, 32));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Manage your academic subjects");
        subtitle.setFont(new Font("Raleway", Font.PLAIN, 16));
        subtitle.setForeground(Color.decode("#666666"));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(subtitle);
        add(headerPanel, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        cardsPanel.setBackground(Color.decode("#e9e9e9"));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Use an inline class that explicitly implements Scrollable to prevent horizontal overflow in JScrollPane
        class ScrollablePanel extends JPanel implements Scrollable {
            public ScrollablePanel(LayoutManager layout) { super(layout); }
            @Override
            public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
            @Override
            public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
            @Override
            public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 50; }
            @Override
            public boolean getScrollableTracksViewportWidth() { return true; }
            @Override
            public boolean getScrollableTracksViewportHeight() { return false; }
        }

        // Wrapper to keep the grid at the top, implementing Scrollable to fit viewport width
        JPanel scrollContent = new ScrollablePanel(new BorderLayout());
        scrollContent.setBackground(Color.decode("#e9e9e9"));
        scrollContent.add(cardsPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
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
                int resp = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove " + sub.getName() + "?",
                        "Remove Subject", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    subjects.remove(sub);
                    if (onDataChanged != null)
                        onDataChanged.run();
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
