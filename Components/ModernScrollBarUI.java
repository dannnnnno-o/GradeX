package Components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ModernScrollBarUI extends BasicScrollBarUI {
    private final int thickness = 8;

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = Color.decode("#c1c1c1");
        this.trackColor = new Color(0, 0, 0, 0);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color color = isDragging ? Color.decode("#888888") : (isThumbRollover() ? Color.decode("#aaaaaa") : thumbColor);
        g2.setColor(color);
        
        int x = thumbBounds.x + 2;
        int y = thumbBounds.y + 2;
        int width = thumbBounds.width - 4;
        int height = thumbBounds.height - 4;
        
        g2.fillRoundRect(x, y, width, height, 10, 10);
        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // No track background for modern look
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension dim = super.getPreferredSize(c);
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return new Dimension(thickness, dim.height);
        } else {
            return new Dimension(dim.width, thickness);
        }
    }
}
