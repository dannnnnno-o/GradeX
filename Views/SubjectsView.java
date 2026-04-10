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

        cardsPanel = new JPanel(new GridLayout(0, 2, 15, 15));
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

        // Wrapper to keep the grid at the top
        JPanel scrollContent = new ScrollablePanel(new BorderLayout());
        scrollContent.setBackground(Color.decode("#e9e9e9"));
        scrollContent.add(cardsPanel, BorderLayout.NORTH);

        // Empty state message in the CENTER of the scroll content to keep it centered in the viewport
        JLabel emptyMsg = new JLabel("No subjects added yet. Click '+ Add Subject' to get started!");
        emptyMsg.setFont(new Font("Raleway", Font.PLAIN, 18));
        emptyMsg.setForeground(Color.GRAY);
        emptyMsg.setHorizontalAlignment(SwingConstants.CENTER);
        emptyMsg.setName("EmptyMessage"); // For identification
        emptyMsg.setVisible(false);
        scrollContent.add(emptyMsg, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        updateView();
    }

    public void updateView() {
        cardsPanel.removeAll();
        
        // Find the empty message label in the parent container
        JLabel emptyMsg = null;
        Container parent = cardsPanel.getParent();
        if (parent != null) {
            for (Component c : parent.getComponents()) {
                if (c instanceof JLabel && "EmptyMessage".equals(c.getName())) {
                    emptyMsg = (JLabel) c;
                    break;
                }
            }
        }

        if (subjects.isEmpty()) {
            if (emptyMsg != null) emptyMsg.setVisible(true);
            cardsPanel.setVisible(false);
        } else {
            if (emptyMsg != null) emptyMsg.setVisible(false);
            cardsPanel.setVisible(true);
            cardsPanel.setLayout(new GridLayout(0, 2, 15, 15));
            for (Subject sub : subjects) {
                SubjectCard card = new SubjectCard(sub);

                card.getRemoveBtn().addActionListener(e -> {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    RemoveTaskDialog dialog = new RemoveTaskDialog(topFrame, "Remove Subject", sub.getName());
                    dialog.setVisible(true);
                    if (dialog.isConfirmed()) {
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
        }
        
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
}
